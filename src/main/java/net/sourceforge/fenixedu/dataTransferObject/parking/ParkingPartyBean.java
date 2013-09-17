package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

public class ParkingPartyBean implements FactoryExecutor, Serializable {

    private ParkingParty parkingParty;

    private Long cardNumber;

    private Integer phdNumber;

    private String email;

    private DateTime cardStartDate;

    private DateTime cardEndDate;

    private ParkingGroup parkingGroup;

    private Boolean cardAlwaysValid;

    private String notes;

    private List<VehicleBean> vehicles = new ArrayList();

    public ParkingPartyBean(ParkingParty parkingParty) {
        setParkingParty(parkingParty);
        setCardNumber(parkingParty.getCardNumber());
        setPhdNumber(parkingParty.getPhdNumber());
        setCardStartDate(parkingParty.getCardStartDate());
        setCardEndDate(parkingParty.getCardEndDate());
        setParkingGroup(parkingParty.getParkingGroup());
        if (parkingParty.getParty().isPerson()) {
            setEmail(((Person) parkingParty.getParty()).getEmail());
        }
        setCardAlwaysValid(parkingParty.getCardStartDate() == null);
        setVehicles(parkingParty.getVehicles());
        setNotes(parkingParty.getNotes());
    }

    @Override
    public Object execute() {
        ParkingParty parkingParty = getParkingParty();
        parkingParty.edit(this);
        return parkingParty;
    }

    public ParkingParty getParkingParty() {
        return parkingParty;
    }

    public void setParkingParty(ParkingParty parkingParty) {
        if (parkingParty != null) {
            this.parkingParty = parkingParty;
        } else {
            this.parkingParty = null;
        }
    }

    public DateTime getCardEndDate() {
        return cardEndDate;
    }

    public void setCardEndDate(DateTime cardEndDate) {
        this.cardEndDate = cardEndDate;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public DateTime getCardStartDate() {
        return cardStartDate;
    }

    public void setCardStartDate(DateTime cardStartDate) {
        this.cardStartDate = cardStartDate;
    }

    public ParkingGroup getParkingGroup() {
        return parkingGroup;
    }

    public void setParkingGroup(ParkingGroup parkingGroup) {
        if (parkingGroup != null) {
            this.parkingGroup = parkingGroup;
        } else {
            this.parkingGroup = null;
        }
    }

    public Integer getPhdNumber() {
        return phdNumber;
    }

    public void setPhdNumber(Integer phdNumber) {
        this.phdNumber = phdNumber;
    }

    public Boolean getCardAlwaysValid() {
        return cardAlwaysValid;
    }

    public void setCardAlwaysValid(Boolean cardAlwaysValid) {
        this.cardAlwaysValid = cardAlwaysValid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VehicleBean> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Collection<Vehicle> vehicles) {
        if (vehicles != null) {
            List<VehicleBean> vehicleBeanList = new ArrayList<VehicleBean>();
            for (Vehicle vehicle : vehicles) {
                vehicleBeanList.add(new VehicleBean(vehicle, vehicle.getParkingParty()));
            }
            this.vehicles = vehicleBeanList;
        }
    }

    public void updateVehicles(List<VehicleBean> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle() {
        getVehicles().add(new VehicleBean(getParkingParty()));
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
