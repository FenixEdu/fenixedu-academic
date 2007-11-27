package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

public class VehicleAllocationHistoryBean extends RoomsPunctualSchedulingHistoryBean {   
    
    private DomainReference<Vehicle> vehicleReference;
    
    public VehicleAllocationHistoryBean() {
	super();
    }    
    
    
    public Vehicle getVehicle() {
	return vehicleReference != null ? vehicleReference.getObject() : null;
    }
    
    public void setVehicle(Vehicle vehicle) {
	this.vehicleReference = vehicle != null ? new DomainReference<Vehicle>(vehicle) : null; 
    }
}
