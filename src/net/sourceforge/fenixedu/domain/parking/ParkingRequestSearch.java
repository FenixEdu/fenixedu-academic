package net.sourceforge.fenixedu.domain.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.StringUtils;

public class ParkingRequestSearch implements Serializable {
    private ParkingRequestState parkingRequestState;

    private PartyClassification partyClassification;

    private String personName;

    private String carPlateNumber;

    public PartyClassification getPartyClassification() {
        return partyClassification;
    }

    public void setPartyClassification(PartyClassification partyClassification) {
        this.partyClassification = partyClassification;
    }

    public ParkingRequestState getParkingRequestState() {
        return parkingRequestState;
    }

    public void setParkingRequestState(ParkingRequestState parkingRequestState) {
        this.parkingRequestState = parkingRequestState;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public List<ParkingRequest> getSearch() {
        List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>();
        for (ParkingRequest request : RootDomainObject.getInstance().getParkingRequests()) {
            if (satisfiedPersonClassification(request) && satisfiedPersonName(request)
                    && satisfiedRequestState(request) && satisfiedCarPlateNumber(request)) {
                parkingRequests.add(request);
            }
        }
        return parkingRequests;
    }

    private boolean satisfiedCarPlateNumber(ParkingRequest request) {
        if (org.apache.commons.lang.StringUtils.isEmpty(getCarPlateNumber())) {
            return true;
        }
        return request.hasVehicleContainingPlateNumber(getCarPlateNumber());       
    }

    private boolean satisfiedRequestState(ParkingRequest request) {
        return getParkingRequestState() == null
                || request.getParkingRequestState() == getParkingRequestState();
    }

    private boolean satisfiedPersonClassification(ParkingRequest request) {
        if (getPartyClassification() == null
                || request.getParkingParty().getParty().getPartyClassification() == getPartyClassification()) {
            if (getPartyClassification() == PartyClassification.TEACHER) {
                Person person = (Person) request.getParkingParty().getParty();
                if (person.getTeacher().isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                    return false;
                }                
            }
            return true;
        }
        return false;
    }

    private boolean satisfiedPersonName(ParkingRequest request) {
        return org.apache.commons.lang.StringUtils.isEmpty(getPersonName())
                || StringUtils.verifyContainsWithEquality(
                        request.getParkingParty().getParty().getName(), getPersonName());
    }

}
