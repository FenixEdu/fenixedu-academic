package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ManagePersonSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	Person loggedPerson = AccessControl.getUserView().getPerson();
	if (spaceInformation.getSpace().personHasPermissionsToManageSpace(loggedPerson)
		|| spaceInformation.getSpace()
			.personHasPermissionToManagePersonOccupations(loggedPerson)) {
	    return super.execute(mapping, actionForm, request, response);
	} else {
	    final ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("error", new ActionMessage(
		    "error.logged.person.not.authorized.to.make.operation"));
	    saveMessages(request, actionMessages);
	}
	request.setAttribute("spaceInformationID", spaceInformation.getIdInternal());
	return mapping.findForward("ManageSpace");
    }

    public ActionForward showSpaceOccupations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	setSpaceAndSpaceInfo(request, spaceInformation);
	return mapping.findForward("showSpaceOccupations");
    }

    public ActionForward prepareEditSpacePersonOccupation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final PersonSpaceOccupation personSpaceOccupation = getPersonSpaceOccupationFromParameter(request);
	setSpaceAndSpaceInfo(request, spaceInformation);
	request.setAttribute("personSpaceOccupation", personSpaceOccupation);
	return showSpaceOccupations(mapping, form, request, response);
    }

    public ActionForward deleteSpacePersonOccupation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	final PersonSpaceOccupation personSpaceOccupation = getPersonSpaceOccupationFromParameter(request);
	Object[] args = { personSpaceOccupation };
	try {
	    executeService(request, "DeletePersonSpaceOccupation", args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	setSpaceAndSpaceInfo(request, spaceInformation);
	return mapping.findForward("showSpaceOccupations");
    }

    private void setSpaceAndSpaceInfo(HttpServletRequest request, final SpaceInformation spaceInformation) {
	request.setAttribute("selectedSpaceInformation", spaceInformation);
	request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
	final String spaceInformationIDString = request.getParameterMap().containsKey(
		"spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
		.getAttribute("spaceInformationID");
	final Integer spaceInformationID = spaceInformationIDString != null ? Integer
		.valueOf(spaceInformationIDString) : null;
	return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }

    private PersonSpaceOccupation getPersonSpaceOccupationFromParameter(final HttpServletRequest request) {
	final String personSpaceOccupationIDString = request.getParameterMap().containsKey(
		"spaceOccupationID") ? request.getParameter("spaceOccupationID") : (String) request
		.getAttribute("spaceOccupationID");
	final Integer personSpaceOccupationID = personSpaceOccupationIDString != null ? Integer
		.valueOf(personSpaceOccupationIDString) : null;
	return (PersonSpaceOccupation) rootDomainObject
		.readSpaceOccupationByOID(personSpaceOccupationID);
    }
}
