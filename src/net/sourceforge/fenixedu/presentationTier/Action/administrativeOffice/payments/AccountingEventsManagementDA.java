package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateGratuityAndAdminOfficeFeeEventBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AccountingEventsManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final CreateGratuityAndAdminOfficeFeeEventBean createGratuityAndAdminOfficeFeeEventBean = new CreateGratuityAndAdminOfficeFeeEventBean(
		getStudentCurricularPlan(request));
	createGratuityAndAdminOfficeFeeEventBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	request.setAttribute("createGratuityAndAdminOfficeFeeBean", createGratuityAndAdminOfficeFeeEventBean);

	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward prepareInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createGratuityAndAdminOfficeFeeBean", getRenderedObject("createGratuityAndAdminOfficeFeeBean"));

	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward createGratuityEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateGratuityAndAdminOfficeFeeEventBean createGratuityAndAdminOfficeFeeEventBean = (CreateGratuityAndAdminOfficeFeeEventBean) getRenderedObject("createGratuityAndAdminOfficeFeeBean");
	try {

	    executeService("CreateGratuityEvent", createGratuityAndAdminOfficeFeeEventBean.getStudentCurricularPlan(),
		    createGratuityAndAdminOfficeFeeEventBean.getExecutionYear());

	    request.setAttribute("createGratuityAndAdminOfficeFeeBean", createGratuityAndAdminOfficeFeeEventBean);

	    addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

	    return mapping.findForward("chooseExecutionYear");

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("createGratuityAndAdminOfficeFeeBean", createGratuityAndAdminOfficeFeeEventBean);
	return mapping.findForward("chooseExecutionYear");

    }

    public ActionForward createAdministrativeOfficeFeeAndInsuranceEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateGratuityAndAdminOfficeFeeEventBean createGratuityAndAdminOfficeFeeEventBean = (CreateGratuityAndAdminOfficeFeeEventBean) getRenderedObject("createGratuityAndAdminOfficeFeeBean");
	try {

	    executeService("CreateAdministrativeOfficeFeeAndInsuranceEvent", createGratuityAndAdminOfficeFeeEventBean
		    .getStudentCurricularPlan(), createGratuityAndAdminOfficeFeeEventBean.getExecutionYear());

	    request.setAttribute("createGratuityAndAdminOfficeFeeBean", createGratuityAndAdminOfficeFeeEventBean);

	    addActionMessage("success", request, "label.accountingEvents.management.createEvents.eventCreatedWithSucess");

	    return mapping.findForward("chooseExecutionYear");

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("createGratuityAndAdminOfficeFeeBean", createGratuityAndAdminOfficeFeeEventBean);
	return mapping.findForward("chooseExecutionYear");

    }

    private StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getIntegerFromRequest(request, "scpID"));
    }

}
