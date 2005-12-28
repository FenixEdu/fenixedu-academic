package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class FenixUserIdentity implements UserIdentity {
    private IUserView userView;

    public FenixUserIdentity(IUserView userView) {
        super();
    
        this.userView = userView;
    }

    public IUserView getUserView() {
        return userView;
    }
}
