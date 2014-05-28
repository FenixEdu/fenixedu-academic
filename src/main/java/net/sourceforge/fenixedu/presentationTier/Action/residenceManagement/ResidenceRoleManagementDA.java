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
package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceRoleManagementBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = ResidenceManagerApplication.class, path = "role-management", titleKey = "title.role.management")
@Mapping(path = "/residenceRoleManagement", module = "residenceManagement")
@Forwards(@Forward(name = "residenceRoleManagement", path = "/residenceManagement/residenceRoleManagement.jsp"))
public class ResidenceRoleManagementDA extends FenixDispatchAction {

    public ActionForward addResidenceRoleManagemenToPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PersonName personName = getResidenceRoleManagementBean().getPersonName();
        addPersonToRole(personName.getPerson(), getResidenceRoleManagement());
        return residencePersonsManagement(mapping, actionForm, request, response);

    }

    public ActionForward removeResidenceRoleManagemenToPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Role role = getResidenceRoleManagement();
        Person person = Person.readPersonByUsername(request.getParameter("personUsername"));
        removePersonFromRole(person, role);
        return residencePersonsManagement(mapping, actionForm, request, response);
    }

    @Atomic
    public void addPersonToRole(Person person, Role role) {
        person.addPersonRoles(role);
    }

    @Atomic
    public void removePersonFromRole(Person person, Role role) {
        person.removePersonRoles(role);
    }

    @EntryPoint
    public ActionForward residencePersonsManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Role role = getResidenceRoleManagement();
        request.setAttribute("persons", role.getAssociatedPersonsSet());
        request.setAttribute("residenceRoleManagement", getResidenceRoleManagementBean());
        return mapping.findForward("residenceRoleManagement");
    }

    private Role getResidenceRoleManagement() {
        return Role.getRoleByRoleType(RoleType.RESIDENCE_MANAGER);
    }

    private ResidenceRoleManagementBean getResidenceRoleManagementBean() {
        ResidenceRoleManagementBean residenceRoleManagementBean = getRenderedObject();
        if (residenceRoleManagementBean == null) {
            return new ResidenceRoleManagementBean();
        }
        return residenceRoleManagementBean;
    }

}