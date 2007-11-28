package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import net.sourceforge.fenixedu.domain.resource.Vehicle;

public class VehicleAllocationHistoryBean extends RoomsPunctualSchedulingHistoryBean {   

    public VehicleAllocationHistoryBean() {
	super();
    }        
    
    public Vehicle getVehicle() {
	return (Vehicle) getResource();
    }
    
    public void setVehicle(Vehicle vehicle) {
	setResource(vehicle); 
    }
}
