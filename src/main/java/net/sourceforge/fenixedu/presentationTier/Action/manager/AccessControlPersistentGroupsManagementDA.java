package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeletePersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.RemovePersistentGroupMember;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "manager", path = "/accessControlPersistentGroupsManagement", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareCreateNewPersistentGroup", path = "/manager/persistentGroups/createNewPersistentGroup.jsp"),
        @Forward(name = "seeAllPersistentGroups", path = "/manager/persistentGroups/seeAllPersistentGroups.jsp") })
public class AccessControlPersistentGroupsManagementDA extends FenixDispatchAction {

    public ActionForward listAllGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        request.setAttribute("persistentGroups", rootDomainObject.getPersistentGroupMembers());
        return mapping.findForward("seeAllPersistentGroups");
    }

    public ActionForward prepareCreateNewGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return mapping.findForward("prepareCreateNewPersistentGroup");
    }

    public ActionForward prepareEditPersistentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
        request.setAttribute("persistentGroup", persistentGroup);
        return mapping.findForward("prepareCreateNewPersistentGroup");
    }

    public ActionForward deletePersistentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
        DeletePersistentGroup.run(persistentGroup);
        return listAllGroups(mapping, form, request, response);
    }

    public ActionForward removePersistentGroupMember(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PersistentGroupMembers persistentGroup = getPersistentGroupFromParameter(request);
        Person person = getPersonFromParameter(request);
        RemovePersistentGroupMember.run(person, persistentGroup);
        return prepareEditPersistentGroup(mapping, form, request, response);
    }

    protected PersistentGroupMembers getPersistentGroupFromParameter(final HttpServletRequest request) {
        final String persistentGroupIDString = request.getParameter("persistentGroupID");
        return FenixFramework.getDomainObject(persistentGroupIDString);
    }

    protected Person getPersonFromParameter(final HttpServletRequest request) {
        final String personIDString = request.getParameter("personID");
        return (Person) FenixFramework.getDomainObject(personIDString);
    }
}