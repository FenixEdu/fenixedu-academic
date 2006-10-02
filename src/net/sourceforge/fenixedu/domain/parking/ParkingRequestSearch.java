package net.sourceforge.fenixedu.domain.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ParkingRequestSearch implements Serializable {
    private ParkingRequestState parkingRequestState;

    private ParkingPartyClassification parkingPartyClassification;

    private String personName;

    private String carPlateNumber;

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
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("creationDate"));
        comparatorChain.addComparator(new BeanComparator("parkingRequestState"));
        Collections.sort(parkingRequests, comparatorChain);
        return parkingRequests;
    }

    private boolean satisfiedCarPlateNumber(ParkingRequest request) {
        return getCarPlateNumber() == null
                || (request.getFirstCarPlateNumber() != null && (request.getFirstCarPlateNumber()
                        .equalsIgnoreCase(getCarPlateNumber()) || request.getFirstCarPlateNumber()
                        .toLowerCase().contains(getCarPlateNumber().toLowerCase())))
                || (request.getSecondCarPlateNumber() != null && (request.getSecondCarPlateNumber()
                        .equalsIgnoreCase(getCarPlateNumber()) || request.getSecondCarPlateNumber()
                        .toLowerCase().contains(getCarPlateNumber().toLowerCase())));
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
        return org.apache.commons.lang.StringUtils.isEmpty(getPersonName())
                || StringUtils.verifyContainsWithEquality(
                        request.getParkingParty().getParty().getName(), getPersonName());
    }

}
