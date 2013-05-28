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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.SetPersonRoles;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RoleOperationLog;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
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

import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Luis Cruz
 */
public class ManageRolesDA extends FenixDispatchAction {

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
            roles = person.getPersonRoles();
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

    private Person getPerson(final String username) throws  FenixServiceException {
        return getPerson(username, "");
    }

    private Person getPerson(final DynaActionForm dynaActionForm) throws  FenixServiceException {
        return getPerson(dynaActionForm.getString("username"), dynaActionForm.getString("documentIdNumber"));
    }

    private Person getPerson(final String username, final String documentIdNumber) throws 
            FenixServiceException {
        final SearchPerson.SearchParameters parameters =
                new SearchParameters(null, null, username, documentIdNumber, null, null, null, null, null, null, null, null,
                        (String) null);
        final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

        final Collection<Person> persons =
                ((CollectionPager<Person>) SearchPerson.runSearchPerson( parameters, predicate ))
                        .getCollection();

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
        request.setAttribute(PresentationConstants.ROLES, rootDomainObject.getRoles());
        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     * 
     * @throws FenixServiceException
     * @
     */
    public ActionForward setPersonRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        DynaActionForm rolesForm = (DynaActionForm) form;
        String[] roleOIDsAsStrings = (String[]) rolesForm.get("roleOIDs");
        final Person person = getPerson(rolesForm);

        final Set<Role> roles = new HashSet<Role>();
        for (final String roleId : roleOIDsAsStrings) {
            roles.add(rootDomainObject.readRoleByOID(Integer.valueOf(roleId)));
        }

        IUserView userView = UserView.getUser();

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