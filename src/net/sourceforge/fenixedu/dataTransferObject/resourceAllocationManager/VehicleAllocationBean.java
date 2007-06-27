package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

import org.joda.time.DateTime;

public class VehicleAllocationBean implements Serializable {

    private String reason;
    
    private DateTime beginDateTime;
    
    private DateTime endDateTime;
    
    private DomainReference<Party> partyReference;
    
    private DomainReference<Vehicle> vehicleReference;
    
    public VehicleAllocationBean() {
    }
    
    public Party getRequestor() {
	 return (this.partyReference != null) ? this.partyReference.getObject() : null;	
    }
    
    public void setRequestor(Party requestor) {
	this.partyReference = (requestor != null) ? new DomainReference<Party>(requestor) : null;
    }
    
    public Vehicle getVehicle() {
	 return (this.vehicleReference != null) ? this.vehicleReference.getObject() : null;
    }
    
    public void setVehicle(Vehicle vehicle) {
	this.vehicleReference = (vehicle != null) ? new DomainReference<Vehicle>(vehicle) : null;
    }
    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(DateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
