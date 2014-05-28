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
package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
        final Role role = Role.getRoleByRoleType(RoleType.PUBLIC_RELATIONS_OFFICE);
        request.setAttribute("persons", role.getAssociatedPersonsSet());
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
        Person person = FenixFramework.getDomainObject(id);
        person.removeRoleByTypeService(RoleType.PUBLIC_RELATIONS_OFFICE);
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
            Person person = user.getPerson();
            if (person != null) {
                person.addPersonRoleByRoleTypeService(RoleType.PUBLIC_RELATIONS_OFFICE);
            }
        } else {
            addActionMessage(request, "error.noUsername", (username.compareTo("") == 0 ? "(vazio)" : username));
        }
        return managePeople(mapping, actionForm, request, response);
    }

}