package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    private DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(final Registration registration, DateTime requestDate,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Boolean urgentRequest, final Boolean average, final Boolean detailed, MobilityProgram mobilityProgram,
	    final CycleType requestedCyle, final Boolean freeProcessed, final Boolean internship, Boolean studyPlan) {
	this();

	this.init(registration, requestDate, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest, average,
		detailed, mobilityProgram, requestedCyle, freeProcessed, internship, studyPlan);
    }

    final protected void init(final Registration registration, DateTime requestDate,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Boolean urgentRequest, final Boolean average, final Boolean detailed, final MobilityProgram mobilityProgram,
	    final CycleType requestedCycle, final Boolean freeProcessed, final Boolean internship, Boolean studyPlan) {

	super.init(registration, requestDate, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		freeProcessed);

	this.checkParameters(average, detailed, mobilityProgram, requestedCycle, internship, studyPlan);
	super.setAverage(average);
	super.setDetailed(detailed);
	super.setMobilityProgram(mobilityProgram);
	super.setInternship(internship);
	super.setStudyPlan(studyPlan);
    }

    final private void checkParameters(final Boolean average, final Boolean detailed, final MobilityProgram mobilityProgram,
	    final CycleType requestedCycle, final Boolean internship, final Boolean studyPlan) {
	if (average == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}

	if (mobilityProgram == null && hasAnyExternalEntriesToReport()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null");
	}

	if (internship != null && studyPlan != null && internship && studyPlan) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.must.indicate.only.one.reason.for.not.printing");
	}

	if (getDegreeType().isComposite()) {
	    if (requestedCycle == null) {
		throw new DomainException("DegreeFinalizationCertificateRequest.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(requestedCycle)) {
		throw new DomainException(
			"DegreeFinalizationCertificateRequest.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }

	    super.setRequestedCycle(requestedCycle);
	}

	checkForDiplomaRequest(requestedCycle);
    }

    private void checkForDiplomaRequest(final CycleType requestedCycle) {
	final DiplomaRequest diplomaRequest = getRegistration().getDiplomaRequest(requestedCycle);
	if (diplomaRequest == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutDiplomaRequest");
	} else if (diplomaRequest.isPayedUponCreation() && diplomaRequest.hasEvent() && !diplomaRequest.getEvent().isPayed()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutPayedDiplomaRequest");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToProcess()) {
	    checkForDiplomaRequest(getRequestedCycle());

	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.not.submited.to.conclusion.process");
	    }

	    if (getMobilityProgram() == null && hasAnyExternalEntriesToReport()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null");
	    }

	    if (!getFreeProcessed()) {
		if (hasCycleCurriculumGroup()) {
		    final ExecutionYear executionYear = getCycleCurriculumGroup().getIEnrolmentsLastExecutionYear();
		    if (executionYear != null && getRegistration().hasGratuityDebts(executionYear)) {
			throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
		    }
		} else if (getRegistration().hasGratuityDebtsCurrently()) {
		    throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
		}
	    }
	}

	if (academicServiceRequestBean.isToConclude()) {

	    if (!hasNumberOfPages()) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new CertificateRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
	    }
	}
    }

    @Override
    final public String getDescription() {
	final DegreeType degreeType = getDegreeType();
	final CycleType requestedCycle = getRequestedCycle();

	return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "."
		+ degreeType.name() + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
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

    /* TODO refactor, always set requested cycle type in document creation */

    public CycleType getWhatShouldBeRequestedCycle() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCycleType() : null;
    }

    public CycleCurriculumGroup getCycleCurriculumGroup() {
	final CycleType requestedCycle = getRequestedCycle();
	final Registration registration = getRegistration();

	if (requestedCycle == null) {
	    if (registration.getDegreeType().hasExactlyOneCycleType()) {
		return registration.getLastStudentCurricularPlan().getLastCycleCurriculumGroup();
	    } else {
		return null;
	    }
	} else {
	    return registration.getLastStudentCurricularPlan().getCycle(requestedCycle);
	}
    }

    public boolean hasCycleCurriculumGroup() {
	return getCycleCurriculumGroup() != null;
    }

    @Override
    final public EventType getEventType() {
	return EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getDetailed() ? getEntriesToReport().size() : 0;
    }

    final private RegistrationConclusionBean getBean() {
	return new RegistrationConclusionBean(getRegistration(), getCycleCurriculumGroup());
    }

    final public YearMonthDay getConclusionDate() {
	return getBean().getConclusionDate();
    }

    final public Integer getFinalAverage() {
	return getBean().getFinalAverage();
    }

    final public double getEctsCredits() {
	return getBean().getEctsCredits();
    }

    final public ICurriculum getCurriculum() {
	return getBean().getCurriculumForConclusion();
    }

    final public Collection<ICurriculumEntry> getEntriesToReport() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

	for (final ICurriculumEntry entry : getCurriculum().getCurriculumEntries()) {
	    if (entry instanceof Dismissal) {
		final Dismissal dismissal = (Dismissal) entry;
		if (dismissal.getCredits().isEquivalence()
			|| (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
		    continue;
		}
	    }

	    result.add(entry);
	}

	return result;
    }

    final public boolean hasAnyExternalEntriesToReport() {
	for (final ICurriculumEntry entry : getEntriesToReport()) {
	    if (entry instanceof ExternalEnrolment) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

    @Override
    final public boolean isToPrint() {
	return super.isToPrint() && (getInternship() == null || !getInternship())
		&& (getStudyPlan() == null || !getStudyPlan() || getRegistration().isBolonha());
    }

}
