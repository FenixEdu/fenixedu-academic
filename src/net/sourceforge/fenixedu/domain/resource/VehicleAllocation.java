package net.sourceforge.fenixedu.domain.resource;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class VehicleAllocation extends VehicleAllocation_Base {
    
    public final static Comparator<VehicleAllocation> COMPARATOR_BY_VEHICLE_NUMBER_PLATE = new ComparatorChain();    
    static {	
	((ComparatorChain) COMPARATOR_BY_VEHICLE_NUMBER_PLATE).addComparator(new BeanComparator("vehicle.numberPlate"));
	((ComparatorChain) COMPARATOR_BY_VEHICLE_NUMBER_PLATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")    
    public VehicleAllocation(DateTime beginDateTime, DateTime endDateTime, Vehicle vehicle, Party requestor, String reason) {
        super();
        setResource(vehicle);
        setRequestor(requestor);
        setReason(reason);  
        setAllocationInterval(beginDateTime, endDateTime);
    }
    
    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")
    public void edit(DateTime beginDateTime, DateTime endDateTime, Vehicle vehicle, Party requestor, String reason) {		
	setResource(vehicle);
	setRequestor(requestor);
	setReason(reason);  
	setAllocationInterval(beginDateTime, endDateTime);
    }
    
    @Override
    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")    
    public void delete() {
	DateTime currentDateTime = new DateTime();
	if(occursInThePast(currentDateTime)) {
	    throw new DomainException("error.cannot.delete.allocation.already.occured");
	}
        super.setRequestor(null);
	super.delete();
    }
    
    @Override
    public void setBeginDateTime(DateTime beginDateTime) {
	throw new DomainException("error.invalid.operation");
    }
        
    @Override
    public void setEndDateTime(DateTime endDateTime) {
	throw new DomainException("error.invalid.operation");
    }
    
    public void setAllocationInterval(DateTime begin, DateTime end) {
	checkAllocationIntersection(begin, end);
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
    public void setResource(Resource resource) {		
	if (resource == null || !resource.isVehicle()) {
	    throw new DomainException("error.allocation.invalid.resource");
	}
	super.setResource(resource);
    }
        
    public Vehicle getVehicle() {
	return (Vehicle) getResource();
    }
    
    public void setVehicle(Vehicle vehicle) {
	setResource(vehicle);
    }
    
    public Boolean getReasonAvailable() {
	return !StringUtils.isEmpty(getReason());
    }
    
    public boolean intersectPeriod(DateTime firstDayOfMonth, DateTime lastDayOfMonth) {			
	return !getBeginDateTime().isAfter(lastDayOfMonth) && !getEndDateTime().isBefore(firstDayOfMonth);
    }
    
    public boolean isActive(DateTime currentDate) {
	return intersectPeriod(currentDate, currentDate);
    }

    private boolean occursInTheFuture(DateTime currentDateTime) {
	return getBeginDateTime().isAfter(currentDateTime);
    }
    
    private boolean occursInThePast(DateTime currentDateTime) {
	return !occursInTheFuture(currentDateTime) && !isActive(currentDateTime);
    }
    
    public void checkAllocationIntersection(DateTime begin, DateTime end) {		
	if(getBeginDateTime() != null && getEndDateTime() != null && occursInThePast(new DateTime())) {
	    throw new DomainException("error.cannot.delete.allocation.already.occured");
	}
	checkBeginDateAndEndDate(begin, end);	
	for (ResourceAllocation resourceAllocation : getVehicle().getResourceAllocations()) {
	    if (!resourceAllocation.equals(this) && ((VehicleAllocation)resourceAllocation).allocationsIntersection(begin, end)) {
		throw new DomainException("error.VehicleAllocation.allocation.intersection");
	    }
	}
    }

    private boolean allocationsIntersection(DateTime begin, DateTime end) {
	return !getBeginDateTime().isAfter(end) && !getEndDateTime().isBefore(begin);
    }

    private void checkBeginDateAndEndDate(DateTime begin, DateTime end) {
	if (begin == null) {
	    throw new DomainException("error.VehicleAllocation.no.begin");
	}
	if (end == null) {
	    throw new DomainException("error.VehicleAllocation.no.end");
	}
	if (!end.isAfter(begin)) {
	    throw new DomainException("error.VehicleAllocation.begin.after.end");
	}
    }
    
    public static Set<VehicleAllocation> getActiveVehicleAllocations(){
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if(resourceAllocation.isVehicleAllocation() && ((VehicleAllocation)resourceAllocation).isActive(currentDateTime)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }
    
    public static Set<VehicleAllocation> getFutureVehicleAllocations(){
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if(resourceAllocation.isVehicleAllocation() && ((VehicleAllocation)resourceAllocation).occursInTheFuture(currentDateTime)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }
    
    public static Set<VehicleAllocation> getPastVehicleAllocations(DateTime begin, DateTime end){
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if(resourceAllocation.isVehicleAllocation()
		    && ((VehicleAllocation)resourceAllocation).occursInThePast(currentDateTime)
		    && ((VehicleAllocation)resourceAllocation).intersectPeriod(begin, end)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }
}
