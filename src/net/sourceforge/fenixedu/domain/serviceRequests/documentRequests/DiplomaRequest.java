package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class DiplomaRequest extends DiplomaRequest_Base implements IDiplomaRequest {

    public DiplomaRequest() {
	super();
    }

    public DiplomaRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);

	checkParameters(bean);
	if (isPayedUponCreation() && !isFree()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getHasCycleTypeDependency()) {
	    if (bean.getRequestedCycle() == null) {
		throw new DomainException("DiplomaRequest.diploma.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
		throw new DomainException(
			"DiplomaRequest.diploma.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }
	    super.setRequestedCycle(bean.getRequestedCycle());
	} else {
	    if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
		super.setRequestedCycle(getRegistration().getDegree().getDegreeType().getCycleType());
	    }
	}

	if (DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.getAdministrativeOfficeTypes().contains(AdministrativeOfficeType.DEGREE)
		|| DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.getAdministrativeOfficeTypes().contains(
			AdministrativeOfficeType.MASTER_DEGREE)) {
	    if (getRegistration().isBolonha()
		    && !getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
		final RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getRequestedCycle());
		if (registryRequest == null) {
		    throw new DomainException("DiplomaRequest.registration.withoutRegistryRequest");
		} else if (registryRequest.isPayedUponCreation() && registryRequest.hasEvent()
			&& !registryRequest.getEvent().isPayed()) {
		    throw new DomainException("DiplomaRequest.registration.withoutPayedRegistryRequest");
		}
	    }
	}

	checkForDuplicate(bean.getRequestedCycle());
    }

    private void checkForDuplicate(final CycleType requestedCycle) {
	final DiplomaRequest diplomaRequest = getRegistration().getDiplomaRequest(requestedCycle);
	if (diplomaRequest != null && diplomaRequest != this) {
	    throw new DomainException("DiplomaRequest.diploma.already.requested");
	}
    }

    @Override
    final public void setRequestedCycle(final CycleType requestedCycle) {
	throw new DomainException("DiplomaRequest.cannot.modify.requestedCycle");
    }

    @Override
    final public String getDescription() {
	final DegreeType degreeType = getDegreeType();
	final CycleType requestedCycle = getRequestedCycle();

	return getDescription(getAcademicServiceRequestType(),
		getDocumentRequestType().getQualifiedName() + "." + degreeType.name()
			+ (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    final public String getDocumentTemplateKey() {
	String result = getClass().getName() + "." + getDegreeType().getName();
	if (getDegreeType().isComposite()) {
	    result += "." + getRequestedCycle().name();
	}

	return result;
    }

    @Override
    final public EventType getEventType() {
	switch (getDegreeType()) {
	case DEGREE:
	case BOLONHA_DEGREE:
	    return EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST;
	case MASTER_DEGREE:
	case BOLONHA_MASTER_DEGREE:
	    return EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST
		    : EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return EventType.BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToProcess()) {
	    if (NOT_AVAILABLE.contains(getRegistration().getDegreeType())) {
		throw new DomainException("DiplomaRequest.diploma.not.available");
	    }

	    checkForDuplicate(getRequestedCycle());

	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
	    }

	    if (hasDissertationTitle() && !getRegistration().hasDissertationThesis()) {
		throw new DomainException("DiplomaRequest.registration.doesnt.have.dissertation.thesis");
	    }

	    if (!getFreeProcessed()) {
		if (hasCycleCurriculumGroup()) {
		    assertPayedEvents(getCycleCurriculumGroup().getIEnrolmentsLastExecutionYear());
		} else {
		    assertPayedEvents();
		}
	    }

	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }

	    if (DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.getAdministrativeOfficeTypes().contains(
		    AdministrativeOfficeType.DEGREE)
		    || DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.getAdministrativeOfficeTypes().contains(
			    AdministrativeOfficeType.MASTER_DEGREE)) {
		RegistryCode code = getRegistryCode();
		// FIXME: later, when all lagacy diplomas are dealt with, the
		// code can never be null, as it is created in the DR request
		// that is a pre-requisite for this request.
		if (code != null) {
		    if (!code.hasDocumentRequest(this)) {
			code.addDocumentRequest(this);
			getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
		    }
		} else {
		    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
		    getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
		}
		if (getLastGeneratedDocument() == null) {
		    generateDocument();
		}
	    }
	}

	if (academicServiceRequestBean.isToConclude() && !isFree() && !hasEvent() && !isPayedUponCreation()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent() && getEvent().isOpen()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}
    }

    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA,
	    DegreeType.BOLONHA_SPECIALIZATION_DEGREE);

    final public boolean hasFinalAverageDescription() {
	return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
	return getDegreeType() == DegreeType.MASTER_DEGREE;
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
    public boolean hasPersonalInfo() {
	return true;
    }

    @Override
    public boolean isPagedDocument() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    public void generateRegistryCode() {
	if (getRegistryCode() == null) {
	    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
	    getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
	}
	if (getLastGeneratedDocument() == null) {
	    generateDocument();
	}
    }

    @Override
    public RegistryCode getRegistryCode() {
	RegistryDiplomaRequest registry = getRegistration().getRegistryDiplomaRequest(getWhatShouldBeRequestedCycle());
	return registry != null ? registry.getRegistryCode() : super.getRegistryCode();
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	// FIXME: diplomas should be intended for official purposes and those
	// imply external entity signature. DFAs should therefore be another
	// type of document with a specific workflow, the document is completely
	// different anyway.
	return getDegree() == null || getDegreeType() == null
		|| !getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	// FIXME: see isPossibleToSendToOtherEntity()
	return getDegree() == null || getDegreeType() == null
		|| !getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return getDegreeType() != DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
    }

    @Override
    public boolean isCanGenerateRegistryCode() {
	return isSendToExternalEntitySituationAccepted() && !hasRegistryCode()
		&& getRegistration().getDegreeType().getQualifiesForGraduateTitle();
    }

    @Override
    @Checked("AcademicServiceRequestPredicates.REVERT_TO_PROCESSING_STATE")
    public void revertToProcessingState() {
	internalRevertToProcessingState();
    }

    public boolean hasRegistryDiplomaRequest() {
	return getRegistration().getRegistryDiplomaRequest(getWhatShouldBeRequestedCycle()) != null;
    }

}
