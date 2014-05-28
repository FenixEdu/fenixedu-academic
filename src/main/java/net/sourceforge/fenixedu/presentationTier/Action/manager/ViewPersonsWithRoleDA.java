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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RoleOperationLog;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPeopleApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = ManagerPeopleApp.class, path = "view-people-with-role", titleKey = "title.manage.roles")
@Mapping(module = "manager", path = "/viewPersonsWithRole")
@Forwards({ @Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp"),
        @Forward(name = "ShowPersons", path = "/manager/viewPersonsWithRole_bd.jsp"),
        @Forward(name = "SelectRole", path = "/manager/viewPersonsWithRole_bd.jsp") })
public class ViewPersonsWithRoleDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("roles", Bennu.getInstance().getRolesSet());
        return mapping.findForward("SelectRole");
    }

    public ActionForward searchWithRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("role", getDomainObject(request, "roleID"));
        return mapping.findForward("ShowPersons");
    }

    public ActionForward removePersonFromRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Role role = FenixFramework.getDomainObject(request.getParameter("roleID"));
        Person person = Person.readPersonByUsername(request.getParameter("personUsername"));
        removePersonFromRole(person, role);
        return searchWithRole(mapping, form, request, response);
    }

    @Atomic
    public void removePersonFromRole(Person person, Role role) {
        person.removePersonRoles(role);
    }

    public ActionForward showRoleOperationLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String roleID = (String) getFromRequest(request, "roleID");

        final String pageNumberString = (String) getFromRequest(request, "pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        final Role role = FenixFramework.getDomainObject(roleID);

        ArrayList<RoleOperationLog> listOfRoleOperationLog = role.getRoleOperationLogArrayListOrderedByDate();
        CollectionPager<RoleOperationLog> collectionPager =
                new CollectionPager<RoleOperationLog>(
                        listOfRoleOperationLog != null ? listOfRoleOperationLog : new ArrayList<RoleOperationLog>(), 25);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("domainObjectActionLogs", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));
        request.setAttribute("role", role);

        return mapping.findForward("ShowRoleOperationLogs");
    }

}
