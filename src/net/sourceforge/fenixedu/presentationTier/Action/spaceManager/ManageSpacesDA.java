package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.AccessGroupPersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceComparator;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ManageSpacesDA extends FenixDispatchAction {

    public ActionForward viewSpaces(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final SortedSet<Space> spaces = new TreeSet<Space>(SpaceComparator.SPACE_COMPARATOR_BY_CLASS);

	// Filter OldBuildings and OldRooms. These two classes will soon be
	// removed from the source code.
	// When this happens, spaces.addAll(rootDomainObject.getSpacesSet())
	// will replace the following snip.
	// ---> Start of snip <---
	for (final Space space : rootDomainObject.getSpacesSet()) {
	    if (!(space instanceof OldBuilding) && !(space instanceof OldRoom)
		    && !space.hasSuroundingSpace()) {
		spaces.add(space);
	    }
	}
	// ---> End of snip <---

	request.setAttribute("spaces", spaces);
	return mapping.findForward("ShowSpaces");
    }

    protected SpaceInformation executeSpaceFactoryMethod(final HttpServletRequest request)
	    throws FenixFilterException, FenixServiceException {
	final Object serviceResult = executeFactoryMethod(request);
	if (serviceResult instanceof Space) {
	    return ((Space) serviceResult).getSpaceInformation();
	} else if (serviceResult instanceof SpaceInformation) {
	    return (SpaceInformation) serviceResult;
	} else {
	    return null;
	}
    }

    protected ActionForward manageSpace(final ActionMapping mapping, final HttpServletRequest request,
	    final SpaceInformation spaceInformation) {
	
	final Space space = spaceInformation.getSpace();
	request.setAttribute("selectedSpace", space);	
	SortedSet<Space> spaces = new TreeSet<Space>(SpaceComparator.SPACE_COMPARATOR_BY_CLASS);
	spaces.addAll(space.getContainedSpaces());	
	request.setAttribute("spaces", spaces);
	request.setAttribute("selectedSpaceInformation", spaceInformation);
	return mapping.findForward("ManageSpace");
    }

    public ActionForward manageSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	return manageSpace(mapping, request, spaceInformation);
    }

    public ActionForward executeFactoryMethod(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SpaceInformation spaceInformation = executeSpaceFactoryMethod(request);
	return (spaceInformation == null) ? viewSpaces(mapping, form, request, response) : manageSpace(
		mapping, request, spaceInformation);
    }

    public ActionForward deleteSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Space space = getSpaceFromParameter(request);
	final Space surroundingSpace = space.getSuroundingSpace();

	final Object[] args = { space };
	executeService(request, "DeleteSpace", args);

	if (surroundingSpace == null) {
	    return viewSpaces(mapping, form, request, response);
	} else {
	    final SpaceInformation surroundingSpaceInformation = surroundingSpace
		    .getOrderedSpaceInformations().last();
	    return manageSpace(mapping, request, surroundingSpaceInformation);
	}
    }

    public ActionForward setSelectedSpaceInformationAndForward(final ActionMapping mapping,
	    final HttpServletRequest request, final String forward) {
	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	request.setAttribute("selectedSpaceInformation", spaceInformation);
	return mapping.findForward(forward);
    }

    public ActionForward showCreateSubSpaceForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return setSelectedSpaceInformationAndForward(mapping, request, "ShowCreateSubSpaceForm");
    }

    public ActionForward viewSpaceInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return setSelectedSpaceInformationAndForward(mapping, request, "ViewSpaceInformation");
    }

    public ActionForward prepareEditSpace(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return setSelectedSpaceInformationAndForward(mapping, request, "EditSpace");
    }

    public ActionForward prepareCreateSpaceInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final FactoryExecutor factoryExecutor = spaceInformation.getSpaceFactoryEditor();
	request.setAttribute("SpaceFactoryEditor", factoryExecutor);
	return mapping.findForward("CreateSpaceInformation");
    }

    public ActionForward deleteSpaceInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final Space space = spaceInformation.getSpace();

	final Object[] args = { spaceInformation };
	try {
	    executeService(request, "DeleteSpaceInformation", args);
	} catch (DomainException e) {
	    saveMessages(request, e);
	}

	final SpaceInformation previousSpaceInformation = space.getOrderedSpaceInformations().last();
	return manageSpace(mapping, request, previousSpaceInformation);
    }

    public ActionForward manageAccessGroups(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final Space space = spaceInformation.getSpace();
	request.setAttribute("selectedSpace", space);
	request.setAttribute("selectedSpaceInformation", spaceInformation);
	return mapping.findForward("ManageSpaceAccessGroups");
    }

    public ActionForward addPersonToAccessGroup(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final Space space = spaceInformation.getSpace();

	final IViewState viewState = RenderUtils.getViewState();
	AccessGroupPersonBean bean = (AccessGroupPersonBean) viewState.getMetaObject().getObject();
	RenderUtils.invalidateViewState();

	final Object[] args = { space, (bean != null) ? bean.getAccessGroupType() : null,
		(bean != null) ? bean.getPerson() : null, true };
	try {
	    executeService(request, "SpaceAccessGroupsManagement", args);
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	}
	return manageAccessGroups(mapping, form, request, response);
    }

    public ActionForward removePersonFromAccessGroup(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final Space space = spaceInformation.getSpace();
	final SpaceAccessGroupType groupType = SpaceAccessGroupType.valueOf(request
		.getParameter("spaceAccessGroupType"));
	final Person person = getPersonFromParameter(request);

	final Object[] args = { space, groupType, person, false };
	try {
	    executeService(request, "SpaceAccessGroupsManagement", args);
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	}
	return manageAccessGroups(mapping, form, request, response);
    }

    // Private Methods
    private void saveMessages(HttpServletRequest request, DomainException e) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
	saveMessages(request, actionMessages);
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
	final String spaceInformationIDString = request.getParameterMap().containsKey(
		"spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
		.getAttribute("spaceInformationID");
	final Integer spaceInformationID = spaceInformationIDString != null ? Integer
		.valueOf(spaceInformationIDString) : null;
	return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }

    private Person getPersonFromParameter(final HttpServletRequest request) {
	final String personIDString = request.getParameterMap().containsKey("personID") ? request
		.getParameter("personID") : (String) request.getAttribute("personID");
	final Integer personID = personIDString != null ? Integer.valueOf(personIDString) : null;
	return (Person) rootDomainObject.readPartyByOID(personID);
    }

    private Space getSpaceFromParameter(final HttpServletRequest request) {
	final String spaceIDString = request.getParameter("spaceID");
	final Integer spaceID = Integer.valueOf(spaceIDString);
	return rootDomainObject.readSpaceByOID(spaceID);
    }
}