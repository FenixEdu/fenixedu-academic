package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AuthenticationExpiredAction extends FenixDispatchAction {

    protected static final boolean useCASAuthentication = PropertiesManager
            .getBooleanProperty("cas.enabled");

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("username", request.getParameter("username"));
        actionForm.set("page", 0);
        actionForm.set("fromCAS", request.getParameter("fromCAS"));
        saveErrors(request, null);
        return mapping.findForward("changePass");
    }

    public ActionForward changePass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {

            final IUserView userView = changePasswordAndAuthenticateUser(form, request);

            if (userView == null || userView.getRoles().isEmpty()) {
                return authenticationFailedForward(mapping, request, "errors.noAuthorization");
            }

            //TODO: remove when fenix CAS support is activated
            //This is here until we move authentication expiration page to CAS
            String fromCAS = ((DynaActionForm) form).getString("fromCAS");

            if (useCASAuthentication || (fromCAS != null && fromCAS.equalsIgnoreCase("true"))) {
                String casLoginUrl = PropertiesManager.getProperty("cas.loginUrl");
                ActionForward actionForward = new ActionForward();

                actionForward.setRedirect(true);
                actionForward.setPath(casLoginUrl);

                return actionForward;

            } else {

                final HttpSession session = request.getSession(true);

                // Store the UserView into the session and return
                session.setAttribute(SessionConstants.U_VIEW, userView);
                session.setAttribute(SessionConstants.SESSION_IS_VALID, Boolean.TRUE);

                Collection userRoles = userView.getRoles();

                Role firstTimeStudentInfoRole = Role.getRoleByRoleType(RoleType.FIRST_TIME_STUDENT);

                if (userRoles.contains(firstTimeStudentInfoRole)) {
                    // TODO impose a period time limit
                    Role infoRole = getRole(RoleType.FIRST_TIME_STUDENT, userRoles);
                    return buildRoleForward(infoRole);
                } else {
                    Role personInfoRole = Role.getRoleByRoleType(RoleType.PERSON);
                    int numberOfSubApplications = getNumberOfSubApplications(userRoles);
                    if (numberOfSubApplications == 1 || !userRoles.contains(personInfoRole)) {
                        final Role firstInfoRole = ((userRoles.isEmpty()) ? null
                                : (Role) userRoles.iterator().next());
                        return buildRoleForward(firstInfoRole);
                    } else {
                        return mapping.findForward("sucess");
                    }
                }
            }

        } catch (ExcepcaoAutenticacao e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("invalidAuthentication", new ActionError("errors.invalidAuthentication"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (InvalidPasswordServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.person.impossible.change", new ActionError(
                    "error.person.impossible.change"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

    }

    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(Collection userRoles) {
        List subApplications = new ArrayList();
        Iterator iterator = userRoles.iterator();
        while (iterator.hasNext()) {
            Role infoRole = (Role) iterator.next();
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
    private ActionForward buildRoleForward(Role infoRole) {
        ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(false);
        actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page="
                + infoRole.getPage());
        return actionForward;
    }

    private Role getRole(RoleType roleType, Collection rolesList) {

        Role infoRole = Role.getRoleByRoleType(roleType);

        Iterator iterator = rolesList.iterator();
        while (iterator.hasNext()) {

            Role role = (Role) iterator.next();
            if (role.equals(infoRole))
                return role;

        }
        return null;
    }

    private IUserView changePasswordAndAuthenticateUser(final ActionForm form,
            final HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        DynaActionForm authenticationForm = (DynaActionForm) form;
        final String username = (String) authenticationForm.get("username");
        final String password = (String) authenticationForm.get("password");
        final String newPassword = (String) authenticationForm.get("newPassword");
        final String requestURL = request.getRequestURL().toString();
        
        String remoteHostName = BaseAuthenticationAction.getRemoteHostName(request);
        Object argsAutenticacao[] = { username, password, newPassword, requestURL, remoteHostName};
                
        return (IUserView) ServiceManagerServiceFactory.executeService(null, "AuthenticationExpired",
                argsAutenticacao);
    }

    protected ActionForward authenticationFailedForward(final ActionMapping mapping,
            final HttpServletRequest request, final String messageKey) {
        final ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(messageKey, new ActionError(messageKey));
        saveErrors(request, actionErrors);
        return mapping.getInputForward();
    }
}
