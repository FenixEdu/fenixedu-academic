package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class FunctionalitySection extends FunctionalitySection_Base {
    
    protected FunctionalitySection() {
        super();
    }
    
    public FunctionalitySection(Site site, Functionality functionality) {
        this();
        
        if (site == null) {
            throw new NullPointerException();
        }
        
        setSite(site);
        setFunctionality(functionality);
    }

    @Override
    public MultiLanguageString getName() {
        return getFunctionality().getName();
    }

    @Override
    public MultiLanguageString getTitle() {
        return getFunctionality().getTitle();
    }

    @Override
    public AvailabilityPolicy getAvailabilityPolicy() {
        return getFunctionality().getAvailabilityPolicy();
    }

    @Override
    public Group getPermittedGroup() {
        if (getAvailabilityPolicy() == null) {
            return null;
        }
        else {
            return ((ExpressionGroupAvailability) getAvailabilityPolicy()).getTargetGroup().getGroup();
        }
    }

    @Override
    public Boolean isVisible() {
        return getFunctionality().isVisible();
    }
    
    @Override
    public boolean isVisible(FunctionalityContext context) {
        return getFunctionality().isVisible(context);
    }

    @Override
    public Boolean isEnabled() {
        return getFunctionality().isEnabled();
    }

    @Override
    public boolean isAllowedToEditPermissions() {
        return false;
    }

    @Override
    public boolean isItemAllowed() {
        return false;
    }

    @Override
    public boolean isSubSectionAllowed() {
        return false;
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        
        removeFunctionality();
    }
 
}
