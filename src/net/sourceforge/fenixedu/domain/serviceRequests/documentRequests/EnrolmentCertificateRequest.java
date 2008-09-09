package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
	super();
    }

    public EnrolmentCertificateRequest(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed, ExecutionYear executionYear) {

	this();
	init(registration, requestDate, executionYear, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		detailed);
    }

    protected void init(Registration registration, DateTime requestDate, ExecutionYear executionYear,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    Boolean detailed) {

	checkParameters(registration, detailed, executionYear);
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	super.setDetailed(detailed);
    }

    private void checkParameters(final Registration registration, final Boolean detailed, final ExecutionYear executionYear) {
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
	} else if (!registration.hasAnyEnrolmentsIn(executionYear)) {
	    throw new DomainException("EnrolmentCertificateRequest.no.enrolments.for.registration.in.given.executionYear");
	}
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.ENROLMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public void setDetailed(Boolean detailed) {
	throw new DomainException("error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public EventType getEventType() {
	return EventType.ENROLMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getEntriesToReport().size() + getExtraCurricularEntriesToReport().size() + getPropaedeuticEntriesToReport().size();
    }

    final public Collection<Enrolment> getEntriesToReport() {
	return filterEntries();
    }

    private Collection<Enrolment> filterEntries() {
	final Collection<Enrolment> result = new HashSet<Enrolment>();
	if (extraCurricular == null) {
	    extraCurricular = new HashSet<Enrolment>();
	} else {
	    extraCurricular.clear();
	}
	if (propaedeutic == null) {
	    propaedeutic = new HashSet<Enrolment>();
	} else {
	    propaedeutic.clear();
	}

	for (final Enrolment entry : getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear())) {
	    if (entry.isExtraCurricular() && !entry.hasAnyEnrolmentWrappers()) {
		extraCurricular.add(entry);
		continue;
	    } else if (entry.isPropaedeutic()) {
		propaedeutic.add(entry);
		continue;
	    }

	    result.add(entry);
	}

	return result;
    }

    Collection<Enrolment> extraCurricular = null;

    final public Collection<Enrolment> getExtraCurricularEntriesToReport() {
	if (extraCurricular == null) {
	    filterEntries();
	}

	return extraCurricular;
    }

    Collection<Enrolment> propaedeutic = null;

    final public Collection<Enrolment> getPropaedeuticEntriesToReport() {
	if (propaedeutic == null) {
	    filterEntries();
	}

	return propaedeutic;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return true;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
