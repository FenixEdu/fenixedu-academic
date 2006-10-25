package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageMaterialSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showMaterialSpaceOccupations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        readAndSetSpaceInformation(request);
        return mapping.findForward("showMaterialSpaceOccupations");
    }

    public ActionForward prepareInsertMaterialOccupation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
               
        IViewState viewState = RenderUtils.getViewState("materialTypeWithMaterial");
        if(viewState != null) {
            MaterialTypeBean materialTypeBean = (MaterialTypeBean) viewState.getMetaObject().getObject();
            if(materialTypeBean.getMaterial() == null) {
        	addActionMessage(request, "error.material.not.found");
            }
        }
        viewState = (viewState == null) ? RenderUtils.getViewState("materialTypeToCreate") : viewState;               
        return setMaterialTypeBean(mapping, request, viewState);
    }       
    
    public ActionForward prepareChooseMaterial(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        IViewState viewState = RenderUtils.getViewState();  
        RenderUtils.invalidateViewState();
        return setMaterialTypeBean(mapping, request, viewState);
    }  
    
    public ActionForward prepareEditMaterialSpaceOccupation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        
        readAndSetSpaceInformation(request);
        MaterialSpaceOccupation materialOccupation = getMaterialOccupationFromParameter(request);
        request.setAttribute("materialSpaceOccupation", materialOccupation);
        return mapping.findForward("prepareEditMaterialSpaceOccupation");        
    }
    
    public ActionForward deleteMaterialSpaceOccupation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        
        MaterialSpaceOccupation materialOccupation = getMaterialOccupationFromParameter(request);
        Class occupationClass = materialOccupation.getMaterial().getMaterialSpaceOccupationSubClass();
        Object[] args = { occupationClass.cast(materialOccupation) };        
        try {
            executeService(request, "DeleteMaterialSpaceOccupation", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}	
        readAndSetSpaceInformation(request);
        return mapping.findForward("showMaterialSpaceOccupations");           
    }

    public ActionForward prepareChooseMaterialType(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

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
        final String spaceInformationIDString = request.getParameterMap().containsKey(
                "spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                .getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer
                .valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }
    
    private MaterialSpaceOccupation getMaterialOccupationFromParameter(final HttpServletRequest request) {
        final String materialOccupationIDString = request.getParameterMap().containsKey(
                "materialOccupationID") ? request.getParameter("materialOccupationID") : (String) request
                .getAttribute("materialOccupationID");
        final Integer materialOccupationID = materialOccupationIDString != null ? Integer
                .valueOf(materialOccupationIDString) : null;
        return (MaterialSpaceOccupation)rootDomainObject.readSpaceOccupationByOID(materialOccupationID);
    }
}
