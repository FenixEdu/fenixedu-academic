package net.sourceforge.fenixedu.domain.resource;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.ResourcePredicates;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Vehicle extends Vehicle_Base {

    public Vehicle(String numberPlate, String make, String model, YearMonthDay acquisition, YearMonthDay cease,
            BigDecimal allocationCostMultiplier) {
//        check(this, ResourcePredicates.checkPermissionsToManageVehicle);
        super();
        setNumberPlate(numberPlate);
        setMake(make);
        setModel(model);
        setAcquisition(acquisition);
        setCease(cease);
        setAllocationCostMultiplier(allocationCostMultiplier);
    }

    public void edit(String numberPlate, String make, String model, YearMonthDay acquisition, YearMonthDay cease,
            BigDecimal allocationCostMultiplier) {
        check(this, ResourcePredicates.checkPermissionsToManageVehicle);
        setNumberPlate(numberPlate);
        setMake(make);
        setModel(model);
        setAcquisition(acquisition);
        setCease(cease);
        setAllocationCostMultiplier(allocationCostMultiplier);
    }

    @Override
    public void delete() {
        check(this, ResourcePredicates.checkPermissionsToManageVehicle);
        super.delete();
    }

    @Override
    public boolean isVehicle() {
        return true;
    }

    @Override
    public void setAllocationCostMultiplier(BigDecimal allocationCostMultiplier) {
        if (allocationCostMultiplier == null || allocationCostMultiplier.compareTo(BigDecimal.ZERO) == -1) {
            throw new DomainException("error.Vehicle.invalid.allocation.cost.multiplier");
        }
        super.setAllocationCostMultiplier(allocationCostMultiplier);
    }

    @Override
    public void setAcquisition(YearMonthDay acquisition) {
        if (acquisition == null || (getCease() != null && getCease().isBefore(acquisition))) {
            throw new DomainException("error.Vehicle.invalid.acquisitionDate");
        }
        super.setAcquisition(acquisition);
    }

    @Override
    public void setCease(YearMonthDay cease) {
        if (getAcquisition() == null || (cease != null && cease.isBefore(getAcquisition()))) {
            throw new DomainException("error.Vehicle.invalid.ceaseDate");
        }
        super.setCease(cease);
    }

    @Override
    public void setModel(String model) {
        if (StringUtils.isEmpty(model)) {
            throw new DomainException("error.Vehicle.empty.model");
        }
        super.setModel(model);
    }

    @Override
    public void setMake(String make) {
        if (StringUtils.isEmpty(make)) {
            throw new DomainException("error.Vehicle.empty.make");
        }
        super.setMake(make);
    }

    @Override
    public void setNumberPlate(String numberPlate) {
        if (numberPlate == null) {
            throw new DomainException("error.Vehicle.empty.number.plate");
        }
        numberPlate = numberPlate.trim();
        checksIfVehicleAlreadyExists(numberPlate);
        super.setNumberPlate(numberPlate);
    }

    public String getPresentationName() {
        return getNumberPlate() + " (" + getMake() + " " + getModel() + ")";
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!getAcquisition().isAfter(currentDate) && (getCease() == null || !getCease().isBefore(currentDate)));
    }

    private void checksIfVehicleAlreadyExists(String numberPlate) {
        Collection<Resource> resources = RootDomainObject.getInstance().getResources();
        for (Resource resource : resources) {
            if (resource.isVehicle() && !resource.equals(this)
                    && ((Vehicle) resource).getNumberPlate().equalsIgnoreCase(numberPlate)) {
                throw new DomainException("error.Vehicle.already.exists");
            }
        }
    }

    public static Vehicle getVehicleByNumberPlate(String numberPlate) {
        Collection<Resource> resources = RootDomainObject.getInstance().getResources();
        for (Resource resource : resources) {
            if (resource.isVehicle() && ((Vehicle) resource).getNumberPlate().equalsIgnoreCase(numberPlate)) {
                return (Vehicle) resource;
            }
        }
        return null;
    }

    public static List<Vehicle> getAllActiveVehicles() {
        List<Vehicle> result = new ArrayList<Vehicle>();
        YearMonthDay currentDate = new YearMonthDay();
        Collection<Resource> resources = RootDomainObject.getInstance().getResources();
        for (Resource resource : resources) {
            if (resource.isVehicle() && ((Vehicle) resource).isActive(currentDate)) {
                result.add((Vehicle) resource);
            }
        }
        return result;
    }

    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> result = new ArrayList<Vehicle>();
        Collection<Resource> resources = RootDomainObject.getInstance().getResources();
        for (Resource resource : resources) {
            if (resource.isVehicle()) {
                result.add((Vehicle) resource);
            }
        }
        return result;
    }

    @Deprecated
    public boolean hasAcquisition() {
        return getAcquisition() != null;
    }

    @Deprecated
    public boolean hasModel() {
        return getModel() != null;
    }

    @Deprecated
    public boolean hasAllocationCostMultiplier() {
        return getAllocationCostMultiplier() != null;
    }

    @Deprecated
    public boolean hasNumberPlate() {
        return getNumberPlate() != null;
    }

    @Deprecated
    public boolean hasCease() {
        return getCease() != null;
    }

    @Deprecated
    public boolean hasMake() {
        return getMake() != null;
    }

}
