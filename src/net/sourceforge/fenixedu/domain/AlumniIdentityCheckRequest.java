package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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

	setContactEmail(contactEmail);
	setDocumentIdNumber(documentIdNumber);
	setFullName(fullName);
	setDateOfBirthYearMonthDay(dateOfBirthYearMonthDay);
	setDistrictOfBirth(districtOfBirth);
	setDistrictSubdivisionOfBirth(districtSubdivisionOfBirth);
	setParishOfBirth(parishOfBirth);
	setSocialSecurityNumber(socialSecurityNumber);
	setNameOfFather(nameOfFather);
	setNameOfMother(nameOfMother);
	setCreationDateTime(new DateTime());
	setRequestType(requestType);

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

}