package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginCAS")
public class CASAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected User doAuthentication(ActionForm form, HttpServletRequest request) {
        // Actual authentication is performed by CasAuthenticationFilter, so just return the user
        return Authenticate.getUser();
    }

    /*
     * When authentication fails on CAS
     */
    @Override
    protected ActionForward getAuthenticationFailedForward(ActionMapping mapping, HttpServletRequest request, String actionKey,
            String messageKey) {
        Authenticate.logout(request.getSession());
        return new ActionForward(CoreConfiguration.casConfig().getCasLogoutUrl(), true);
    }

}