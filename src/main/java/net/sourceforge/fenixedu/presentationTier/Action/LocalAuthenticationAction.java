package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.LocalAuthenticationAction.AuthenticationForm;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.core.util.CoreConfiguration.CasConfig;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/login", formBeanClass = AuthenticationForm.class)
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
    protected User doAuthentication(ActionForm form, HttpServletRequest request) {
        final CasConfig casConfig = CoreConfiguration.casConfig();
        if (casConfig != null && casConfig.isCasEnabled()) {
            throw AuthorizationException.authenticationFailed();
        }

        final AuthenticationForm authenticationForm = (AuthenticationForm) form;
        final String username = authenticationForm.getUsername();
        final String password = authenticationForm.getPassword();

        return Authenticate.login(request.getSession(true), username, password);
    }

}