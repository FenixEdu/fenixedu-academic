package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.RegistrationAcademicServiceRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class AcademicServiceRequestsManagementDispatchAction extends FenixDispatchAction {

    private static final int REQUESTS_PER_PAGE = 50;
    private static final String REQUEST_NUMBER_YEAR = "serviceRequestNumberYear";
    private static final String REGISTRATION_NUMBER = "registration.number";
    private static final String DESCRIPTION = "description";
    private static final String EXECUTION_YEAR = "executionYear";
    private static final String URGENT_REQUEST = "urgentRequest";
    private static final String REQUEST_DATE = "requestDate";
    private static final String ACTIVE_SITUATION_DATE = "activeSituationDate";
    private static final String DEFAULT_ORDER_GETTER = ACTIVE_SITUATION_DATE;
    private static final String ORDER_PARAMETER = "sortBy";
    private static final String ORDER_MARKER = "=";
    public static final String[] ASC_ORDER_DIR = { "ascending", "asc" };
    public static final String DEFAULT_ORDER_DIR = "desc";

    private RegistrationAcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	Integer academicServiceRequestId = getRequestParameterAsInteger(request, "academicServiceRequestId");
	if (academicServiceRequestId == null) {
	    academicServiceRequestId = (Integer) request.getAttribute("academicServiceRequestId");
	}
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(academicServiceRequestId);
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return (RegistrationAcademicServiceRequest) academicServiceRequest;
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request,
		"registrationID"));
	request.setAttribute("registration", registration);
	return registration;
    }

    private String getAndSetUrl(ActionForm actionForm, HttpServletRequest request) {
	final StringBuilder result = new StringBuilder();

	if (!StringUtils.isEmpty(request.getParameter("backAction"))) {
	    result.append("/").append(request.getParameter("backAction")).append(".do?");

	    if (!StringUtils.isEmpty(request.getParameter("backMethod"))) {
		result.append("method=").append(request.getParameter("backMethod"));
	    }
	}

	request.setAttribute("url", result.toString());
	return result.toString();
    }

    public ActionForward viewRegistrationAcademicServiceRequestsHistoric(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getAndSetRegistration(request));
	return mapping.findForward("viewRegistrationAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAndSetAcademicServiceRequest(request);
	getAndSetUrl(form, request);
	return mapping.findForward("viewAcademicServiceRequest");
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    executeService("ProcessNewAcademicServiceRequests", academicServiceRequest);
	    addActionMessage(request, "academic.service.request.processed.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("failingCondition", ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	if (academicServiceRequest.isDocumentRequest()
		&& ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
	    return prepareConcludeAcademicServiceRequest(mapping, actionForm, request, response);
	} else if (request.getParameter("academicSituationType") != null) {
	    return search(mapping, actionForm, request, response);
	} else {
	    request.setAttribute("registration", academicServiceRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");
	}
    }

    public ActionForward prepareSendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getAndSetAcademicServiceRequest(request);
	request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean());
	return mapping.findForward("prepareSendAcademicServiceRequest");
    }

    public ActionForward sendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

	try {
	    executeService("SendAcademicServiceRequestToExternalEntity", new Object[] { serviceRequest,
		    requestBean.getSituationDate(), requestBean.getJustification() });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareSendAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareSendAcademicServiceRequest");
	}

	request.setAttribute("registration", serviceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareReceiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getAndSetAcademicServiceRequest(request);
	request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean());
	return mapping.findForward("prepareReceiveAcademicServiceRequest");
    }

    public ActionForward receiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

	try {
	    executeService("ReceivedAcademicServiceRequestFromExternalEntity", new Object[] { serviceRequest,
		    requestBean.getSituationDate(), requestBean.getJustification() });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareReceiveAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareReceiveAcademicServiceRequest");
	}

	request.setAttribute("registration", serviceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareRejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareRejectAcademicServiceRequest");
    }

    public ActionForward rejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    executeService("RejectAcademicServiceRequest", new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    executeService("CancelAcademicServiceRequest", new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareConcludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	((DynaActionForm) actionForm).set("sendEmailToStudent", Boolean.TRUE);

	if (academicServiceRequest.isDocumentRequest()) {
	    request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean());
	    return mapping.findForward("prepareConcludeDocumentRequest");
	} else {
	    return mapping.findForward("prepareConcludeServiceRequest");
	}
    }

    public ActionForward concludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final DynaActionForm form = (DynaActionForm) actionForm;

	try {
	    executeService("ConcludeAcademicServiceRequest", new Object[] { academicServiceRequest, getSendEmailToStudent(form),
		    getSituationDate(), getJustification() });
	    addActionMessage(request, "academic.service.request.concluded.with.success");

	    if (academicServiceRequest.isDocumentRequest()
		    && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
		return deliveredAcademicServiceRequest(mapping, actionForm, request, response);
	    }
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    private Boolean getSendEmailToStudent(final DynaActionForm form) {
	return (Boolean) form.get("sendEmailToStudent");
    }

    private YearMonthDay getSituationDate() {
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");
	return requestBean == null ? null : requestBean.getSituationDate();
    }

    private String getJustification() {
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");
	return requestBean == null ? null : requestBean.getJustification();
    }

    public ActionForward deliveredAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    executeService("DeliveredAcademicServiceRequest", new Object[] { academicServiceRequest });
	    addActionMessage(request, "academic.service.request.delivered.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final AcademicServiceRequestBean bean = getOrCreateAcademicServiceRequestBean(request);
	request.setAttribute("bean", bean);

	final Collection<AcademicServiceRequest> remainingRequests = bean.searchAcademicServiceRequests();
	final Collection<AcademicServiceRequest> specificRequests = getAndRemoveSpecificRequests(bean, remainingRequests);

	final SortedSet<AcademicServiceRequest> sorted = new TreeSet<AcademicServiceRequest>(getComparator(request));
	sorted.addAll(remainingRequests);
	request.setAttribute("remainingRequests", remainingRequests);
	request.setAttribute("specificRequests", specificRequests);

	final CollectionPager<AcademicServiceRequest> pager = new CollectionPager<AcademicServiceRequest>(sorted,
		REQUESTS_PER_PAGE);
	request.setAttribute("collectionPager", pager);
	request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);
	request.setAttribute("resultPage", pager.getPage(page));

	return mapping.findForward("searchResults");
    }

    private AcademicServiceRequestBean getOrCreateAcademicServiceRequestBean(HttpServletRequest request) {
	AcademicServiceRequestBean bean = (AcademicServiceRequestBean) getObjectFromViewState("bean");
	if (bean == null) {
	    Integer year = getIntegerFromRequest(request, "serviceRequestYear");
	    if (year == null) {
		year = new YearMonthDay().getYear();
	    }

	    bean = new AcademicServiceRequestBean(AcademicServiceRequestSituationType.valueOf(request
		    .getParameter("academicSituationType")), getEmployee(), year);
	}
	return bean;
    }

    private Employee getEmployee() {
	return AccessControl.getPerson().getEmployee();
    }

    private Comparator getComparator(HttpServletRequest request) {
	final String orderParameter = request.getParameter(ORDER_PARAMETER);
	final String orderGetter = StringUtils.isEmpty(orderParameter) ? DEFAULT_ORDER_GETTER : orderParameter.substring(0,
		orderParameter.indexOf(ORDER_MARKER));

	final String orderDir = StringUtils.isEmpty(orderParameter) ? DEFAULT_ORDER_DIR : orderParameter.substring(orderParameter
		.indexOf(ORDER_MARKER) + 1, orderParameter.length());
	final boolean orderAsc = Arrays.asList(ASC_ORDER_DIR).contains(orderDir);

	if (orderGetter.equals(REQUEST_NUMBER_YEAR)) {
	    return orderAsc ? AcademicServiceRequest.COMPARATOR_BY_NUMBER : new ReverseComparator(
		    AcademicServiceRequest.COMPARATOR_BY_NUMBER);
	} else if (orderGetter.equals(EXECUTION_YEAR)) {
	    return orderAsc ? AcademicServiceRequest.EXECUTION_YEAR_AND_OID_COMPARATOR : new ReverseComparator(
		    AcademicServiceRequest.EXECUTION_YEAR_AND_OID_COMPARATOR);
	} else if (orderGetter.equals(REGISTRATION_NUMBER) || orderGetter.equals(DESCRIPTION)
		|| orderGetter.equals(URGENT_REQUEST) || orderGetter.equals(REGISTRATION_NUMBER)
		|| orderGetter.equals(REQUEST_DATE) || orderGetter.equals(ACTIVE_SITUATION_DATE)) {
	    final ComparatorChain chain = new ComparatorChain();
	    chain.addComparator(orderAsc ? new BeanComparator(orderGetter) : new ReverseComparator(
		    new BeanComparator(orderGetter)));
	    chain.addComparator(AcademicServiceRequest.COMPARATOR_BY_ID);
	    return chain;
	}

	return null;
    }

    private Collection<AcademicServiceRequest> getAndRemoveSpecificRequests(final AcademicServiceRequestBean bean,
	    final Collection<AcademicServiceRequest> remainingRequests) {
	final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (Iterator<AcademicServiceRequest> iter = remainingRequests.iterator(); iter.hasNext();) {
	    final AcademicServiceRequest academicServiceRequest = (AcademicServiceRequest) iter.next();
	    if (bean.getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW) {
		if (!academicServiceRequest.getActiveSituation().hasEmployee()) {
		    iter.remove();
		    result.add(academicServiceRequest);
		}
	    } else {
		if (academicServiceRequest.getActiveSituation().getEmployee() == getEmployee()) {
		    iter.remove();
		    result.add(academicServiceRequest);
		}
	    }
	}

	return result;
    }

    public ActionForward chooseServiceRequestType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", new RegistrationAcademicServiceRequestCreator(
		getAndSetRegistration(request)));
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypeInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward backToViewRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	getAndSetRegistration(request);
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward confirmCreateServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	return mapping.findForward("confirmCreateServiceRequest");
    }

    public ActionForward createServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeFactoryMethod(request);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return confirmCreateServiceRequest(mapping, actionForm, request, response);
	}

	final RegistrationAcademicServiceRequestCreator bean = (RegistrationAcademicServiceRequestCreator) getRenderedObject("academicServiceRequestCreateBean");
	request.setAttribute("registration", bean.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }
}
