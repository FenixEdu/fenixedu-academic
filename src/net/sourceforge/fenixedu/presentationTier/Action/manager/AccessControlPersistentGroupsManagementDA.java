package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AccessControlPersistentGroupsManagementDA extends FenixDispatchAction {

    public ActionForward listAllGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	request.setAttribute("persistentGroups", rootDomainObject.getPersistentGroupMembers());
	return mapping.findForward("seeAllPersistentGroups");
    }

    public ActionForward prepareCreateNewGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return mapping.findForward("prepareCreateNewPersistentGroup");
    }

    public ActionForward prepareEditPersistentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
	request.setAttribute("persistentGroup", persistentGroup);
	return mapping.findForward("prepareCreateNewPersistentGroup");
    }

    public ActionForward deletePersistentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
	executeService(request, "DeletePersistentGroupInManager", new Object[] { persistentGroup });
	return listAllGroups(mapping, form, request, response);
    }

    public ActionForward removePersistentGroupMember(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
	Person person = getPersonFromParameter(request);
	executeService(request, "RemovePersistentGroupMemberInManager", new Object[] { person, persistentGroup });
	return prepareEditPersistentGroup(mapping, form, request, response);
    }

    protected PersistentGroupMembers getPersistentGroupFromParameter(final HttpServletRequest request) {
	final String persistentGroupIDString = request.getParameter("persistentGroupID");
	final Integer persistentGroupID = Integer.valueOf(persistentGroupIDString);
	return rootDomainObject.readPersistentGroupMembersByOID(persistentGroupID);
    }

    protected Person getPersonFromParameter(final HttpServletRequest request) {
	final String personIDString = request.getParameter("personID");
	final Integer personID = Integer.valueOf(personIDString);
	return (Person) rootDomainObject.readPartyByOID(personID);
    }
}
