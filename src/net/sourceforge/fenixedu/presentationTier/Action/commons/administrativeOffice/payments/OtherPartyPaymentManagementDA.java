package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CreateOtherPartyPayment;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateOtherPartyPaymentBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Forwards({
	@Forward(name = "showEvents", path = "/academicAdminOffice/payments/otherPartyPayment/showEventsForOtherPartyPayment.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "showPaymentsForEvent", path = "/academicAdminOffice/payments/otherPartyPayment/showOtherPartyPaymentsForEvent.jsp", tileProperties = @Tile(title = "wow2")),
	@Forward(name = "prepareCreate", path = "/academicAdminOffice/payments/otherPartyPayment/prepareCreateOtherPartyPayment.jsp", tileProperties = @Tile(title = "wow3")),
	@Forward(name = "confirmCreate", path = "/academicAdminOffice/payments/otherPartyPayment/confirmCreateOtherPartyPayment.jsp", tileProperties = @Tile(title = "wow4")),
	@Forward(name = "showGuide", path = "/academicAdminOffice/payments/otherPartyPayment/showGuideForOtherPartyPayment.jsp", tileProperties = @Tile(title = "wow5")),
	@Forward(name = "printGuide", path = "/payments/otherPartyPayment/printGuideForOtherPartyPayment.jsp", /*
													        * useTile
													        * =
													        * false
													        */tileProperties = @Tile(title = "wow6")),
	@Forward(name = "showOperations", path = "/payments.do?method=showOperations", tileProperties = @Tile(title = "wow7")) })
public abstract class OtherPartyPaymentManagementDA extends PaymentsManagementDispatchAction {

    public ActionForward showEventsForOtherPartyPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEvents");

    }

    public ActionForward showOtherPartyPaymentsForEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("event", getEvent(request));

	return mapping.findForward("showPaymentsForEvent");
    }

    public ActionForward prepareCreateOtherPartyPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = new CreateOtherPartyPaymentBean(getEvent(request));

	request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	return mapping.findForward("prepareCreate");
    }

    public ActionForward confirmCreateOtherPartyPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean", getCreateOtherPartyBeanFromViewState("createOtherPartyPayment"));

	return mapping.findForward("confirmCreate");

    }

    public ActionForward createOtherPartyPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = getCreateOtherPartyBeanFromViewState("confirmCreateOtherPartyPayment");

	try {
	    CreateOtherPartyPayment.run(getUserView(request).getPerson(), createOtherPartyPaymentBean);
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	    return mapping.findForward("prepareCreate");
	}

	return showEventsForOtherPartyPayment(mapping, form, request, response);

    }

    private CreateOtherPartyPaymentBean getCreateOtherPartyBeanFromViewState(String name) {
	return (CreateOtherPartyPaymentBean) RenderUtils.getViewState(name).getMetaObject().getObject();
    }

    public ActionForward preparePrintGuideForOtherParty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createOtherPartyPayment", getObjectFromViewState("createOtherPartyPayment"));

	return mapping.findForward("showGuide");
    }

    public ActionForward printGuideForOtherParty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("currentUnit", getCurrentUnit(request));
	request.setAttribute("createOtherPartyPayment", getObjectFromViewState("createOtherPartyPayment"));

	return mapping.findForward("printGuide");
    }

    public ActionForward prepareCreateOtherPartyPaymentInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean", getObjectFromViewState("createOtherPartyPayment"));

	return mapping.findForward("prepareCreate");
    }

    @Override
    public ActionForward backToShowOperations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final ActionForward actionForward = new ActionForward(mapping.findForward("showOperations").getPath() + "&"
		+ buildContextParameters(request));

	actionForward.setRedirect(true);

	return actionForward;
    }

    protected String buildContextParameters(final HttpServletRequest request) {
	return "personId=" + getPerson(request).getIdInternal();
    }

}