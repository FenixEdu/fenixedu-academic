package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteMaterialSpaceOccupation;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "SpaceManager", path = "/manageMaterialSpaceOccupations", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareEditMaterialSpaceOccupation", path = "/spaceManager/editMaterialSpaceOccupation.jsp"),
        @Forward(name = "showMaterialSpaceOccupations", path = "/spaceManager/manageMaterialSpaceOccupations.jsp",
                tileProperties = @Tile(title = "private.spacemanagement.searchspaces")),
        @Forward(name = "insertMaterialOccupation", path = "/spaceManager/insertNewMaterialOccupation.jsp"),
        @Forward(name = "chooseMaterialType", path = "/spaceManager/insertNewMaterialOccupation.jsp") })
public class ManageMaterialSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showMaterialSpaceOccupations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        readAndSetSpaceInformation(request);
        return mapping.findForward("showMaterialSpaceOccupations");
    }

    public ActionForward prepareInsertMaterialOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        IViewState viewState = RenderUtils.getViewState("materialTypeWithMaterial");
        if (viewState != null) {
            MaterialTypeBean materialTypeBean = (MaterialTypeBean) viewState.getMetaObject().getObject();
            if (materialTypeBean.getMaterialType() == null) {
                addActionMessage(request, "error.material.not.found");
            }
            request.setAttribute("prepare-search-was-called", Boolean.TRUE);
        }
        viewState = (viewState == null) ? RenderUtils.getViewState("materialTypeToCreate") : viewState;
        return setMaterialTypeBean(mapping, request, viewState);
    }

    public ActionForward prepareChooseMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        IViewState viewState = RenderUtils.getViewState();
        RenderUtils.invalidateViewState();
        return setMaterialTypeBean(mapping, request, viewState);
    }

    public ActionForward prepareEditMaterialSpaceOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        readAndSetSpaceInformation(request);
        MaterialSpaceOccupation materialOccupation = getMaterialOccupationFromParameter(request);
        request.setAttribute("materialSpaceOccupation", materialOccupation);
        return mapping.findForward("prepareEditMaterialSpaceOccupation");
    }

    public ActionForward deleteMaterialSpaceOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        MaterialSpaceOccupation materialOccupation = getMaterialOccupationFromParameter(request);

        try {
            DeleteMaterialSpaceOccupation.run(materialOccupation);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        readAndSetSpaceInformation(request);
        return mapping.findForward("showMaterialSpaceOccupations");
    }

    public ActionForward prepareChooseMaterialType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        SpaceInformation spaceInformation = readAndSetSpaceInformation(request);
        request.setAttribute("materialTypeBean", new MaterialTypeBean(spaceInformation));
        return mapping.findForward("insertMaterialOccupation");
    }

    private ActionForward setMaterialTypeBean(ActionMapping mapping, HttpServletRequest request, IViewState viewState) {
        final MaterialTypeBean materialTypeBean = (MaterialTypeBean) viewState.getMetaObject().getObject();
        request.setAttribute("materialTypeBean", materialTypeBean);
        return mapping.findForward("insertMaterialOccupation");
    }

    private SpaceInformation readAndSetSpaceInformation(HttpServletRequest request) {
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
        return spaceInformation;
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString =
                request.getParameterMap().containsKey("spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                        .getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer.valueOf(spaceInformationIDString) : null;
        return AbstractDomainObject.fromExternalId(spaceInformationID);
    }

    private MaterialSpaceOccupation getMaterialOccupationFromParameter(final HttpServletRequest request) {
        final String materialOccupationIDString =
                request.getParameterMap().containsKey("materialOccupationID") ? request.getParameter("materialOccupationID") : (String) request
                        .getAttribute("materialOccupationID");
        final Integer materialOccupationID =
                materialOccupationIDString != null ? Integer.valueOf(materialOccupationIDString) : null;
        return (MaterialSpaceOccupation) AbstractDomainObject.fromExternalId(materialOccupationID);
    }
}