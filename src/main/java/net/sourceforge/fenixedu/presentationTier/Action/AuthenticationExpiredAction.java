package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.AuthenticateExpiredKerberos;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.AuthenticationExpiredAction.AuthenticationExpiredForm;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginExpired", formBeanClass = AuthenticationExpiredForm.class, input = "/loginExpired.jsp?")
public class AuthenticationExpiredAction extends FenixDispatchAction {

    public static class AuthenticationExpiredForm extends ValidatorForm {

        private static final long serialVersionUID = 316277744795378668L;

        private String username;
        private String password;
        private String newPassword;
        private String reTypeNewPassword;
        private String fromCAS;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getReTypeNewPassword() {
            return reTypeNewPassword;
        }

        public void setReTypeNewPassword(String reTypeNewPassword) {
            this.reTypeNewPassword = reTypeNewPassword;
        }

        public String getFromCAS() {
            return fromCAS;
        }

        public void setFromCAS(String fromCAS) {
            this.fromCAS = fromCAS;
        }

        @Override
        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
            ActionErrors errors = new ActionErrors();

            if (StringUtils.isEmpty(username)) {
                errors.add("label.username", new ActionMessage("errors.required", "Username"));
            }
            if (StringUtils.isEmpty(password)) {
                errors.add("label.pass", new ActionMessage("errors.required", "Password"));
            }

            if (!GenericValidator.isBlankOrNull(newPassword)) {
                try {
                    if (!newPassword.equals(reTypeNewPassword)) {
                        errors.add("label.candidate.newPasswordError", new ActionMessage("errors.different.passwords"));
                    }
                } catch (Exception e) {
                    errors.add("label.candidate.newPasswordError", new ActionMessage("errors.different.passwords"));
                }
            }

            return errors;
        }

    }

    protected static final boolean useCASAuthentication = PropertiesManager.getBooleanProperty("cas.enabled");

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AuthenticationExpiredForm actionForm = (AuthenticationExpiredForm) form;
        actionForm.setUsername(request.getParameter("username"));
        actionForm.setPage(0);
        actionForm.setFromCAS(request.getParameter("fromCAS"));
        saveErrors(request, null);
        return new ActionForward("/loginExpired.jsp");
    }

    public ActionForward changePass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {

            final IUserView userView = changePasswordAndAuthenticateUser(form, request);

            if (userView == null || userView.getRoleTypes().isEmpty()) {
                return authenticationFailedForward(mapping, request, "errors.noAuthorization");
            }

            // TODO: remove when fenix CAS support is activated
            // This is here until we move authentication expiration page to
            // CAS
            String fromCAS = ((AuthenticationExpiredForm) form).getFromCAS();

            if (useCASAuthentication || (fromCAS != null && fromCAS.equalsIgnoreCase("true"))) {
                String casLoginUrl = PropertiesManager.getProperty("cas.loginUrl");
                ActionForward actionForward = new ActionForward();

                actionForward.setRedirect(true);
                actionForward.setPath(casLoginUrl);

                return actionForward;

            } else {

                final HttpSession session = request.getSession(true);

                // Store the UserView into the session and return
                UserView.setUser(userView);
                session.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);

                int numberOfSubApplications = getNumberOfSubApplications(userView.getRoleTypes());
                if (numberOfSubApplications == 1 || !userView.hasRoleType(RoleType.PERSON)) {
                    final Role firstInfoRole =
                            ((userView.getRoleTypes().isEmpty()) ? null : Role.getRoleByRoleType(userView.getRoleTypes()
                                    .iterator().next()));
                    return buildRoleForward(firstInfoRole);
                } else {
                    return new ActionForward("/home.do");
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
            actionErrors.add("error.person.impossible.change", new ActionError("error.person.impossible.change"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

    }

    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(Collection<RoleType> roleTypes) {
        final Set<String> subApplications = new HashSet<String>();
        for (final RoleType roleType : roleTypes) {
            final Role role = Role.getRoleByRoleType(roleType);
            final String subApplication = role.getPortalSubApplication();
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
        actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page=" + infoRole.getPage());
        return actionForward;
    }

    private Role getRole(RoleType roleType, Collection rolesList) {

        Role infoRole = Role.getRoleByRoleType(roleType);

        Iterator iterator = rolesList.iterator();
        while (iterator.hasNext()) {

            Role role = (Role) iterator.next();
            if (role.equals(infoRole)) {
                return role;
            }

        }
        return null;
    }

    private IUserView changePasswordAndAuthenticateUser(final ActionForm form, final HttpServletRequest request)
            throws FenixServiceException, ExcepcaoPersistencia {
        AuthenticationExpiredForm authenticationForm = (AuthenticationExpiredForm) form;
        final String username = authenticationForm.getUsername();
        final String password = authenticationForm.getPassword();
        final String newPassword = authenticationForm.getNewPassword();
        final String requestURL = request.getRequestURL().toString();

        String remoteHostName = BaseAuthenticationAction.getRemoteHostName(request);

        return AuthenticateExpiredKerberos.runAuthenticationExpired(username, password, newPassword, requestURL, remoteHostName);
    }

    protected ActionForward authenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
            final String messageKey) {
        final ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(messageKey, new ActionError(messageKey));
        saveErrors(request, actionErrors);
        return mapping.getInputForward();
    }
}
