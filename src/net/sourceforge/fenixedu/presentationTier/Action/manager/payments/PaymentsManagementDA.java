package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CancelEventBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.TransferPaymentsToOtherEventAndCancelBean;
import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PaymentsManagementDA extends FenixDispatchAction {

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studentNumberBean", new StudentNumberBean());

	return mapping.findForward("chooseStudent");
    }

    public ActionForward chooseStudentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studentNumberBean", getObjectFromViewState("studentNumberBean"));

	return mapping.findForward("chooseStudent");
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Student student = getStudent();
	request.setAttribute("person", student.getPerson());

	return mapping.findForward("showEvents");
    }

    public ActionForward showPaymentsForEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("event", getEvent(request));

	return mapping.findForward("showPaymentsForEvent");
    }

    private Student getStudent() {
	final StudentNumberBean studentNumberBean = (StudentNumberBean) getObjectFromViewState("studentNumberBean");
	return Student.readStudentByNumber(studentNumberBean.getNumber());
    }

    public ActionForward prepareCancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("cancelEventBean", new CancelEventBean(getEvent(request), getLoggedPerson(request).getEmployee()));

	return mapping.findForward("editCancelEventJustification");
    }

    public ActionForward cancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CancelEventBean cancelEventBean = getCancelEventBean();

	try {
	    executeService("CancelEvent", cancelEventBean.getEvent(), cancelEventBean.getEmployee(), cancelEventBean
		    .getJustification());
	} catch (DomainExceptionWithLabelFormatter ex) {

	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("cancelEventBean", cancelEventBean);

	    return mapping.findForward("editCancelEventJustification");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("cancelEventBean", cancelEventBean);

	    return mapping.findForward("editCancelEventJustification");
	}

	request.setAttribute("person", cancelEventBean.getEvent().getPerson());

	return mapping.findForward("showEvents");

    }

    private CancelEventBean getCancelEventBean() {
	final CancelEventBean cancelEventBean = (CancelEventBean) getObjectFromViewState("cancelEventBean");
	return cancelEventBean;
    }

    public ActionForward backToShowEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEvents");

    }

    private Person getPerson(HttpServletRequest request) {
	return (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));
    }

    private Event getEvent(HttpServletRequest request) {
	return rootDomainObject.readEventByOID(getRequestParameterAsInteger(request, "eventId"));
    }

    public ActionForward prepareTransferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Event event = getEvent(request);

	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean = new TransferPaymentsToOtherEventAndCancelBean(
		event, getLoggedPerson(request).getEmployee());

	request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	return mapping.findForward("chooseTargetEventForPaymentsTransfer");

    }

    public ActionForward transferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean = (TransferPaymentsToOtherEventAndCancelBean) getObjectFromViewState("transferPaymentsBean");

	try {
	    executeService("TransferPaymentsToOtherEventAndCancel", transferPaymentsBean.getEmployee(), transferPaymentsBean
		    .getSourceEvent(), transferPaymentsBean.getTargetEvent(), transferPaymentsBean.getCancelJustification());
	} catch (DomainExceptionWithLabelFormatter ex) {

	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	    return mapping.findForward("chooseTargetEventForPaymentsTransfer");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	    return mapping.findForward("chooseTargetEventForPaymentsTransfer");
	}

	request.setAttribute("event", transferPaymentsBean.getSourceEvent());

	return mapping.findForward("showPaymentsForEvent");

    }

}
