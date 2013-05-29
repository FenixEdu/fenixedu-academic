package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.AccountingEventsCreator;
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
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/accountingEventsManagement", module = "academicAdministration")
@Forwards({

        @Forward(name = "chooseEventType", path = "/academicAdminOffice/accountingEventsManagement/chooseEventType.jsp"),
        @Forward(name = "createGratuityEvent", path = "/academicAdminOffice/accountingEventsManagement/createGratuityEvent.jsp"),
        @Forward(name = "createAdministrativeOfficeFeeAndInsuranceEvent",
                path = "/academicAdminOffice/accountingEventsManagement/createAdministrativeOfficeFeeAndInsuranceEvent.jsp"),
        @Forward(name = "createInsuranceEvent", path = "/academicAdminOffice/accountingEventsManagement/createInsuranceEvent.jsp"),
        @Forward(name = "createEnrolmentOutOfPeriodEvent",
                path = "/academicAdminOffice/accountingEventsManagement/createEnrolmentOutOfPeriodEvent.jsp"),
        @Forward(name = "createDfaRegistrationEvent",
                path = "/academicAdminOffice/accountingEventsManagement/createDfaRegistrationEvent.jsp")

})
public class AccountingEventsManagementDA extends FenixDispatchAction {

    private static List<EventType> supportedEventTypes = Arrays.asList(EventType.GRATUITY,
            EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, EventType.ENROLMENT_OUT_OF_PERIOD);

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);

        request.setAttribute("eventTypes", supportedEventTypes);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        /**
         * The insurance is an {@link EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE} if the target
         * degree has no PhdProgram associated
         */
        request.setAttribute("officeFeeInsurance", !studentCurricularPlan.getDegree().hasPhdProgram());

        return mapping.findForward("chooseEventType");

    }

    public ActionForward prepareInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("eventTypes", supportedEventTypes);

        return mapping.findForward("chooseEventType");
    }

    private AccountingEventCreateBean getAccountingEventCreateBean() {
        return getRenderedObject("accountingEventCreateBean");
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
        case INSURANCE:
            return prepareCreateInsuranceEvent(mapping, request);
        case DFA_REGISTRATION:
            return prepareCreateDfaRegistration(mapping, request);
        default:
            throw new RuntimeException("Unknown event type");
        }

    }

    private ActionForward prepareCreateDfaRegistration(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("accountingEventCreateBean", new AccountingEventCreateBean(getStudentCurricularPlan(request)));
        return mapping.findForward("createDfaRegistrationEvent");
    }

    public ActionForward prepareCreateDfaRegistrationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
        return mapping.findForward("createDfaRegistrationEvent");
    }

    public ActionForward createDfaRegistrationEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
        try {
            AccountingEventsCreator.createDfaRegistrationEvent(accountingEventCreateBean.getStudentCurricularPlan(),
                    accountingEventCreateBean.getExecutionYear());

            addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

            request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getExternalId());
            return prepare(mapping, form, request, response);

        } catch (DomainExceptionWithInvocationResult e) {
            addActionMessages("error", request, e.getInvocationResult().getMessages());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        request.setAttribute("accountingEventCreateBean", accountingEventCreateBean);

        return mapping.findForward("createDfaRegistrationEvent");
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
            HttpServletResponse response) {

        final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
        try {

            AccountingEventsCreator.createGratuityEvent(accountingEventCreateBean.getStudentCurricularPlan(),
                    accountingEventCreateBean.getExecutionYear());

            addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

            request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getExternalId());
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
            HttpServletRequest request, HttpServletResponse response) {

        final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
        try {

            AccountingEventsCreator.createAdministrativeOfficeFeeAndInsuranceEvent(
                    accountingEventCreateBean.getStudentCurricularPlan(), accountingEventCreateBean.getExecutionYear());

            addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

            request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getExternalId());
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
            HttpServletResponse response) {

        final EnrolmentOutOfPeriodEventCreateBean accountingEventCreateBean =
                (EnrolmentOutOfPeriodEventCreateBean) getAccountingEventCreateBean();

        try {

            AccountingEventsCreator.createEnrolmentOutOfPeriodEvent(accountingEventCreateBean.getStudentCurricularPlan(),
                    accountingEventCreateBean.getExecutionPeriod(), accountingEventCreateBean.getNumberOfDelayDays());

            addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

            request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getExternalId());
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
        return getDomainObject(request, "scpID");
    }

    private ActionForward prepareCreateInsuranceEvent(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("accountingEventCreateBean", new AccountingEventCreateBean(getStudentCurricularPlan(request)));
        return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward prepareCreateInsuranceEventInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("accountingEventCreateBean", getAccountingEventCreateBean());
        return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward createInsuranceEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final AccountingEventCreateBean accountingEventCreateBean = getAccountingEventCreateBean();
        try {

            AccountingEventsCreator.createInsuranceEvent(accountingEventCreateBean.getStudentCurricularPlan(),
                    accountingEventCreateBean.getExecutionYear());

            addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

            request.setAttribute("scpID", accountingEventCreateBean.getStudentCurricularPlan().getExternalId());
            return prepare(mapping, form, request, response);

        } catch (DomainExceptionWithInvocationResult e) {
            addActionMessages("error", request, e.getInvocationResult().getMessages());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        request.setAttribute("accountingEventCreateBean", accountingEventCreateBean);

        return mapping.findForward("createInsuranceEvent");

    }

}
