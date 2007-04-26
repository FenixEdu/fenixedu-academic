package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class SpaceAccessGroupsManagement extends Service {

    public void run(Space space, SpaceAccessGroupType accessGroupType, Person person, boolean toAdd, boolean isToMaintainElements) 
    	throws FenixServiceException {
	
	space.addOrRemovePersonFromAccessGroup(accessGroupType, person, Boolean.valueOf(toAdd), Boolean.valueOf(isToMaintainElements));	    
    }   
}
