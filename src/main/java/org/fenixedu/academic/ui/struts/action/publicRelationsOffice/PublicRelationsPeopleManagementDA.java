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
package org.fenixedu.academic.ui.struts.action.publicRelationsOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = PublicRelationsApplication.class, path = "manage-members", titleKey = "title.manage.members")
@Mapping(path = "/managePublicRelationsPeople", module = "publicRelations")
@Forwards({ @Forward(name = "managePeople", path = "/publicRelations/managePeople/managePeople.jsp") })
public class PublicRelationsPeopleManagementDA extends FenixDispatchAction {

    /**
     * Method responsible for listing all persons with role PUBLIC_RELATIONS_OFFICE
     * 
     */
    @EntryPoint
    public ActionForward managePeople(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("role", RoleType.PUBLIC_RELATIONS_OFFICE.actualGroup());
        request.setAttribute("bean", new PersonBean());
        return mapping.findForward("managePeople");
    }

    /**
     * Method responsible for removing the role 'public relations' from a person
     * 
     */
    public ActionForward removeManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        String id = request.getParameter("managerID");
        User person = FenixFramework.getDomainObject(id);
        RoleType.revoke(RoleType.PUBLIC_RELATIONS_OFFICE, person);
        return managePeople(mapping, actionForm, request, response);
    }

    /**
     * Method responsible for adding the role 'public relations' to a person by its Username
     * 
     */
    public ActionForward addPersonManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        PersonBean bean = (PersonBean) RenderUtils.getViewState("addPerson").getMetaObject().getObject();
        String username = bean.getUsername();
        User user;
        if (username != null && (user = User.findByUsername(username)) != null) {
            RoleType.grant(RoleType.PUBLIC_RELATIONS_OFFICE, user);
        } else {
            addActionMessage(request, "error.noUsername", (username.compareTo("") == 0 ? "(vazio)" : username));
        }
        return managePeople(mapping, actionForm, request, response);
    }

}