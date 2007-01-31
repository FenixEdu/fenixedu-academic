package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class DegreeSite extends DegreeSite_Base {

    public DegreeSite(Degree degree) {
        super();
        
        setDegree(degree);
    }
    
    @Override
    public IGroup getOwner() {
        return new CurrentDegreeCoordinatorsGroup(getDegree());
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        groups.add(new CurrentDegreeCoordinatorsGroup(getDegree()));
        
        return groups;
    }
    
}
