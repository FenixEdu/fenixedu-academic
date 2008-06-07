package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.events.AccountingEventCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.events.EnrolmentOutOfPeriodEventCreateBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AccountingEventsManagementDA extends FenixDispatchAction {

    private static List<EventType> supportedEventTypes = Arrays.asList(EventType.GRATUITY,
	    EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, EventType.ENROLMENT_OUT_OF_PERIOD);

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("eventTypes", supportedEventTypes);
	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));

	return mapping.findForward("chooseEventType");

    }

    public ActionForward prepareInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("eventTypes", supportedEventTypes);

	return mapping.findForward("chooseEventType");
    }

    private AccountingEventCreateBean getAccountingEventCreateBean() {
	return (AccountingEventCreateBean) getRenderedObject("accountingEventCreateBean");
    }

    public ActionForward chooseEventType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final EventType eventType = getEventType(request);

	switch (eventType) {
	case GRATUITY:
	    return prepareCreateGratuityEvent(mapping, request);
	case ADMINISTRATIVE_OFFICE_FEE_INSURANCE:
	    return prepareCreateAdministrativeOfficeFeeAndInsuranceEvent(mapping, request);
	case ENROLMENT_OUT_OF_PERIOD:
	    return prepareCreateEnrolmentOutOfPeriod(mapping, request);
	default:
	    throw new RuntimeException("Unknown event type");
	}

    }

    private ActionForward prepareCreateGratuityEvent(ActionMapping mapping, HttpServletRequest request) {
	request.setAttribute("accountingEventCreateBean", new AccountingEventCreateBean(getStudentCurricularPlan(request)));
	return mapping.findForward("createGratuityEvent");
    }

    public ActionForward prepareCreateGratuityEventInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
	return mapping.findForward("createGratuityEvent");
    }

    private EventType getEventType(HttpServletRequest request) {
	return EventType.valueOf(request.getParameter("eventType"));
    }

    public ActionForward createGratuityEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
	try {

	    executeService("CreateGratuityEvent", accountingEventCreateBean.getStudentCurricularPlan(), accountingEventCreateBean
		    .getExecutionYear());

	    addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

	    request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getIdInternal());
	    return prepare(mapping, form, request, response);

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("accountingEventCreateBean", accountingEventCreateBean);

	return mapping.findForward("createGratuityEvent");

    }

    private ActionForward prepareCreateAdministrativeOfficeFeeAndInsuranceEvent(ActionMapping mapping, HttpServletRequest request) {
	request.setAttribute("accountingEventCreateBean", new AccountingEventCreateBean(getStudentCurricularPlan(request)));
	return mapping.findForward("createAdministrativeOfficeFeeAndInsuranceEvent");
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsuranceEventInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
	return mapping.findForward("createAdministrativeOfficeFeeAndInsuranceEvent");
    }

    public ActionForward createAdministrativeOfficeFeeAndInsuranceEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
	try {

	    executeService("CreateAdministrativeOfficeFeeAndInsuranceEvent",
		    accountingEventCreateBean.getStudentCurricularPlan(), accountingEventCreateBean.getExecutionYear());

	    addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

	    request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getIdInternal());
	    return prepare(mapping, form, request, response);

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("accountingEventCreateBean", accountingEventCreateBean);

	return mapping.findForward("createAdministrativeOfficeFeeAndInsuranceEvent");

    }

    private ActionForward prepareCreateEnrolmentOutOfPeriod(ActionMapping mapping, HttpServletRequest request) {
	request.setAttribute("accountingEventCreateBean", new EnrolmentOutOfPeriodEventCreateBean(
		getStudentCurricularPlan(request)));
	return mapping.findForward("createEnrolmentOutOfPeriodEvent");
    }

    public ActionForward prepareCreateEnrolmentOutOfPeriodPostback(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
	RenderUtils.invalidateViewState("accountingEventCreateBean");

	return mapping.findForward("createEnrolmentOutOfPeriodEvent");
    }

    public ActionForward prepareCreateEnrolmentOutOfPeriodInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
	return mapping.findForward("createEnrolmentOutOfPeriodEvent");
    }

    public ActionForward createEnrolmentOutOfPeriodEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EnrolmentOutOfPeriodEventCreateBean accountingEventCreateBean = (EnrolmentOutOfPeriodEventCreateBean) getAccountingEventCreateBean();

	try {

	    executeService("CreateEnrolmentOutOfPeriodEvent", accountingEventCreateBean.getStudentCurricularPlan(),
		    accountingEventCreateBean.getExecutionPeriod(), accountingEventCreateBean.getNumberOfDelayDays());

	    addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

	    request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getIdInternal());
	    return prepare(mapping, form, request, response);

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("accountingEventCreateBean", accountingEventCreateBean);

	return mapping.findForward("createEnrolmentOutOfPeriodEvent");
    }

    private StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getIntegerFromRequest(request, "scpID"));
    }

}
