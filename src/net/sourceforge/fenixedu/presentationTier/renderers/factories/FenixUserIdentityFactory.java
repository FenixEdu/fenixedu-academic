package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import pt.ist.fenixWebFramework.renderers.model.UserIdentity;
import pt.ist.fenixWebFramework.renderers.model.UserIdentityFactory;

public class FenixUserIdentityFactory extends UserIdentityFactory {

    @Override
    public UserIdentity createUserIdentity(HttpServletRequest request) {
        IUserView userView = (IUserView) request.getSession().getAttribute("UserView");
        
        return new FenixUserIdentity(userView);
    }

}
