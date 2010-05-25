package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.events.AccountingEventCreateBean;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;
import net.sourceforge.fenixedu.util.InvocationResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdAccountingEventsManagement", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "chooseEventType", path = "/phd/academicAdminOffice/payments/chooseEventType.jsp"),

@Forward(name = "createInsuranceEvent", path = "/phd/academicAdminOffice/payments/createInsuranceEvent.jsp")

})
public class PhdAccountingEventsManagementDA extends PhdProcessDA {

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("chooseEventType");
    }

    public ActionForward createPhdRegistrationFee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    PhdRegistrationFee.create(getProcess(request));
	} catch (DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}

	return prepare(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateInsuranceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("eventBean", new AccountingEventCreateBean());
	return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward prepareCreateInsuranceEventInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("eventBean", getRenderedObject("eventBean"));
	return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward createInsuranceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    final AccountingEventCreateBean bean = (AccountingEventCreateBean) getRenderedObject("eventBean");
	    final PhdIndividualProgramProcess process = getProcess(request);

	    if (process.getExecutionYear().isAfter(bean.getExecutionYear())) {
		addActionMessage("error", request, "error.phd.accounting.events.insurance.invalid.execution.period", process
			.getExecutionYear().getQualifiedName());
		return prepareCreateInsuranceEvent(mapping, actionForm, request, response);
	    }

	    final InvocationResult result = new AccountingEventsManager().createInsuranceEvent(process.getPerson(), bean
		    .getExecutionYear());

	    if (result.isSuccess()) {
		addActionMessage("success", request, "message.phd.accounting.events.insurance.created.with.success");
		return prepare(mapping, actionForm, request, response);
	    } else {
		addActionMessages("error", request, result.getMessages());
	    }

	} catch (DomainExceptionWithInvocationResult e) {
	    addActionMessages("error", request, e.getInvocationResult().getMessages());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	return prepareCreateInsuranceEvent(mapping, actionForm, request, response);
    }

}
