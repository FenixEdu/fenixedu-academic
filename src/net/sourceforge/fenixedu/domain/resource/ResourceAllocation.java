package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class ResourceAllocation extends ResourceAllocation_Base {
    
    protected ResourceAllocation() {
        super();
        setOjbConcreteClass(getClass().getName());
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public boolean isSpaceOccupation() {
	return false;
    }
    
    public boolean isVehicleAllocation() {
	return false;
    }
    
    public boolean isPersonSpaceOccupation() {
	return false;
    }
    
    public boolean isMaterialSpaceOccupation() {
	return false;
    }
    
    public boolean isExtensionSpaceOccupation() {
	return false;
    }
    
    public boolean isUnitSpaceOccupation() {
	return false;
    }
    
    @Override
    public void setResource(Resource resource) {
	if (resource == null) {
	    throw new DomainException("error.allocation.no.space");
	}	
	super.setResource(resource);
    }
    
    public void delete() {
	super.setResource(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }     
}
