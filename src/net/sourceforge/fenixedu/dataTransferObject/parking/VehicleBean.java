package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;

public class VehicleBean implements Serializable {

    private DomainReference<Vehicle> vehicle;

    private DomainReference<ParkingParty> parkingParty;

    private String vehiclePlateNumber;

    private String vehicleMake;

    private Boolean deleteVehicle;

    public VehicleBean(Vehicle vehicle, ParkingParty parkingParty) {
        setVehicle(vehicle);
        setParkingParty(parkingParty);
        setVehiclePlateNumber(vehicle.getPlateNumber());
        setVehicleMake(vehicle.getVehicleMake());
        setDeleteVehicle(false);
    }

    public VehicleBean(ParkingParty parkingParty) {
        setParkingParty(parkingParty);
        setDeleteVehicle(false);
    }

    public Vehicle getVehicle() {
        return vehicle == null ? null : vehicle.getObject();
    }

    public void setVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            this.vehicle = new DomainReference<Vehicle>(vehicle);
        } else {
            this.vehicle = null;
        }
    }

    public ParkingParty getParkingParty() {
        return parkingParty == null ? null : parkingParty.getObject();
    }

    public void setParkingParty(ParkingParty parkingParty) {
        if (parkingParty != null) {
            this.parkingParty = new DomainReference<ParkingParty>(parkingParty);
        } else {
            this.parkingParty = null;
        }
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public Boolean getDeleteVehicle() {
        return deleteVehicle;
    }

    public void setDeleteVehicle(Boolean deleteVehicle) {
        this.deleteVehicle = deleteVehicle;
    }
}
