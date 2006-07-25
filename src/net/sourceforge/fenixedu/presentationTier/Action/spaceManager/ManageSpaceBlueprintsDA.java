package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ManageSpaceBlueprintsDA extends FenixDispatchAction {

    public ActionForward showBlueprintVersions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        Blueprint blueprint = getSpaceBlueprintFromParameter(request);

        if (blueprint == null) {
            blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        }

        setBlueprint(request, blueprint);
        setSpaceInfo(request, spaceInformation);                
        return mapping.findForward("showBlueprintVersions");
    }

    public ActionForward createBlueprintVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState("spaceBlueprintVersion");
        final CreateBlueprintSubmissionBean blueprintSubmissionBean = (CreateBlueprintSubmissionBean) viewState
                .getMetaObject().getObject();

        SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
        
        Blueprint newBlueprint = null;
        try {
            newBlueprint = (Blueprint) ServiceUtils.executeService(getUserView(request), "CreateNewBlueprintVersion",
                    new Object[] { blueprintSubmissionBean });

        } catch (DomainException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            newBlueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        } catch (FileManagerException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            newBlueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        }

        //RenderUtils.invalidateViewState();                
        setBlueprint(request, newBlueprint);
        setSpaceInfo(request, spaceInformation);        
        return mapping.findForward("showBlueprintVersions");
    }
    
    public ActionForward prepareCreateBlueprintVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInfo(request, spaceInformation);
        request.setAttribute("blueprintBean", new CreateBlueprintSubmissionBean(spaceInformation));        
        return mapping.findForward("createNewBlueprintVersion");
    }
    
    public ActionForward prepareEditBlueprintVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
     
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        Blueprint blueprint = getSpaceBlueprintFromParameter(request);
        setBlueprint(request, blueprint);
        setSpaceInfo(request, spaceInformation);        
        request.setAttribute("editBlueprint", true);
        request.setAttribute("blueprintBean", new CreateBlueprintSubmissionBean(spaceInformation));
        return mapping.findForward("createNewBlueprintVersion");
    }
    
    public ActionForward editBlueprintVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    
        final IViewState viewState = RenderUtils.getViewState("spaceBlueprintVersion");
        final CreateBlueprintSubmissionBean blueprintSubmissionBean = (CreateBlueprintSubmissionBean) viewState
                .getMetaObject().getObject();
        
        SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
        Blueprint blueprint = getSpaceBlueprintFromParameter(request);
        
        try {
            ServiceUtils.executeService(getUserView(request), "EditBlueprintVersion",
                    new Object[] { blueprint, blueprintSubmissionBean });

        } catch (DomainException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        } catch (FileManagerException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        }

        //RenderUtils.invalidateViewState();                
        setBlueprint(request, blueprint);
        setSpaceInfo(request, spaceInformation);        
        return mapping.findForward("showBlueprintVersions");
    }
    
    public ActionForward deleteBlueprintVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        Blueprint blueprint = getSpaceBlueprintFromParameter(request);
        
        try {
            ServiceUtils.executeService(getUserView(request), "DeleteBlueprintVersion",
                    new Object[] { blueprint });

        } catch (DomainException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
        }
        
        setBlueprint(request, spaceInformation.getSpace().getMostRecentBlueprint());
        setSpaceInfo(request, spaceInformation);        
        return mapping.findForward("showBlueprintVersions");
    }

    private void setSpaceInfo(HttpServletRequest request, SpaceInformation spaceInformation) {
        if(spaceInformation != null) {
            request.setAttribute("selectedSpaceInformation", spaceInformation);
            request.setAttribute("selectedSpace", spaceInformation.getSpace());
        }
    }

    private void setBlueprint(HttpServletRequest request, Blueprint blueprint) {
        if(blueprint != null) {
            request.setAttribute("selectedSpaceBlueprint", blueprint);
        }
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey(
                "spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                .getAttribute("spaceInformationID");
        final Integer spaceInformationID = (!StringUtils.isEmpty(spaceInformationIDString)) ? Integer
                .valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }

    private Blueprint getSpaceBlueprintFromParameter(HttpServletRequest request) {
        final String spaceBlueprintIDString = request.getParameterMap().containsKey("spaceBlueprintID") ? request
                .getParameter("spaceBlueprintID")
                : (String) request.getAttribute("spaceBlueprintID");
        final Integer spaceBlueprintID = (!StringUtils.isEmpty(spaceBlueprintIDString)) ? Integer
                .valueOf(spaceBlueprintIDString) : null;
        return rootDomainObject.readBlueprintByOID(spaceBlueprintID);
    }

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(errorKey, new ActionMessage(errorKey, args));
        saveMessages(request, actionMessages);
    }
}
