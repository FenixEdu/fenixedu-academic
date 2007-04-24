package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class Resource extends Resource_Base {
    
    protected Resource() {
        super();        
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
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
    
    public boolean isExtension() {
	return false;
    }
}

