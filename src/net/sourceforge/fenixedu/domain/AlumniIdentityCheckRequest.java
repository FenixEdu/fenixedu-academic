package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AlumniIdentityCheckRequest extends AlumniIdentityCheckRequest_Base {

    public AlumniIdentityCheckRequest(String contactEmail, String documentIdNumber, String fullName,
	    YearMonthDay dateOfBirthYearMonthDay, String districtOfBirth, String districtSubdivisionOfBirth,
	    String parishOfBirth, String socialSecurityNumber, String nameOfFather, String nameOfMother,
	    AlumniRequestType requestType) {
	super();

	checkParameters(contactEmail, documentIdNumber, fullName, dateOfBirthYearMonthDay, districtOfBirth,
		districtSubdivisionOfBirth, parishOfBirth, socialSecurityNumber, nameOfFather, nameOfMother, requestType);

	setContactEmail(contactEmail.trim());
	setDocumentIdNumber(documentIdNumber.trim());
	setFullName(fullName.trim());
	setDateOfBirthYearMonthDay(dateOfBirthYearMonthDay);
	setDistrictOfBirth(districtOfBirth.trim());
	setDistrictSubdivisionOfBirth(districtSubdivisionOfBirth.trim());
	setParishOfBirth(parishOfBirth.trim());
	setSocialSecurityNumber(socialSecurityNumber);
	setNameOfFather(nameOfFather.trim());
	setNameOfMother(nameOfMother.trim());
	setCreationDateTime(new DateTime());
	setRequestType(requestType);
	setRequestToken(UUID.randomUUID());

	setRootDomainObject(RootDomainObject.getInstance());
    }

    private void checkParameters(String contactEmail, String documentIdNumber, String fullName,
	    YearMonthDay dateOfBirthYearMonthDay, String districtOfBirth, String districtSubdivisionOfBirth,
	    String parishOfBirth, String socialSecurityNumber, String nameOfFather, String nameOfMother,
	    AlumniRequestType requestType) {

	if (StringUtils.isEmpty(contactEmail)) {
	    throw new DomainException("alumni.identity.request.contactEmail.null");
	}

	if (StringUtils.isEmpty(documentIdNumber)) {
	    throw new DomainException("alumni.identity.request.documentIdNumber.null");
	}

	if (requestType == null) {
	    throw new DomainException("alumni.identity.request.requestType.null");
	}

    }

    public static boolean hasPendingRequestsForDocumentNumber(String documentIdNumber) {
	for (AlumniIdentityCheckRequest request : RootDomainObject.getInstance().getAlumniIdentityRequest()) {
	    if (request.getDocumentIdNumber().equals(documentIdNumber)) {
		return true;
	    }
	}
	return false;
    }

    public static Collection<AlumniIdentityCheckRequest> readPendingRequests() {
	Collection<AlumniIdentityCheckRequest> pendingRequests = new ArrayList<AlumniIdentityCheckRequest>();
	Set<AlumniIdentityCheckRequest> requests = RootDomainObject.readAllDomainObjects(AlumniIdentityCheckRequest.class);

	AlumniIdentityCheckRequest request;
	Iterator iter = requests.iterator();
	while (iter.hasNext()) {
	    request = (AlumniIdentityCheckRequest) iter.next();
	    if (request.getApproved() == null) {
		pendingRequests.add(request);
	    }
	}
	return pendingRequests;
    }

    public static Object readClosedRequests() {
	Collection<AlumniIdentityCheckRequest> pendingRequests = new ArrayList<AlumniIdentityCheckRequest>();
	Set<AlumniIdentityCheckRequest> requests = RootDomainObject.readAllDomainObjects(AlumniIdentityCheckRequest.class);

	AlumniIdentityCheckRequest request;
	Iterator iter = requests.iterator();
	while (iter.hasNext()) {
	    request = (AlumniIdentityCheckRequest) iter.next();
	    if (request.getApproved() != null) {
		pendingRequests.add(request);
	    }
	}
	return pendingRequests;
    }

    public boolean isValid() {
	// ugly: refactor
	Person person = getAlumni().getStudent().getPerson();
	return (!StringUtils.isEmpty(person.getName()) && person.getName().equals(getFullName()))
		&& (person.getDateOfBirthYearMonthDay().equals(getDateOfBirthYearMonthDay()))
		&& (!StringUtils.isEmpty(person.getDistrictOfBirth()) && person.getDistrictOfBirth().equals(getDistrictOfBirth()))
		&& (!StringUtils.isEmpty(person.getDistrictSubdivisionOfBirth()) && person.getDistrictSubdivisionOfBirth()
			.equals(getDistrictSubdivisionOfBirth()))
		&& (!StringUtils.isEmpty(person.getParishOfBirth()) && person.getParishOfBirth().equals(getParishOfBirth()))
		&& (!StringUtils.isEmpty(person.getSocialSecurityNumber()) && person.getSocialSecurityNumber().equals(
			getSocialSecurityNumber()))
		&& (!StringUtils.isEmpty(person.getNameOfFather()) && person.getNameOfFather().equals(getNameOfFather()))
		&& (!StringUtils.isEmpty(person.getNameOfMother()) && person.getNameOfMother().equals(getNameOfMother()));
    }

    public void validate(Boolean approval) {
	setApproved(approval);
	setDecisionDateTime(new DateTime());
    }

    public void validate(Boolean approval, Person operator) {
	validate(approval);
	setOperator(operator);
    }


	@Deprecated
	public java.util.Date getDateOfBirth(){
		org.joda.time.YearMonthDay ymd = getDateOfBirthYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setDateOfBirth(java.util.Date date){
		if(date == null) setDateOfBirthYearMonthDay(null);
		else setDateOfBirthYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}

	@Deprecated
	public java.util.Date getCreation(){
		org.joda.time.DateTime dt = getCreationDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setCreation(java.util.Date date){
		if(date == null) setCreationDateTime(null);
		else setCreationDateTime(new org.joda.time.DateTime(date.getTime()));
	}

	@Deprecated
	public java.util.Date getDecision(){
		org.joda.time.DateTime dt = getDecisionDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setDecision(java.util.Date date){
		if(date == null) setDecisionDateTime(null);
		else setDecisionDateTime(new org.joda.time.DateTime(date.getTime()));
	}


}
