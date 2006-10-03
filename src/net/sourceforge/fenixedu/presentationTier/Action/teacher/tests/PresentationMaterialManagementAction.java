package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterialType;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PresentationMaterialManagementAction extends FenixDispatchAction {

	public ActionForward invalidPrepareCreatePresentationMaterial(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("choose-material-type");

		request.setAttribute("bean", bean);

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward prepareCreatePresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("choose-material-type");

		request.setAttribute("bean", bean);

		if(bean.getPresentationMaterialType().equals(NewPresentationMaterialType.PICTURE)) {
			return mapping.findForward("createPictureMaterial");
		}
		
		return mapping.findForward("createPresentationMaterial");
	}

	public ActionForward prepareEditPresentationMaterials(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer testElementId = getCodeFromRequest(request, "oid");

		NewTestElement testElement = rootDomainObject.readNewTestElementByOID(testElementId);

		request.setAttribute("bean", new PresentationMaterialBean(testElement, request
				.getParameter("returnPath"), getCodeFromRequest(request, "returnId"), request
				.getParameter("contextKey")));

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward editPresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer testElementId = getCodeFromRequest(request, "oid");

		NewTestElement testElement = rootDomainObject.readNewTestElementByOID(testElementId);

		request.setAttribute("bean", new PresentationMaterialBean(testElement, request
				.getParameter("returnPath"), getCodeFromRequest(request, "returnId"), request
				.getParameter("contextKey")));

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward prepareDeletePresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer presentationMaterialId = getCodeFromRequest(request, "oid");

		NewPresentationMaterial presentationMaterial = rootDomainObject
				.readNewPresentationMaterialByOID(presentationMaterialId);

		request.setAttribute("presentationMaterial", presentationMaterial);
		request.setAttribute("bean", new PresentationMaterialBean(presentationMaterial.getTestElement(),
				request.getParameter("returnPath"), getCodeFromRequest(request, "returnId"), request
						.getParameter("contextKey")));

		return mapping.findForward("deletePresentationMaterial");
	}

	public ActionForward deletePresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer presentationMaterialId = getCodeFromRequest(request, "oid");

		NewPresentationMaterial presentationMaterial = rootDomainObject
				.readNewPresentationMaterialByOID(presentationMaterialId);

		request.setAttribute("oid", presentationMaterial.getTestElement().getIdInternal());

		ServiceUtils.executeService(getUserView(request), "DeletePresentationMaterial",
				new Object[] { presentationMaterial });

		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("delete-presentation-material");

		request.setAttribute("bean", bean);

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward switchPresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		Integer presentationMaterialId = getCodeFromRequest(request, "oid");

		NewPresentationMaterial presentationMaterial = rootDomainObject
				.readNewPresentationMaterialByOID(presentationMaterialId);

		Integer relativePosition = getCodeFromRequest(request, "relativePosition");

		ServiceUtils.executeService(getUserView(request), "SwitchPosition",
				new Object[] { presentationMaterial, relativePosition });
		
		request.setAttribute("bean", new PresentationMaterialBean(presentationMaterial.getTestElement(),
				request.getParameter("returnPath"), getCodeFromRequest(request, "returnId"), request
						.getParameter("contextKey")));

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward createPresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("edit-presentation-materials");
		
		bean.setPresentationMaterialType(null);
		
		RenderUtils.invalidateViewState("edit-presentation-materials");
		
		request.setAttribute("bean", bean);

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward createPictureMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("create-picture-material");
		
		IUserView userView = getUserView(request);
		
		ServiceUtils.executeService(userView, "CreatePictureMaterial",
				new Object[] { userView.getPerson().getTeacher(), bean.getTestElement(), bean.isInline(),
			bean.getInputStream(), bean.getOriginalFileName(), bean.getFileName() });
		
		request.setAttribute("bean", new PresentationMaterialBean(bean));

		return mapping.findForward("editPresentationMaterials");
	}

	public ActionForward invalidCreatePresentationMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("edit-presentation-materials");
		
		request.setAttribute("bean", bean);

		return mapping.findForward("createPresentationMaterial");
	}

	public ActionForward invalidCreatePictureMaterial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("create-picture-material");
		
		request.setAttribute("bean", bean);

		return mapping.findForward("createPictureMaterial");
	}

	private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
		Integer code = null;
		Object objectCode = request.getAttribute(codeString);

		if (objectCode != null) {
			if (objectCode instanceof String)
				code = new Integer((String) objectCode);
			else if (objectCode instanceof Integer)
				code = (Integer) objectCode;
		} else {
			String thisCodeString = request.getParameter(codeString);
			if (thisCodeString != null)
				code = new Integer(thisCodeString);
		}

		return code;
	}

	private Object getMetaObject() {
		return getMetaObject(null);
	}

	private Object getMetaObject(String key) {
		IViewState viewState;

		if (key == null) {
			viewState = RenderUtils.getViewState(key);
		} else {
			viewState = RenderUtils.getViewState();
		}

		/*
		 * if (viewState == null || !viewState.isValid()) { return null; }
		 */

		return viewState.getMetaObject().getObject();
	}

	private ActionForward getReturnAction(HttpServletRequest request, NewTestElement testElement) {
		return new ActionForward(request.getParameter("returnTo") + "&oid="
				+ testElement.getIdInternal(), true);
	}
}
