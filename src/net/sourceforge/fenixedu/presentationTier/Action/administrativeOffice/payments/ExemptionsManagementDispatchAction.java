package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SecondCycleIndividualCandidacyExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/exemptionsManagement", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "showEventsToApplyExemption", path = "/academicAdminOffice/payments/exemptions/showEventsToApplyExemption.jsp"),
	@Forward(name = "showForGratuityEvent", path = "/academicAdminOffice/payments/exemptions/showForGratuityEvent.jsp"),
	@Forward(name = "showForImprovementOfApprovedEnrolmentEvent", path = "/academicAdminOffice/payments/exemptions/showForImprovementOfApprovedEnrolmentEvent.jsp"),
	@Forward(name = "showForAdministrativeOfficeFeeAndInsuranceEvent", path = "/academicAdminOffice/payments/exemptions/showForAdministrativeOfficeFeeAndInsuranceEvent.jsp"),
	@Forward(name = "showForSecondCycleIndividualCandidacyEvent", path = "/academicAdminOffice/payments/exemptions/showForSecondCycleIndividualCandidacyEvent.jsp"),
	@Forward(name = "createGratuityExemption", path = "/academicAdminOffice/payments/exemptions/gratuity/create.jsp"),
	@Forward(name = "createInstallmentPenaltyExemption", path = "/academicAdminOffice/payments/exemptions/penalty/createInstallmentExemption.jsp"),
	@Forward(name = "createImprovementOfApprovedEnrolmentPenaltyExemption", path = "/academicAdminOffice/payments/exemptions/penalty/createImprovementOfApprovedEnrolmentExemption.jsp"),
	@Forward(name = "createAdministrativeOfficeFeeAndInsurancePenaltyExemption", path = "/academicAdminOffice/payments/exemptions/penalty/createAdministrativeOfficeFeeAndInsuranceExemption.jsp"),
	@Forward(name = "createSecondCycleIndividualCandidacyExemption", path = "/academicAdminOffice/payments/exemptions/createSecondCycleIndividualCandidacyExemption.jsp")

})
public class ExemptionsManagementDispatchAction extends AcademicAdminOfficePaymentsManagementDispatchAction {

    public ActionForward showEventsToApplyExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = getPerson(request);
	request.setAttribute("person", person);
	request.setAttribute("eventsToApplyExemption", person.getEventsWithExemptionAppliable());

