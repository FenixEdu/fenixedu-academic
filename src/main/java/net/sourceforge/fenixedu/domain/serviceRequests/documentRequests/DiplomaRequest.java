package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituation;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class DiplomaRequest extends DiplomaRequest_Base implements IDiplomaRequest, IRectorateSubmissionBatchDocumentEntry {

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

        if (getRegistration().isBolonha()
                && !getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
                && !getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {
            final RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getRequestedCycle());
            if (registryRequest == null) {
                throw new DomainException("DiplomaRequest.registration.withoutRegistryRequest");
            } else if (registryRequest.isPayedUponCreation() && registryRequest.hasEvent()
                    && !registryRequest.getEvent().isPayed()) {
                throw new DomainException("DiplomaRequest.registration.withoutPayedRegistryRequest");
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
            return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST : EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
            return EventType.BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST;
        case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
            return EventType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA_REQUEST;
        default:
            throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
        }
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DIPLOMA_REQUEST;
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

            if (!getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) &&
                    !getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {
                RegistryCode code = getRegistryCode();
                if (code != null) {
                    if (!code.getDocumentRequestSet().contains(this)) {
                        code.addDocumentRequest(this);
                        getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
                    }
                } else {
                    // FIXME: later, when all legacy diplomas are dealt with,
                    // the
                    // code can never be null, as it is created in the DR
                    // request
                    // that is a pre-requisite for this request.
                    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
                    getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
                }
            }

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }

        if (academicServiceRequestBean.isToConclude() && !isFree() && !hasEvent() && !isPayedUponCreation()) {
            DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
        }

        if (academicServiceRequestBean.isToCancelOrReject() && hasEvent() && getEvent().isOpen()) {
            getEvent().cancel(academicServiceRequestBean.getResponsible());
        }
    }

    /**
     * The DocumentRequestCreator should never have created Past Diploma
     * Requests as DiplomaRequests. This method can be used for data migrations,
     * but should be removed once all past diploma requests are migrated.
     */
    @Deprecated
    private boolean isPastDiplomaRequestHack() {
        TreeSet<AcademicServiceRequestSituation> sortedSituations =
                new TreeSet<AcademicServiceRequestSituation>(
                        AcademicServiceRequestSituation.COMPARATOR_BY_MOST_RECENT_SITUATION_DATE_AND_ID);
        sortedSituations.addAll(getAcademicServiceRequestSituationsSet());

        AcademicServiceRequestSituation deliveredSituation, concludedSituation, receivedSituation, sentSituation, processedSituation, newSituation;
        try {
            Iterator<AcademicServiceRequestSituation> situationsIterator = sortedSituations.iterator();
            deliveredSituation = situationsIterator.next();
            concludedSituation = situationsIterator.next();
            receivedSituation = situationsIterator.next();
            sentSituation = situationsIterator.next();
            processedSituation = situationsIterator.next();
            newSituation = situationsIterator.next();
        } catch (NoSuchElementException ex) {
            return false;
        }

        if (!deliveredSituation.getAcademicServiceRequestSituationType().equals(AcademicServiceRequestSituationType.DELIVERED)) {
            return false;
        }
        if (!deliveredSituation.getJustification().equals("-")) {
            return false;
        }
        if (!(deliveredSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(deliveredSituation.getSituationDate().minuteOfHour().get() == 5)) {
            return false;
        }

        // #####################################################

        if (!concludedSituation.getCreator().equals(deliveredSituation.getCreator())) {
            return false;
        }
        if (!concludedSituation.getAcademicServiceRequestSituationType().equals(AcademicServiceRequestSituationType.CONCLUDED)) {
            return false;
        }
        if (!concludedSituation.getJustification().equals("-")) {
            return false;
        }
        if (!(concludedSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(concludedSituation.getSituationDate().minuteOfHour().get() == 4)) {
            return false;
        }

        // #####################################################

        if (!receivedSituation.getCreator().equals(deliveredSituation.getCreator())) {
            return false;
        }
        if (!receivedSituation.getAcademicServiceRequestSituationType().equals(
                AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY)) {
            return false;
        }
        if (!receivedSituation.getJustification().equals("-")) {
            return false;
        }
        if (!(receivedSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(receivedSituation.getSituationDate().minuteOfHour().get() == 3)) {
            return false;
        }

        // #####################################################

        if (!sentSituation.getCreator().equals(deliveredSituation.getCreator())) {
            return false;
        }
        if (!sentSituation.getAcademicServiceRequestSituationType().equals(
                AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY)) {
            return false;
        }
        if (!sentSituation.getJustification().equals("-")) {
            return false;
        }
        if (!(sentSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(sentSituation.getSituationDate().minuteOfHour().get() == 2)) {
            return false;
        }
        if (!sentSituation.getSituationDate().toLocalDate().equals(receivedSituation.getSituationDate().toLocalDate())) {
            return false;
        }

        // #####################################################

        if (!processedSituation.getCreator().equals(deliveredSituation.getCreator())) {
            return false;
        }
        if (!processedSituation.getAcademicServiceRequestSituationType().equals(AcademicServiceRequestSituationType.PROCESSING)) {
            return false;
        }
        if (!processedSituation.getJustification().equals("-")) {
            return false;
        }
        if (!(processedSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(processedSituation.getSituationDate().minuteOfHour().get() == 1)) {
            return false;
        }
        if (!processedSituation.getSituationDate().toLocalDate().equals(receivedSituation.getSituationDate().toLocalDate())) {
            return false;
        }

        // #####################################################

        if (!newSituation.getCreator().equals(deliveredSituation.getCreator())) {
            return false;
        }
        if (!newSituation.getAcademicServiceRequestSituationType().equals(AcademicServiceRequestSituationType.NEW)) {
            return false;
        }
        if (!StringUtils.isEmpty(newSituation.getJustification())) {
            return false;
        }
        if (!(newSituation.getSituationDate().hourOfDay().get() == 0)) {
            return false;
        }
        if (!(newSituation.getSituationDate().minuteOfHour().get() == 0)) {
            return false;
        }
        if (!newSituation.getSituationDate().toLocalDate().equals(receivedSituation.getSituationDate().toLocalDate())) {
            return false;
        }

        return true;
    }

    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(DegreeType.BOLONHA_SPECIALIZATION_DEGREE);

    final public boolean hasFinalAverageDescription() {
        return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
        return getDegreeType() == DegreeType.MASTER_DEGREE;
    }

    /* TODO refactor, always set requested cycle type in document creation */

    @Override
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
    public void revertToProcessingState() {
        check(this, AcademicPredicates.SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE);
        internalRevertToProcessingState();
    }

    public boolean hasRegistryDiplomaRequest() {
        return getRegistration().getRegistryDiplomaRequest(getWhatShouldBeRequestedCycle()) != null;
    }

    @Override
    public LocalDate getConclusionDate() {
        final RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(getRegistration(), getCycleCurriculumGroup());

        return calculateConclusionDate(registrationConclusionBean);
    }

    private LocalDate calculateConclusionDate(final RegistrationConclusionBean registrationConclusionBean) {
        if (hasDissertationTitle()) {
            LocalDate date = registrationConclusionBean.getRegistration().getDissertationThesisDiscussedDate();
            if (date == null) {
                throw new DomainException("DiplomaRequest.dissertation.not.discussed");
            }
            return date;
        }
        return new LocalDate(registrationConclusionBean.getConclusionDate());
    }

    @Override
    public Integer getFinalAverage() {
        final RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(getRegistration(), getCycleCurriculumGroup());

        return registrationConclusionBean.getFinalAverage();
    }

    @Override
    public String getFinalAverageQualified() {
        return getRegistration().getDegreeType().getGradeScale().getQualifiedName(getFinalAverage().toString());
    }

    @Override
    public String getDissertationThesisTitle() {
        return getRegistration().getDissertationThesisTitle();
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getRegistration().getGraduateTitle(getWhatShouldBeRequestedCycle(), locale);
    }

    public String getDegreeFilteredName() {
        final RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(getRegistration(), getCycleCurriculumGroup());

        ExecutionYear executionYear = registrationConclusionBean.getConclusionYear();
        return getRegistration().getDegree().getFilteredName(executionYear);
    }

    @Override
    public String getProgrammeTypeDescription() {
        return getDegreeType().getLocalizedName();
    }

    @Override
    public String getViewStudentProgrammeLink() {
        return "/student.do?method=visualizeRegistration&amp;registrationID=" + getRegistration().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
        return "/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId="
                + getExternalId();
    }

    @Override
    public boolean isProgrammeLinkVisible() {
        return getRegistration().isAllowedToManageRegistration();
    }

    @Deprecated
    public boolean hasRequestedCycle() {
        return getRequestedCycle() != null;
    }

}
