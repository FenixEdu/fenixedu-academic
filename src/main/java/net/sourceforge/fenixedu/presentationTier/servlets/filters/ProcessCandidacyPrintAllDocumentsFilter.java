package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderPhotoEntry;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Jpeg;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class ProcessCandidacyPrintAllDocumentsFilter implements Filter {
    private static final String SANTANDER_APPLICATION_PDF_PATH = "/SANTANDER_APPLICATION_FORM.pdf";
    private static final String SANTANDER_APPLICATION_CARD_PDF_PATH = "/SANTANDER_APPLICATION_CARD_FORM.pdf";
    private static final String BPI_AEIST_CARD_PDF_PATH = "/BPI_AEIST_CARD_FORM.pdf";
    private static final String BPI_DIGITAL_DOCUMENTATION_PDF_PATH = "/BPI_DIGITAL_DOCUMENTATION_FORM.pdf";
    private static final String BPI_PRODUCTS_SERVICES_PDF_PATH = "/BPI_PRODUCTS_SERVICES_FORM.pdf";
    private static final String BPI_PERSONAL_INFORMATION_PDF_PATH = "/BPI_PERSONAL_INFORMATION_FORM.pdf";
    private static final String ACADEMIC_ADMIN_SHEET_REPORT_PATH = "/reports/processOpeningAndUpdating.jasper";
    private ServletContext servletContext;

    private class SantanderPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException {
            InputStream istream = getClass().getResourceAsStream(SANTANDER_APPLICATION_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            reader.selectPages("1,2");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("topmostSubform[0].Page1[0].Nomecompleto[0]", person.getName());
            String documentIdNumber = person.getDocumentIdNumber();
            if (person.getIdDocumentType().equals(IDDocumentType.CITIZEN_CARD)
                    || person.getIdDocumentType().equals(IDDocumentType.IDENTITY_CARD)) {
                setField("topmostSubform[0].Page1[0].NumBICartaoCidadaooutro[0]", documentIdNumber);
                setField("topmostSubform[0].Page1[0].Checkdigit[0]", person.getIdentificationDocumentSeriesNumberValue());
            } else {
                setField("topmostSubform[0].Page1[0].Outrotipodocidentificacao[0]", documentIdNumber);
            }

            YearMonthDay emissionDate = person.getEmissionDateOfDocumentIdYearMonthDay();
            if (emissionDate != null) {
                setField("topmostSubform[0].Page1[0].Dataemissao[0]",
                        emissionDate.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            }
            setField("topmostSubform[0].Page1[0].Datavalidade[0]",
                    person.getExpirationDateOfDocumentIdYearMonthDay().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            setField("topmostSubform[0].Page1[0].NIF[0]", person.getSocialSecurityNumber());
            setField("topmostSubform[0].Page1[0].Nacionalidade[0]", person.getCountryOfBirth().getCountryNationality().toString());
            setField("topmostSubform[0].Page1[0].Datanascimento[0]",
                    person.getDateOfBirthYearMonthDay().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            YearMonthDay dateOfBirthYearMonthDay = person.getDateOfBirthYearMonthDay();
            Period periodBetween = new Period(dateOfBirthYearMonthDay, new YearMonthDay());
            setField("topmostSubform[0].Page1[0].Idadeactual[0]", String.valueOf(periodBetween.get(DurationFieldType.years())));
            if (person.isFemale()) {
                setField("topmostSubform[0].Page1[0].Sexo[0]", "F"); // female
            } else if (person.isMale()) {
                setField("topmostSubform[0].Page1[0].Sexo[0]", "M"); // male
            }

            switch (person.getMaritalStatus()) {
            case CIVIL_UNION:
                setField("topmostSubform[0].Page1[0].Uniaofacto[0]", "1");
                break;
            case DIVORCED:
                setField("topmostSubform[0].Page1[0].Divorciado[0]", "1");
                break;
            case MARRIED:
                setField("topmostSubform[0].Page1[0].Casado[0]", "1");
                break;
            case SEPARATED:
                setField("topmostSubform[0].Page1[0].Separado[0]", "1");
                break;
            case SINGLE:
                setField("topmostSubform[0].Page1[0].Solteiro[0]", "1");
                break;
            case WIDOWER:
                setField("topmostSubform[0].Page1[0].Viuvo[0]", "1");
                break;
            }
            setField("topmostSubform[0].Page1[0].Telemovel[0]", person.getDefaultMobilePhoneNumber());
            setField("topmostSubform[0].Page1[0].E-mail[0]", person.getInstitutionalEmailAddressValue());
            setField("topmostSubform[0].Page1[0].Moradaprincipal[0]", person.getAddress());
            setField("topmostSubform[0].Page1[0].localidade[0]", person.getAreaOfAreaCode());
            String postalCode = person.getPostalCode();
            int dashIndex = postalCode.indexOf('-');
            setField("topmostSubform[0].Page1[0].CodPostal[0]", postalCode.substring(0, 4));
            String last3Numbers = person.getPostalCode().substring(dashIndex + 1, dashIndex + 4);
            setField("topmostSubform[0].Page1[0].ExtensaoCodPostal[0]", last3Numbers);

            setField("topmostSubform[0].Page1[0].Nacionalidade[0]", person.getCountryOfBirth().getCountryNationality().toString());
            setField("topmostSubform[0].Page1[0].Nacionalidade[0]", person.getCountryOfBirth().getCountryNationality().toString());

            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    private class SantanderCardPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person, StudentCandidacy candidacy) throws IOException,
                DocumentException {
            InputStream istream = getClass().getResourceAsStream(SANTANDER_APPLICATION_CARD_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("StudentIdentification", person.getIstUsername());
            setField("Phone", person.getDefaultMobilePhoneNumber());
            setField("Email", person.getInstitutionalEmailAddressValue());
            setField("CurrentDate", new DateTime().toString(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")));

            SantanderPhotoEntry photoEntryForPerson =
                    SantanderPhotoEntry.getOrCreatePhotoEntryForPerson(candidacy.getRegistration().getPerson());
            setField("Sequence", photoEntryForPerson.getPhotoIdentifier());

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BarcodeImageHandler.writeJPEG(BarcodeFactory.createCode39(photoEntryForPerson.getPhotoIdentifier(), false), baos);
                Jpeg sequenceBarcodeImg = new Jpeg(baos.toByteArray());
                float[] sequenceFieldPositions = form.getFieldPositions("SequenceBarcode"); // 1-lowerleftX, 2-lly, 3-upperRightX, 4-ury
                sequenceBarcodeImg.setAbsolutePosition(sequenceFieldPositions[1], sequenceFieldPositions[2]);
                sequenceBarcodeImg.scalePercent(45);
                stamper.getOverContent(1).addImage(sequenceBarcodeImg);
            } catch (OutputException e) {
                e.printStackTrace();
            } catch (BarcodeException be) {
                be.printStackTrace();
            }

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BarcodeImageHandler.writeJPEG(BarcodeFactory.createCode128(person.getIstUsername()), baos);
                Jpeg studentIdBarcodeImg = new Jpeg(baos.toByteArray());
                float[] studentIdFieldPositions = form.getFieldPositions("StudentIdentificationBarcode"); // 1-lowerleftX, 2-lly, 3-upperRightX, 4-ury
                studentIdBarcodeImg.setAbsolutePosition(studentIdFieldPositions[1], studentIdFieldPositions[2]);
                studentIdBarcodeImg.scalePercent(30);
                stamper.getOverContent(1).addImage(studentIdBarcodeImg);
            } catch (OutputException e) {
                e.printStackTrace();
            } catch (BarcodeException be) {
                be.printStackTrace();
            }

            Jpeg photo = new Jpeg(photoEntryForPerson.getPhotoAsByteArray());
            float[] photoFieldPositions = form.getFieldPositions("Photo"); // 1-lowerleftX, 2-lly, 3-upperRightX, 4-ury
            photo.setAbsolutePosition(photoFieldPositions[1], photoFieldPositions[2]);
            photo.scalePercent(95);
            stamper.getOverContent(1).addImage(photo);

            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    private class BPIPersonalInformationPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException {
            InputStream istream = getClass().getResourceAsStream(BPI_PERSONAL_INFORMATION_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            reader.selectPages("1,2");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("Nome completo_1", person.getName());
            setField("NIF", person.getSocialSecurityNumber());
            setField("Nº", person.getDocumentIdNumber());

            setField("Nacionalidade", person.getCountryOfBirth().getCountryNationality().toString());
            setField("Naturalidade", person.getCountryOfBirth().getName());

            setField("Distrito", person.getDistrictOfBirth());
            setField("Concelho", person.getDistrictSubdivisionOfBirth());
            setField("Freguesia", person.getParishOfBirth());
            setField("Nome do Pai", person.getNameOfFather());
            setField("Nome da Mãe", person.getNameOfMother());
            setField("Morada de Residencia_1", person.getAddress());
            setField("Localidade", person.getAreaOfAreaCode());
            setField("Designação Postal", person.getAreaOfAreaCode());
            setField("País", person.getCountryOfResidence().getName());

            String postalCode = person.getPostalCode();
            int dashIndex = postalCode.indexOf('-');
            setField("Código Postal4", postalCode.substring(0, 4));
            String last3Numbers = postalCode.substring(dashIndex + 1, dashIndex + 4);
            setField("Código Postal_5", last3Numbers);
            setField("Móvel", person.getDefaultMobilePhoneNumber());
            setField("E-mail", person.getInstitutionalEmailAddressValue());

            YearMonthDay emissionDate = person.getEmissionDateOfDocumentIdYearMonthDay();
            if (emissionDate != null) {
                setField("Dia_1", String.valueOf(emissionDate.getDayOfMonth()));
                setField("Mês_1", String.valueOf(emissionDate.getMonthOfYear()));
                setField("Ano_1", String.valueOf(emissionDate.getYear()));
            }

            YearMonthDay expirationDate = person.getExpirationDateOfDocumentIdYearMonthDay();
            setField("Dia_2", String.valueOf(expirationDate.getDayOfMonth()));
            setField("Mês_2", String.valueOf(expirationDate.getMonthOfYear()));
            setField("Ano_2", String.valueOf(expirationDate.getYear()));

            YearMonthDay birthdayDate = person.getDateOfBirthYearMonthDay();
            setField("Dia3", String.valueOf(birthdayDate.getDayOfMonth()));
            setField("Mês3", String.valueOf(birthdayDate.getMonthOfYear()));
            setField("Ano_3", String.valueOf(birthdayDate.getYear()));

            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    private class BPICardAEISTPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException {
            InputStream istream = getClass().getResourceAsStream(BPI_AEIST_CARD_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("BI/CC", person.getDocumentIdNumber());
            setField("Nome", person.getName());
            setField("topmostSubform[0].Page1[0].Datavalidade[0]",
                    person.getExpirationDateOfDocumentIdYearMonthDay().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            setField("dia_1", String.valueOf(person.getExpirationDateOfDocumentIdYearMonthDay().getDayOfMonth()));
            setField("Mês_1", String.valueOf(person.getExpirationDateOfDocumentIdYearMonthDay().getMonthOfYear()));
            setField("Ano_1", String.valueOf(person.getExpirationDateOfDocumentIdYearMonthDay().getYear()));

            LocalDate today = new LocalDate();
            setField("dia", String.valueOf(today.getDayOfMonth()));
            setField("Mês", String.valueOf(today.getMonthOfYear()));
            setField("Ano", String.valueOf(today.getYear()));

            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    private class BPIDigitalDocumentionPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException {
            InputStream istream = getClass().getResourceAsStream(BPI_DIGITAL_DOCUMENTATION_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            //reader.selectPages("1");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("Cliente", person.getName());
            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    private class BPIProductsAndServicesPdfFiller {
        AcroFields form;

        public ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException {
            InputStream istream = getClass().getResourceAsStream(BPI_PRODUCTS_SERVICES_PDF_PATH);
            PdfReader reader = new PdfReader(istream);
            reader.getAcroForm().remove(PdfName.SIGFLAGS);
            reader.selectPages("1");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, output);
            form = stamper.getAcroFields();

            setField("Nome_1", person.getName());
            stamper.setFormFlattening(true);
            stamper.close();
            return output;
        }

        private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
            if (fieldContent != null) {
                form.setField(fieldName, fieldContent);
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        servletContext = arg0.getServletContext();
    }

    @Override
    public void destroy() {
        // empty
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        arg2.doFilter(arg0, arg1);

        HttpServletRequest request = (HttpServletRequest) arg0;
        ResponseWrapper response = (ResponseWrapper) arg1;

        if ("doOperation".equals(request.getParameter("method"))
                && "PRINT_ALL_DOCUMENTS".equals(request.getParameter("operationType"))) {

            try {
                // clean the response html and make a DOM document out of it
                String responseHtml = clean(response.getContent());
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(responseHtml.getBytes()));

                // alter paths of link/img tags so itext can use them properly
                patchLinks(doc, request);

                // structure pdf document
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocument(doc, "");
                renderer.layout();

                // create the pdf
                ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
                renderer.createPDF(pdfStream);

                // concatenate with other docs
                final Person person = (Person) request.getAttribute("person");
                final StudentCandidacy candidacy = getCandidacy(request);
                ByteArrayOutputStream finalPdfStream = concatenateDocs(pdfStream.toByteArray(), person, candidacy);
                byte[] pdfByteArray = finalPdfStream.toByteArray();

                // associate the summary file to the candidacy
                associateSummaryFile(pdfByteArray, person.getStudent().getNumber().toString(), candidacy);

                // redirect user to the candidacy summary page
                response.reset();
                response.sendRedirect(buildRedirectURL(request, candidacy));

                response.flushBuffer();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Service
    private void associateSummaryFile(byte[] pdfByteArray, String studentNumber, StudentCandidacy studentCandidacy) {
        studentCandidacy.setSummaryFile(new CandidacySummaryFile(studentNumber + ".pdf", pdfByteArray, studentCandidacy));
    }

    private String clean(String dirtyHtml) {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();

            TagNode root = cleaner.clean(dirtyHtml);

            return new SimpleHtmlSerializer(cleaner.getProperties()).getAsString(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    private void patchLinks(Document doc, HttpServletRequest request) {
        // build basePath
        String appContext = FenixWebFramework.getConfig().getAppContext();

        // patch css link nodes
        NodeList linkNodes = doc.getElementsByTagName("link");
        for (int i = 0; i < linkNodes.getLength(); i++) {
            Element link = (Element) linkNodes.item(i);
            String href = link.getAttribute("href");

            if (appContext != null && appContext.length() > 0 && href.contains(appContext)) {
                href = href.substring(appContext.length() + 1);
            }

            try {
                String realPath = servletContext.getResource(href).toString();
                link.setAttribute("href", realPath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        // patch image nodes
        NodeList imageNodes = doc.getElementsByTagName("img");
        for (int i = 0; i < imageNodes.getLength(); i++) {
            Element img = (Element) imageNodes.item(i);
            String src = img.getAttribute("src");

            if (appContext != null && appContext.length() > 0 && src.contains(appContext)) {
                src = src.substring(appContext.length() + 1);
            }

            try {
                String realPath = servletContext.getResource(src).toString();
                img.setAttribute("src", realPath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private ByteArrayOutputStream concatenateDocs(byte[] originalDoc, Person person, StudentCandidacy candidacy)
            throws IOException, DocumentException {
        ByteArrayOutputStream concatenatedPdf = new ByteArrayOutputStream();
        PdfCopyFields copy = new PdfCopyFields(concatenatedPdf);

        try {
            copy.addDocument(new PdfReader(createAcademicAdminProcessSheet(person).toByteArray()));
        } catch (JRException e) {
            e.printStackTrace();
        }
        copy.addDocument(new PdfReader(originalDoc));
        copy.addDocument(new PdfReader(new SantanderPdfFiller().getFilledPdf(person).toByteArray()));
        copy.addDocument(new PdfReader(new SantanderCardPdfFiller().getFilledPdf(person, candidacy).toByteArray()));
        copy.addDocument(new PdfReader(new BPIPersonalInformationPdfFiller().getFilledPdf(person).toByteArray()));
        copy.addDocument(new PdfReader(new BPICardAEISTPdfFiller().getFilledPdf(person).toByteArray()));
        copy.addDocument(new PdfReader(new BPIDigitalDocumentionPdfFiller().getFilledPdf(person).toByteArray()));
        copy.addDocument(new PdfReader(new BPIProductsAndServicesPdfFiller().getFilledPdf(person).toByteArray()));
        copy.close();

        return concatenatedPdf;
    }

    @SuppressWarnings("unchecked")
    private ByteArrayOutputStream createAcademicAdminProcessSheet(Person person) throws JRException {
        InputStream istream = getClass().getResourceAsStream(ACADEMIC_ADMIN_SHEET_REPORT_PATH);
        JasperReport report = (JasperReport) JRLoader.loadObject(istream);

        @SuppressWarnings("rawtypes")
        HashMap map = new HashMap();

        try {
            final Student student = person.getStudent();
            final Registration registration = findRegistration(student);

            map.put("executionYear", ExecutionYear.readCurrentExecutionYear().getYear());
            if (registration != null) {
                map.put("course", registration.getDegree().getNameI18N().toString());
            }
            map.put("studentNumber", student.getNumber().toString());
            map.put("fullName", person.getName());

            try {
                map.put("photo", new ByteArrayInputStream(person.getPersonalPhotoEvenIfPending().getAvatar().getBytes()));
            } catch (Exception e) {
                // nothing; print everything else
            }

            map.put("sex", BundleUtil.getStringFromResourceBundle("resources/EnumerationResources", person.getGender().name()));
            map.put("maritalStatus", person.getMaritalStatus().getPresentationName());
            map.put("profession", person.getProfession());
            map.put("idDocType", person.getIdDocumentType().getLocalizedName());
            map.put("idDocNumber", person.getDocumentIdNumber());

            YearMonthDay emissionDate = person.getEmissionDateOfDocumentIdYearMonthDay();
            if (emissionDate != null) {
                map.put("idDocEmissionDate", emissionDate.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            }

            map.put("idDocExpirationDate",
                    person.getExpirationDateOfDocumentIdYearMonthDay().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            map.put("idDocEmissionLocation", person.getEmissionLocationOfDocumentId());

            String nif = person.getSocialSecurityNumber();
            if (nif != null) {
                map.put("NIF", nif);
            }

            map.put("birthDate", person.getDateOfBirthYearMonthDay().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
            map.put("nationality", person.getCountryOfBirth().getCountryNationality().toString());
            map.put("parishOfBirth", person.getParishOfBirth());
            map.put("districtSubdivisionOfBirth", person.getDistrictSubdivisionOfBirth());
            map.put("districtOfBirth", person.getDistrictOfBirth());
            map.put("countryOfBirth", person.getCountryOfBirth().getName());
            map.put("fathersName", person.getNameOfFather());
            map.put("mothersName", person.getNameOfMother());
            map.put("address", person.getAddress());
            map.put("postalCode", person.getPostalCode());
            map.put("locality", person.getAreaOfAreaCode());
            map.put("cellphoneNumber", person.getDefaultMobilePhoneNumber());
            map.put("telephoneNumber", person.getDefaultPhoneNumber());
            map.put("emailAddress", person.getInstitutionalEmailAddressValue());
            map.put("currentDate", new java.text.SimpleDateFormat("'Lisboa, 'dd' de 'MMMM' de 'yyyy", new java.util.Locale("PT",
                    "pt")).format(new java.util.Date()));
        } catch (NullPointerException e) {
            // nothing; will cause printing of incomplete form
            // better than no form at all
        }

        JasperPrint print = JasperFillManager.fillReport(report, map);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(print, output);
        return output;
    }

    private Registration findRegistration(final Student student) {
        return student.getLastRegistration();
    }

    private StudentCandidacy getCandidacy(HttpServletRequest request) {
        return AbstractDomainObject.fromExternalId(request.getParameter("candidacyID"));
    }

    private String buildRedirectURL(HttpServletRequest request, final StudentCandidacy candidacy) {
        String url =
                "/candidate/degreeCandidacyManagement.do?method=showCandidacyDetails&candidacyID="
                        + candidacy.getExternalId()
                        + "&"
                        + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                        + "=/portal-do-candidato/portal-do-candidato";

        String urlWithChecksum =
                pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.injectChecksumInUrl(
                        request.getContextPath(), url);

        return request.getContextPath() + urlWithChecksum;
    }
}
