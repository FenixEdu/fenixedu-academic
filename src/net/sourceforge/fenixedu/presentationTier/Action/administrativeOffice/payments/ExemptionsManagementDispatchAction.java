package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExemptionsManagementDispatchAction extends
	AcademicAdminOfficePaymentsManagementDispatchAction {

    public ActionForward showEventsToApplyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getPerson(request);
	request.setAttribute("person", person);
	request.setAttribute("eventsToApplyExemption", person.getEventsWithExemptionAppliable());

	return mapping.findForward("showEventsToApplyExemption");
    }

    public ActionForward prepareCreateGratuityExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createGratuityExemptionBean", new CreateGratuityExemptionBean(
		getGratuityEvent(request)));

	return mapping.findForward("createGratuityExemption");
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping,
	    HttpServletRequest request, String propertyName, String messageKey) {
	return invalidCreateGratuityExemption(mapping, request, propertyName, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping,
	    HttpServletRequest request, String messageKey, String[] args) {
	return invalidCreateGratuityExemption(mapping, request, null, messageKey, args);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping,
	    HttpServletRequest request, String messageKey) {
	return invalidCreateGratuityExemption(mapping, request, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping,
	    HttpServletRequest request, String propertyName, String messageKey, String[] args) {

	if (propertyName == null) {
	    addActionMessage(request, messageKey, args);
	} else {
	    addActionMessage(propertyName, request, messageKey, args);
	}

	request.setAttribute("createGratuityExemptionBean", RenderUtils.getViewState(
		"createGratuityExemptionBean").getMetaObject().getObject());

	return mapping.findForward("createGratuityExemption");
    }

    public ActionForward createGratuityExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateGratuityExemptionBean createGratuityExemptionBean = (CreateGratuityExemptionBean) RenderUtils
		.getViewState("createGratuityExemptionBean").getMetaObject().getObject();

	if (!createGratuityExemptionBean.isPercentageExemption()
		&& createGratuityExemptionBean.getAmount() == null) {
	    return invalidCreateGratuityExemption(mapping, request, "context",
		    "error.payments.gratuityExemption.amount.or.percentage.are.required");
	}

	if (createGratuityExemptionBean.isPercentageExemption()
		&& createGratuityExemptionBean.getAmount() != null) {
	    return invalidCreateGratuityExemption(mapping, request, "context",
		    "error.payments.gratuityExemption.cannot.select.both.amount.and.percentage");
	}

	try {
	    executeService(request, "CreateGratuityExemption", new Object[] {
		    getUserView(request).getPerson().getEmployee(), createGratuityExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    return invalidCreateGratuityExemption(mapping, request, ex.getKey(),
		    solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    return invalidCreateGratuityExemption(mapping, request, ex.getKey());

	}

	request.setAttribute("eventId", createGratuityExemptionBean.getGratuityEvent().getIdInternal());

	return showExemptions(mapping, form, request, response);
    }

    public ActionForward deleteExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final Exemption exemption = getExemption(request);
	request.setAttribute("eventId", exemption.getEvent().getIdInternal());

	try {
	    executeService(request, "DeleteExemption", new Object[] { exemption });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	}

	return showExemptions(mapping, form, request, response);

    }

    public ActionForward prepareCreateInstallmentPenaltyExemption(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createInstallmentPenaltyExemptionBean",
		new CreateInstallmentPenaltyExemptionBean(
			(GratuityEventWithPaymentPlan) getGratuityEvent(request)));

	// Logic to decide the view to show based on gratuity event should
	// be placed here
	return mapping.findForward("createInstallmentPenaltyExemption");

    }

    public ActionForward prepareCreateInstallmentPenaltyExemptionInvalid(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("createInstallmentPenaltyExemptionBean", RenderUtils.getViewState(
		"create-installment-penalty-exemption-bean").getMetaObject().getObject());

	return mapping.findForward("createInstallmentPenaltyExemption");
    }

    public ActionForward createInstallmentPenaltyExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateInstallmentPenaltyExemptionBean createInstallmentPenaltyExemptionBean = (CreateInstallmentPenaltyExemptionBean) RenderUtils
		.getViewState("create-installment-penalty-exemption-bean").getMetaObject().getObject();
	request.setAttribute("eventId", createInstallmentPenaltyExemptionBean
		.getGratuityEventWithPaymentPlan().getIdInternal());

	try {
	    executeService(request, "CreateInstallmentPenaltyExemption", new Object[] {
		    getLoggedPerson(request).getEmployee(), createInstallmentPenaltyExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));

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

    public ActionForward showExemptions(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

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
	} else {
	    throw new UnsupportedOperationException();
	}
    }

    private Exemption getExemption(final HttpServletRequest request) {
	return (Exemption) rootDomainObject.readExemptionByOID(getIntegerFromRequest(request,
		"exemptionId"));
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemption(
	    ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createPenaltyExemptionBean",
		new CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean(
			(AdministrativeOfficeFeeAndInsuranceEvent) getEvent(request)));

	return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");

    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(
	    ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("createPenaltyExemptionBean", RenderUtils.getViewState(
		"create-penalty-exemption-bean").getMetaObject().getObject());

	return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");
    }

    public ActionForward createAdministrativeOfficeFeeAndInsurancePenaltyExemption(
	    ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean = (CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean) RenderUtils
		.getViewState("create-penalty-exemption-bean").getMetaObject().getObject();
	request.setAttribute("eventId", penaltyExemptionBean.getEvent().getIdInternal());

	try {
	    executeService(request, "CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption",
		    new Object[] { getLoggedPerson(request).getEmployee(), penaltyExemptionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));

	    return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping,
		    form, request, response);

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping,
		    form, request, response);
	}

	return showExemptions(mapping, form, request, response);
    }

}
