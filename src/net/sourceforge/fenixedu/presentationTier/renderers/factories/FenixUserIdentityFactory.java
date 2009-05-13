package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import pt.ist.fenixWebFramework.renderers.model.UserIdentity;
import pt.ist.fenixWebFramework.renderers.model.UserIdentityFactory;
import pt.ist.fenixWebFramework.security.UserView;

public class FenixUserIdentityFactory extends UserIdentityFactory {

    @Override
    public UserIdentity createUserIdentity(final HttpServletRequest request) {
	final IUserView userView = UserView.getUser();
	return new FenixUserIdentity(userView);
    }

}
