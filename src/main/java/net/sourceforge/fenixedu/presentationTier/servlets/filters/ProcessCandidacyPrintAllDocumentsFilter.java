/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.candidacydocfiller.PdfFiller;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

public class ProcessCandidacyPrintAllDocumentsFilter implements Filter {

    private static final Set<PdfFiller> pdfFillersSet = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(ProcessCandidacyPrintAllDocumentsFilter.class);

    private static final String ACADEMIC_ADMIN_SHEET_REPORT_PATH = "/reports/processOpeningAndUpdating.jasper";
    private ServletContext servletContext;

    private String getMail(Person person) {
        if (person.hasInstitutionalEmailAddress()) {
            return person.getInstitutionalEmailAddressValue();
        } else {
            String emailForSendingEmails = person.getEmailForSendingEmails();
            return emailForSendingEmails != null ? emailForSendingEmails : StringUtils.EMPTY;
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

        if ("doOperation".equals(request.getParameter("method"))
                && "PRINT_ALL_DOCUMENTS".equals(request.getParameter("operationType"))) {

            try {
                ResponseWrapper response = (ResponseWrapper) arg1;
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
                ByteArrayOutputStream finalPdfStream = concatenateDocs(pdfStream.toByteArray(), person);
                byte[] pdfByteArray = finalPdfStream.toByteArray();

                // associate the summary file to the candidacy
                associateSummaryFile(pdfByteArray, person.getStudent().getNumber().toString(), candidacy);

                // redirect user to the candidacy summary page
                response.reset();
                response.sendRedirect(buildRedirectURL(request, candidacy));

                response.flushBuffer();
            } catch (ParserConfigurationException e) {
                logger.error(e.getMessage(), e);
            } catch (SAXException e) {
                logger.error(e.getMessage(), e);
            } catch (DocumentException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Atomic
    private void associateSummaryFile(byte[] pdfByteArray, String studentNumber, StudentCandidacy studentCandidacy) {
        studentCandidacy.setSummaryFile(new CandidacySummaryFile(studentNumber + ".pdf", pdfByteArray, studentCandidacy));
    }

    private String clean(String dirtyHtml) {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();

            TagNode root = cleaner.clean(dirtyHtml);

            return new SimpleHtmlSerializer(cleaner.getProperties()).getAsString(root);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    private void patchLinks(Document doc, HttpServletRequest request) {
        // build basePath
        String appContext = request.getContextPath();

        // patch css link nodes
        NodeList linkNodes = doc.getElementsByTagName("link");
        for (int i = 0; i < linkNodes.getLength(); i++) {
            Element link = (Element) linkNodes.item(i);
            String href = link.getAttribute("href");

            if (appContext.length() > 0 && href.contains(appContext)) {
                href = href.substring(appContext.length());
            }

            try {
                String realPath = servletContext.getResource(href).toString();
                link.setAttribute("href", realPath);
            } catch (MalformedURLException e) {
                logger.error(e.getMessage(), e);
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
                logger.error(e.getMessage(), e);
            }
        }
    }

    private ByteArrayOutputStream concatenateDocs(byte[] originalDoc, Person person) throws IOException, DocumentException {
        ByteArrayOutputStream concatenatedPdf = new ByteArrayOutputStream();
        PdfCopyFields copy = new PdfCopyFields(concatenatedPdf);

        try {
            copy.addDocument(new PdfReader(createAcademicAdminProcessSheet(person).toByteArray()));
        } catch (JRException e) {
            logger.error(e.getMessage(), e);
        }
        copy.addDocument(new PdfReader(originalDoc));
        for (PdfFiller pdfFiller : pdfFillersSet) {
            copy.addDocument(new PdfReader(pdfFiller.getFilledPdf(person).toByteArray()));
        }
        copy.close();

        return concatenatedPdf;
    }

    public static void registerFiller(PdfFiller pdfFiller) {
        pdfFillersSet.add(pdfFiller);
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
                map.put("photo", new ByteArrayInputStream(person.getPersonalPhotoEvenIfPending().getDefaultAvatar()));
            } catch (Exception e) {
                // nothing; print everything else
            }

            map.put("sex", BundleUtil.getString(Bundle.ENUMERATION, person.getGender().name()));
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
            map.put("emailAddress", getMail(person));
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
        return FenixFramework.getDomainObject(request.getParameter("candidacyID"));
    }

    private String buildRedirectURL(HttpServletRequest request, final StudentCandidacy candidacy) {
        String url =
                "/candidate/degreeCandidacyManagement.do?method=showCandidacyDetails&candidacyID=" + candidacy.getExternalId();

        String urlWithChecksum = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url, request.getSession());

        return request.getContextPath() + urlWithChecksum;
    }
}
