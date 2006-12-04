package net.sourceforge.fenixedu.domain.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
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
        return parkingRequests;
    }

    private boolean satisfiedCarPlateNumber(ParkingRequest request) {
        if (getCarPlateNumber() == null) {
            return true;
        }
        return request.hasVehicleContainingPlateNumber(getCarPlateNumber());       
    }

    private boolean satisfiedRequestState(ParkingRequest request) {
        return getParkingRequestState() == null
                || request.getParkingRequestState() == getParkingRequestState();
    }

    private boolean satisfiedPersonClassification(ParkingRequest request) {
        if (getParkingPartyClassification() == null
                || request.getParkingParty().getParty().getPartyClassification() == getParkingPartyClassification()) {
            if (getParkingPartyClassification() == ParkingPartyClassification.TEACHER) {
                Person person = (Person) request.getParkingParty().getParty();
                if (person.getTeacher().isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                    return false;
                }
                return true;
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
