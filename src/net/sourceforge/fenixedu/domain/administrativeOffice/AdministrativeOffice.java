package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    private AdministrativeOffice() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType) {
	this();
	init(administrativeOfficeType);
    }

    private void checkParameters(AdministrativeOfficeType administrativeOfficeType) {
	if (administrativeOfficeType == null) {
	    throw new DomainException("error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
	}
	checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);
    }

    private void checkIfExistsAdministrativeOfficeForType(AdministrativeOfficeType administrativeOfficeType) {
	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance().getAdministrativeOffices()) {
	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		throw new DomainException(
			"error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
	    }
	}
    }

    protected void init(AdministrativeOfficeType administrativeOfficeType) {
	checkParameters(administrativeOfficeType);
	super.setAdministrativeOfficeType(administrativeOfficeType);
    }

    @Override
    public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
	throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
    }

    @Override
    public void setUnit(AdministrativeOfficeUnit unit) {
	throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
    }

    public Collection<AcademicServiceRequest> searchRegistrationAcademicServiceRequests(Integer serviceRequestYear,
	    AcademicServiceRequestSituationType requestSituationType, Campus campus) {

	final Collection<AcademicServiceRequest> result = new ArrayList<AcademicServiceRequest>();

	for (final AcademicServiceRequest request : getAcademicServiceRequestsSet()) {
	    if (!request.isRequestForRegistration()) {
		continue;
	    }

	    final RegistrationAcademicServiceRequest registrationAcademicServiceRequest = (RegistrationAcademicServiceRequest) request;

	    if (serviceRequestYear != null
		    && registrationAcademicServiceRequest.getServiceRequestYear().intValue() != serviceRequestYear.intValue()) {
		continue;
	    }

	    if (requestSituationType != null
		    && registrationAcademicServiceRequest.getAcademicServiceRequestSituationType() != requestSituationType) {
		continue;
	    }

	    if (campus != null && registrationAcademicServiceRequest.isDocumentRequest()) {
		final DocumentRequest documentRequest = (DocumentRequest) registrationAcademicServiceRequest;
		final Campus documentCampus = documentRequest.getCampus();

		if (documentCampus != null && !documentCampus.equals(campus)) {
		    continue;
		}
	    }

	    result.add(registrationAcademicServiceRequest);
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
    public static AdministrativeOffice readByAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {

	for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance().getAdministrativeOffices()) {

	    if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
		return administrativeOffice;
	    }

	}
	return null;

    }

    public static AdministrativeOffice getResponsibleAdministrativeOffice(Degree degree) {
	return readByAdministrativeOfficeType(degree.getDegreeType().getAdministrativeOfficeType());
    }

    public Collection<DegreeType> getAdministratedDegreeTypes() {
	Collection<DegreeType> result = new HashSet<DegreeType>();

	for (final DegreeType degreeType : DegreeType.values()) {
	    if (degreeType.getAdministrativeOfficeType() == getAdministrativeOfficeType()) {
		result.add(degreeType);
	    }
	}

	return result;
    }

    public Set<Degree> getAdministratedDegrees() {
	final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())) {
		result.add(degree);
	    }
	}

	return result;
    }

    public Set<Degree> getAdministratedDegreesForMarkSheets() {
	final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	for (Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (degreeType.getAdministrativeOfficeType().equals(this.getAdministrativeOfficeType())
		    && !degreeType.equals(DegreeType.MASTER_DEGREE)
		    && !degreeType.equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
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

	// FIXME: TEMPORARY HACK! TO REMOVE AFTER NOVEMBER 2007!!
	if (getAdministrativeOfficeType() == AdministrativeOfficeType.MASTER_DEGREE) {
	    result.add(Degree.readBySigla("POSI"));
	}

	Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	return result;
    }

    public void delete() {
	checkRulesToDelete();

	removeUnit();
	removeServiceAgreementTemplate();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasAnyAcademicServiceRequests()) {
	    throw new DomainException("error.AdministrativeOffice.cannot.delete");
	}
    }

    public boolean isDegree() {
	return getAdministrativeOfficeType().equals(AdministrativeOfficeType.DEGREE);
    }
}
