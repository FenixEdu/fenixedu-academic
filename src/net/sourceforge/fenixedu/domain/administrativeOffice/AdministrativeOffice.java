package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    private AdministrativeOffice() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
	this();
	init(administrativeOfficeType, unit);
    }

    private void checkParameters(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
	if (administrativeOfficeType == null) {
	    throw new DomainException(
		    "error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
	}
	if (unit == null) {
	    throw new DomainException(
		    "error.administrativeOffice.AdministrativeOffice.unit.cannot.be.null");
	}

	checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);
    }

    private void checkIfExistsAdministrativeOfficeForType(
	    AdministrativeOfficeType administrativeOfficeType) {

	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
		.getAdministrativeOffices()) {
	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		throw new DomainException(
			"error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
	    }
	}
    }

    protected void init(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
	checkParameters(administrativeOfficeType, unit);
	super.setAdministrativeOfficeType(administrativeOfficeType);
	super.setUnit(unit);

    }

    @Override
    public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
	throw new DomainException(
		"error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
    }

    @Override
    public void setUnit(Unit unit) {
	throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
    }

    public List<DocumentRequest> searchDocumentsBy(DocumentRequestType documentRequestType,
	    AcademicServiceRequestSituationType requestSituationType, Boolean isUrgent,
	    Registration registration) {

	final List<DocumentRequest> result = new ArrayList<DocumentRequest>();

	for (final AcademicServiceRequest serviceRequest : getAcademicServiceRequestsSet()) {

	    if (serviceRequest instanceof DocumentRequest) {

		final DocumentRequest documentRequest = (DocumentRequest) serviceRequest;

		if (documentRequestType != null
			&& documentRequest.getDocumentRequestType() != documentRequestType) {
		    continue;
		}

		if (requestSituationType != null
			&& documentRequest.getAcademicServiceRequestSituationType() != requestSituationType) {
		    continue;
		}

		if (isUrgent != null
			&& documentRequest.isCertificate()
			&& ((CertificateRequest) documentRequest).isUrgentRequest() != isUrgent
				.booleanValue()) {
		    continue;
		}

		if (registration != null && documentRequest.getRegistration() != registration) {
		    continue;
		}

		result.add(documentRequest);
	    }
	}

	return result;
    }

    public List<AcademicServiceRequest> getNewAcademicServiceRequests() {

	final List<AcademicServiceRequest> result = new ArrayList<AcademicServiceRequest>();
	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequests()) {
	    if (academicServiceRequest.isNewRequest()) {
		result.add(academicServiceRequest);
	    }
	}
	return result;
    }

    // static methods
    public static AdministrativeOffice readByAdministrativeOfficeType(
	    AdministrativeOfficeType administrativeOfficeType) {

	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
		.getAdministrativeOffices()) {

	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		return administrativeOffice;
	    }

	}
	return null;

    }

    public static AdministrativeOffice getResponsibleAdministrativeOffice(Degree degree) {
	return readByAdministrativeOfficeType(degree.getDegreeType().getAdministrativeOfficeType());
    }

    public static AdministrativeOffice readByEmployee(Employee employee) {
	final Unit employeeWorkingPlace = employee.getCurrentWorkingPlace();
	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
		.getAdministrativeOffices()) {
	    if (administrativeOffice.getUnit() == employeeWorkingPlace) {
		return administrativeOffice;
	    }
	}

	return null;
    }

    public Set<Degree> getAdministratedDegrees() {
	final Set<Degree> result = new TreeSet<Degree>(new BeanComparator("name"));
	for (Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())) {
		result.add(degree);
	    }
	}

	return result;
    }

    public List<Degree> getAdministratedDegreesForStudentCreationWithoutCandidacy() {
	final List<Degree> result = new ArrayList<Degree>();
	for (Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())
		    && degreeType.canCreateStudent() && !degreeType.canCreateStudentOnlyWithCandidacy()) {
		result.add(degree);
	    }
	}

	Collections.sort(result, Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
	return result;
    }

}
