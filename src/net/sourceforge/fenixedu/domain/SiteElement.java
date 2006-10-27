package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;

/**
 * Base class for all the accessible elements of a {@link net.sourceforge.fenixedu.domain.Site Site}.
 * 
 * @author cfgi
 */
public abstract class SiteElement extends SiteElement_Base {
    
    public SiteElement() {
        super();
    }
 
    public Group getPermittedGroup() {
        GroupAvailability groupAvailability = (GroupAvailability) getAvailabilityPolicy();
        
        if (groupAvailability == null) {
            return null;
        }
        else {
            return groupAvailability.getTargetGroup();
        }
    }
    
    public void setPermittedGroup(Group group) {
        if (group == null) {
            setAvailabilityPolicy(null);
        }
        
        new GroupAvailability(this, group);
    }
}
