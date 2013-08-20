package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.SetPersonRoles;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.FindPersonFactory;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RecoverInactivePersonDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("showSearchForm");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final FindPersonFactory findPersonFactory = (FindPersonFactory) executeFactoryMethod();
        for (final Iterator<Person> personIterator = findPersonFactory.getPeople().iterator(); personIterator.hasNext();) {
            final Person person = personIterator.next();
            if (!person.getPersonRolesSet().isEmpty()) {
                personIterator.remove();
            }
        }
        request.setAttribute("findPersonFactory", findPersonFactory);
        return prepare(mapping, form, request, response);
    }

    public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final String personIDString = request.getParameter("personID");
        if (personIDString != null && StringUtils.isNumeric(personIDString)) {
            final Person person = (Person) AbstractDomainObject.fromExternalId(personIDString);
            final Set<Role> roles = new HashSet<Role>();
            roles.add(Role.getRoleByRoleType(RoleType.PERSON));

            SetPersonRoles.run(person, roles);
        }
        return prepare(mapping, form, request, response);
    }

}