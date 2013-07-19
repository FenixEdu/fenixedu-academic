package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.LocalAuthenticationAction.AuthenticationForm;
import net.sourceforge.fenixedu.presentationTier.config.FenixExceptionHandler;
import net.sourceforge.fenixedu.presentationTier.config.LoginInvalidPasswordExceptionHandler;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/login", formBeanClass = AuthenticationForm.class, input = "/loginPage.jsp")
@Exceptions({
        @ExceptionHandling(type = InvalidPasswordServiceException.class,
                key = "net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException",
                handler = LoginInvalidPasswordExceptionHandler.class),
        @ExceptionHandling(type = PasswordExpiredServiceException.class,
                key = "net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException",
                path = "/loginExpired.do?method=start", handler = FenixExceptionHandler.class) })
@Forwards({ @Forward(name = "success", path = "/home.do", redirect = true),
        @Forward(name = "changePass", path = "/person/changePasswordForward.do"),
        @Forward(name = "expirationWarning", path = "/expirationWarning.jsp") })
public class LocalAuthenticationAction extends BaseAuthenticationAction {

    public static class AuthenticationForm extends FenixActionForm {

        private static final long serialVersionUID = 4638914162166756449L;

        private String username;
        private String password;
        private String pendingRequest;

        public String getPendingRequest() {
            return pendingRequest;
        }

        public void setPendingRequest(String pendingRequest) {
            this.pendingRequest = pendingRequest;
        }

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

    }

    @Override
    protected IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
            throws FenixServiceException {

        final String serverName = request.getServerName();
        final CasConfig casConfig = FenixWebFramework.getConfig().getCasConfig(serverName);
        if (casConfig != null && casConfig.isCasEnabled()) {
            throw new ExcepcaoAutenticacao("errors.noAuthorization");
        }

        final AuthenticationForm authenticationForm = (AuthenticationForm) form;
        final String username = authenticationForm.getUsername();
        final String password = authenticationForm.getPassword();
        final String requestURL = request.getRequestURL().toString();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ExcepcaoAutenticacao();
        } else {
            return Authenticate.runAuthenticate(username, password, requestURL, remoteHostName);
        }
    }

    @Override
    protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
            final String actionKey, final String messageKey) {
        final ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(actionKey, new ActionError(messageKey));
        saveErrors(request, actionErrors);
        return new ActionForward("/loginPage.jsp");
    }

}