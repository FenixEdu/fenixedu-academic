package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.sibsPaymentFileProcessReport.SibsPaymentFileProcessReportDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFile;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

@Mapping(path = "/SIBSPayments", module = "manager")
@Forwards( { @Forward(name = "prepareUploadSIBSPaymentFiles", path = "/manager/payments/prepareUploadSIBSPaymentFiles.jsp") })
public class SIBSPaymentsDA extends FenixDispatchAction {

    static private final String[] SUPPORTED_FILE_EXTENSIONS = new String[] { "INP", "ZIP" };

    static public class UploadBean implements Serializable {
	private static final long serialVersionUID = 3625314688141697558L;

	private transient InputStream inputStream;

	private String filename;

	public InputStream getInputStream() {
	    return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
	    this.inputStream = inputStream;
	}

	public String getFilename() {
	    return filename;
	}

	public void setFilename(String filename) {
	    this.filename = StringNormalizer.normalize(filename);
	}
    }

    private class ProcessResult {

	private final HttpServletRequest request;
	private boolean processFailed = false;

	public ProcessResult(HttpServletRequest request) {
	    this.request = request;
	}

	public void addMessage(String message, String... args) {
	    addActionMessage("message", request, message, args);
	}

	public void reportFailure() {
	    processFailed = true;
	}

	public boolean hasFailed() {
	    return processFailed;
	}
    }

    public ActionForward prepareUploadSIBSPaymentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	UploadBean bean = (UploadBean) getRenderedObject("uploadBean");
	RenderUtils.invalidateViewState("uploadBean");
	if (bean == null) {
	    bean = new UploadBean();
	}

