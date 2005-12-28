package net.sourceforge.fenixedu.renderers.model;

import javax.servlet.http.HttpServletRequest;

public abstract class UserIdentityFactory {
    public static UserIdentityFactory DEFAULT_FACTORY = new DefaultUserIdentityFactory();

    private static UserIdentityFactory currentFactory = DEFAULT_FACTORY;

    public static void setCurrentFactory(UserIdentityFactory factory) {
        currentFactory = factory;
    }
    
    public static UserIdentityFactory getCurrentFactory() {
        return currentFactory;
    }
    
    public static UserIdentity create(HttpServletRequest request) {
        return currentFactory.createUserIdentity(request);
    }
    
    public abstract UserIdentity createUserIdentity(HttpServletRequest request);

}
