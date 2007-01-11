package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
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
import net.sourceforge.fenixedu.util.spaceBlueprints.SpaceBlueprintsDWGProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ManageSpaceBlueprintsDA extends FenixDispatchAction {

    public ActionForward showBlueprintVersions(final ActionMapping mapping,
	    final HttpServletRequest request, final SpaceInformation spaceInformation,
	    final Blueprint blueprint) {
	setBlueprint(request, blueprint);
	setSpaceInfo(request, spaceInformation);
	return mapping.findForward("showBlueprintVersions");
    }

    public ActionForward showBlueprintVersions(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	Blueprint blueprint = getSpaceBlueprintFromParameter(request);

	if (blueprint == null) {
	    blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	}

	return showBlueprintVersions(mapping, request, spaceInformation, blueprint);
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
	    newBlueprint = (Blueprint) ServiceUtils.executeService(getUserView(request),
		    "CreateNewBlueprintVersion", new Object[] { blueprintSubmissionBean });

	} catch (DomainException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    newBlueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	} catch (FileManagerException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    newBlueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	}

	return showBlueprintVersions(mapping, request, spaceInformation, newBlueprint);
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
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final IViewState viewState = RenderUtils.getViewState("spaceBlueprintVersion");
	final CreateBlueprintSubmissionBean blueprintSubmissionBean = (CreateBlueprintSubmissionBean) viewState
		.getMetaObject().getObject();

	SpaceInformation spaceInformation = blueprintSubmissionBean.getSpaceInformation();
	Blueprint blueprint = getSpaceBlueprintFromParameter(request);

	try {
	    ServiceUtils.executeService(getUserView(request), "EditBlueprintVersion", new Object[] {
		    blueprint, blueprintSubmissionBean });

	} catch (DomainException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	} catch (FileManagerException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	}

	return showBlueprintVersions(mapping, request, spaceInformation, blueprint);
    }

    public ActionForward deleteBlueprintVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	Blueprint blueprint = getSpaceBlueprintFromParameter(request);

	try {
	    ServiceUtils.executeService(getUserView(request), "DeleteBlueprintVersion",
		    new Object[] { blueprint });

	} catch (DomainException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	} catch (FileManagerException ex) {
	    saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());
	    blueprint = spaceInformation.getSpace().getMostRecentBlueprint();
	}

	setBlueprint(request, spaceInformation.getSpace().getMostRecentBlueprint());
	setSpaceInfo(request, spaceInformation);
	return mapping.findForward("showBlueprintVersions");
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	
	Boolean isSuroundingSpaceBlueprint = isSuroundingSpaceBlueprint(request);
	Boolean isToViewOriginalSpaceBlueprint = isToViewOriginalSpaceBlueprint(request);
	
	Boolean viewBlueprintNumbers = isToViewBlueprintNumbers(request);
	Boolean isToViewIdentifications = isToViewSpaceIdentifications(request);
	Boolean isToViewDoorNumbers = isToViewDoorNumbers(request); 

	final String blueprintIdString = request.getParameter("blueprintId");
	final Integer blueprintId = Integer.valueOf(blueprintIdString);
	final Blueprint blueprint = rootDomainObject.readBlueprintByOID(blueprintId);
	final BlueprintFile blueprintFile = blueprint.getBlueprintFile();

	// If dspace worked properly we could do this...
	// final byte[] blueprintBytes =
	// FileManagerFactory.getFileManager().retrieveFile(blueprintFile.getExternalStorageIdentification());
	// Science it doesn't, we'll do...
	
	final byte[] blueprintBytes = blueprintFile.getContent().getBytes();
	final InputStream inputStream = new ByteArrayInputStream(blueprintBytes);

	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=blueprint.jpeg");
	final ServletOutputStream writer = response.getOutputStream();

	SpaceBlueprintsDWGProcessor processor = null;

	if (isToViewOriginalSpaceBlueprint != null && isToViewOriginalSpaceBlueprint) {
	    processor = new SpaceBlueprintsDWGProcessor();

	} else if (isSuroundingSpaceBlueprint != null && isSuroundingSpaceBlueprint) {
	    SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	    processor = new SpaceBlueprintsDWGProcessor(blueprint.getSpace(), spaceInformation.getSpace(), viewBlueprintNumbers, isToViewIdentifications, isToViewDoorNumbers);

	} else {
	    processor = new SpaceBlueprintsDWGProcessor(blueprint.getSpace(), viewBlueprintNumbers, isToViewIdentifications, isToViewDoorNumbers);
	}

	if (processor != null) {
	    processor.generateJPEGImage(inputStream, writer);
	}

	return null;
    }
    
    private void setSpaceInfo(HttpServletRequest request, SpaceInformation spaceInformation) {
	if (spaceInformation != null) {
	    request.setAttribute("selectedSpaceInformation", spaceInformation);
	    request.setAttribute("selectedSpace", spaceInformation.getSpace());
	}
    }

    private void setBlueprint(HttpServletRequest request, Blueprint blueprint) {
	if (blueprint != null) {
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

    private Boolean isToViewBlueprintNumbers(HttpServletRequest request) {
	final String viewBlueprintNumbersString = request.getParameterMap().containsKey(
		"viewBlueprintNumbers") ? request.getParameter("viewBlueprintNumbers")
		: (String) request.getAttribute("viewBlueprintNumbers");
	return viewBlueprintNumbersString != null ? Boolean.valueOf(viewBlueprintNumbersString) : null;
    }

    private Boolean isSuroundingSpaceBlueprint(HttpServletRequest request) {
	final String suroundingSpaceBlueprintString = request.getParameterMap().containsKey(
		"suroundingSpaceBlueprint") ? request.getParameter("suroundingSpaceBlueprint")
		: (String) request.getAttribute("suroundingSpaceBlueprint");
	return suroundingSpaceBlueprintString != null ? Boolean.valueOf(suroundingSpaceBlueprintString)
		: null;
    }

    private Boolean isToViewOriginalSpaceBlueprint(HttpServletRequest request) {
	final String viewOriginalSpaceBlueprintString = request.getParameterMap().containsKey(
		"viewOriginalSpaceBlueprint") ? request.getParameter("viewOriginalSpaceBlueprint")
		: (String) request.getAttribute("viewOriginalSpaceBlueprint");
	return viewOriginalSpaceBlueprintString != null ? Boolean
		.valueOf(viewOriginalSpaceBlueprintString) : null;
    }
    
    private Boolean isToViewSpaceIdentifications(HttpServletRequest request) {
	final String viewSpaceIdentificationsString = request.getParameterMap().containsKey(
		"viewSpaceIdentifications") ? request.getParameter("viewSpaceIdentifications")
		: (String) request.getAttribute("viewSpaceIdentifications");
	return viewSpaceIdentificationsString != null ? Boolean
		.valueOf(viewSpaceIdentificationsString) : null;
    }
    
    private Boolean isToViewDoorNumbers(HttpServletRequest request) {
	final String viewDoorNumbersString = request.getParameterMap().containsKey(
		"viewDoorNumbers") ? request.getParameter("viewDoorNumbers")
		: (String) request.getAttribute("viewDoorNumbers");
	return viewDoorNumbersString != null ? Boolean
		.valueOf(viewDoorNumbersString) : null;
    }

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add(errorKey, new ActionMessage(errorKey, args));
	saveMessages(request, actionMessages);
    } 
}
