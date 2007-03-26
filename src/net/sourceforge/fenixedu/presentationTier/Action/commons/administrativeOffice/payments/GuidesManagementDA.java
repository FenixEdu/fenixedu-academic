package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class GuidesManagementDA extends PaymentsManagementDispatchAction {

    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return internalPreparePrintGuide(mapping, request, "showGuide", "showEvents");
    }

    public ActionForward preparePrintGuideWithInstallments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return internalPreparePrintGuide(mapping, request, "showGuide", "showEventsWithInstallments");
    }

    private ActionForward internalPreparePrintGuide(final ActionMapping mapping,
	    final HttpServletRequest request, final String successForward, final String errorForward) {

	final PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState(
		"paymentsManagementDTO").getMetaObject().getObject();
	request.setAttribute("paymentsManagementDTO", managementDTO);

	if (managementDTO.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.guide.entries.selection.is.required");
	    request.setAttribute("personId", managementDTO.getPerson().getIdInternal());
	    return mapping.findForward(errorForward);
	} else {
	    return mapping.findForward(successForward);
	}
    }

    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState(
		"paymentsManagementDTO").getMetaObject().getObject();
	request.setAttribute("paymentsManagementDTO", managementDTO);
	request.setAttribute("currentUnit", getCurrentUnit(request));
	return mapping.findForward("printGuide");
    }

}
