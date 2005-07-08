/*
 * Created on 2003/12/04
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm rolesForm = (DynaActionForm) form;
        String username = (String) rolesForm.get("username");

        Object[] args = { username };
        List roles = null;
        try {
            roles = (List) ServiceUtils.executeService(userView, "ReadRolesByUser", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage().equals("error.noUsername")) {
                errors.add("noUsername", new ActionError("error.noUsername", username));
            }
        }
        if (roles == null || roles.size() <= 0) {
            errors.add("noRoles", new ActionError("error.noRoles"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        String[] roleOIDs = new String[roles.size()];

        for (int i = 0; i < roles.size(); i++) {
            roleOIDs[i] = ((InfoRole) roles.get(i)).getIdInternal().toString();
        }

        rolesForm.set("roleOIDs", roleOIDs);

        request.setAttribute(SessionConstants.USERNAME, username);

        return prepareAddRoleToPerson(mapping, form, request, response);
    }

    public ActionForward prepareAddRoleToPerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Object[] args = {};
        List roles = (List) ServiceUtils.executeService(userView, "ReadRoles", args);

        request.setAttribute(SessionConstants.ROLES, roles);

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward setPersonRoles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm rolesForm = (DynaActionForm) form;
        String[] roleOIDsAsStrings = (String[]) rolesForm.get("roleOIDs");
        String username = (String) rolesForm.get("username");

        List roleOIDs = new ArrayList();
        for (int i = 0; i < roleOIDsAsStrings.length; i++) {
            roleOIDs.add(new Integer(roleOIDsAsStrings[i]));
        }

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { username, roleOIDs };
        ServiceUtils.executeService(userView, "SetPersonRoles", args);

        return mapping.findForward("Manage");
    }

}