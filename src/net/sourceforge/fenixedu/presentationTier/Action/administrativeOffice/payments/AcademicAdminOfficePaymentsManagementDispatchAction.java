package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AcademicAdminOfficePaymentsManagementDispatchAction extends
	PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return AdministrativeOffice.readByEmployee(getUserView(request).getPerson().getEmployee());
    }

    @Override
    protected void setContextInformation(HttpServletRequest request) {
	request.setAttribute("student", getPerson(request).getStudent());
    }

    @Override
    protected ActionForward findMainForward(ActionMapping mapping) {
	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward showGratuityEvents(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getPerson(request);
	request.setAttribute("person", person);
	request.setAttribute("gratuityEvents", person.getGratuityEvents());

	return mapping.findForward("gratuityExemptions.showGratuityEvents");
    }

    public ActionForward prepareCreateExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createGratuityExemptionBean", new CreateGratuityExemptionBean(
		getGratuityEvent(request)));

	return mapping.findForward("gratuityExemptions.create");
    }

    private ActionForward invalidCreateExemption(ActionMapping mapping, HttpServletRequest request,
	    String messageKey) {

	addActionMessage(request, messageKey);

	request.setAttribute("createGratuityExemptionBean", RenderUtils.getViewState(
		"createGratuityExemptionBean").getMetaObject().getObject());

	return mapping.findForward("gratuityExemptions.create");
    }

    public ActionForward createExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateGratuityExemptionBean createGratuityExemptionBean = (CreateGratuityExemptionBean) RenderUtils
		.getViewState("createGratuityExemptionBean").getMetaObject().getObject();

	if (!createGratuityExemptionBean.isPercentageExemption()
		&& createGratuityExemptionBean.getAmount() == null) {
	    return invalidCreateExemption(mapping, request,
		    "error.payments.gratuityExemption.amount.or.percentage.are.required");
	}

	if (createGratuityExemptionBean.isPercentageExemption()
		&& createGratuityExemptionBean.getAmount() != null) {
	    return invalidCreateExemption(mapping, request,
		    "error.payments.gratuityExemption.cannot.select.both.amount.and.percentage");
	}

	try {
	    executeService(request, "CreateGratuityExemption", new Object[] {
		    getUserView(request).getPerson().getEmployee(), createGratuityExemptionBean });
	} catch (DomainException ex) {
	    return invalidCreateExemption(mapping, request, ex.getKey());
	}

	request.setAttribute("personId", createGratuityExemptionBean.getGratuityEvent().getPerson()
		.getIdInternal());

	return showGratuityEvents(mapping, form, request, response);
    }

    private GratuityEvent getGratuityEvent(HttpServletRequest request) {
	return (GratuityEvent) rootDomainObject.readDomainObjectEventByOID(getRequestParameterAsInteger(
		request, "gratuityEventId"));
    }

    public ActionForward viewExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("gratuityExemption", getGratuityEvent(request).getGratuityExemption());

	return mapping.findForward("gratuityExemptions.view");

    }

    public ActionForward deleteExemption(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final GratuityExemption gratuityExemption = (GratuityExemption) RenderUtils.getViewState(
		"gratuityExemption").getMetaObject().getObject();
	try {
	    executeService(request, "DeleteGratuityExemption", new Object[] { gratuityExemption });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("gratuityExemption", gratuityExemption);
	    return mapping.findForward("gratuityExemptions.view");
	}

	request.setAttribute("personId", getPerson(request).getIdInternal());

	return showGratuityEvents(mapping, form, request, response);

    }

}
