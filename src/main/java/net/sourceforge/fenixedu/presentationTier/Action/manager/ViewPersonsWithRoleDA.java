package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RoleOperationLog;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class ViewPersonsWithRoleDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute(PresentationConstants.ROLES, rootDomainObject.getRoles());
        return mapping.findForward("SelectRole");
    }

    public ActionForward searchWithRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Role role = rootDomainObject.readRoleByOID(Integer.valueOf(request.getParameter("roleID")));
        request.setAttribute("persons", role.getAssociatedPersons());
        request.setAttribute("roleID", request.getParameter("roleID"));
        return mapping.findForward("ShowPersons");
    }

    public ActionForward removePersonFromRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Role role = rootDomainObject.readRoleByOID(Integer.valueOf(request.getParameter("roleID")));
        Person person = Person.readPersonByUsername(request.getParameter("personUsername"));
        removePersonFromRole(person, role);
        return searchWithRole(mapping, form, request, response);
    }

    @Service
    public void removePersonFromRole(Person person, Role role) {
        person.removePersonRoles(role);
    }

    public ActionForward showRoleOperationLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer roleID = Integer.valueOf((String) getFromRequest(request, "roleID"));

        final String pageNumberString = (String) getFromRequest(request, "pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        final Role role = rootDomainObject.readRoleByOID(roleID);

        ArrayList<RoleOperationLog> listOfRoleOperationLog = role.getRoleOperationLogArrayListOrderedByDate();
        CollectionPager<RoleOperationLog> collectionPager =
                new CollectionPager<RoleOperationLog>(
                        listOfRoleOperationLog != null ? listOfRoleOperationLog : new ArrayList<RoleOperationLog>(), 25);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("domainObjectActionLogs", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));
        request.setAttribute("roleID", roleID);

        return mapping.findForward("ShowRoleOperationLogs");
    }

}
