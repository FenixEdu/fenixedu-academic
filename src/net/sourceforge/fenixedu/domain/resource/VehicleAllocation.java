package net.sourceforge.fenixedu.domain.resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class VehicleAllocation extends VehicleAllocation_Base {

    public final static Comparator<VehicleAllocation> COMPARATOR_BY_VEHICLE_NUMBER_PLATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_VEHICLE_NUMBER_PLATE).addComparator(new BeanComparator("vehicle.numberPlate"));
	((ComparatorChain) COMPARATOR_BY_VEHICLE_NUMBER_PLATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")
    public VehicleAllocation(DateTime beginDateTime, DateTime endDateTime, Vehicle vehicle, Party requestor, String reason,
	    BigDecimal distance, BigDecimal amountCharged) {

	super();
	setResource(vehicle);
	setRequestor(requestor);
	setReason(reason);
	setDistance(distance);
	setAllocationInterval(beginDateTime, endDateTime);
	setAmountCharged(amountCharged);
    }

    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")
    public void edit(DateTime beginDateTime, DateTime endDateTime, Vehicle vehicle, Party requestor, String reason,
	    BigDecimal distance, BigDecimal amountCharged) {

	setResource(vehicle);
	setRequestor(requestor);
	setReason(reason);
	setDistance(distance);
	setAllocationInterval(beginDateTime, endDateTime);
	setAmountCharged(amountCharged);
    }

    @Override
    @Checked("ResourceAllocationPredicates.checkPermissionsToManageVehicleAllocations")
    public void delete() {
	DateTime currentDateTime = new DateTime();
	if (occursInThePast(currentDateTime)) {
	    throw new DomainException("error.cannot.delete.allocation.already.occured");
	}
	super.setRequestor(null);
	super.delete();
    }

    @Override
    public void setAmountCharged(BigDecimal amountCharged) {
	if (amountCharged != null && amountCharged.compareTo(BigDecimal.ZERO) != 1) {
	    throw new DomainException("error.Vehicle.allocation.invalid.amountCharged");
	}
	super.setAmountCharged(amountCharged);
    }

    @Override
    public void setDistance(BigDecimal distance) {
	if (distance != null && distance.compareTo(BigDecimal.ZERO) != 1) {
	    throw new DomainException("error.Vehicle.allocation.invalid.distance");
	}
	super.setDistance(distance);
    }

    public BigDecimal getAllocationCost() {
	return getAllocationCost(getVehicle(), getDistance(), getBeginDateTime(), getEndDateTime());
    }

    public static BigDecimal getAllocationCost(Vehicle vehicle, BigDecimal distance, DateTime begin, DateTime end) {

	if (distance == null || vehicle == null || begin == null || end == null) {
	    return null;
	}

	BigDecimal distanceValue = getDistanceValue(vehicle, distance);
	BigDecimal hourValue = getHourValue();
	BigDecimal hoursNumber = getNumberOfAllocationHours(begin, end);

	return distance.multiply(distanceValue).add((hoursNumber.multiply(hourValue))).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getNumberOfAllocationHours() {
	return getNumberOfAllocationHours(getBeginDateTime(), getEndDateTime());
    }

    public static BigDecimal getNumberOfAllocationHours(DateTime begin, DateTime end) {
	int minutes = Minutes.minutesBetween(begin, end).getMinutes();
	BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(Lesson.NUMBER_OF_MINUTES_IN_HOUR), 2,
		RoundingMode.HALF_UP);
	return hours;
    }

    private static BigDecimal getHourValue() {
	return BigDecimal.TEN;
    }

    private static BigDecimal getDistanceValue(Vehicle vehicle, BigDecimal distance) {

	BigDecimal allocationCostMultiplier = vehicle.getAllocationCostMultiplier();

	BigDecimal firstConstant = BigDecimal.valueOf(0.6);
	BigDecimal secondConstant = BigDecimal.valueOf(-1);
	BigDecimal thirdConstant = BigDecimal.valueOf(780);
	BigDecimal fourConstant = BigDecimal.valueOf(1000);

	return (allocationCostMultiplier.multiply((firstConstant.multiply(secondConstant).multiply(distance).add(thirdConstant)))
		.divide(fourConstant));
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
	if (requestor == null) {
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
	if (getBeginDateTime() != null && getEndDateTime() != null && occursInThePast(new DateTime())) {
	    throw new DomainException("error.cannot.delete.allocation.already.occured");
	}
	checkBeginDateAndEndDate(begin, end);
	for (ResourceAllocation resourceAllocation : getVehicle().getResourceAllocations()) {
	    if (!resourceAllocation.equals(this) && ((VehicleAllocation) resourceAllocation).allocationsIntersection(begin, end)) {
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

    public static Set<VehicleAllocation> getActiveVehicleAllocations() {
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if (resourceAllocation.isVehicleAllocation() && ((VehicleAllocation) resourceAllocation).isActive(currentDateTime)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }

    public static Set<VehicleAllocation> getFutureVehicleAllocations() {
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if (resourceAllocation.isVehicleAllocation()
		    && ((VehicleAllocation) resourceAllocation).occursInTheFuture(currentDateTime)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }

    public static Set<VehicleAllocation> getPastVehicleAllocations(DateTime begin, DateTime end, Vehicle vehicle) {
	List<ResourceAllocation> resourceAllocations = RootDomainObject.getInstance().getResourceAllocations();
	Set<VehicleAllocation> result = new TreeSet<VehicleAllocation>(COMPARATOR_BY_VEHICLE_NUMBER_PLATE);
	DateTime currentDateTime = new DateTime();
	for (ResourceAllocation resourceAllocation : resourceAllocations) {
	    if (resourceAllocation.isVehicleAllocation()
		    && (vehicle == null || ((VehicleAllocation) resourceAllocation).getVehicle().equals(vehicle))
		    && ((VehicleAllocation) resourceAllocation).occursInThePast(currentDateTime)
		    && ((VehicleAllocation) resourceAllocation).intersectPeriod(begin, end)) {
		result.add((VehicleAllocation) resourceAllocation);
	    }
	}
	return result;
    }
}
