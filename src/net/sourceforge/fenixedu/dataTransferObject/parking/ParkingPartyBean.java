package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

public class ParkingPartyBean implements FactoryExecutor, Serializable {

    private DomainReference<ParkingParty> parkingParty;

    private Long cardNumber;

    private Integer phdNumber;

    private String firstCarPlateNumber;

    private String firstCarMake;

    private String secondCarPlateNumber;

    private String secondCarMake;

    private DateTime cardStartDate;

    private DateTime cardEndDate;

    private DomainReference<ParkingGroup> parkingGroup;
    
    private Boolean deleteFirstCar;
    private Boolean deleteSecondCar;
    private Boolean cardAlwaysValid;

    public ParkingPartyBean(ParkingParty parkingParty) {
        setParkingParty(parkingParty);
        setCardNumber(parkingParty.getCardNumber());
        setPhdNumber(parkingParty.getPhdNumber());
        setFirstCarPlateNumber(parkingParty.getFirstCarPlateNumber());
        setFirstCarMake(parkingParty.getFirstCarMake());
        setSecondCarPlateNumber(parkingParty.getSecondCarPlateNumber());
        setSecondCarMake(parkingParty.getSecondCarMake());
        setCardStartDate(parkingParty.getCardStartDate());
        setCardEndDate(parkingParty.getCardEndDate());
        setParkingGroup(parkingParty.getParkingGroup());
    }

    public Object execute() {
        ParkingParty parkingParty = getParkingParty();
        parkingParty.edit(this);
        return parkingParty;
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

    public String getFirstCarMake() {
        return firstCarMake;
    }

    public void setFirstCarMake(String firstCarMake) {
        this.firstCarMake = firstCarMake;
    }

    public String getFirstCarPlateNumber() {
        return firstCarPlateNumber;
    }

    public void setFirstCarPlateNumber(String firstCarPlateNumber) {
        this.firstCarPlateNumber = firstCarPlateNumber;
    }

    public ParkingGroup getParkingGroup() {
        return parkingGroup == null ? null : parkingGroup.getObject();
    }

    public void setParkingGroup(ParkingGroup parkingGroup) {
        if (parkingGroup != null) {
            this.parkingGroup = new DomainReference<ParkingGroup>(parkingGroup);
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

    public String getSecondCarMake() {
        return secondCarMake;
    }

    public void setSecondCarMake(String secondCarMake) {
        this.secondCarMake = secondCarMake;
    }

    public String getSecondCarPlateNumber() {
        return secondCarPlateNumber;
    }

    public void setSecondCarPlateNumber(String secondCarPlateNumber) {
        this.secondCarPlateNumber = secondCarPlateNumber;
    }

    public Boolean getDeleteFirstCar() {
        return deleteFirstCar;
    }

    public void setDeleteFirstCar(Boolean deleteFirstCar) {
        this.deleteFirstCar = deleteFirstCar;
    }

    public Boolean getDeleteSecondCar() {
        return deleteSecondCar;
    }

    public void setDeleteSecondCar(Boolean deleteSecondCar) {
        this.deleteSecondCar = deleteSecondCar;
    }

    public Boolean getCardAlwaysValid() {
        return cardAlwaysValid;
    }

    public void setCardAlwaysValid(Boolean cardAlwaysValid) {
        this.cardAlwaysValid = cardAlwaysValid;
    }

}
