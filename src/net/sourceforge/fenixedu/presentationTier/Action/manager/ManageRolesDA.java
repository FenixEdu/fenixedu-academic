/*
 * Created on 2003/12/04
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz
 */
public class ManageRolesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("SelectUserPage");
    }

    public ActionForward selectUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ActionErrors errors = new ActionErrors();

        final DynaActionForm rolesForm = (DynaActionForm) form;
        final String username = (String) rolesForm.get("username");
        final Person person = Person.readPersonByUsername(username);
        final List<Role> roles;
        if (person == null) {
        	roles = null;
        	return showError(request, mapping, errors, "noUsername", new ActionError("error.noUsername", username));
        } else {
        	roles = person.getPersonRoles();
            if (roles.size() <= 0) {
            	return showError(request, mapping, errors, "noRoles", new ActionError("error.noRoles", null));
            }
        }

        final String[] roleOIDs = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleOIDs[i] = roles.get(i).getIdInternal().toString();
        }
        rolesForm.set("roleOIDs", roleOIDs);

        request.setAttribute(SessionConstants.USERNAME, username);
        return prepareAddRoleToPerson(mapping, form, request, response);
    }

    private ActionForward showError(final HttpServletRequest request, final ActionMapping mapping,
    		final ActionErrors errors, final String key, ActionError error) {
    	errors.add(key, error);
    	saveErrors(request, errors);
    	return mapping.getInputForward();
	}

	public ActionForward prepareAddRoleToPerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(SessionConstants.ROLES, rootDomainObject.getRoles());
        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward setPersonRoles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm rolesForm = (DynaActionForm) form;
        String[] roleOIDsAsStrings = (String[]) rolesForm.get("roleOIDs");
        String username = (String) rolesForm.get("username");
        final Person person = Person.readPersonByUsername(username);

        final Set<Role> roles = new HashSet<Role>();
        for (final String roleId : roleOIDsAsStrings) {
            roles.add(rootDomainObject.readRoleByOID(Integer.valueOf(roleId)));
        }

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { person, roles };
        try {
            ServiceUtils.executeService(userView, "SetPersonRoles", args);
        } catch (Exception e) {
            ActionMessages messages = new ActionMessages();
            messages.add("invalidRole", new ActionMessage("error.invalidRole"));
            saveMessages(request, messages);   
            return mapping.getInputForward();
        } 

        return mapping.findForward("Manage");
    }

}