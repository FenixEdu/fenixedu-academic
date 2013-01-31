package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.serviceRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdDocumentRequestManagement", module = "academicAdministration")
@Forwards({ @Forward(
		name = "createNewDocumentRequest",
		path = "/phd/academicAdminOffice/serviceRequests/document/createNewDocumentRequest.jsp") })
public class PhdDocumentRequestManagementDA extends PhdAcademicServiceRequestsManagementDA {

	@Override
	protected PhdDocumentRequestCreateBean getPhdAcademicServiceRequestCreateBean() {
		return (PhdDocumentRequestCreateBean) super.getPhdAcademicServiceRequestCreateBean();
	}

	@Override
	protected PhdDocumentRequest getPhdAcademicServiceRequest(HttpServletRequest request) {
		return (PhdDocumentRequest) super.getPhdAcademicServiceRequest(request);
	}

	@Override
	public ActionForward prepareCreateNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
		PhdDocumentRequestCreateBean phdDocumentRequestCreateBean = new PhdDocumentRequestCreateBean(process);
		request.setAttribute("phdAcademicServiceRequestCreateBean", phdDocumentRequestCreateBean);

		return mapping.findForward("createNewDocumentRequest");
	}

	@Override
	public ActionForward createNewRequestInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		super.createNewRequestInvalid(mapping, form, request, response);
		return mapping.findForward("prepareCreateNewRequest");
	}

	public ActionForward createNewRequestPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("phdAcademicServiceRequestCreateBean", getPhdAcademicServiceRequestCreateBean());

		RenderUtils.invalidateViewState("phd-academic-service-request-create-bean");
		RenderUtils.invalidateViewState("phd-academic-service-request-create-bean-choose-document-type");

		return mapping.findForward("createNewDocumentRequest");
	}

}
