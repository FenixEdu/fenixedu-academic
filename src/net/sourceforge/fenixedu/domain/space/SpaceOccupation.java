package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class SpaceOccupation extends SpaceOccupation_Base {

    protected SpaceOccupation() {
	super();	
    }
            
    public abstract Group getAccessGroup();

    public void checkPermissionsToManageSpaceOccupations() {
	Person loggedPerson = AccessControl.getPerson();
	if (getSpace().personHasPermissionsToManageSpace(loggedPerson)) {
	    return;
	}

	final Group group = getAccessGroup();
	if (group != null && group.isMember(loggedPerson)) {
	    return;
	}
	
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    public void checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole() {
	Person loggedPerson = AccessControl.getPerson();	
	final Group group = getAccessGroup();
	if (group != null && group.isMember(loggedPerson)) {
	    return;
	}	
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    } 
    
    public Space getSpace() {
	return (Space) getResource();
    }

    @Override
    public void setResource(Resource resource) {	
	super.setResource(resource);
	if (!resource.isSpace()) {
	    throw new DomainException("error.allocation.invalid.resource.type");
	}
    }
          
    @Override
    public boolean isSpaceOccupation() {
        return true;
    }       
}
