/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.residenceManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.residenceManagement.ResidenceRoleManagementBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = ResidenceManagerApplication.class, path = "role-management", titleKey = "title.role.management")
@Mapping(path = "/residenceRoleManagement", module = "residenceManagement")
@Forwards(@Forward(name = "residenceRoleManagement", path = "/residenceManagement/residenceRoleManagement.jsp"))
public class ResidenceRoleManagementDA extends FenixDispatchAction {

    public ActionForward addResidenceRoleManagemenToPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynamicGroup group = getResidenceRoleManagement();
        group.mutator().changeGroup(group.underlyingGroup().grant(getResidenceRoleManagementBean().getPerson().getUser()));
        return residencePersonsManagement(mapping, actionForm, request, response);
    }

    public ActionForward removeResidenceRoleManagemenToPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = User.findByUsername(request.getParameter("userToRemove"));
        DynamicGroup group = getResidenceRoleManagement();
        group.mutator().changeGroup(group.underlyingGroup().revoke(user));
        return residencePersonsManagement(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward residencePersonsManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Group role = getResidenceRoleManagement();
        request.setAttribute("role", role);
        request.setAttribute("residenceRoleManagement", getResidenceRoleManagementBean());
        return mapping.findForward("residenceRoleManagement");
    }

    private DynamicGroup getResidenceRoleManagement() {
        return (DynamicGroup) RoleType.RESIDENCE_MANAGER.actualGroup();
    }

    private ResidenceRoleManagementBean getResidenceRoleManagementBean() {
        ResidenceRoleManagementBean residenceRoleManagementBean = getRenderedObject();
        if (residenceRoleManagementBean == null) {
            return new ResidenceRoleManagementBean();
        }
        return residenceRoleManagementBean;
    }

}