package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginCAS")
public class CASAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected User doAuthentication(ActionForm form, HttpServletRequest request) {
        // Actual authentication is performed by CasAuthenticationFilter, so just return the user
        return Authenticate.getUser();
    }

}