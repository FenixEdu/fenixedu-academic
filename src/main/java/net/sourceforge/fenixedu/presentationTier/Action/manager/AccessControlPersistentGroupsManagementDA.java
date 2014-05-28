/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeletePersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.RemovePersistentGroupMember;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerSystemManagementApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "groups-management",
        titleKey = "label.access.control.persistent.groups.management")
@Mapping(module = "manager", path = "/accessControlPersistentGroupsManagement")
@Forwards({ @Forward(name = "prepareCreateNewPersistentGroup", path = "/manager/persistentGroups/createNewPersistentGroup.jsp"),
        @Forward(name = "seeAllPersistentGroups", path = "/manager/persistentGroups/seeAllPersistentGroups.jsp") })
public class AccessControlPersistentGroupsManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listAllGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        request.setAttribute("persistentGroups", rootDomainObject.getPersistentGroupMembersSet());
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