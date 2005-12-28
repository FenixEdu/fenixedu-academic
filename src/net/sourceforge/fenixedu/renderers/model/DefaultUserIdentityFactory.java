package net.sourceforge.fenixedu.renderers.model;

import javax.servlet.http.HttpServletRequest;

public class DefaultUserIdentityFactory extends UserIdentityFactory {

    @Override
    public UserIdentity createUserIdentity(HttpServletRequest request) {
        String sessionId = request.getSession() != null ? request.getSession().getId() : null;
        
        return new SimpleUserIdentity(sessionId);
    }

}
