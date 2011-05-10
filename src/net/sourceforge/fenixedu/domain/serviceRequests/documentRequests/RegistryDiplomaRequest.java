package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.RegistryDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;

import org.joda.time.LocalDate;

public class RegistryDiplomaRequest extends RegistryDiplomaRequest_Base implements IRegistryDiplomaRequest,
	IRectorateSubmissionBatchDocumentEntry {

    public RegistryDiplomaRequest() {
	super();
    }

    public RegistryDiplomaRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);
	checkParameters(bean);
	if (isPayedUponCreation() && !isFree()) {
	    RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
	if (bean.getRegistration().isBolonha()) {
	    setDiplomaSupplement(new DiplomaSupplementRequest(bean));
	}
    }

    public String getFamilyNames() {
	return getRegistration().getPerson().getFamilyNames();
    }

    public String getGivenNames() {
	return getRegistration().getPerson().getGivenNames();
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
	if (bean.getHasCycleTypeDependency()) {
	    if (bean.getRequestedCycle() == null) {
		throw new DomainException("error.registryDiploma.requestedCycleMustBeGiven");
	    } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
		throw new DomainException("error.registryDiploma.requestedCycleTypeIsNotAllowedForGivenStudentCurricularPlan");
	    }
	    super.setRequestedCycle(bean.getRequestedCycle());
	} else {
	    if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
		super.setRequestedCycle(bean.getRegistration().getDegreeType().getCycleType());
	    }
	}
	if (getRegistration().getDiplomaRequest(bean.getRequestedCycle()) != null)
	    throw new DomainException("error.registryDiploma.alreadyHasDiplomaRequest");
	if (getRegistration().getRegistryDiplomaRequest(bean.getRequestedCycle()) != this)
	    throw new DomainException("error.registryDiploma.alreadyRequested");
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
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	switch (getDegreeType()) {
	case DEGREE:
	case BOLONHA_DEGREE:
	    return EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case MASTER_DEGREE:
	case BOLONHA_MASTER_DEGREE:
	    return EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST
		    : EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return EventType.BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("error.registryDiploma.notAvailableForGivenDegreeType");
	}
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);
	if (academicServiceRequestBean.isToProcess()) {
	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("error.registryDiploma.registrationNotSubmitedToConclusionProcess");
	    }
	    if (!getFreeProcessed()) {
		assertPayedEvents();
	    }
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	    if (getRegistryCode() == null) {
		getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
		getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
	    }
	    if (getLastGeneratedDocument() == null) {
		generateDocument();
	    }
	    if (getRegistration().isBolonha()) {
		getDiplomaSupplement().process();
	    }
	} else if (academicServiceRequestBean.isToConclude()) {
	    if (!isFree() && !hasEvent() && !isPayedUponCreation()) {
		RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	    }
	    if (getRegistration().isBolonha() && getDiplomaSupplement().isConcludedSituationAccepted()) {
		getDiplomaSupplement().conclude();
	    }
	} else if (academicServiceRequestBean.isToCancelOrReject()) {
	    if (hasEvent()) {
		getEvent().cancel(academicServiceRequestBean.getEmployee());
	    }
	    if (getRegistration().isBolonha() && academicServiceRequestBean.isToCancel()) {
		getDiplomaSupplement().cancel(academicServiceRequestBean.getJustification());
	    }
	    if (getRegistration().isBolonha() && academicServiceRequestBean.isToReject()) {
		getDiplomaSupplement().reject(academicServiceRequestBean.getJustification());
	    }
	}
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean isPagedDocument() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
	throw new DomainException("error.registryDiploma.cannotModifyRequestedCycle");
    }

    public LocalDate getConclusionDate() {
	if (getRegistration().isBolonha()) {
	    return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
		    .getConclusionDate();
	} else {
	    return getRegistration().getConclusionProcess().getConclusionDate();
	}
    }

    public ExecutionYear getConclusionYear() {
	if (getRegistration().isBolonha()) {
	    return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess().getConclusionYear();
	} else {
	    return getRegistration().getConclusionProcess().getConclusionYear();
	}
    }

    @Override
    public String getGraduateTitle(Locale locale) {
	return getRegistration().getGraduateTitle(getRequestedCycle(), locale);
    }

    @Override
    public String getFinalAverage(final Locale locale) {
	return String.valueOf(getRegistration().getFinalAverage(getRequestedCycle()));
    }

    @Override
    public String getQualifiedAverageGrade(final Locale locale) {
	Integer finalAverage = getRegistration().getFinalAverage(getRequestedCycle());

	String qualifiedAverageGrade;

	if (finalAverage <= 13) {
	    qualifiedAverageGrade = "sufficient";
	} else if (finalAverage <= 15) {
	    qualifiedAverageGrade = "good";
	} else if (finalAverage <= 17) {
	    qualifiedAverageGrade = "verygood";
	} else {
	    qualifiedAverageGrade = "excelent";
	}

	return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    public String getProgrammeTypeDescription() {
	return getDegreeType().getLocalizedName();
    }

    @Override
    public String getViewStudentProgrammeLink() {
	return "/student.do?method=visualizeRegistration&amp;registrationID=" + getRegistration().getIdInternal();
    }

    @Override
    public String getReceivedActionLink() {
	return "/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId="
		+ getIdInternal();
    }
}