	return mapping.findForward("showEventsToApplyExemption");
    }

    public ActionForward prepareCreateGratuityExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createGratuityExemptionBean", new CreateGratuityExemptionBean(getGratuityEvent(request)));

	return mapping.findForward("createGratuityExemption");
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String propertyName,
	    String messageKey) {
	return invalidCreateGratuityExemption(mapping, request, propertyName, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String messageKey,
	    String[] args) {
	return invalidCreateGratuityExemption(mapping, request, null, messageKey, args);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String messageKey) {
	return invalidCreateGratuityExemption(mapping, request, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String propertyName,
	    String messageKey, String[] args) {

	if (propertyName == null) {
	    addActionMessage(request, messageKey, args);
	} else {
	    addActionMessage(propertyName, request, messageKey, args);
	}

	request.setAttribute("createGratuityExemptionBean", getRenderedObject("createGratuityExemptionBean"));

	return mapping.findForward("createGratuityExemption");
    }

    public ActionForward createGratuityExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateGratuityExemptionBean createGratuityExemptionBean = (CreateGratuityExemptionBean) getRenderedObject("createGratuityExemptionBean");

	if (!createGratuityExemptionBean.isPercentageExemption() && createGratuityExemptionBean.getAmount() == null) {
	    return invalidCreateGratuityExemption(mapping, request, "context",
		    "error.payments.gratuityExemption.amount.or.percentage.are.required");
	}

	if (createGratuityExemptionBean.isPercentageExemption() && createGratuityExemptionBean.getAmount() != null) {
	    return invalidCreateGratuityExemption(mapping, request, "context",
		    "error.payments.gratuityExemption.cannot.select.both.amount.and.percentage");
	}

	try {
	    executeService(request, "CreateGratuityExemption", new Object[] { getUserView(request).getPerson().getEmployee(),
		    createGratuityExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    return invalidCreateGratuityExemption(mapping, request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    return invalidCreateGratuityExemption(mapping, request, ex.getKey());

	}

	request.setAttribute("eventId", createGratuityExemptionBean.getGratuityEvent().getIdInternal());

	return showExemptions(mapping, form, request, response);
    }

    public ActionForward deleteExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Exemption exemption = getExemption(request);
	request.setAttribute("eventId", exemption.getEvent().getIdInternal());

	try {
	    executeService(request, "DeleteExemption", new Object[] { exemption });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	}

	return showExemptions(mapping, form, request, response);

    }

    public ActionForward prepareCreateInstallmentPenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createInstallmentPenaltyExemptionBean", new CreateInstallmentPenaltyExemptionBean(
		(GratuityEventWithPaymentPlan) getGratuityEvent(request)));

	// Logic to decide the view to show based on gratuity event should
	// be placed here
	return mapping.findForward("createInstallmentPenaltyExemption");

    }

    public ActionForward prepareCreateInstallmentPenaltyExemptionInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createInstallmentPenaltyExemptionBean",
		getRenderedObject("create-installment-penalty-exemption-bean"));

	return mapping.findForward("createInstallmentPenaltyExemption");
    }

    public ActionForward createInstallmentPenaltyExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateInstallmentPenaltyExemptionBean createInstallmentPenaltyExemptionBean = (CreateInstallmentPenaltyExemptionBean) RenderUtils
		.getViewState("create-installment-penalty-exemption-bean").getMetaObject().getObject();
	request.setAttribute("eventId", createInstallmentPenaltyExemptionBean.getGratuityEventWithPaymentPlan().getIdInternal());

	try {
	    executeService(request, "CreateInstallmentPenaltyExemption", new Object[] { getLoggedPerson(request).getEmployee(),
		    createInstallmentPenaltyExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));

	    return prepareCreateInstallmentPenaltyExemptionInvalid(mapping, form, request, response);

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    return prepareCreateInstallmentPenaltyExemptionInvalid(mapping, form, request, response);
	}

	return showExemptions(mapping, form, request, response);
    }

    private GratuityEvent getGratuityEvent(HttpServletRequest request) {
	return (GratuityEvent) getEvent(request);
    }

    public ActionForward showExemptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Event event = getEvent(request);
	request.setAttribute("person", event.getPerson());
	request.setAttribute("event", event);

	if (event instanceof GratuityEvent) {
	    if (event instanceof GratuityEventWithPaymentPlan) {
		request.setAttribute("hasPaymentPlan", true);
	    }
	    return mapping.findForward("showForGratuityEvent");
	} else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
	    return mapping.findForward("showForAdministrativeOfficeFeeAndInsuranceEvent");
	} else if (event instanceof ImprovementOfApprovedEnrolmentEvent) {
	    return mapping.findForward("showForImprovementOfApprovedEnrolmentEvent");
	} else if (event instanceof SecondCycleIndividualCandidacyEvent) {
	    return mapping.findForward("showForSecondCycleIndividualCandidacyEvent");
	} else {
	    throw new UnsupportedOperationException();
	}
    }

    private Exemption getExemption(final HttpServletRequest request) {
	return (Exemption) rootDomainObject.readExemptionByOID(getIntegerFromRequest(request, "exemptionId"));
    }

    public ActionForward prepareCreateImprovementOfApprovedEnrolmentPenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createPenaltyExemptionBean", new CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean(
		(ImprovementOfApprovedEnrolmentEvent) getEvent(request)));

	return mapping.findForward("createImprovementOfApprovedEnrolmentPenaltyExemption");

    }

    public ActionForward prepareCreateImprovementOfApprovedEnrolmentPenaltyExemptionInvalid(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));
	return mapping.findForward("createImprovementOfApprovedEnrolmentPenaltyExemption");
    }

    public ActionForward createImprovementOfApprovedEnrolmentPenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean penaltyExemptionBean = (CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean) RenderUtils
		.getViewState("create-penalty-exemption-bean").getMetaObject().getObject();
	request.setAttribute("eventId", penaltyExemptionBean.getEvent().getIdInternal());

	try {
	    executeService(request, "CreateImprovementOfApprovedEnrolmentPenaltyExemption", new Object[] {
		    getLoggedPerson(request).getEmployee(), penaltyExemptionBean });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    return prepareCreateImprovementOfApprovedEnrolmentPenaltyExemptionInvalid(mapping, form, request, response);
	}

	return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createPenaltyExemptionBean", new CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean(
		(AdministrativeOfficeFeeAndInsuranceEvent) getEvent(request)));

	return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");

    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));

	return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");
    }

    public ActionForward createAdministrativeOfficeFeeAndInsurancePenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean = (CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean) RenderUtils
		.getViewState("create-penalty-exemption-bean").getMetaObject().getObject();
	request.setAttribute("eventId", penaltyExemptionBean.getEvent().getIdInternal());

	try {
	    executeService(request, "CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption", new Object[] {
		    getLoggedPerson(request).getEmployee(), penaltyExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));

	    return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping, form, request, response);

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping, form, request, response);
	}

	return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateSecondCycleIndividualCandidacyExemption(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createPenaltyExemptionBean", new SecondCycleIndividualCandidacyExemptionBean(
		(SecondCycleIndividualCandidacyEvent) getEvent(request)));
	return mapping.findForward("createSecondCycleIndividualCandidacyExemption");
    }

    public ActionForward prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));
	return mapping.findForward("createSecondCycleIndividualCandidacyExemption");
    }

    public ActionForward createSecondCycleIndividualCandidacyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeService("CreateSecondCycleIndividualCandidacyExemption", getLoggedPerson(request).getEmployee(),
		    getRenderedObject("create-penalty-exemption-bean"));

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    return prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(mapping, form, request, response);

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    return prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(mapping, form, request, response);
	}

	return showExemptions(mapping, form, request, response);
    }
}
