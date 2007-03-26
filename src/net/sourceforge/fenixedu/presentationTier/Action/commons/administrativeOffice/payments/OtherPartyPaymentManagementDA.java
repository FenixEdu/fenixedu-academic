package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateOtherPartyPaymentBean;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class OtherPartyPaymentManagementDA extends ReceiptsManagementDA {

    public ActionForward showEventsForOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("otherPartyPayment.showEvents");

    }

    public ActionForward showOtherPartyPaymentsForEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("event", getEvent(request));

	return mapping.findForward("otherPartyPayment.showPaymentsForEvent");
    }

    public ActionForward prepareCreateOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = new CreateOtherPartyPaymentBean(
		getEvent(request));

	request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	return mapping.findForward("otherPartyPayment.prepareCreate");
    }

    public ActionForward confirmCreateOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean",
		getCreateOtherPartyBeanFromViewState("createOtherPartyPayment"));

	return mapping.findForward("otherPartyPayment.confirmCreate");

    }

    public ActionForward createOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = getCreateOtherPartyBeanFromViewState("confirmCreateOtherPartyPayment");

	try {
	    executeService(request, "CreateOtherPartyPayment", new Object[] {
		    getUserView(request).getPerson(), createOtherPartyPaymentBean });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	    return mapping.findForward("otherPartyPayment.prepareCreate");
	}

	return showEventsForOtherPartyPayment(mapping, form, request, response);

    }

    private CreateOtherPartyPaymentBean getCreateOtherPartyBeanFromViewState(String name) {
	return (CreateOtherPartyPaymentBean) RenderUtils.getViewState(name).getMetaObject().getObject();
    }

    public ActionForward preparePrintGuideForOtherParty(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPayment",
		getObjectFromViewState("createOtherPartyPayment"));
	return mapping.findForward("otherPartyPayment.showGuide");
    }

    public ActionForward printGuideForOtherParty(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("currentUnit", getCurrentUnit(request));
	request.setAttribute("createOtherPartyPayment",
		getObjectFromViewState("createOtherPartyPayment"));

	return mapping.findForward("otherPartyPayment.printGuide");
    }

    public ActionForward prepareCreateOtherPartyPaymentInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean",
		getObjectFromViewState("createOtherPartyPayment"));
	return mapping.findForward("otherPartyPayment.prepareCreate");
    }
}
