package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

import org.joda.time.YearMonthDay;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    private DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(final DocumentRequestCreateBean bean) {
	this();
	bean.setExecutionYear(null);
	super.init(bean);

	checkParameters(bean);
	super.setAverage(bean.getAverage());
	super.setDetailed(bean.getDetailed());
	super.setMobilityProgram(bean.getMobilityProgram());
	super.setTechnicalEngineer(bean.getTechnicalEngineer());
	super.setInternshipAbolished(bean.getInternshipAbolished());
	super.setInternshipApproved(bean.getInternshipApproved());
	super.setStudyPlan(bean.getStudyPlan());
	super.setExceptionalConclusionDate(bean.getExceptionalConclusionDate());
	// super.setLanguage(bean.getLanguage());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getAverage() == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}

	if (bean.getDetailed() == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}

	if (bean.getMobilityProgram() == null && hasAnyExternalEntriesToReport()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null");
	}

	if ((bean.getInternshipAbolished() || bean.getInternshipApproved() || bean.getStudyPlan())
		&& bean.getExceptionalConclusionDate() == null) {
	    throw new DomainException(
		    "DegreeFinalizationCertificateRequest.must.indicate.date.for.exceptional.conclusion.situation");
	}

	if (getDegreeType().isComposite()) {
	    if (bean.getRequestedCycle() == null) {
		throw new DomainException("DegreeFinalizationCertificateRequest.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
		throw new DomainException(
			"DegreeFinalizationCertificateRequest.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }

	    super.setRequestedCycle(bean.getRequestedCycle());
	}

	checkSpecificConditions();
    }

    private void checkSpecificConditions() {
	if (getRegistration().getDegreeType().getQualifiesForGraduateTitle()) {
	    checkForDiplomaRequest(getRegistration(), getRequestedCycle());
	} else {
	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
	    }
	}
    }

    static public void checkForDiplomaRequest(final Registration registration, final CycleType requestedCycle) {
	final DiplomaRequest diplomaRequest = registration.getDiplomaRequest(requestedCycle);
	if (diplomaRequest == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutDiplomaRequest");
	} else if (diplomaRequest.isPayedUponCreation() && diplomaRequest.hasEvent() && !diplomaRequest.getEvent().isPayed()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutPayedDiplomaRequest");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToProcess()) {
	    checkSpecificConditions();

	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.not.submited.to.conclusion.process");
	    }

	    if (getMobilityProgram() == null && hasAnyExternalEntriesToReport()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null");
	    }

	    if (!getFreeProcessed()) {
		if (hasCycleCurriculumGroup()) {
		    assertPayedEvents(getCycleCurriculumGroup().getIEnrolmentsLastExecutionYear());
		} else {
		    assertPayedEvents();
		}
	    }

	    if (hasPersonalInfo() && hasMissingPersonalInfo()) {
		throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
	    }
	}

	if (academicServiceRequestBean.isToConclude()) {
	    tryConcludeServiceRequest(academicServiceRequestBean);
	}

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}

	if (academicServiceRequestBean.isToDeliver()) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
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
		return registration.getLastStudentCurricularPlan().getLastOrderedCycleCurriculumGroup();
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

    final public boolean hasExceptionalConclusionDate() {
	return getExceptionalConclusionDate() != null;
    }

    final public boolean hasExceptionalConclusionInfo() {
	return getTechnicalEngineer() || getInternshipAbolished() || getInternshipApproved() || getStudyPlan();
    }

    final public boolean mustHideConclusionDate() {
	return getInternshipAbolished() || getInternshipApproved()
		|| (getStudyPlan() && getRegistration().isFirstCycleAtributionIngression());
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

}
