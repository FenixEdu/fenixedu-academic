package net.sourceforge.fenixedu.presentationTier.Action.candidate.degree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.candidacy.ExecuteStateOperation;
import net.sourceforge.fenixedu.applicationTier.Servico.candidacy.LogFirstTimeCandidacyTimestamp;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentForFirstTimeStudents;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.workflow.CandidacyOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintAllDocumentsOperation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DegreeCandidacyManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("showWelcome");
    }

    public ActionForward showCandidacyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final StudentCandidacy candidacy = getCandidacy(request);
	request.setAttribute("candidacy", candidacy);

	final SortedSet<Operation> operations = new TreeSet<Operation>();
	operations.addAll(candidacy.getActiveCandidacySituation().getOperationsForPerson(getLoggedPerson(request)));
	request.setAttribute("operations", operations);

	request.setAttribute("person", getUserView(request).getPerson());
	if (getCandidacy(request).getExecutionDegree().getCampus().isCampusTaguspark()) {
	    request.setAttribute("isInTaguspark", "true");
	}

	return mapping.findForward("showCandidacyDetails");
    }

    public ActionForward doOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final CandidacyOperation operation = (CandidacyOperation) getCandidacy(request).getActiveCandidacySituation()
		.getOperationByTypeAndPerson(getOperationType(request), getLoggedPerson(request));
	request.setAttribute("operation", operation);
	request.setAttribute("candidacy", getCandidacy(request));
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	if (operation != null && operation.isInput()) {
	    LogFirstTimeCandidacyTimestamp.logTimestamp(getCandidacy(request), FirstTimeCandidacyStage.STARTED_FILLING_FORMS);
	    request.setAttribute("currentForm", operation.moveToNextForm());
	    return mapping.findForward("fillData");
	} else {
	    return executeOperation(mapping, form, request, response, operation);
	}

    }

    private String getSchemaSuffixForPerson(HttpServletRequest request) {
	return (getUserView(request).getPerson().hasRole(RoleType.EMPLOYEE)) ? ".forEmployee" : "";
    }

    public ActionForward processForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
	request.setAttribute("candidacy", getCandidacy(request));

	final CandidacyOperation operation = (CandidacyOperation) RenderUtils.getViewState("operation-view-state")
		.getMetaObject().getObject();
	request.setAttribute("operation", operation);
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	if (!validateCurrentForm(request)) {
	    return mapping.findForward("fillData");
	}

	if (operation.hasMoreForms()) {
	    request.setAttribute("currentForm", operation.moveToNextForm());
	    return mapping.findForward("fillData");
	} else {
	    final StudentCandidacy candidacy = getCandidacy(request);
	    if (candidacy.isConcluded()) {
		request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));
		request.setAttribute("candidacyID", candidacy.getIdInternal());

		addActionMessage(request, "warning.candidacy.process.is.already.concluded");

		return showCandidacyDetails(mapping, actionForm, request, response);
	    }

	    executeOperation(mapping, actionForm, request, response, operation);
	    LogFirstTimeCandidacyTimestamp.logTimestamp(candidacy, FirstTimeCandidacyStage.FINISHED_FILLING_FORMS);

	    return new ActionForward(buildSummaryPdfGeneratorURL(request, candidacy), true);
	}
    }

    private boolean validateCurrentForm(HttpServletRequest request) {
	final Form form = (Form) RenderUtils.getViewState("fillData" + getCurrentFormPosition(request)).getMetaObject()
		.getObject();
	final List<LabelFormatter> messages = form.validate();
	if (!messages.isEmpty()) {
	    request.setAttribute("formMessages", solveLabelFormatterArgs(request, messages.toArray(new LabelFormatter[messages
		    .size()])));
	    request.setAttribute("currentForm", form);

	    return false;
	} else {
	    return true;
	}

    }

    private ActionForward executeOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, CandidacyOperation candidacyOperation) throws FenixServiceException,
	    FenixFilterException, FenixActionException {

	final IUserView userView = getUserView(request);

	if (candidacyOperation == null) {
	    // possible due to first-time candidacy summary generation link in
	    // manager portal
	    candidacyOperation = new PrintAllDocumentsOperation(RoleType.STUDENT, getCandidacy(request));
	} else {
	    ExecuteStateOperation.run(candidacyOperation, getLoggedPerson(request));
	}

	if (candidacyOperation.getType() == CandidacyOperationType.PRINT_SCHEDULE) {
	    final List<InfoLesson> infoLessons = ReadStudentTimeTable.run(getCandidacy(request).getRegistration());
	    request.setAttribute("person", getCandidacy(request).getPerson());
	    request.setAttribute("infoLessons", infoLessons);
	    return mapping.findForward("printSchedule");

	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_REGISTRATION_DECLARATION) {
	    request.setAttribute("registration", getCandidacy(request).getRegistration());
	    request.setAttribute("executionYear", getCandidacy(request).getExecutionDegree().getExecutionYear());
	    return mapping.findForward("printRegistrationDeclaration");

	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_UNDER_23_TRANSPORTS_DECLARATION) {
	    request.setAttribute("person", getCandidacy(request).getRegistration().getPerson());
	    request.setAttribute("campus", getCandidacy(request).getRegistration().getCampus().getName());
	    request.setAttribute("executionYear", getCandidacy(request).getExecutionDegree().getExecutionYear());
	    return mapping.findForward("printUnder23TransportsDeclation");

	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_MEASUREMENT_TEST_DATE) {
	    request.setAttribute("registration", getCandidacy(request).getRegistration());
	    return mapping.findForward("printMeasurementTestDate");

	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_ALL_DOCUMENTS) {
	    request.setAttribute("candidacy", getCandidacy(request));
	    request.setAttribute("registration", getCandidacy(request).getRegistration());
	    request.setAttribute("executionYear", getCandidacy(request).getExecutionDegree().getExecutionYear());
	    request.setAttribute("person", getCandidacy(request).getRegistration().getPerson());
	    request.setAttribute("campus", getCandidacy(request).getRegistration().getCampus().getName());
	    request.setAttribute("administrativeOfficeFeeAndInsurancePaymentCode",
		    administrativeOfficeFeeAndInsurancePaymentCode(getCandidacy(request).getAvailablePaymentCodes()));
	    request.setAttribute("installmentPaymentCodes", installmmentPaymentCodes(getCandidacy(request)
		    .getAvailablePaymentCodes()));
	    request.setAttribute("totalGratuityPaymentCode", totalGratuityPaymentCode(getCandidacy(request)
		    .getAvailablePaymentCodes()));
	    request.setAttribute("firstInstallmentEndDate", calculateFirstInstallmentEndDate(getCandidacy(request)
		    .getRegistration(), getCandidacy(request).getAvailablePaymentCodes()));
	    request.setAttribute("sibsEntityCode", PropertiesManager.getProperty("sibs.entityCode"));

	    final List<InfoLesson> infoLessons = ReadStudentTimeTable.run(getCandidacy(request).getRegistration());
	    request.setAttribute("infoLessons", infoLessons);

	    return mapping.findForward("printAllDocuments");

	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_SYSTEM_ACCESS_DATA) {
	    request.setAttribute("person", userView.getPerson());
	    return mapping.findForward("printSystemAccessData");

	} else if (candidacyOperation.getType() == CandidacyOperationType.FILL_PERSONAL_DATA) {
	    request.setAttribute("aditionalInformation", getResources(request).getMessage(
		    "label.candidacy.username.changed.message", userView.getPerson().getIstUsername()));
	} else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_GRATUITY_PAYMENT_CODES) {
	    request.setAttribute("registration", getCandidacy(request).getRegistration());
	    request.setAttribute("paymentCodes", getCandidacy(request).getAvailablePaymentCodes());
	    request.setAttribute("sibsEntityCode", PropertiesManager.getProperty("sibs.entityCode"));
	    request.setAttribute("administrativeOfficeFeeAndInsurancePaymentCode",
		    administrativeOfficeFeeAndInsurancePaymentCode(getCandidacy(request).getAvailablePaymentCodes()));
	    request.setAttribute("installmentPaymentCodes", installmmentPaymentCodes(getCandidacy(request)
		    .getAvailablePaymentCodes()));
	    request.setAttribute("totalGratuityPaymentCode", totalGratuityPaymentCode(getCandidacy(request)
		    .getAvailablePaymentCodes()));

	    request.setAttribute("firstInstallmentEndDate", calculateFirstInstallmentEndDate(getCandidacy(request)
		    .getRegistration(), getCandidacy(request).getAvailablePaymentCodes()));

	    return mapping.findForward("printGratuityPaymentCodes");

	}

	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));
	request.setAttribute("candidacyID", candidacyOperation.getCandidacy().getIdInternal());

	return showCandidacyDetails(mapping, form, request, response);

    }

    private Object totalGratuityPaymentCode(List<PaymentCode> availablePaymentCodes) {
	for (PaymentCode paymentCode : availablePaymentCodes) {
	    if (PaymentCodeType.GRATUITY_FIRST_INSTALLMENT.equals(paymentCode.getType())
		    && !(paymentCode instanceof InstallmentPaymentCode)) {
		return paymentCode;
	    }
	}

	return null;
    }

    private Object installmmentPaymentCodes(List<PaymentCode> availablePaymentCodes) {
	List<PaymentCode> installmentPaymentCodes = new ArrayList<PaymentCode>();

	CollectionUtils.select(availablePaymentCodes, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		PaymentCode paymentCode = (PaymentCode) arg0;

		if (paymentCode instanceof InstallmentPaymentCode) {
		    return true;
		}

		return false;
	    }
	}, installmentPaymentCodes);

	Collections.sort(installmentPaymentCodes, new BeanComparator("code"));

	return installmentPaymentCodes;
    }

    private Object administrativeOfficeFeeAndInsurancePaymentCode(List<PaymentCode> availablePaymentCodes) {
	for (PaymentCode paymentCode : availablePaymentCodes) {
	    if (PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE.equals(paymentCode.getType())) {
		return paymentCode;
	    }
	}

	return null;
    }

    private YearMonthDay calculateFirstInstallmentEndDate(final Registration registration, List<PaymentCode> availablePaymentCodes) {
	for (PaymentCode paymentCode : availablePaymentCodes) {
	    if (!paymentCode.isInstallmentPaymentCode()) {
		continue;
	    }

	    InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;
	    if (!installmentPaymentCode.getInstallment().isForFirstTimeStudents()) {
		continue;
	    }

	    InstallmentForFirstTimeStudents firstInstallment = (InstallmentForFirstTimeStudents) installmentPaymentCode
		    .getInstallment();
	    return registration.getStartDate().plusDays(firstInstallment.getNumberOfDaysToStartApplyingPenalty());
	}

	return null;
    }

    public ActionForward showCurrentForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	request.setAttribute("candidacy", getCandidacy(request));
	request.setAttribute("operation", RenderUtils.getViewState("operation-view-state").getMetaObject().getObject());
	request.setAttribute("currentForm", RenderUtils.getViewState("fillData" + getCurrentFormPosition(request))
		.getMetaObject().getObject());
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	if (isPostback(request)) {
	    RenderUtils.invalidateViewState();
	}

	return mapping.findForward("fillData");
    }

    private boolean isPostback(HttpServletRequest request) {
	return request.getParameter("postback") != null && Boolean.valueOf(request.getParameter("postback")).equals(Boolean.TRUE);
    }

    private StudentCandidacy getCandidacy(HttpServletRequest request) {
	return (StudentCandidacy) rootDomainObject.readCandidacyByOID(Integer.valueOf(getFromRequest(request, "candidacyID")
		.toString()));
    }

    private Integer getCurrentFormPosition(HttpServletRequest request) {
	return getRequestParameterAsInteger(request, "currentFormPosition");
    }

    private CandidacyOperationType getOperationType(HttpServletRequest request) {
	return CandidacyOperationType.valueOf(getFromRequest(request, "operationType").toString());
    }

    @Override
    protected Map<String, String> getMessageResourceProviderBundleMappings() {
	final Map<String, String> bundleMappings = new HashMap<String, String>();
	bundleMappings.put("enum", "ENUMERATION_RESOURCES");
	bundleMappings.put("application", "");

	return bundleMappings;
    }

    private String buildSummaryPdfGeneratorURL(HttpServletRequest request, final StudentCandidacy candidacy) {
	String url = "/candidate/degreeCandidacyManagement.do?method=doOperation&operationType=PRINT_ALL_DOCUMENTS&candidacyID="
		+ candidacy.getIdInternal() + "&contentContextPath_PATH=/portal-do-candidato/portal-do-candidato";

	String urlWithChecksum = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url);

	return urlWithChecksum.substring("/candidate".length());
    }

    public ActionForward generateSummaryFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return new ActionForward(buildSummaryPdfGeneratorURL(request, getCandidacy(request)), true);
    }

    public ActionForward showSummaryFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidacySummaryFile file = getCandidacy(request).getSummaryFile();

	response.reset();
	try {
	    response.getOutputStream().write(file.getContents());
	    response.setContentLength(file.getContents().length);
	    response.setContentType("application/pdf");
	    response.flushBuffer();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }
}