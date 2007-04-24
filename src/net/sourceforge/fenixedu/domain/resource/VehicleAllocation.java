package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

public class VehicleAllocation extends VehicleAllocation_Base {
    
    public VehicleAllocation() {
        super();
    }
    
    @Override
    public void setBeginDateTime(DateTime beginDateTime) {
        checkAllocationIntersection(beginDateTime, getEndDateTime(), getVehicle());
	super.setBeginDateTime(beginDateTime);
    }
        
    @Override
    public void setEndDateTime(DateTime endDateTime) {
	checkAllocationIntersection(getBeginDateTime(), endDateTime, getVehicle());
	super.setEndDateTime(endDateTime);
    }
    
    public void setAllocationInterval(DateTime begin, DateTime end) {
	checkAllocationIntersection(begin, end, getVehicle());
	super.setBeginDateTime(begin);
	super.setEndDateTime(end);
    }
    
    @Override
    public void setRequestor(Party requestor) {
        if(requestor == null) {
            throw new DomainException("error.VehicleAllocation.empty.requestor");
        }
	super.setRequestor(requestor);
    }   
    
    @Override
    public boolean isVehicleAllocation() {
        return true;
    }
    
    @Override
    public void delete() {
        super.setRequestor(null);
	super.delete();
    }
    
    public Vehicle getVehicle() {
	return (Vehicle) getResource();
    }

    public void checkAllocationIntersection(DateTime begin, DateTime end, Vehicle vehicle) {
	checkBeginDateAndEndDate(begin, end);	
	for (ResourceAllocation resourceAllocation : vehicle.getResourceAllocations()) {
	    if (!resourceAllocation.equals(this) && ((VehicleAllocation)resourceAllocation).allocationsIntersection(begin, end)) {
		throw new DomainException("error.VehicleAllocation.allocation.intersection");
	    }
	}
    }

    private boolean allocationsIntersection(DateTime begin, DateTime end) {
	return ((end == null || !getBeginDateTime().isAfter(end))
		&& (getEndDateTime() == null || !getEndDateTime().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(DateTime begin, DateTime end) {
	if (begin == null) {
	    throw new DomainException("error.VehicleAllocation.no.begin");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.VehicleAllocation.begin.after.end");
	}
    }
}
