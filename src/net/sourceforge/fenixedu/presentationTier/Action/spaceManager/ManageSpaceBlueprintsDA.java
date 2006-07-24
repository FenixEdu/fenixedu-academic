package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
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
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class ManageSpaceBlueprintsDA extends FenixDispatchAction {

    public ActionForward showBlueprintVersions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInfo(request, spaceInformation);
        setNewBlueprintBean(request, spaceInformation);
        setMostRecentBlueprintFile(request, spaceInformation);
        return mapping.findForward("showBlueprintVersions");
    }

    public ActionForward submitBlueprint(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState("spaceBlueprintVersion");
        final CreateBlueprintSubmissionBean blueprintSubmissionBean = (CreateBlueprintSubmissionBean) viewState
                .getMetaObject().getObject();

        try {
            ServiceUtils.executeService(getUserView(request), "CreateNewBlueprintVersion",
                    new Object[] { blueprintSubmissionBean });

        } catch (DomainException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            RenderUtils.invalidateViewState();

        } catch (FileManagerException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
            RenderUtils.invalidateViewState();
        }
               
        request.setAttribute("blueprintBean", blueprintSubmissionBean);
        setMostRecentBlueprintFile(request, blueprintSubmissionBean.getSpaceInformation());
        setSpaceInfo(request, blueprintSubmissionBean.getSpaceInformation());
        return mapping.findForward("showBlueprintVersions");
    }

    private void setNewBlueprintBean(HttpServletRequest request, SpaceInformation spaceInformation) {
        request.setAttribute("blueprintBean", new CreateBlueprintSubmissionBean(spaceInformation));
    }

    private void setSpaceInfo(HttpServletRequest request, SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }

    private void setMostRecentBlueprintFile(HttpServletRequest request, SpaceInformation spaceInformation) {
        Blueprint mostRecentBlueprint = spaceInformation.getSpace().getMostRecentBlueprint();
        BlueprintFile blueprintFile = (mostRecentBlueprint != null) ? mostRecentBlueprint
                .getBlueprintFile() : null;
        if (blueprintFile != null) {
            String directDownloadUrlFormat = FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(blueprintFile.getExternalStorageIdentification(),
                            blueprintFile.getFilename());

            request.setAttribute("directDownloadUrlFormat", directDownloadUrlFormat);
            request.setAttribute("blueprint", mostRecentBlueprint);
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

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(errorKey, new ActionMessage(errorKey, args));
        saveMessages(request, actionMessages);
    }
}
