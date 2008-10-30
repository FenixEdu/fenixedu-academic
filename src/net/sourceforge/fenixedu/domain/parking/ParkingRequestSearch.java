package net.sourceforge.fenixedu.domain.parking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.StringUtils;

public class ParkingRequestSearch implements Serializable {
    private ParkingRequestState parkingRequestState;

    private PartyClassification partyClassification;

    private String personName;

    private String carPlateNumber;

    private DomainListReference<ParkingRequest> searchResult;

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

    public void doSearch() {
	final List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>();

	if (personName != null && !personName.isEmpty()) {
	    final Collection<Person> people = Person.findPerson(personName);
	    for (final Person person : people) {
		final ParkingParty parkingParty = person.getParkingParty();
		if (parkingParty != null) {
		    for (final ParkingRequest parkingRequest : parkingParty.getParkingRequestsSet()) {
			if (satisfiedPersonClassification(parkingRequest)
				&& satisfiedRequestState(parkingRequest)
				&& satisfiedCarPlateNumber(parkingRequest)) {
			    parkingRequests.add(parkingRequest);
			}			
		    }
		}
	    }
	} else if (parkingRequestState != null || partyClassification != null || carPlateNumber != null) {
	    for (ParkingRequest request : RootDomainObject.getInstance().getParkingRequests()) {
		if (satisfiedPersonClassification(request) && satisfiedPersonName(request) && satisfiedRequestState(request)
			&& satisfiedCarPlateNumber(request)) {
		    parkingRequests.add(request);
		}
	    }	    
	}

	setSearchResult(parkingRequests);
    }

    private boolean satisfiedCarPlateNumber(ParkingRequest request) {
	if (org.apache.commons.lang.StringUtils.isEmpty(getCarPlateNumber())) {
	    return true;
	}
	return request.hasVehicleContainingPlateNumber(getCarPlateNumber());
    }

    private boolean satisfiedRequestState(ParkingRequest request) {
	return getParkingRequestState() == null || request.getParkingRequestState() == getParkingRequestState();
    }

    private boolean satisfiedPersonClassification(ParkingRequest request) {
	final ParkingParty parkingParty = request.getParkingParty();
	if (getPartyClassification() == null
		|| parkingParty.getParty().getPartyClassification() == getPartyClassification()) {
	    if (getPartyClassification() == PartyClassification.TEACHER) {
		final Person person = (Person) parkingParty.getParty();
		final Teacher teacher = person.getTeacher();
		return teacher == null || !teacher.isMonitor(ExecutionSemester.readActualExecutionSemester());
	    }
	    return true;
	}
	return false;
    }

    private boolean satisfiedPersonName(ParkingRequest request) {
	return org.apache.commons.lang.StringUtils.isEmpty(getPersonName())
		|| StringUtils.verifyContainsWithEquality(request.getParkingParty().getParty().getName(), getPersonName());
    }

    public List<ParkingRequest> getSearchResult() {
	return searchResult;
    }

    public void setSearchResult(List<ParkingRequest> result) {
	this.searchResult = new DomainListReference<ParkingRequest>(result);
    }
}
