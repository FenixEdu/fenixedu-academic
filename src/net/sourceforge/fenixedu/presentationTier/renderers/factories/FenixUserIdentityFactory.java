package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;
import net.sourceforge.fenixedu.renderers.model.UserIdentityFactory;

public class FenixUserIdentityFactory extends UserIdentityFactory {

    @Override
    public UserIdentity createUserIdentity(HttpServletRequest request) {
        IUserView userView = (IUserView) request.getSession().getAttribute("UserView");
        
        return new FenixUserIdentity(userView);
    }

}
