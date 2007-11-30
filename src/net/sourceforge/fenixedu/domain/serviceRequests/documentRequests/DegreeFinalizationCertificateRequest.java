package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    private DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(final Registration registration,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Boolean urgentRequest, final Boolean average, final Boolean detailed, MobilityProgram mobilityProgram, 
	    final CycleType requestedCyle) {
	this();

	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		average, detailed, mobilityProgram, requestedCyle);
    }

    final protected void init(final Registration registration, final DocumentPurposeType documentPurposeType,
	    final String otherDocumentPurposeTypeDescription, final Boolean urgentRequest, final Boolean average,
	    final Boolean detailed, final MobilityProgram mobilityProgram, final CycleType requestedCycle) {
	
	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	this.checkParameters(average, detailed, mobilityProgram, requestedCycle);
	super.setAverage(average);
	super.setDetailed(detailed);
	super.setMobilityProgram(mobilityProgram);
    }

    final private void checkParameters(final Boolean average, final Boolean detailed, final MobilityProgram mobilityProgram, final CycleType requestedCycle) {
	if (average == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}
	if (mobilityProgram == null && getRegistration().hasAnyExternalApprovedEnrolment()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null.for.registration.with.external.enrolments");
	}
	if (getDegreeType().isComposite()) {
	    if (requestedCycle == null) {
		throw new DomainException("DegreeFinalizationCertificateRequest.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(requestedCycle)) {
		throw new DomainException("DegreeFinalizationCertificateRequest.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }

	    super.setRequestedCycle(requestedCycle);
	}

	checkForDiplomaRequest(requestedCycle);
    }

    private void checkForDiplomaRequest(final CycleType requestedCycle) {
	if (!getRegistration().hasDiplomaRequest(requestedCycle)) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutDiplomaRequest");
	}
    }
    
    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {
	    checkForDiplomaRequest(getRequestedCycle());
	}
    }

    @Override
    final public String getDescription() {
	final DegreeType degreeType = getDegreeType();
	final CycleType requestedCycle = getRequestedCycle();
	
	return getDescription(AcademicServiceRequestType.DOCUMENT, getDocumentRequestType().getQualifiedName() + "." + degreeType.name() + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public void setAverage(final Boolean average) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    final public void setDetailed(final Boolean detailed) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public void setRequestedCycle(final CycleType requestedCycle) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.requestedCycle");
    }

    @Override
    final public EventType getEventType() {
	return EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getDetailed() ? getRegistration().getCurriculum().getCurriculumEntries().size() : 0;
    }

}
