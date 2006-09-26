package net.sourceforge.fenixedu.domain.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ParkingRequestSearch implements Serializable {
    private ParkingRequestState parkingRequestState;

    private ParkingPartyClassification parkingPartyClassification;

    private String personName;

    public ParkingPartyClassification getParkingPartyClassification() {
        return parkingPartyClassification;
    }

    public void setParkingPartyClassification(ParkingPartyClassification parkingPartyClassification) {
        this.parkingPartyClassification = parkingPartyClassification;
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

    public List<ParkingRequest> getSearch() {
        List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>();
        for (ParkingRequest request : RootDomainObject.getInstance().getParkingRequests()) {
            if (satisfiedPersonClassification(request) && satisfiedPersonName(request)
                    && satisfiedRequestState(request)) {
                parkingRequests.add(request);
            }
        }
        return parkingRequests;
    }

    private boolean satisfiedRequestState(ParkingRequest request) {
        return getParkingRequestState() == null
                || request.getParkingRequestState() == getParkingRequestState();
    }

    private boolean satisfiedPersonClassification(ParkingRequest request) {
        return getParkingPartyClassification() == null
                || request.getParkingParty().getParty().getPartyClassification() == getParkingPartyClassification();
    }

    private boolean satisfiedPersonName(ParkingRequest request) {
        return getPersonName() == null || getPersonName().trim().length() == 0
                || request.getParkingParty().getParty().getName().equals(getPersonName());
    }
}
