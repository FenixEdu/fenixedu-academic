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
/*
 * Created on 2003/12/04
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.SetPersonRoles;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RoleOperationLog;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPersonManagementApp;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Luis Cruz
 */
@StrutsFunctionality(app = ManagerPersonManagementApp.class, path = "manage-roles",
        titleKey = "label.manager.privilegesManagement")
@Mapping(module = "manager", path = "/manageRoles", formBean = "rolesForm")
@Forwards({ @Forward(name = "SelectUserPage", path = "/manager/manageRoles_bd.jsp"),
        @Forward(name = "ShowRoleOperationLogs", path = "/manager/showRoleOperationLogs.jsp"),
        @Forward(name = "Manage", path = "/manager/manageRoles_bd.jsp") })
public class ManageRolesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("SelectUserPage");
    }

    public ActionForward showRoleOperationLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String username = (String) getFromRequest(request, "username");

        final String pageNumberString = (String) getFromRequest(request, "pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        final Person person = Person.readPersonByUsername(username);

        ArrayList<RoleOperationLog> listOfRoleOperationLog = person.getPersonRoleOperationLogArrayListOrderedByDate();
        CollectionPager<RoleOperationLog> collectionPager =
                new CollectionPager<RoleOperationLog>(
                        listOfRoleOperationLog != null ? listOfRoleOperationLog : new ArrayList<RoleOperationLog>(), 25);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("domainObjectActionLogs", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));

        return mapping.findForward("ShowRoleOperationLogs");
    }

    public ActionForward selectUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ActionErrors errors = new ActionErrors();

        final DynaActionForm rolesForm = (DynaActionForm) form;
        final String username = (String) rolesForm.get("username");
        final Person person = getPerson(rolesForm);

        final List<Role> roles;
        if (person == null) {
            roles = null;
            return showError(request, mapping, errors, "noUsername", new ActionError("error.noUsername", username));
        } else {
            roles = new ArrayList<>(person.getPersonRoles());
            // if (roles.size() <= 0) {
            // return showError(request, mapping, errors, "noRoles", new
            // ActionError("error.noRoles", null));
            // }
        }

        final String[] roleOIDs = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleOIDs[i] = roles.get(i).getExternalId().toString();
        }
        rolesForm.set("roleOIDs", roleOIDs);

        request.setAttribute("person", person);
        request.setAttribute(PresentationConstants.USERNAME, username);
        return prepareAddRoleToPerson(mapping, form, request, response);
    }

    private Person getPerson(final DynaActionForm dynaActionForm) throws FenixServiceException {
        return getPerson(dynaActionForm.getString("username"), dynaActionForm.getString("documentIdNumber"));
    }

    private Person getPerson(final String username, final String documentIdNumber) throws FenixServiceException {
        final SearchPerson.SearchParameters parameters =
                new SearchParameters(null, null, username, documentIdNumber, null, null, null, null, null, null, null, null,
                        (String) null);
        final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

        final Collection<Person> persons = SearchPerson.runSearchPerson(parameters, predicate).getCollection();

        return persons.isEmpty() ? null : persons.iterator().next();
    }

    private ActionForward showError(final HttpServletRequest request, final ActionMapping mapping, final ActionErrors errors,
            final String key, ActionError error) {
        errors.add(key, error);
        saveErrors(request, errors);
        return mapping.getInputForward();
    }

    public ActionForward prepareAddRoleToPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute(PresentationConstants.ROLES, rootDomainObject.getRolesSet());
        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     * 
     * @throws FenixServiceException
     *             @
     */
    public ActionForward setPersonRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm rolesForm = (DynaActionForm) form;
        String[] roleOIDsAsStrings = (String[]) rolesForm.get("roleOIDs");
        final Person person = getPerson(rolesForm);

        final Set<Role> roles = new HashSet<Role>();
        for (final String roleId : roleOIDsAsStrings) {
            roles.add(FenixFramework.<Role> getDomainObject(roleId));
        }

        try {
            SetPersonRoles.run(person, roles);
        } catch (Exception e) {
            ActionMessages messages = new ActionMessages();
            messages.add("invalidRole", new ActionMessage("error.invalidRole"));
            saveMessages(request, messages);
            return mapping.getInputForward();
        }

        return mapping.findForward("Manage");
    }

}