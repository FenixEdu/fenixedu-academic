package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Resource extends Resource_Base {
      
    protected Resource() {
        super();        
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete() {
	if(!canBeDeleted()) {
	    throw new DomainException("error.resource.cannot.be.deleted");
	}	
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private boolean canBeDeleted() {
        return !hasAnyResourceAllocations() && !hasAnyResourceResponsibility();
    }
           
    public boolean isSpace() {
	return false;
    }
    
    public boolean isVehicle() {
	return false;
    }
    
    public boolean isMaterial() {
	return false;
    }
    
    public boolean isCampus() {
	return false;
    }

    public boolean isBuilding() {
	return false;
    }

    public boolean isFloor() {
	return false;
    }

    public boolean isRoom() {
	return false;
    }
    
    public boolean isRoomSubdivision() {
	return false;
    }
    
    public boolean isExtension() {
	return false;
    }

    public boolean isFireExtinguisher() {
	return false;
    }       
    
    public boolean isAllocatableSpace() {
	return false;
    }
}

