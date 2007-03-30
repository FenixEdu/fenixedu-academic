package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class FenixUserIdentity implements UserIdentity {
    
    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private IUserView userView;

    public FenixUserIdentity(IUserView userView) {
        super();
    
        this.userView = userView;
    }

    public IUserView getUserView() {
        return userView;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof FenixUserIdentity)) {
            return false;
        }
        
        FenixUserIdentity other = (FenixUserIdentity) obj;
        
        return this.userView.equals(other.userView);
    }
    
    @Override
    public int hashCode() {
        return this.userView.hashCode();
    }
    
}
