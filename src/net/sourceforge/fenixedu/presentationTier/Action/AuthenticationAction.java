package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.mapping.ActionMappingForAuthentication;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jorge
 */
public class AuthenticationAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = null;
        try {
            DynaActionForm authenticationForm = (DynaActionForm) form;
            ActionMappingForAuthentication authenticationMapping = (ActionMappingForAuthentication) mapping;

			final String username = (String) authenticationForm.get("username");
			final String password = (String) authenticationForm.get("password");
			final String application = authenticationMapping.getApplication();
			final String requestURL = request.getRequestURL().toString();
            Object argsAutenticacao[] = { username, password, application, requestURL };

            userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsAutenticacao);
        } catch (ExcepcaoAutenticacao e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("invalidAuthentication", new ActionError("errors.invalidAuthentication"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (userView.getRoles().isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.noAuthorization", new ActionError("errors.noAuthorization"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        // Invalidate existing session if it exists
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }

        // Create a new session for this user
        sessao = request.getSession(true);

        // Store the UserView into the session and return
        sessao.setAttribute(SessionConstants.U_VIEW, userView);
        sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));

        Collection userRoles = userView.getRoles();

        int numberOfSubApplications = getNumberOfSubApplications(userRoles);
        ActionForward forwardToReturn = mapping.findForward("sucess");

        Iterator iterator = userRoles.iterator();
        InfoRole firstInfoRole = null;
        while (iterator.hasNext()) {
            firstInfoRole = (InfoRole) iterator.next();
            break;
        }

        InfoRole firstTimeStudentInfoRole = new InfoRole();
        firstTimeStudentInfoRole.setRoleType(RoleType.FIRST_TIME_STUDENT);

        if (userRoles.contains(firstTimeStudentInfoRole)) {
            //ActionErrors actionErrors = new ActionErrors();
            //actionErrors.add("invalidAuthentication", new
            // ActionError("errors.invalidAuthentication"));
            //saveErrors(request, actionErrors);
            //return mapping.getInputForward();
            //TODO impose a period time limit
            InfoRole infoRole = getRole(RoleType.FIRST_TIME_STUDENT, userRoles);
            forwardToReturn = buildRoleForward(infoRole);
        } else {
            InfoRole personInfoRole = new InfoRole();
            personInfoRole.setRoleType(RoleType.PERSON);
            if (numberOfSubApplications == 1 || !userRoles.contains(personInfoRole)) {
                forwardToReturn = buildRoleForward(firstInfoRole);
            }
        }

        return forwardToReturn;
    }

    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(Collection userRoles) {
        List subApplications = new ArrayList();
        Iterator iterator = userRoles.iterator();
        while (iterator.hasNext()) {
            InfoRole infoRole = (InfoRole) iterator.next();
            String subApplication = infoRole.getPortalSubApplication();
            if (!subApplications.contains(subApplication) && !subApplication.equals("/teacher")) {
                subApplications.add(subApplication);
            }
        }
        return subApplications.size();
    }

    /**
     * @param infoRole
     * @return
     */
    private ActionForward buildRoleForward(InfoRole infoRole) {
        ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(false);
        actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page="
                + infoRole.getPage());
        return actionForward;
    }

    private InfoRole getRole(RoleType roleType, Collection rolesList) {

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(roleType);

        Iterator iterator = rolesList.iterator();
        while (iterator.hasNext()) {

            InfoRole role = (InfoRole) iterator.next();
            if (role.equals(infoRole))
                return role;

        }
        return null;
    }
}