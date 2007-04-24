package net.sourceforge.fenixedu.domain.resource;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Vehicle extends Vehicle_Base {
    
    public Vehicle() {
        super();
    }     
    
    @Override
    public void setNumberPlate(String numberPlate) {
	if(numberPlate == null) {
            throw new DomainException("error.Vehicle.empty.number.plate");
        }
        checksIfVehicleAlreadyExists(numberPlate);	
	super.setNumberPlate(numberPlate);
    }
    
    private void checksIfVehicleAlreadyExists(String numberPlate) {
	List<Resource> resources = RootDomainObject.getInstance().getResources();
	for (Resource resource : resources) {
	    if(!resource.equals(this) && resource.isVehicle() && ((Vehicle)resource).getNumberPlate().equals(numberPlate)) {
		throw new DomainException("error.Vehicle.already.exists");
	    }
	}
    }
}
