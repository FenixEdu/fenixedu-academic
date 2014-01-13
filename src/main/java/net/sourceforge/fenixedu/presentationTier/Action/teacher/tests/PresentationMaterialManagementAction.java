package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CreatePictureMaterial;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeletePresentationMaterial;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.SwitchPosition;
import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterialType;
import net.sourceforge.fenixedu.domain.tests.NewStringMaterial;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(module = "teacher", path = "/tests/questionBank/presentationMaterial", attribute = "testForm", formBean = "testForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "createPresentationMaterial",
                path = "/teacher/tests/questionBank/presentationMaterial/createPresentationMaterial.jsp", tileProperties = @Tile(
                        navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "editPresentationMaterials",
                path = "/teacher/tests/questionBank/presentationMaterial/editPresentationMaterials.jsp", tileProperties = @Tile(
                        navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "createPictureMaterial",
                path = "/teacher/tests/questionBank/presentationMaterial/createPictureMaterial.jsp", tileProperties = @Tile(
                        navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "deletePresentationMaterial",
                path = "/teacher/tests/questionBank/presentationMaterial/deletePresentationMaterial.jsp", tileProperties = @Tile(
                        navLocal = "/teacher/commons/navigationBarIndex.jsp")) })
public class PresentationMaterialManagementAction extends FenixDispatchAction {

    public ActionForward invalidPrepareCreatePresentationMaterial(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("choose-material-type");

        request.setAttribute("bean", bean);

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward prepareCreatePresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("choose-material-type");

        request.setAttribute("bean", bean);

        if (bean.getPresentationMaterialType().equals(NewPresentationMaterialType.PICTURE)) {
            return mapping.findForward("createPictureMaterial");
        }

        return mapping.findForward("createPresentationMaterial");
    }

    public ActionForward prepareEditPresentationMaterials(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        NewTestElement testElement = getDomainObject(request, "oid");

        request.setAttribute("bean", new PresentationMaterialBean(testElement, request.getParameter("returnPath"),
                getCodeFromRequest(request, "returnId"), request.getParameter("contextKey")));

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward editPresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        NewTestElement testElement = getDomainObject(request, "oid");

        request.setAttribute("bean", new PresentationMaterialBean(testElement, request.getParameter("returnPath"),
                getCodeFromRequest(request, "returnId"), request.getParameter("contextKey")));

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward prepareDeletePresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewPresentationMaterial presentationMaterial = getDomainObject(request, "oid");

        request.setAttribute("presentationMaterial", presentationMaterial);
        request.setAttribute("bean",
                new PresentationMaterialBean(presentationMaterial.getTestElement(), request.getParameter("returnPath"),
                        getCodeFromRequest(request, "returnId"), request.getParameter("contextKey")));

        return mapping.findForward("deletePresentationMaterial");
    }

    public ActionForward deletePresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewPresentationMaterial presentationMaterial = getDomainObject(request, "oid");

        request.setAttribute("oid", presentationMaterial.getTestElement().getExternalId());

        if (presentationMaterial instanceof NewMathMlMaterial) {
            DeletePresentationMaterial.run((NewMathMlMaterial) presentationMaterial);
        } else if (presentationMaterial instanceof NewPictureMaterial) {
            DeletePresentationMaterial.run((NewPictureMaterial) presentationMaterial);
        } else if (presentationMaterial instanceof NewStringMaterial) {
            DeletePresentationMaterial.run((NewStringMaterial) presentationMaterial);
        }
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("delete-presentation-material");

        request.setAttribute("bean", bean);

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward switchPresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewPresentationMaterial presentationMaterial = getDomainObject(request, "oid");

        Integer relativePosition = getCodeFromRequest(request, "relativePosition");

        SwitchPosition.run(presentationMaterial, relativePosition);

        request.setAttribute("bean",
                new PresentationMaterialBean(presentationMaterial.getTestElement(), request.getParameter("returnPath"),
                        getCodeFromRequest(request, "returnId"), request.getParameter("contextKey")));

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward createPresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("edit-presentation-materials");

        bean.setPresentationMaterialType(null);

        RenderUtils.invalidateViewState("edit-presentation-materials");

        request.setAttribute("bean", bean);

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward createPictureMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("create-picture-material");

        User userView = getUserView(request);

        File file = FileUtils.copyToTemporaryFile(bean.getInputStream());
        try {
            CreatePictureMaterial.run(userView.getPerson().getTeacher(), bean.getTestElement(), bean.isInline(), file,
                    bean.getOriginalFileName(), bean.getFileName());
        } finally {
            file.delete();
        }
        request.setAttribute("bean", new PresentationMaterialBean(bean));

        return mapping.findForward("editPresentationMaterials");
    }

    public ActionForward invalidCreatePresentationMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("edit-presentation-materials");

        request.setAttribute("bean", bean);

        return mapping.findForward("createPresentationMaterial");
    }

    public ActionForward invalidCreatePictureMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        PresentationMaterialBean bean = (PresentationMaterialBean) getMetaObject("create-picture-material");

        request.setAttribute("bean", bean);

        return mapping.findForward("createPictureMaterial");
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);

        if (objectCode != null) {
            if (objectCode instanceof String) {
                code = new Integer((String) objectCode);
            } else if (objectCode instanceof Integer) {
                code = (Integer) objectCode;
            }
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null) {
                code = new Integer(thisCodeString);
            }
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
        return new ActionForward(request.getParameter("returnTo") + "&oid=" + testElement.getExternalId(), true);
    }
}