	request.setAttribute("uploadBean", bean);
	return mapping.findForward("prepareUploadSIBSPaymentFiles");
    }

    public ActionForward uploadSIBSPaymentFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	List<InputStream> fileStreams = new ArrayList<InputStream>();

	UploadBean bean = (UploadBean) getRenderedObject("uploadBean");
	RenderUtils.invalidateViewState("uploadBean");
	if (bean == null) {
	    return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
	}
	/*
	 * Accepting ZIP files (for later work) if (bean.getFileType() ==
	 * PAYMENT_FILE_EXTENSIONS[1]) { ZipInputStream zipStream =
	 * (ZipInputStream) bean.getInputStream();
	 * 
	 * ZipFile zipFile = new
	 * ZipFile(FileUtils.copyToTemporaryFile(zipStream)); Enumeration
	 * zipEntries = zipFile.entries(); zipFile.getInputStream(entry)
	 * 
	 * ZipEntry entry; while ((entry = zipStream.getNextEntry()) != null) {
	 * } } else { fileStreams.add(bean.getInputStream()); }
	 */

	fileStreams.add(bean.getInputStream());
	ProcessResult result = new ProcessResult(request);
	for (final InputStream stream : fileStreams) {
	    result.addMessage("label.manager.SIBS.processingFile", bean.getFilename());
	    processFile(bean.getFilename(), stream, result);
	}
	result.addMessage("label.manager.SIBS.allDone");

	return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
    }

    private void processFile(String filename, InputStream file, ProcessResult result) {
	try {
	    final Person person = AccessControl.getPerson();
	    final SibsIncommingPaymentFile sibsFile = SibsIncommingPaymentFile.parse(filename, file);

	    result.addMessage("label.manager.SIBS.linesFound", String.valueOf(sibsFile.getDetailLines().size()));
	    result.addMessage("label.manager.SIBS.startingProcess");

	    for (final SibsIncommingPaymentFileDetailLine detailLine : sibsFile.getDetailLines()) {
		try {
		    processCode(detailLine, person.getIdInternal(), result);
		} catch (Exception e) {
		    result.addMessage("error.processException", detailLine.getCode());
		}
	    }

	    result.addMessage("label.manager.SIBS.creatingReport");
	    if (!result.hasFailed()) {
		if (SibsPaymentFileProcessReport.hasAny(sibsFile.getWhenProcessedBySibs(), sibsFile.getVersion())) {
		    result.addMessage("warning.manager.SIBS.reportAlreadyProcessed");
		} else {
		    try {
			createSibsFileReport(sibsFile, result);
		    } catch (Exception ex) {
			result.addMessage("error.manager.SIBS.reportException", ex.getMessage());
		    }
		}
	    } else {
		result.addMessage("error.manager.SIBS.nonProcessedCodes");
	    }
	    result.addMessage("label.manager.SIBS.done");

	} catch (Exception e) {
	    result.addMessage("error.manager.SIBS.fileException", e.getMessage());
	} finally {
	    if (file != null) {
		try {
		    file.close();
		} catch (IOException e) {
		    result.addMessage("error.manager.SIBS.IOException");
		}
	    }
	}
    }

    private void processCode(SibsIncommingPaymentFileDetailLine detailLine, Integer personId, ProcessResult result)
	    throws Exception {
	final Person person = (Person) rootDomainObject.readPartyByOID(personId);
	final PaymentCode paymentCode = getPaymentCode(detailLine, result);

	if (paymentCode == null) {
	    result.addMessage("error.manager.SIBS.codeNotFound", detailLine.getCode());
	    throw new Exception();
	}

	final PaymentCode codeToProcess = getPaymentCodeToProcess(paymentCode, ExecutionYear.readByDateTime(detailLine
		.getWhenOccuredTransaction()), result);

	if (codeToProcess.getState() == PaymentCodeState.INVALID) {
	    result.addMessage("warning.manager.SIBS.invalidCode", codeToProcess.getCode());
	}

	if (codeToProcess.isProcessed() && codeToProcess.getWhenUpdated().isBefore(detailLine.getWhenOccuredTransaction())) {
	    result.addMessage("warning.manager.SIBS.codeAlreadyProcessed", codeToProcess.getCode());
	}

	codeToProcess.process(person, detailLine.getAmount(), detailLine.getWhenOccuredTransaction(), detailLine
		.getSibsTransactionId(), StringUtils.EMPTY);

    }

    private void createSibsFileReport(SibsIncommingPaymentFile sibsIncomingPaymentFile, ProcessResult result) throws Exception {
	final SibsPaymentFileProcessReportDTO reportDTO = new SibsPaymentFileProcessReportDTO(sibsIncomingPaymentFile);
	for (final SibsIncommingPaymentFileDetailLine detailLine : sibsIncomingPaymentFile.getDetailLines()) {
	    reportDTO.addAmount(detailLine, getPaymentCode(detailLine, result));
	}
	SibsPaymentFileProcessReport.create(reportDTO);
	result.addMessage("label.manager.SIBS.reportCreated");
    }

    private PaymentCode getPaymentCodeToProcess(final PaymentCode paymentCode, ExecutionYear executionYear, ProcessResult result) {
	final PaymentCodeMapping mapping = paymentCode.getOldPaymentCodeMapping(executionYear);

	final PaymentCode codeToProcess;
	if (mapping != null) {
	    result.addMessage("warning.manager.SIBS.foundMapping", paymentCode.getCode(), mapping.getNewPaymentCode().getCode());
	    result.addMessage("warning.manager.SIBS.invalidating", paymentCode.getCode());

	    codeToProcess = mapping.getNewPaymentCode();
	    paymentCode.setState(PaymentCodeState.INVALID);

	} else {
	    codeToProcess = paymentCode;
	}
	return codeToProcess;
    }

    private PaymentCode getPaymentCode(final SibsIncommingPaymentFileDetailLine detailLine, ProcessResult result) {
	return getPaymentCode(detailLine.getCode(), result);
    }

    private PaymentCode getPaymentCode(final String code, ProcessResult result) {
	final Integer studentNumber = PaymentCodeGenerator.getStudentNumberFrom(code);
	Student student = Student.readStudentByNumber(studentNumber);

	// TODO: remove this temporary workaround
	if (student == null) {
	    final List<Registration> registrations = Registration.readByNumber(studentNumber);

	    if (registrations.isEmpty()) {
		result.addMessage("error.manager.SIBS.noSuchStudent", String.valueOf(studentNumber));
		return null;
	    }

	    if (registrations.size() == 1) {
		student = registrations.iterator().next().getStudent();
	    } else {
		for (final Registration registration : registrations) {
		    if (student == null) {
			student = registration.getStudent();
		    } else if (student != registration.getStudent()) {
			result.addMessage("warning.manager.SIBS.multipleRegistrations", String.valueOf(studentNumber));
		    }
		}
	    }
	}

	if (student == null) {
	    result.addMessage("error.manager.SIBS.studentNotFound", String.valueOf(studentNumber));
	    return null;
	}

	return student.getPaymentCodeBy(code);
    }
}
