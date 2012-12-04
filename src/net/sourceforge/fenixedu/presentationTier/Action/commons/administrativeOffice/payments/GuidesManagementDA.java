package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.presentationTier.docs.accounting.GuideDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Forwards({
	@Forward(name = "showGuide", path = "/academicAdminOffice/payments/guides/showGuide.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "showEvents", path = "/payments.do?method=showEvents", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")) })
public abstract class GuidesManagementDA extends PaymentsManagementDispatchAction {

    @Override
    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO")
		.getMetaObject().getObject();
	request.setAttribute("paymentsManagementDTO", managementDTO);

	if (managementDTO.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.guide.entries.selection.is.required");
	    request.setAttribute("personId", managementDTO.getPerson().getIdInternal());
	    return mapping.findForward("showEvents");
	} else {
	    return mapping.findForward("showGuide");
	}
    }

    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JRException {

	PaymentsManagementDTO managementDTO = (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO")
		.getMetaObject().getObject();

	final GuideDocument document = new GuideDocument(managementDTO, getCurrentUnit(request),
		getMessageResourceProvider(request));
	final byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(document);

	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

	response.getOutputStream().write(data);

	return null;

    }

}
