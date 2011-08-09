package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class LibraryCardSystem extends LibraryCardSystem_Base {
    
    public  LibraryCardSystem() {
        super();
    }
 
    @Override
    public Group getHigherClearenceGroup() {
	Group group = super.getHigherClearenceGroup(); 
	if (group == null){
	    group = new FixedSetGroup();
	    setHigherClearenceGroup(group);
	}
        return group;
    }
}
