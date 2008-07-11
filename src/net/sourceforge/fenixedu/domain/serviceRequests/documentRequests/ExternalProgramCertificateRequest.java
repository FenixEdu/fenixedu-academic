package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class ExternalProgramCertificateRequest extends ExternalProgramCertificateRequest_Base {

    private ExternalProgramCertificateRequest() {
	super();
    }

    public ExternalProgramCertificateRequest(final Registration registration, DateTime requestDate,
	    final ExecutionYear executionYear, final DocumentPurposeType purposeType, final String otherPurposeTypeDescription,
	    final Integer numberOfPrograms, final Unit institution, final Boolean urgentRequest) {
	this();
	super.init(registration, requestDate, executionYear, Boolean.FALSE, purposeType, otherPurposeTypeDescription,
		urgentRequest);
	checkParameters(numberOfPrograms, institution);
	setNumberOfPrograms(numberOfPrograms);
	setInstitution(institution);
    }

    private void checkParameters(final Integer numberOfPrograms, final Unit institution) {
	if (numberOfPrograms == null || numberOfPrograms.intValue() == 0) {
	    throw new DomainException("error.ExternalProgramCertificateRequest.invalid.numberOfPrograms");
	}
	if (institution == null) {
	    throw new DomainException("error.ExternalProgramCertificateRequest.invalid.institution");
	}
    }
    
    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.unmodifiableList(super.getEnrolments());
    }

    @Override
    public Set<Enrolment> getEnrolmentsSet() {
	return Collections.unmodifiableSet(super.getEnrolmentsSet());
    }

    @Override
    public Iterator<Enrolment> getEnrolmentsIterator() {
	return getEnrolmentsSet().iterator();
    }

    @Override
    public void addEnrolments(Enrolment enrolments) {
	throw new DomainException("error.ExternalProgramCertificateRequest.cannot.add.enrolments");
    }

    @Override
    public void removeEnrolments(Enrolment enrolments) {
	throw new DomainException("error.ExternalProgramCertificateRequest.cannot.remove.enrolments");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.EXTERNAL_PROGRAM_CERTIFICATE;
    }

    @Override
    public EventType getEventType() {
	return EventType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST;
    }

    @Override
    public void delete() {
        super.setInstitution(null);
        super.delete();
    }
}
