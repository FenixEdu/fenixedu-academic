package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StringUtils;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class ProcessCandidacyPrintAllDocumentsFilter implements Filter {
    public static final String CGD_PDF_PATH = "/MOD43.pdf";

    private class CGDPdfFiller {
	AcroFields form;

	public ByteArrayOutputStream getFilledPdf() throws IOException, DocumentException {
	    InputStream istream = getClass().getResourceAsStream(CGD_PDF_PATH);
	    PdfReader reader = new PdfReader(istream);
	    reader.getAcroForm().remove(PdfName.SIGFLAGS);

	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    PdfStamper stamper = new PdfStamper(reader, output);
	    form = stamper.getAcroFields();

	    final IUserView userView = UserView.getUser();
	    final Person person = userView.getPerson();

	    final Student student = person.getStudent();
	    final Registration registration = findRegistration(student);

	    setField("T_NomeComp", person.getName());
	    setField("T_Email", person.getEmailForSendingEmails());

	    if (person.isFemale()) {
		setField("RB8", "Yes"); // female checkbox
	    } else if (person.isMale()) {
		setField("RB2", "Yes"); // male checkbox
	    }

	    setField("Cod_data_1", person.getDateOfBirthYearMonthDay().toString(DateTimeFormat.forPattern("ddMMyyyy")));
	    setField("NIF1", person.getSocialSecurityNumber());
	    setField("CB_EstCivil01", person.getMaritalStatus().getPresentationName());
	    setField("T_DocIdent", person.getDocumentIdNumber());

	    YearMonthDay emissionDate = person.getEmissionDateOfDocumentIdYearMonthDay();
	    if (emissionDate != null) {
		setField("Cod_data_2", emissionDate.toString(DateTimeFormat.forPattern("ddMMyyyy")));
	    }
	    setField("Cod_data_3",
		    person.getExpirationDateOfDocumentIdYearMonthDay().toString(DateTimeFormat.forPattern("ddMMyyyy")));

	    setField("T_NomeMae", person.getNameOfMother());
	    setField("T_NomePai", person.getNameOfFather());
	    setField("T_NatFreg", person.getParishOfBirth());
	    setField("T_NatConc", person.getDistrictSubdivisionOfBirth());

	    setField("T_PaisRes", person.getCountryOfResidence().getName());
	    setField("T_Nacion", person.getCountryOfBirth().getCountryNationality().toString());
	    setField("T_Morada01", person.getAddress());
	    setField("T_Telef", person.getDefaultPhoneNumber());

	    String postalCode = person.getPostalCode();
	    int dashIndex = postalCode.indexOf('-');
	    setField("T_CodPos01", postalCode.substring(0, 4));
	    String last3Numbers = person.getPostalCode().substring(dashIndex + 1, dashIndex + 4);
	    setField("T_CodPos02", last3Numbers);

	    setField("T_Localid02", person.getAreaOfAreaCode());
	    setField("T_Telem", person.getDefaultMobilePhoneNumber());
	    setField("T_Freguesia", person.getParishOfResidence());
	    setField("T_Concelho", person.getDistrictSubdivisionOfResidence());

	    setField("T_CodCur", registration.getDegree().getMinistryCode());
	    setField("T_Curso", CardGenerationEntry.normalizeDegreeName(registration.getDegree()));
	    setField("T_CodEstEns", Campus.getUniversityCode(registration.getLastDegreeCurricularPlan().getCurrentCampus()));
	    setField("T_EstEns", "Instituto Superior Técnico");
	    setField("T_NumAL", student.getStudentNumber().getNumber().toString());
	    setField("T_GrauEns", CardGenerationEntry.normalizeDegreeType12(registration.getDegreeType()));
	    setField("CB2", "");

	    // other existing controls in the pdf
	    // setField("T_NCartao", "T_NCartao");
	    // setField("T_CodFIn", "T_CodFIn");
	    // setField("T_DocIdent01", "");
	    // setField("T_Localid01", "T_Localid01");
	    // setField("T_Localid03", "T_Localid03");
	    // setField("T_CodPos03", "CP3");
	    // setField("T_CodPos04", "CP4");
	    // setField("T_Localid04", "T_Localid04");
	    // setField("Cod_data_4", "Cod__4");
	    // setField("Cod_data_5", "Cod__5");
	    // setField("T_AnoCurr", "T_AnoCurr");
	    // setField("Cod_data_6", "Cod__6");
	    // setField("Cod_data_7", "Cod__7");
	    // setField("T_TOutra", "T_TOutra");
	    // setField("T_Profis", "T_Profis");
	    // setField("T_EntPatron", "T_EntPatron");
	    // setField("T_PaisFiscal", "T_PaisFiscal");
	    // setField("T_DocComp", "T_DocComp");
	    // Button2: Pushbutton
	    // RB_13: Radiobutton
	    // RB_12: Radiobutton
	    // RB_9: Radiobutton
	    // RB2: Radiobutton
	    // RB_4
	    // RB_3: Radiobutton
	    // RB_6: Radiobutton
	    // CB_2: Checkbox
	    // CB_1: Checkbox
	    // CB_0: Checkbox

	    stamper.setFormFlattening(true);
	    stamper.close();
	    return output;
	}

	private void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
	    if (fieldContent != null) {
		form.setField(fieldName, fieldContent);
	    }
	}

	private Registration findRegistration(final Student student) {
	    final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	    for (final Registration registration : student.getRegistrationsSet()) {
		if (executionYear.equals(registration.getStartExecutionYear()) && registration.isActive()) {
		    return registration;
		}
	    }
	    return null;
	}
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
	// empty
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
		ByteArrayOutputStream finalPdfStream = concatenateDocs(pdfStream.toByteArray());
		byte[] pdfByteArray = finalPdfStream.toByteArray();

		// clear response and set the header properly
		response.reset();
		String studentNumber = getStudentNumber(doc);
		response.setHeader("Content-Disposition", "attachment; filename=documentos-" + studentNumber.trim() + ".pdf");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/pdf");
		response.setContentLength(pdfByteArray.length);

		// flush response to the browser
		response.getOutputStream().write(pdfByteArray);
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
	String url = request.getRequestURL().toString();
	String basePath = url.substring(0, url.indexOf(appContext)) + appContext + "/";

	// patch css link nodes
	NodeList linkNodes = doc.getElementsByTagName("link");
	for (int i = 0; i < linkNodes.getLength(); i++) {
	    Element link = (Element) linkNodes.item(i);

	    String href = link.getAttribute("href");
	    // remove redundant "/ciapl/" from href and append it to base path
	    // to obtain the real file path on the system
	    String realPath = basePath.concat(href.substring(7));

	    link.setAttribute("href", realPath);
	}

	// patch image nodes
	NodeList imageNodes = doc.getElementsByTagName("img");
	for (int i = 0; i < imageNodes.getLength(); i++) {
	    Element img = (Element) imageNodes.item(i);

	    String src = img.getAttribute("src");
	    // remove redundant "/ciapl/" from src and append it to base path
	    // to obtain the real file path on the system
	    String realPath = basePath.concat(src.substring(7));

	    img.setAttribute("src", realPath);
	}
    }

    private String getStudentNumber(Document doc) {
	IUserView userView = UserView.getUser();
	return userView.getUtilizador();
    }

    private ByteArrayOutputStream concatenateDocs(byte[] firstDoc) throws IOException, DocumentException {
	ByteArrayOutputStream concatenatedPdf = new ByteArrayOutputStream();
	PdfCopyFields copy = new PdfCopyFields(concatenatedPdf);

	copy.addDocument(new PdfReader(firstDoc));
	copy.addDocument(new PdfReader(new CGDPdfFiller().getFilledPdf().toByteArray()));
	copy.close();

	return concatenatedPdf;
    }
}
