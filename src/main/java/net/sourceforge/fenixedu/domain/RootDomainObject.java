package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class RootDomainObject extends RootDomainObject_Base {

    private static RootDomainObject instance = null;

    @Atomic(mode = TxMode.READ)
    public static void initialize() {
        instance = FenixFramework.getDomainRoot().getRootDomainObject();
    }

    public static RootDomainObject getInstance() {
        return instance;
    }

    @Atomic
    public static void ensureRootDomainObject() {
        if (FenixFramework.getDomainRoot().getRootDomainObject() == null) {
            FenixFramework.getDomainRoot().setRootDomainObject(new RootDomainObject());
        }
    }

    public RootDomainObject() {
        checkIfIsSingleton();
    }

    private void checkIfIsSingleton() {
        RootDomainObject currentRoot = FenixFramework.getDomainRoot().getRootDomainObject();
        if (currentRoot != null && currentRoot != this) {
            throw new Error("There can only be one! (instance of RootDomainObject)");
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonRegularTeachingService> getNonRegularTeachingServices() {
        return getNonRegularTeachingServicesSet();
    }

    @Deprecated
    public boolean hasAnyNonRegularTeachingServices() {
        return !getNonRegularTeachingServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.system.CronScriptState> getCronScriptStates() {
        return getCronScriptStatesSet();
    }

    @Deprecated
    public boolean hasAnyCronScriptStates() {
        return !getCronScriptStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Alert> getActiveAlerts() {
        return getActiveAlertsSet();
    }

    @Deprecated
    public boolean hasAnyActiveAlerts() {
        return !getActiveAlertsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewAnswer> getAnswers() {
        return getAnswersSet();
    }

    @Deprecated
    public boolean hasAnyAnswers() {
        return !getAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.ResourceResponsibility> getResourceResponsibility() {
        return getResourceResponsibilitySet();
    }

    @Deprecated
    public boolean hasAnyResourceResponsibility() {
        return !getResourceResponsibilitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Lesson> getLessons() {
        return getLessonsSet();
    }

    @Deprecated
    public boolean hasAnyLessons() {
        return !getLessonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Contributor> getContributors() {
        return getContributorsSet();
    }

    @Deprecated
    public boolean hasAnyContributors() {
        return !getContributorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation> getCompetenceCourseInformations() {
        return getCompetenceCourseInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseInformations() {
        return !getCompetenceCourseInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlanEntry> getEquivalencePlanEntries() {
        return getEquivalencePlanEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEquivalencePlanEntries() {
        return !getEquivalencePlanEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Exemption> getExemptions() {
        return getExemptionsSet();
    }

    @Deprecated
    public boolean hasAnyExemptions() {
        return !getExemptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod> getParkingRequestPeriods() {
        return getParkingRequestPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyParkingRequestPeriods() {
        return !getParkingRequestPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation> getCycleCourseGroupInformation() {
        return getCycleCourseGroupInformationSet();
    }

    @Deprecated
    public boolean hasAnyCycleCourseGroupInformation() {
        return !getCycleCourseGroupInformationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultParticipation> getResultParticipations() {
        return getResultParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyResultParticipations() {
        return !getResultParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Role> getRoles() {
        return getRolesSet();
    }

    @Deprecated
    public boolean hasAnyRoles() {
        return !getRolesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionVote> getVotes() {
        return getVotesSet();
    }

    @Deprecated
    public boolean hasAnyVotes() {
        return !getVotesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.BibliographicReference> getBibliographicReferences() {
        return getBibliographicReferencesSet();
    }

    @Deprecated
    public boolean hasAnyBibliographicReferences() {
        return !getBibliographicReferencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile> getSibsPaymentFiles() {
        return getSibsPaymentFilesSet();
    }

    @Deprecated
    public boolean hasAnySibsPaymentFiles() {
        return !getSibsPaymentFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate> getInquiryTemplates() {
        return getInquiryTemplatesSet();
    }

    @Deprecated
    public boolean hasAnyInquiryTemplates() {
        return !getInquiryTemplatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.IdDocumentTypeObject> getIdDocumentTypes() {
        return getIdDocumentTypesSet();
    }

    @Deprecated
    public boolean hasAnyIdDocumentTypes() {
        return !getIdDocumentTypesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentGroup> getStudentGroups() {
        return getStudentGroupsSet();
    }

    @Deprecated
    public boolean hasAnyStudentGroups() {
        return !getStudentGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentPeriod> getEnrolmentPeriods() {
        return getEnrolmentPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentPeriods() {
        return !getEnrolmentPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.Resource> getResources() {
        return getResourcesSet();
    }

    @Deprecated
    public boolean hasAnyResources() {
        return !getResourcesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation> getTeacherEvaluation() {
        return getTeacherEvaluationSet();
    }

    @Deprecated
    public boolean hasAnyTeacherEvaluation() {
        return !getTeacherEvaluationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.QuestionCondition> getQuestionsConditions() {
        return getQuestionsConditionsSet();
    }

    @Deprecated
    public boolean hasAnyQuestionsConditions() {
        return !getQuestionsConditionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonAffiliatedTeacher> getNonAffiliatedTeachers() {
        return getNonAffiliatedTeachersSet();
    }

    @Deprecated
    public boolean hasAnyNonAffiliatedTeachers() {
        return !getNonAffiliatedTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder> getThesisSubjectOrders() {
        return getThesisSubjectOrdersSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjectOrders() {
        return !getThesisSubjectOrdersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.precedences.Precedence> getPrecedences() {
        return getPrecedencesSet();
    }

    @Deprecated
    public boolean hasAnyPrecedences() {
        return !getPrecedencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Discount> getDiscounts() {
        return getDiscountsSet();
    }

    @Deprecated
    public boolean hasAnyDiscounts() {
        return !getDiscountsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.transactions.Transaction> getTransactions() {
        return getTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyTransactions() {
        return !getTransactionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeContext> getDegreeContexts() {
        return getDegreeContextsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeContexts() {
        return !getDegreeContextsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Message> getPendingUtilEmailMessages() {
        return getPendingUtilEmailMessagesSet();
    }

    @Deprecated
    public boolean hasAnyPendingUtilEmailMessages() {
        return !getPendingUtilEmailMessagesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program> getPrograms() {
        return getProgramsSet();
    }

    @Deprecated
    public boolean hasAnyPrograms() {
        return !getProgramsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement> getMobilityAgreements() {
        return getMobilityAgreementsSet();
    }

    @Deprecated
    public boolean hasAnyMobilityAgreements() {
        return !getMobilityAgreementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice> getAdministrativeOffices() {
        return getAdministrativeOfficesSet();
    }

    @Deprecated
    public boolean hasAnyAdministrativeOffices() {
        return !getAdministrativeOfficesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber> getPhdIndividualProcessNumbers() {
        return getPhdIndividualProcessNumbersSet();
    }

    @Deprecated
    public boolean hasAnyPhdIndividualProcessNumbers() {
        return !getPhdIndividualProcessNumbersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad> getWeeklyWorkLoads() {
        return getWeeklyWorkLoadsSet();
    }

    @Deprecated
    public boolean hasAnyWeeklyWorkLoads() {
        return !getWeeklyWorkLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Alert> getAlerts() {
        return getAlertsSet();
    }

    @Deprecated
    public boolean hasAnyAlerts() {
        return !getAlertsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup> getExpectationEvaluationGroups() {
        return getExpectationEvaluationGroupsSet();
    }

    @Deprecated
    public boolean hasAnyExpectationEvaluationGroups() {
        return !getExpectationEvaluationGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitSiteBanner> getUnitSiteBanners() {
        return getUnitSiteBannersSet();
    }

    @Deprecated
    public boolean hasAnyUnitSiteBanners() {
        return !getUnitSiteBannersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.FileLocalContent> getFileLocalContents() {
        return getFileLocalContentsSet();
    }

    @Deprecated
    public boolean hasAnyFileLocalContents() {
        return !getFileLocalContentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.GlossaryEntry> getGlossaryEntrys() {
        return getGlossaryEntrysSet();
    }

    @Deprecated
    public boolean hasAnyGlossaryEntrys() {
        return !getGlossaryEntrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Coordinator> getCoordinators() {
        return getCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyCoordinators() {
        return !getCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad> getTSDCurricularLoads() {
        return getTSDCurricularLoadsSet();
    }

    @Deprecated
    public boolean hasAnyTSDCurricularLoads() {
        return !getTSDCurricularLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.messaging.AnnouncementCategory> getCategories() {
        return getCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyCategories() {
        return !getCategoriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.caseHandling.ProcessLog> getProcessesLogs() {
        return getProcessesLogsSet();
    }

    @Deprecated
    public boolean hasAnyProcessesLogs() {
        return !getProcessesLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.CreditNoteEntry> getCreditNoteEntries() {
        return getCreditNoteEntriesSet();
    }

    @Deprecated
    public boolean hasAnyCreditNoteEntries() {
        return !getCreditNoteEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym> getUnitAcronyms() {
        return getUnitAcronymsSet();
    }

    @Deprecated
    public boolean hasAnyUnitAcronyms() {
        return !getUnitAcronymsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PersonIdentificationDocumentExtraInfo> getPersonIdentificationDocumentExtraInfo() {
        return getPersonIdentificationDocumentExtraInfoSet();
    }

    @Deprecated
    public boolean hasAnyPersonIdentificationDocumentExtraInfo() {
        return !getPersonIdentificationDocumentExtraInfoSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeInfo> getDegreeInfos() {
        return getDegreeInfosSet();
    }

    @Deprecated
    public boolean hasAnyDegreeInfos() {
        return !getDegreeInfosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatchRequester> getSantanderBatchRequesters() {
        return getSantanderBatchRequestersSet();
    }

    @Deprecated
    public boolean hasAnySantanderBatchRequesters() {
        return !getSantanderBatchRequestersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
        return getStudentInquiriesTeachingResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesTeachingResults() {
        return !getStudentInquiriesTeachingResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch> getSantanderBatches() {
        return getSantanderBatchesSet();
    }

    @Deprecated
    public boolean hasAnySantanderBatches() {
        return !getSantanderBatchesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.GrantOwnerEquivalent> getGrantOwnerEquivalences() {
        return getGrantOwnerEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyGrantOwnerEquivalences() {
        return !getGrantOwnerEquivalencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitName> getUnitName() {
        return getUnitNameSet();
    }

    @Deprecated
    public boolean hasAnyUnitName() {
        return !getUnitNameSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.CandidacySituation> getCandidacySituations() {
        return getCandidacySituationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacySituations() {
        return !getCandidacySituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment> getInquiryResultComments() {
        return getInquiryResultCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResultComments() {
        return !getInquiryResultCommentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExternalCurricularCourse> getExternalCurricularCourses() {
        return getExternalCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyExternalCurricularCourses() {
        return !getExternalCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PaymentPhase> getPaymentPhases() {
        return getPaymentPhasesSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPhases() {
        return !getPaymentPhasesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherPersonalExpectationPeriod> getTeacherPersonalExpectationPeriods() {
        return getTeacherPersonalExpectationPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherPersonalExpectationPeriods() {
        return !getTeacherPersonalExpectationPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitCostCenterCode> getUnitCostCenterCodes() {
        return getUnitCostCenterCodesSet();
    }

    @Deprecated
    public boolean hasAnyUnitCostCenterCodes() {
        return !getUnitCostCenterCodesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.project.ProjectParticipation> getProjectParticipations() {
        return getProjectParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyProjectParticipations() {
        return !getProjectParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation> getCandidacyPrecedentDegreeInformations() {
        return getCandidacyPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPrecedentDegreeInformations() {
        return !getCandidacyPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.Prize> getPrizes() {
        return getPrizesSet();
    }

    @Deprecated
    public boolean hasAnyPrizes() {
        return !getPrizesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes> getOldInquiriesTeachersRess() {
        return getOldInquiriesTeachersRessSet();
    }

    @Deprecated
    public boolean hasAnyOldInquiriesTeachersRess() {
        return !getOldInquiriesTeachersRessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionSemester> getExecutionPeriods() {
        return getExecutionPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionPeriods() {
        return !getExecutionPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PendingRequest> getPendingRequest() {
        return getPendingRequestSet();
    }

    @Deprecated
    public boolean hasAnyPendingRequest() {
        return !getPendingRequestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmission> getProjectSubmissions() {
        return getProjectSubmissionsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissions() {
        return !getProjectSubmissionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ServiceAgreement> getServiceAgreements() {
        return getServiceAgreementsSet();
    }

    @Deprecated
    public boolean hasAnyServiceAgreements() {
        return !getServiceAgreementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper> getVigilantWrappers() {
        return getVigilantWrappersSet();
    }

    @Deprecated
    public boolean hasAnyVigilantWrappers() {
        return !getVigilantWrappersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.ArticleAssociation> getArticleAssociations() {
        return getArticleAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyArticleAssociations() {
        return !getArticleAssociationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime> getServiceProviderRegimes() {
        return getServiceProviderRegimesSet();
    }

    @Deprecated
    public boolean hasAnyServiceProviderRegimes() {
        return !getServiceProviderRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventEdition> getEventEditions() {
        return getEventEditionsSet();
    }

    @Deprecated
    public boolean hasAnyEventEditions() {
        return !getEventEditionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalence> getCurricularCourseEquivalences() {
        return getCurricularCourseEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourseEquivalences() {
        return !getCurricularCourseEquivalencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.District> getDistricts() {
        return getDistrictsSet();
    }

    @Deprecated
    public boolean hasAnyDistricts() {
        return !getDistrictsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryAnswer> getCerimonyInquiryAnswer() {
        return getCerimonyInquiryAnswerSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryAnswer() {
        return !getCerimonyInquiryAnswerSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesis> getMasterDegreeThesiss() {
        return getMasterDegreeThesissSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesiss() {
        return !getMasterDegreeThesissSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Sender> getUtilEmailSenders() {
        return getUtilEmailSendersSet();
    }

    @Deprecated
    public boolean hasAnyUtilEmailSenders() {
        return !getUtilEmailSendersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry> getPhdStudyPlanEntries() {
        return getPhdStudyPlanEntriesSet();
    }

    @Deprecated
    public boolean hasAnyPhdStudyPlanEntries() {
        return !getPhdStudyPlanEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProcessState> getPhdProgramStates() {
        return getPhdProgramStatesSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramStates() {
        return !getPhdProgramStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.Email> getEmailQueue() {
        return getEmailQueueSet();
    }

    @Deprecated
    public boolean hasAnyEmailQueue() {
        return !getEmailQueueSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Test> getTests() {
        return getTestsSet();
    }

    @Deprecated
    public boolean hasAnyTests() {
        return !getTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatchSender> getSantanderBatchSenders() {
        return getSantanderBatchSendersSet();
    }

    @Deprecated
    public boolean hasAnySantanderBatchSenders() {
        return !getSantanderBatchSendersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport> getStudentCourseReports() {
        return getStudentCourseReportsSet();
    }

    @Deprecated
    public boolean hasAnyStudentCourseReports() {
        return !getStudentCourseReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse> getInquiriesCourses() {
        return getInquiriesCoursesSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesCourses() {
        return !getInquiriesCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DeleteFileLog> getDeleteFileLogs() {
        return getDeleteFileLogsSet();
    }

    @Deprecated
    public boolean hasAnyDeleteFileLogs() {
        return !getDeleteFileLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.ExternalActivity> getExternalActivitys() {
        return getExternalActivitysSet();
    }

    @Deprecated
    public boolean hasAnyExternalActivitys() {
        return !getExternalActivitysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdConfigurationIndividualProgramProcess> getPhdConfigurationIndividualProgramProcess() {
        return getPhdConfigurationIndividualProgramProcessSet();
    }

    @Deprecated
    public boolean hasAnyPhdConfigurationIndividualProgramProcess() {
        return !getPhdConfigurationIndividualProgramProcessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship> getTSDProfessorships() {
        return getTSDProfessorshipsSet();
    }

    @Deprecated
    public boolean hasAnyTSDProfessorships() {
        return !getTSDProfessorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.FakeShiftEnrollment> getFakeShiftEnrollments() {
        return getFakeShiftEnrollmentsSet();
    }

    @Deprecated
    public boolean hasAnyFakeShiftEnrollments() {
        return !getFakeShiftEnrollmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.CreditNote> getCreditNotes() {
        return getCreditNotesSet();
    }

    @Deprecated
    public boolean hasAnyCreditNotes() {
        return !getCreditNotesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.Candidacy> getCandidacies() {
        return getCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacies() {
        return !getCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion> getStudentTestQuestions() {
        return getStudentTestQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyStudentTestQuestions() {
        return !getStudentTestQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.PublicationsNumber> getPublicationsNumbers() {
        return getPublicationsNumbersSet();
    }

    @Deprecated
    public boolean hasAnyPublicationsNumbers() {
        return !getPublicationsNumbersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationProtocol> getRegistrationProtocols() {
        return getRegistrationProtocolsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationProtocols() {
        return !getRegistrationProtocolsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide> getReimbursementGuides() {
        return getReimbursementGuidesSet();
    }

    @Deprecated
    public boolean hasAnyReimbursementGuides() {
        return !getReimbursementGuidesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Holiday> getHolidays() {
        return getHolidaysSet();
    }

    @Deprecated
    public boolean hasAnyHolidays() {
        return !getHolidaysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CompetenceCourse> getCompetenceCourses() {
        return getCompetenceCoursesSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourses() {
        return !getCompetenceCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber> getPartySocialSecurityNumbers() {
        return getPartySocialSecurityNumbersSet();
    }

    @Deprecated
    public boolean hasAnyPartySocialSecurityNumbers() {
        return !getPartySocialSecurityNumbersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType> getAccountabilityTypes() {
        return getAccountabilityTypesSet();
    }

    @Deprecated
    public boolean hasAnyAccountabilityTypes() {
        return !getAccountabilityTypesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.precedences.Restriction> getRestrictions() {
        return getRestrictionsSet();
    }

    @Deprecated
    public boolean hasAnyRestrictions() {
        return !getRestrictionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.DistributedTest> getDistributedTests() {
        return getDistributedTestsSet();
    }

    @Deprecated
    public boolean hasAnyDistributedTests() {
        return !getDistributedTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderProblem> getSantanderProblem() {
        return getSantanderProblemSet();
    }

    @Deprecated
    public boolean hasAnySantanderProblem() {
        return !getSantanderProblemSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice> getCaseStudyChoices() {
        return getCaseStudyChoicesSet();
    }

    @Deprecated
    public boolean hasAnyCaseStudyChoices() {
        return !getCaseStudyChoicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal> getGroupProposals() {
        return getGroupProposalsSet();
    }

    @Deprecated
    public boolean hasAnyGroupProposals() {
        return !getGroupProposalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gesdis.CourseReport> getCourseReports() {
        return getCourseReportsSet();
    }

    @Deprecated
    public boolean hasAnyCourseReports() {
        return !getCourseReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess> getPhdConclusionProcesses() {
        return getPhdConclusionProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdConclusionProcesses() {
        return !getPhdConclusionProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLog> getRequestLogs() {
        return getRequestLogsSet();
    }

    @Deprecated
    public boolean hasAnyRequestLogs() {
        return !getRequestLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryAnswer> getInquiriesAnswers() {
        return getInquiriesAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesAnswers() {
        return !getInquiriesAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry> getTeachingInquiries() {
        return getTeachingInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyTeachingInquiries() {
        return !getTeachingInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.OccupationPeriodReference> getOccupationPeriodReferences() {
        return getOccupationPeriodReferencesSet();
    }

    @Deprecated
    public boolean hasAnyOccupationPeriodReferences() {
        return !getOccupationPeriodReferencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase> getTSDProcessPhases() {
        return getTSDProcessPhasesSet();
    }

    @Deprecated
    public boolean hasAnyTSDProcessPhases() {
        return !getTSDProcessPhasesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.Seminary> getSeminarys() {
        return getSeminarysSet();
    }

    @Deprecated
    public boolean hasAnySeminarys() {
        return !getSeminarysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Orientation> getOrientations() {
        return getOrientationsSet();
    }

    @Deprecated
    public boolean hasAnyOrientations() {
        return !getOrientationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseScope> getCurricularCourseScopes() {
        return getCurricularCourseScopesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourseScopes() {
        return !getCurricularCourseScopesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion> getMasterDegreeThesisDataVersions() {
        return getMasterDegreeThesisDataVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesisDataVersions() {
        return !getMasterDegreeThesisDataVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPaymentPeriod> getPhdGratuityPaymentPeriods() {
        return getPhdGratuityPaymentPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityPaymentPeriods() {
        return !getPhdGratuityPaymentPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter> getPhdCandidacyRefereeLetters() {
        return getPhdCandidacyRefereeLettersSet();
    }

    @Deprecated
    public boolean hasAnyPhdCandidacyRefereeLetters() {
        return !getPhdCandidacyRefereeLettersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlan> getEquivalencePlans() {
        return getEquivalencePlansSet();
    }

    @Deprecated
    public boolean hasAnyEquivalencePlans() {
        return !getEquivalencePlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuityValues> getGratuityValuess() {
        return getGratuityValuessSet();
    }

    @Deprecated
    public boolean hasAnyGratuityValuess() {
        return !getGratuityValuessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.Context> getContexts() {
        return getContextsSet();
    }

    @Deprecated
    public boolean hasAnyContexts() {
        return !getContextsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess> getTeacherEvaluationProcess() {
        return getTeacherEvaluationProcessSet();
    }

    @Deprecated
    public boolean hasAnyTeacherEvaluationProcess() {
        return !getTeacherEvaluationProcessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Mark> getMarks() {
        return getMarksSet();
    }

    @Deprecated
    public boolean hasAnyMarks() {
        return !getMarksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.library.LibraryCard> getLibraryCards() {
        return getLibraryCardsSet();
    }

    @Deprecated
    public boolean hasAnyLibraryCards() {
        return !getLibraryCardsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate> getMobilityEmailTemplates() {
        return getMobilityEmailTemplatesSet();
    }

    @Deprecated
    public boolean hasAnyMobilityEmailTemplates() {
        return !getMobilityEmailTemplatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy> getOutboundMobilityCandidacy() {
        return getOutboundMobilityCandidacySet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacy() {
        return !getOutboundMobilityCandidacySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Accountability> getAccountabilitys() {
        return getAccountabilitysSet();
    }

    @Deprecated
    public boolean hasAnyAccountabilitys() {
        return !getAccountabilitysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy> getCandidacys() {
        return getCandidacysSet();
    }

    @Deprecated
    public boolean hasAnyCandidacys() {
        return !getCandidacysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LoginAlias> getLoginAlias() {
        return getLoginAliasSet();
    }

    @Deprecated
    public boolean hasAnyLoginAlias() {
        return !getLoginAliasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<pt.ist.bennu.core.domain.User> getUsers() {
        return getUsersSet();
    }

    @Deprecated
    public boolean hasAnyUsers() {
        return !getUsersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.TestChecksum> getTestChecksums() {
        return getTestChecksumsSet();
    }

    @Deprecated
    public boolean hasAnyTestChecksums() {
        return !getTestChecksumsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        return getOutboundMobilityCandidacyContestSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContest() {
        return !getOutboundMobilityCandidacyContestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryBlock> getInquiryBlocks() {
        return getInquiryBlocksSet();
    }

    @Deprecated
    public boolean hasAnyInquiryBlocks() {
        return !getInquiryBlocksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitSiteLink> getUnitSiteLinks() {
        return getUnitSiteLinksSet();
    }

    @Deprecated
    public boolean hasAnyUnitSiteLinks() {
        return !getUnitSiteLinksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.ResearchEvent> getEvents() {
        return getEventsSet();
    }

    @Deprecated
    public boolean hasAnyEvents() {
        return !getEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationRegister> getCardGenerationRegister() {
        return getCardGenerationRegisterSet();
    }

    @Deprecated
    public boolean hasAnyCardGenerationRegister() {
        return !getCardGenerationRegisterSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MarkSheet> getMarkSheets() {
        return getMarkSheetsSet();
    }

    @Deprecated
    public boolean hasAnyMarkSheets() {
        return !getMarkSheetsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Professorship> getProfessorships() {
        return getProfessorshipsSet();
    }

    @Deprecated
    public boolean hasAnyProfessorships() {
        return !getProfessorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem> getCardGenerationProblem() {
        return getCardGenerationProblemSet();
    }

    @Deprecated
    public boolean hasAnyCardGenerationProblem() {
        return !getCardGenerationProblemSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData> getPhdMigrationIndividualProcessData() {
        return getPhdMigrationIndividualProcessDataSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationIndividualProcessData() {
        return !getPhdMigrationIndividualProcessDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.AnnualCreditsState> getAnnualCreditsStates() {
        return getAnnualCreditsStatesSet();
    }

    @Deprecated
    public boolean hasAnyAnnualCreditsStates() {
        return !getAnnualCreditsStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator> getMobilityCoordinators() {
        return getMobilityCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyMobilityCoordinators() {
        return !getMobilityCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper> getEnrolmentWrappers() {
        return getEnrolmentWrappersSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentWrappers() {
        return !getEnrolmentWrappersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse> getNotNeedToEnrollInCurricularCourses() {
        return getNotNeedToEnrollInCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyNotNeedToEnrollInCurricularCourses() {
        return !getNotNeedToEnrollInCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorLog> getCoordinatorLog() {
        return getCoordinatorLogSet();
    }

    @Deprecated
    public boolean hasAnyCoordinatorLog() {
        return !getCoordinatorLogSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.functionalities.ExecutionPath> getExecutionPaths() {
        return getExecutionPathsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionPaths() {
        return !getExecutionPathsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.ScientificJournal> getScientificJournals() {
        return getScientificJournalsSet();
    }

    @Deprecated
    public boolean hasAnyScientificJournals() {
        return !getScientificJournalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLogMonth> getRequestLogMonths() {
        return getRequestLogMonthsSet();
    }

    @Deprecated
    public boolean hasAnyRequestLogMonths() {
        return !getRequestLogMonthsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Metadata> getMetadatas() {
        return getMetadatasSet();
    }

    @Deprecated
    public boolean hasAnyMetadatas() {
        return !getMetadatasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.OccupationPeriod> getOccupationPeriods() {
        return getOccupationPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyOccupationPeriods() {
        return !getOccupationPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Department> getDepartments() {
        return getDepartmentsSet();
    }

    @Deprecated
    public boolean hasAnyDepartments() {
        return !getDepartmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CostCenter> getCostCenters() {
        return getCostCentersSet();
    }

    @Deprecated
    public boolean hasAnyCostCenters() {
        return !getCostCentersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LoginPeriod> getLoginPeriods() {
        return getLoginPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyLoginPeriods() {
        return !getLoginPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ExecutedAction> getExecutedActions() {
        return getExecutedActionsSet();
    }

    @Deprecated
    public boolean hasAnyExecutedActions() {
        return !getExecutedActionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SchoolClass> getSchoolClasss() {
        return getSchoolClasssSet();
    }

    @Deprecated
    public boolean hasAnySchoolClasss() {
        return !getSchoolClasssSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesSummary> getOldInquiriesSummarys() {
        return getOldInquiriesSummarysSet();
    }

    @Deprecated
    public boolean hasAnyOldInquiriesSummarys() {
        return !getOldInquiriesSummarysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod> getUnavailablePeriods() {
        return getUnavailablePeriodsSet();
    }

    @Deprecated
    public boolean hasAnyUnavailablePeriods() {
        return !getUnavailablePeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.ExceptionType> getExceptionTypes() {
        return getExceptionTypesSet();
    }

    @Deprecated
    public boolean hasAnyExceptionTypes() {
        return !getExceptionTypesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getPartys() {
        return getPartysSet();
    }

    @Deprecated
    public boolean hasAnyPartys() {
        return !getPartysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting> getPhdMeetings() {
        return getPhdMeetingsSet();
    }

    @Deprecated
    public boolean hasAnyPhdMeetings() {
        return !getPhdMeetingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData> getMobilityStudentData() {
        return getMobilityStudentDataSet();
    }

    @Deprecated
    public boolean hasAnyMobilityStudentData() {
        return !getMobilityStudentDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.FAQSection> getFAQSections() {
        return getFAQSectionsSet();
    }

    @Deprecated
    public boolean hasAnyFAQSections() {
        return !getFAQSectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Evaluation> getEvaluations() {
        return getEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyEvaluations() {
        return !getEvaluationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmissionLog> getProjectSubmissionLogs() {
        return getProjectSubmissionLogsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissionLogs() {
        return !getProjectSubmissionLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion> getInquiryQuestions() {
        return getInquiryQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryQuestions() {
        return !getInquiryQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Recipient> getUtilEmailRecipients() {
        return getUtilEmailRecipientsSet();
    }

    @Deprecated
    public boolean hasAnyUtilEmailRecipients() {
        return !getUtilEmailRecipientsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.FunctionsAccumulation> getFunctionsAccumulations() {
        return getFunctionsAccumulationsSet();
    }

    @Deprecated
    public boolean hasAnyFunctionsAccumulations() {
        return !getFunctionsAccumulationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingParty> getParkingParties() {
        return getParkingPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParkingParties() {
        return !getParkingPartiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResult> getInquiryResults() {
        return getInquiryResultsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResults() {
        return !getInquiryResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.University> getUniversitys() {
        return getUniversitysSet();
    }

    @Deprecated
    public boolean hasAnyUniversitys() {
        return !getUniversitysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LoginRequest> getLoginRequests() {
        return getLoginRequestsSet();
    }

    @Deprecated
    public boolean hasAnyLoginRequests() {
        return !getLoginRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry> getInquiriesRegistrys() {
        return getInquiriesRegistrysSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesRegistrys() {
        return !getInquiriesRegistrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProfessorshipPermissions> getProfessorshipPermissions() {
        return getProfessorshipPermissionsSet();
    }

    @Deprecated
    public boolean hasAnyProfessorshipPermissions() {
        return !getProfessorshipPermissionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Guide> getGuides() {
        return getGuidesSet();
    }

    @Deprecated
    public boolean hasAnyGuides() {
        return !getGuidesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime> getProfessionalRegimes() {
        return getProfessionalRegimesSet();
    }

    @Deprecated
    public boolean hasAnyProfessionalRegimes() {
        return !getProfessionalRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.ErrorLog> getErrorLogs() {
        return getErrorLogsSet();
    }

    @Deprecated
    public boolean hasAnyErrorLogs() {
        return !getErrorLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.log.PhdLogEntry> getPhdLogEntries() {
        return getPhdLogEntriesSet();
    }

    @Deprecated
    public boolean hasAnyPhdLogEntries() {
        return !getPhdLogEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalCategory> getPersonProfessionalCategories() {
        return getPersonProfessionalCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalCategories() {
        return !getPersonProfessionalCategoriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.ReplyTo> getReplyTos() {
        return getReplyTosSet();
    }

    @Deprecated
    public boolean hasAnyReplyTos() {
        return !getReplyTosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ManagementGroups> getManagementGroups() {
        return getManagementGroupsSet();
    }

    @Deprecated
    public boolean hasAnyManagementGroups() {
        return !getManagementGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestMapping> getRequestMappings() {
        return getRequestMappingsSet();
    }

    @Deprecated
    public boolean hasAnyRequestMappings() {
        return !getRequestMappingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contacts.PartyContactValidation> getInvalidPartyContactValidations() {
        return getInvalidPartyContactValidationsSet();
    }

    @Deprecated
    public boolean hasAnyInvalidPartyContactValidations() {
        return !getInvalidPartyContactValidationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad> getCompetenceCourseLoads() {
        return getCompetenceCourseLoadsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseLoads() {
        return !getCompetenceCourseLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit> getExecutionCourseAudits() {
        return getExecutionCourseAuditsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourseAudits() {
        return !getExecutionCourseAuditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGrade> getIndividualCandidacySeriesGrade() {
        return getIndividualCandidacySeriesGradeSet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacySeriesGrade() {
        return !getIndividualCandidacySeriesGradeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourse> getExecutionCourses() {
        return getExecutionCoursesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourses() {
        return !getExecutionCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewTestGroup> getTestGroups() {
        return getTestGroupsSet();
    }

    @Deprecated
    public boolean hasAnyTestGroups() {
        return !getTestGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Installment> getInstallments() {
        return getInstallmentsSet();
    }

    @Deprecated
    public boolean hasAnyInstallments() {
        return !getInstallmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea> getPhdProgramFocusAreas() {
        return getPhdProgramFocusAreasSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramFocusAreas() {
        return !getPhdProgramFocusAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear> getAcademicServiceRequestYears() {
        return getAcademicServiceRequestYearsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequestYears() {
        return !getAcademicServiceRequestYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry> getCardGenerationEntries() {
        return getCardGenerationEntriesSet();
    }

    @Deprecated
    public boolean hasAnyCardGenerationEntries() {
        return !getCardGenerationEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Alumni> getAlumnis() {
        return getAlumnisSet();
    }

    @Deprecated
    public boolean hasAnyAlumnis() {
        return !getAlumnisSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry> getCerimonyInquiry() {
        return getCerimonyInquirySet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiry() {
        return !getCerimonyInquirySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CandidateSituation> getCandidateSituations() {
        return getCandidateSituationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidateSituations() {
        return !getCandidateSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing> getScheduleings() {
        return getScheduleingsSet();
    }

    @Deprecated
    public boolean hasAnyScheduleings() {
        return !getScheduleingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.curricularRules.CurricularRule> getCurricularRules() {
        return getCurricularRulesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularRules() {
        return !getCurricularRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewGroupElement> getGroupElements() {
        return getGroupElementsSet();
    }

    @Deprecated
    public boolean hasAnyGroupElements() {
        return !getGroupElementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationGuiding> getPhdMigrationGuidings() {
        return getPhdMigrationGuidingsSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationGuidings() {
        return !getPhdMigrationGuidingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExternalUser> getExternalUser() {
        return getExternalUserSet();
    }

    @Deprecated
    public boolean hasAnyExternalUser() {
        return !getExternalUserSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.PersonNamePart> getPersonNamePart() {
        return getPersonNamePartSet();
    }

    @Deprecated
    public boolean hasAnyPersonNamePart() {
        return !getPersonNamePartSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubject> getThesisSubjects() {
        return getThesisSubjectsSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjects() {
        return !getThesisSubjectsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.JournalIssue> getJournalIssues() {
        return getJournalIssuesSet();
    }

    @Deprecated
    public boolean hasAnyJournalIssues() {
        return !getJournalIssuesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Message> getUtilEmailMessages() {
        return getUtilEmailMessagesSet();
    }

    @Deprecated
    public boolean hasAnyUtilEmailMessages() {
        return !getUtilEmailMessagesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.Participation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherService> getTeacherServices() {
        return getTeacherServicesSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServices() {
        return !getTeacherServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry> getYearDelegateCourseInquiries() {
        return getYearDelegateCourseInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyYearDelegateCourseInquiries() {
        return !getYearDelegateCourseInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EducationArea> getEducationAreas() {
        return getEducationAreasSet();
    }

    @Deprecated
    public boolean hasAnyEducationAreas() {
        return !getEducationAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry> getAcademicCalendarEntries() {
        return getAcademicCalendarEntriesSet();
    }

    @Deprecated
    public boolean hasAnyAcademicCalendarEntries() {
        return !getAcademicCalendarEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Locality> getLocalities() {
        return getLocalitiesSet();
    }

    @Deprecated
    public boolean hasAnyLocalities() {
        return !getLocalitiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.MessageId> getUtilEmailMessageIds() {
        return getUtilEmailMessageIdsSet();
    }

    @Deprecated
    public boolean hasAnyUtilEmailMessageIds() {
        return !getUtilEmailMessageIdsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contents.Node> getNodes() {
        return getNodesSet();
    }

    @Deprecated
    public boolean hasAnyNodes() {
        return !getNodesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesTeacher> getInquiriesTeachers() {
        return getInquiriesTeachersSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesTeachers() {
        return !getInquiriesTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher> getTSDTeachers() {
        return getTSDTeachersSet();
    }

    @Deprecated
    public boolean hasAnyTSDTeachers() {
        return !getTSDTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationCoEvaluator> getTeacherEvaluationCoEvaluator() {
        return getTeacherEvaluationCoEvaluatorSet();
    }

    @Deprecated
    public boolean hasAnyTeacherEvaluationCoEvaluator() {
        return !getTeacherEvaluationCoEvaluatorSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Branch> getBranchs() {
        return getBranchsSet();
    }

    @Deprecated
    public boolean hasAnyBranchs() {
        return !getBranchsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument> getCandidacyDocument() {
        return getCandidacyDocumentSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyDocument() {
        return !getCandidacyDocumentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRoom> getInquiriesRooms() {
        return getInquiriesRoomsSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesRooms() {
        return !getInquiriesRoomsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualPersonalData> getPhdMigrationIndividualPersonalData() {
        return getPhdMigrationIndividualPersonalDataSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationIndividualPersonalData() {
        return !getPhdMigrationIndividualPersonalDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry> getReimbursementGuideEntrys() {
        return getReimbursementGuideEntrysSet();
    }

    @Deprecated
    public boolean hasAnyReimbursementGuideEntrys() {
        return !getReimbursementGuideEntrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateLog> getRegistrationStateLogs() {
        return getRegistrationStateLogsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationStateLogs() {
        return !getRegistrationStateLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getExternalScholarshipProvider() {
        return getExternalScholarshipProviderSet();
    }

    @Deprecated
    public boolean hasAnyExternalScholarshipProvider() {
        return !getExternalScholarshipProviderSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter> getFunctionalityParameters() {
        return getFunctionalityParametersSet();
    }

    @Deprecated
    public boolean hasAnyFunctionalityParameters() {
        return !getFunctionalityParametersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.raides.DegreeClassification> getDegreeClassifications() {
        return getDegreeClassificationsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeClassifications() {
        return !getDegreeClassificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.project.Project> getProjects() {
        return getProjectsSet();
    }

    @Deprecated
    public boolean hasAnyProjects() {
        return !getProjectsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation> getProfessionalRelations() {
        return getProfessionalRelationsSet();
    }

    @Deprecated
    public boolean hasAnyProfessionalRelations() {
        return !getProfessionalRelationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PostingRule> getPostingRules() {
        return getPostingRulesSet();
    }

    @Deprecated
    public boolean hasAnyPostingRules() {
        return !getPostingRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent> getCareerWorkshopApplicationEvents() {
        return getCareerWorkshopApplicationEventsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopApplicationEvents() {
        return !getCareerWorkshopApplicationEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventConferenceArticlesAssociation> getEventConferenceArticlesAssociations() {
        return getEventConferenceArticlesAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyEventConferenceArticlesAssociations() {
        return !getEventConferenceArticlesAssociationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DeleteFileRequest> getDeleteFileRequests() {
        return getDeleteFileRequestsSet();
    }

    @Deprecated
    public boolean hasAnyDeleteFileRequests() {
        return !getDeleteFileRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate> getServiceAgreementTemplates() {
        return getServiceAgreementTemplatesSet();
    }

    @Deprecated
    public boolean hasAnyServiceAgreementTemplates() {
        return !getServiceAgreementTemplatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contents.Content> getContents() {
        return getContentsSet();
    }

    @Deprecated
    public boolean hasAnyContents() {
        return !getContentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution> getTeacherServiceDistributions() {
        return getTeacherServiceDistributionsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServiceDistributions() {
        return !getTeacherServiceDistributionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup> getGroups() {
        return getGroupsSet();
    }

    @Deprecated
    public boolean hasAnyGroups() {
        return !getGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse> getTSDCourses() {
        return getTSDCoursesSet();
    }

    @Deprecated
    public boolean hasAnyTSDCourses() {
        return !getTSDCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.FAQEntry> getFAQEntrys() {
        return getFAQEntrysSet();
    }

    @Deprecated
    public boolean hasAnyFAQEntrys() {
        return !getFAQEntrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeInfoCandidacy> getDegreeInfoCandidacys() {
        return getDegreeInfoCandidacysSet();
    }

    @Deprecated
    public boolean hasAnyDegreeInfoCandidacys() {
        return !getDegreeInfoCandidacysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod> getDelegateElectionsPeriods() {
        return getDelegateElectionsPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyDelegateElectionsPeriods() {
        return !getDelegateElectionsPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Shift> getShifts() {
        return getShiftsSet();
    }

    @Deprecated
    public boolean hasAnyShifts() {
        return !getShiftsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Entry> getEntries() {
        return getEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEntries() {
        return !getEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Photograph> getPhotographs() {
        return getPhotographsSet();
    }

    @Deprecated
    public boolean hasAnyPhotographs() {
        return !getPhotographsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LessonPlanning> getLessonPlannings() {
        return getLessonPlanningsSet();
    }

    @Deprecated
    public boolean hasAnyLessonPlannings() {
        return !getLessonPlanningsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear> getRegistrationDataByExecutionYear() {
        return getRegistrationDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationDataByExecutionYear() {
        return !getRegistrationDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistribution> getShiftDistributions() {
        return getShiftDistributionsSet();
    }

    @Deprecated
    public boolean hasAnyShiftDistributions() {
        return !getShiftDistributionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry> getAcademicCalendars() {
        return getAcademicCalendarsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicCalendars() {
        return !getAcademicCalendarsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch> getCardGenerationBatches() {
        return getCardGenerationBatchesSet();
    }

    @Deprecated
    public boolean hasAnyCardGenerationBatches() {
        return !getCardGenerationBatchesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest> getAlumniIdentityRequest() {
        return getAlumniIdentityRequestSet();
    }

    @Deprecated
    public boolean hasAnyAlumniIdentityRequest() {
        return !getAlumniIdentityRequestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.raides.DegreeDesignation> getDegreeDesignations() {
        return getDegreeDesignationsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeDesignations() {
        return !getDegreeDesignationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftEnrolment> getShiftEnrolments() {
        return getShiftEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyShiftEnrolments() {
        return !getShiftEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcessVersion> getConclusionProcessVersions() {
        return getConclusionProcessVersionsSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessVersions() {
        return !getConclusionProcessVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentInstructions> getEnrolmentInstructions() {
        return getEnrolmentInstructionsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentInstructions() {
        return !getEnrolmentInstructionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement> getPhdThesisJuryElements() {
        return getPhdThesisJuryElementsSet();
    }

    @Deprecated
    public boolean hasAnyPhdThesisJuryElements() {
        return !getPhdThesisJuryElementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Receipt> getReceipts() {
        return getReceiptsSet();
    }

    @Deprecated
    public boolean hasAnyReceipts() {
        return !getReceiptsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem> getTeacherServiceItems() {
        return getTeacherServiceItemsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServiceItems() {
        return !getTeacherServiceItemsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewPermissionUnit> getPermissionUnits() {
        return getPermissionUnitsSet();
    }

    @Deprecated
    public boolean hasAnyPermissionUnits() {
        return !getPermissionUnitsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.Email> getEmails() {
        return getEmailsSet();
    }

    @Deprecated
    public boolean hasAnyEmails() {
        return !getEmailsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessYear> getFacultyEvaluationProcessYear() {
        return getFacultyEvaluationProcessYearSet();
    }

    @Deprecated
    public boolean hasAnyFacultyEvaluationProcessYear() {
        return !getFacultyEvaluationProcessYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.notification.PhdNotification> getPhdNotifications() {
        return getPhdNotificationsSet();
    }

    @Deprecated
    public boolean hasAnyPhdNotifications() {
        return !getPhdNotificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota> getMobilityQuotas() {
        return getMobilityQuotasSet();
    }

    @Deprecated
    public boolean hasAnyMobilityQuotas() {
        return !getMobilityQuotasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Account> getAccounts() {
        return getAccountsSet();
    }

    @Deprecated
    public boolean hasAnyAccounts() {
        return !getAccountsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherAuthorization> getTeacherAuthorization() {
        return getTeacherAuthorizationSet();
    }

    @Deprecated
    public boolean hasAnyTeacherAuthorization() {
        return !getTeacherAuthorizationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment> getInquiryGlobalComments() {
        return getInquiryGlobalCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGlobalComments() {
        return !getInquiryGlobalCommentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog> getTeacherServiceLog() {
        return getTeacherServiceLogSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServiceLog() {
        return !getTeacherServiceLogSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift> getMeasurementTestShifts() {
        return getMeasurementTestShiftsSet();
    }

    @Deprecated
    public boolean hasAnyMeasurementTestShifts() {
        return !getMeasurementTestShiftsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.MessageTransportResult> getMessageTransportResult() {
        return getMessageTransportResultSet();
    }

    @Deprecated
    public boolean hasAnyMessageTransportResult() {
        return !getMessageTransportResultSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession> getInternshipCandidacySession() {
        return getInternshipCandidacySessionSet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacySession() {
        return !getInternshipCandidacySessionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentEvaluation> getEnrolmentEvaluations() {
        return getEnrolmentEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentEvaluations() {
        return !getEnrolmentEvaluationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation> getCareerWorkshopConfirmations() {
        return getCareerWorkshopConfirmationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopConfirmations() {
        return !getCareerWorkshopConfirmationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PendingRequestParameter> getPendingRequestParameter() {
        return getPendingRequestParameterSet();
    }

    @Deprecated
    public boolean hasAnyPendingRequestParameter() {
        return !getPendingRequestParameterSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule> getCurriculumModules() {
        return getCurriculumModulesSet();
    }

    @Deprecated
    public boolean hasAnyCurriculumModules() {
        return !getCurriculumModulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DomainOperationLog> getDomainOperationLogs() {
        return getDomainOperationLogsSet();
    }

    @Deprecated
    public boolean hasAnyDomainOperationLogs() {
        return !getDomainOperationLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StrikeDay> getStrikeDay() {
        return getStrikeDaySet();
    }

    @Deprecated
    public boolean hasAnyStrikeDay() {
        return !getStrikeDaySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.Credits> getCredits() {
        return getCreditsSet();
    }

    @Deprecated
    public boolean hasAnyCredits() {
        return !getCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.Space> getLibraries() {
        return getLibrariesSet();
    }

    @Deprecated
    public boolean hasAnyLibraries() {
        return !getLibrariesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency> getCourseEquivalencys() {
        return getCourseEquivalencysSet();
    }

    @Deprecated
    public boolean hasAnyCourseEquivalencys() {
        return !getCourseEquivalencysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryExecutionPeriod> getStudentsInquiriesExecutionPeriod() {
        return getStudentsInquiriesExecutionPeriodSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiriesExecutionPeriod() {
        return !getStudentsInquiriesExecutionPeriodSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentStatute> getStudentStatutes() {
        return getStudentStatutesSet();
    }

    @Deprecated
    public boolean hasAnyStudentStatutes() {
        return !getStudentStatutesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Career> getCareers() {
        return getCareersSet();
    }

    @Deprecated
    public boolean hasAnyCareers() {
        return !getCareersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipSummary> getTutorshipSummaries() {
        return getTutorshipSummariesSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipSummaries() {
        return !getTutorshipSummariesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCredits> getTeacherCredits() {
        return getTeacherCreditsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCredits() {
        return !getTeacherCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesCoursesRes> getOldInquiriesCoursesRess() {
        return getOldInquiriesCoursesRessSet();
    }

    @Deprecated
    public boolean hasAnyOldInquiriesCoursesRess() {
        return !getOldInquiriesCoursesRessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry> getStudentsInquiryRegistries() {
        return getStudentsInquiryRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiryRegistries() {
        return !getStudentsInquiryRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.File> getFiles() {
        return getFilesSet();
    }

    @Deprecated
    public boolean hasAnyFiles() {
        return !getFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeProofVersion> getMasterDegreeProofVersions() {
        return getMasterDegreeProofVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeProofVersions() {
        return !getMasterDegreeProofVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation> getResultUnitAssociations() {
        return getResultUnitAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyResultUnitAssociations() {
        return !getResultUnitAssociationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation> getContractSituations() {
        return getContractSituationsSet();
    }

    @Deprecated
    public boolean hasAnyContractSituations() {
        return !getContractSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Price> getPrices() {
        return getPricesSet();
    }

    @Deprecated
    public boolean hasAnyPrices() {
        return !getPricesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType> getExtraCurricularActivityType() {
        return getExtraCurricularActivityTypeSet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularActivityType() {
        return !getExtraCurricularActivityTypeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Teacher> getTeachers() {
        return getTeachersSet();
    }

    @Deprecated
    public boolean hasAnyTeachers() {
        return !getTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart> getUnitNamePart() {
        return getUnitNamePartSet();
    }

    @Deprecated
    public boolean hasAnyUnitNamePart() {
        return !getUnitNamePartSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.caseHandling.Process> getProcesses() {
        return getProcessesSet();
    }

    @Deprecated
    public boolean hasAnyProcesses() {
        return !getProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup> getDeletedPersistentAccessGroup() {
        return getDeletedPersistentAccessGroupSet();
    }

    @Deprecated
    public boolean hasAnyDeletedPersistentAccessGroup() {
        return !getDeletedPersistentAccessGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EvaluationMethod> getEvaluationMethods() {
        return getEvaluationMethodsSet();
    }

    @Deprecated
    public boolean hasAnyEvaluationMethods() {
        return !getEvaluationMethodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator> getExamCoordinators() {
        return getExamCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyExamCoordinators() {
        return !getExamCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption> getPersonProfessionalExemptions() {
        return getPersonProfessionalExemptionsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalExemptions() {
        return !getPersonProfessionalExemptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SupportLesson> getSupportLessons() {
        return getSupportLessonsSet();
    }

    @Deprecated
    public boolean hasAnySupportLessons() {
        return !getSupportLessonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingRequest> getParkingRequests() {
        return getParkingRequestsSet();
    }

    @Deprecated
    public boolean hasAnyParkingRequests() {
        return !getParkingRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PaymentCode> getPaymentCodes() {
        return getPaymentCodesSet();
    }

    @Deprecated
    public boolean hasAnyPaymentCodes() {
        return !getPaymentCodesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingGroup> getParkingGroups() {
        return getParkingGroupsSet();
    }

    @Deprecated
    public boolean hasAnyParkingGroups() {
        return !getParkingGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation> getPersonContractSituations() {
        return getPersonContractSituationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonContractSituations() {
        return !getPersonContractSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResearchResult> getResults() {
        return getResultsSet();
    }

    @Deprecated
    public boolean hasAnyResults() {
        return !getResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExportGrouping> getExportGroupings() {
        return getExportGroupingsSet();
    }

    @Deprecated
    public boolean hasAnyExportGroupings() {
        return !getExportGroupingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent> getCareerWorkshopConfirmationEvents() {
        return getCareerWorkshopConfirmationEventsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopConfirmationEvents() {
        return !getCareerWorkshopConfirmationEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Identification> getIdentifications() {
        return getIdentificationsSet();
    }

    @Deprecated
    public boolean hasAnyIdentifications() {
        return !getIdentificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Country> getCountrys() {
        return getCountrysSet();
    }

    @Deprecated
    public boolean hasAnyCountrys() {
        return !getCountrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.TestQuestion> getTestQuestions() {
        return getTestQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyTestQuestions() {
        return !getTestQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PaymentPlan> getPaymentPlans() {
        return getPaymentPlansSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPlans() {
        return !getPaymentPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation> getTeacherPersonalExpectations() {
        return getTeacherPersonalExpectationsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherPersonalExpectations() {
        return !getTeacherPersonalExpectationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits> getAnnualTeachingCredits() {
        return getAnnualTeachingCreditsSet();
    }

    @Deprecated
    public boolean hasAnyAnnualTeachingCredits() {
        return !getAnnualTeachingCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipSummaryPeriod> getTutorshipSummaryPeriods() {
        return getTutorshipSummaryPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipSummaryPeriods() {
        return !getTutorshipSummaryPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment> getExternalEnrolments() {
        return getExternalEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyExternalEnrolments() {
        return !getExternalEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgram> getPhdPrograms() {
        return getPhdProgramsSet();
    }

    @Deprecated
    public boolean hasAnyPhdPrograms() {
        return !getPhdProgramsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Question> getQuestions() {
        return getQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyQuestions() {
        return !getQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.Absence> getAbsences() {
        return getAbsencesSet();
    }

    @Deprecated
    public boolean hasAnyAbsences() {
        return !getAbsencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation> getWeeklyOcupations() {
        return getWeeklyOcupationsSet();
    }

    @Deprecated
    public boolean hasAnyWeeklyOcupations() {
        return !getWeeklyOcupationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyDegreeEntry> getOver23IndividualCandidacyDegreeEntries() {
        return getOver23IndividualCandidacyDegreeEntriesSet();
    }

    @Deprecated
    public boolean hasAnyOver23IndividualCandidacyDegreeEntries() {
        return !getOver23IndividualCandidacyDegreeEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gesdis.CourseHistoric> getCourseHistorics() {
        return getCourseHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyCourseHistorics() {
        return !getCourseHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.ExternalRegistrationData> getExternalRegistrationDatas() {
        return getExternalRegistrationDatasSet();
    }

    @Deprecated
    public boolean hasAnyExternalRegistrationDatas() {
        return !getExternalRegistrationDatasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.MeasurementTest> getMeasurementTests() {
        return getMeasurementTestsSet();
    }

    @Deprecated
    public boolean hasAnyMeasurementTests() {
        return !getMeasurementTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal> getProposals() {
        return getProposalsSet();
    }

    @Deprecated
    public boolean hasAnyProposals() {
        return !getProposalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentNumber> getStudentNumbers() {
        return getStudentNumbersSet();
    }

    @Deprecated
    public boolean hasAnyStudentNumbers() {
        return !getStudentNumbersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalContractType> getProfessionalContractTypes() {
        return getProfessionalContractTypesSet();
    }

    @Deprecated
    public boolean hasAnyProfessionalContractTypes() {
        return !getProfessionalContractTypesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.FirstYearShiftsCapacityToggleLog> getFirstYearShiftsCapacityToggleLogs() {
        return getFirstYearShiftsCapacityToggleLogsSet();
    }

    @Deprecated
    public boolean hasAnyFirstYearShiftsCapacityToggleLogs() {
        return !getFirstYearShiftsCapacityToggleLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ScientificArea> getScientificAreas() {
        return getScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyScientificAreas() {
        return !getScientificAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GuideSituation> getGuideSituations() {
        return getGuideSituationsSet();
    }

    @Deprecated
    public boolean hasAnyGuideSituations() {
        return !getGuideSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex> getEctsTableIndex() {
        return getEctsTableIndexSet();
    }

    @Deprecated
    public boolean hasAnyEctsTableIndex() {
        return !getEctsTableIndexSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Advise> getAdvises() {
        return getAdvisesSet();
    }

    @Deprecated
    public boolean hasAnyAdvises() {
        return !getAdvisesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea> getCreditsInAnySecundaryAreas() {
        return getCreditsInAnySecundaryAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInAnySecundaryAreas() {
        return !getCreditsInAnySecundaryAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod> getInquiryResponsePeriods() {
        return getInquiryResponsePeriodsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResponsePeriods() {
        return !getInquiryResponsePeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Grouping> getGroupings() {
        return getGroupingsSet();
    }

    @Deprecated
    public boolean hasAnyGroupings() {
        return !getGroupingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Delegate> getDelegates() {
        return getDelegatesSet();
    }

    @Deprecated
    public boolean hasAnyDelegates() {
        return !getDelegatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PersonAccount> getPersonAccounts() {
        return getPersonAccountsSet();
    }

    @Deprecated
    public boolean hasAnyPersonAccounts() {
        return !getPersonAccountsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipLog> getTutorshipLogs() {
        return getTutorshipLogsSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipLogs() {
        return !getTutorshipLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeCurricularPlan> getDegreeCurricularPlans() {
        return getDegreeCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyDegreeCurricularPlans() {
        return !getDegreeCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.EnrolmentEvaluationLog> getEnrolmentEvaluationLog() {
        return getEnrolmentEvaluationLogSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentEvaluationLog() {
        return !getEnrolmentEvaluationLogSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.Theme> getThemes() {
        return getThemesSet();
    }

    @Deprecated
    public boolean hasAnyThemes() {
        return !getThemesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInScientificArea> getCreditsInScientificAreas() {
        return getCreditsInScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInScientificAreas() {
        return !getCreditsInScientificAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DegreeInfoFuture> getDegreeInfoFutures() {
        return getDegreeInfoFuturesSet();
    }

    @Deprecated
    public boolean hasAnyDegreeInfoFutures() {
        return !getDegreeInfoFuturesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MetaDomainObject> getMetaDomainObjects() {
        return getMetaDomainObjectsSet();
    }

    @Deprecated
    public boolean hasAnyMetaDomainObjects() {
        return !getMetaDomainObjectsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.CurriculumLineLog> getCurriculumLineLogs() {
        return getCurriculumLineLogsSet();
    }

    @Deprecated
    public boolean hasAnyCurriculumLineLogs() {
        return !getCurriculumLineLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.BusinessArea> getBusinessAreas() {
        return getBusinessAreasSet();
    }

    @Deprecated
    public boolean hasAnyBusinessAreas() {
        return !getBusinessAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultTeacher> getResultTeachers() {
        return getResultTeachersSet();
    }

    @Deprecated
    public boolean hasAnyResultTeachers() {
        return !getResultTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess> getTSDProcesses() {
        return getTSDProcessesSet();
    }

    @Deprecated
    public boolean hasAnyTSDProcesses() {
        return !getTSDProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Photograph> getPendingPhotographs() {
        return getPendingPhotographsSet();
    }

    @Deprecated
    public boolean hasAnyPendingPhotographs() {
        return !getPendingPhotographsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers> getPersistentGroupMembers() {
        return getPersistentGroupMembersSet();
    }

    @Deprecated
    public boolean hasAnyPersistentGroupMembers() {
        return !getPersistentGroupMembersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment> getWrittenEvaluationEnrolments() {
        return getWrittenEvaluationEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyWrittenEvaluationEnrolments() {
        return !getWrittenEvaluationEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ServiceExemption> getServiceExemptions() {
        return getServiceExemptionsSet();
    }

    @Deprecated
    public boolean hasAnyServiceExemptions() {
        return !getServiceExemptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.InstitutionRegistryCodeGenerator> getRegistryCodeGenerator() {
        return getRegistryCodeGeneratorSet();
    }

    @Deprecated
    public boolean hasAnyRegistryCodeGenerator() {
        return !getRegistryCodeGeneratorSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent> getSecretaryEnrolmentStudents() {
        return getSecretaryEnrolmentStudentsSet();
    }

    @Deprecated
    public boolean hasAnySecretaryEnrolmentStudents() {
        return !getSecretaryEnrolmentStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup> getPersistentAccessGroup() {
        return getPersistentAccessGroupSet();
    }

    @Deprecated
    public boolean hasAnyPersistentAccessGroup() {
        return !getPersistentAccessGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.AccountingTransaction> getAccountingTransactions() {
        return getAccountingTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyAccountingTransactions() {
        return !getAccountingTransactionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption> getOutboundMobilityCandidacyPeriodConfirmationOption() {
        return getOutboundMobilityCandidacyPeriodConfirmationOptionSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyPeriodConfirmationOption() {
        return !getOutboundMobilityCandidacyPeriodConfirmationOptionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Summary> getSummarys() {
        return getSummarysSet();
    }

    @Deprecated
    public boolean hasAnySummarys() {
        return !getSummarysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog> getStudentTestLogs() {
        return getStudentTestLogsSet();
    }

    @Deprecated
    public boolean hasAnyStudentTestLogs() {
        return !getStudentTestLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent> getGroupStudents() {
        return getGroupStudentsSet();
    }

    @Deprecated
    public boolean hasAnyGroupStudents() {
        return !getGroupStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.ResourceAllocation> getResourceAllocations() {
        return getResourceAllocationsSet();
    }

    @Deprecated
    public boolean hasAnyResourceAllocations() {
        return !getResourceAllocationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult> getStudentInquiriesCourseResults() {
        return getStudentInquiriesCourseResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesCourseResults() {
        return !getStudentInquiriesCourseResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory> getProfessionalCategories() {
        return getProfessionalCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyProfessionalCategories() {
        return !getProfessionalCategoriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule> getDegreeModules() {
        return getDegreeModulesSet();
    }

    @Deprecated
    public boolean hasAnyDegreeModules() {
        return !getDegreeModulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.library.LibraryDocument> getLibraryDocument() {
        return getLibraryDocumentSet();
    }

    @Deprecated
    public boolean hasAnyLibraryDocument() {
        return !getLibraryDocumentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.StudentCurricularPlan> getStudentCurricularPlans() {
        return getStudentCurricularPlansSet();
    }

    @Deprecated
    public boolean hasAnyStudentCurricularPlans() {
        return !getStudentCurricularPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment> getPunctualRoomsOccupationComments() {
        return getPunctualRoomsOccupationCommentsSet();
    }

    @Deprecated
    public boolean hasAnyPunctualRoomsOccupationComments() {
        return !getPunctualRoomsOccupationCommentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess> getConclusionProcesses() {
        return getConclusionProcessesSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcesses() {
        return !getConclusionProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmissionGrade> getOutboundMobilityCandidacySubmissionGrade() {
        return getOutboundMobilityCandidacySubmissionGradeSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacySubmissionGrade() {
        return !getOutboundMobilityCandidacySubmissionGradeSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest> getAcademicServiceRequests() {
        return getAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequests() {
        return !getAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.Vigilancy> getVigilancies() {
        return getVigilanciesSet();
    }

    @Deprecated
    public boolean hasAnyVigilancies() {
        return !getVigilanciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport> getSibsPaymentFileProcessReports() {
        return getSibsPaymentFileProcessReportsSet();
    }

    @Deprecated
    public boolean hasAnySibsPaymentFileProcessReports() {
        return !getSibsPaymentFileProcessReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry> getSibsPaymentFileEntrys() {
        return getSibsPaymentFileEntrysSet();
    }

    @Deprecated
    public boolean hasAnySibsPaymentFileEntrys() {
        return !getSibsPaymentFileEntrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.TestScope> getTestScopes() {
        return getTestScopesSet();
    }

    @Deprecated
    public boolean hasAnyTestScopes() {
        return !getTestScopesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcess> getFacultyEvaluationProcess() {
        return getFacultyEvaluationProcessSet();
    }

    @Deprecated
    public boolean hasAnyFacultyEvaluationProcess() {
        return !getFacultyEvaluationProcessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalContract> getPersonProfessionalContracts() {
        return getPersonProfessionalContractsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalContracts() {
        return !getPersonProfessionalContractsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest> getPunctualRoomsOccupationRequests() {
        return getPunctualRoomsOccupationRequestsSet();
    }

    @Deprecated
    public boolean hasAnyPunctualRoomsOccupationRequests() {
        return !getPunctualRoomsOccupationRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.system.CronScriptInvocation> getCronScriptInvocations() {
        return getCronScriptInvocationsSet();
    }

    @Deprecated
    public boolean hasAnyCronScriptInvocations() {
        return !getCronScriptInvocationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DomainObjectActionLog> getDomainObjectActionLogs() {
        return getDomainObjectActionLogsSet();
    }

    @Deprecated
    public boolean hasAnyDomainObjectActionLogs() {
        return !getDomainObjectActionLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.PartyType> getPartyTypes() {
        return getPartyTypesSet();
    }

    @Deprecated
    public boolean hasAnyPartyTypes() {
        return !getPartyTypesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.system.CronRegistry> getCronRegistry() {
        return getCronRegistrySet();
    }

    @Deprecated
    public boolean hasAnyCronRegistry() {
        return !getCronRegistrySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DistrictSubdivision> getDistrictSubdivisions() {
        return getDistrictSubdivisionsSet();
    }

    @Deprecated
    public boolean hasAnyDistrictSubdivisions() {
        return !getDistrictSubdivisionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState> getRegistrationStates() {
        return getRegistrationStatesSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationStates() {
        return !getRegistrationStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesStudentExecutionPeriod> getInquiriesStudentExecutionPeriods() {
        return getInquiriesStudentExecutionPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyInquiriesStudentExecutionPeriods() {
        return !getInquiriesStudentExecutionPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.SupportRequest> getSupportRequests() {
        return getSupportRequestsSet();
    }

    @Deprecated
    public boolean hasAnySupportRequests() {
        return !getSupportRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdParticipant> getPhdParticipants() {
        return getPhdParticipantsSet();
    }

    @Deprecated
    public boolean hasAnyPhdParticipants() {
        return !getPhdParticipantsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PersonalGroup> getPersonalGroups() {
        return getPersonalGroupsSet();
    }

    @Deprecated
    public boolean hasAnyPersonalGroups() {
        return !getPersonalGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionInterval> getExecutionIntervals() {
        return getExecutionIntervalsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionIntervals() {
        return !getExecutionIntervalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.Vehicle> getVehicles() {
        return getVehiclesSet();
    }

    @Deprecated
    public boolean hasAnyVehicles() {
        return !getVehiclesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularSemester> getCurricularSemesters() {
        return getCurricularSemestersSet();
    }

    @Deprecated
    public boolean hasAnyCurricularSemesters() {
        return !getCurricularSemestersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElection> getDelegateElections() {
        return getDelegateElectionsSet();
    }

    @Deprecated
    public boolean hasAnyDelegateElections() {
        return !getDelegateElectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewCorrector> getCorrectors() {
        return getCorrectorsSet();
    }

    @Deprecated
    public boolean hasAnyCorrectors() {
        return !getCorrectorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreeInformation() {
        return getPrecedentDegreeInformationSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreeInformation() {
        return !getPrecedentDegreeInformationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent> getTeacherDegreeFinalProjectStudents() {
        return getTeacherDegreeFinalProjectStudentsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherDegreeFinalProjectStudents() {
        return !getTeacherDegreeFinalProjectStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Employee> getEmployees() {
        return getEmployeesSet();
    }

    @Deprecated
    public boolean hasAnyEmployees() {
        return !getEmployeesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData> getGiafProfessionalData() {
        return getGiafProfessionalDataSet();
    }

    @Deprecated
    public boolean hasAnyGiafProfessionalData() {
        return !getGiafProfessionalDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup> getVigilantGroups() {
        return getVigilantGroupsSet();
    }

    @Deprecated
    public boolean hasAnyVigilantGroups() {
        return !getVigilantGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.ResearchInterest> getResearchInterests() {
        return getResearchInterestsSet();
    }

    @Deprecated
    public boolean hasAnyResearchInterests() {
        return !getResearchInterestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson> getCerimonyInquiryPerson() {
        return getCerimonyInquiryPersonSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryPerson() {
        return !getCerimonyInquiryPersonSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GuideEntry> getGuideEntrys() {
        return getGuideEntrysSet();
    }

    @Deprecated
    public boolean hasAnyGuideEntrys() {
        return !getGuideEntrysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.InsuranceValue> getInsuranceValues() {
        return getInsuranceValuesSet();
    }

    @Deprecated
    public boolean hasAnyInsuranceValues() {
        return !getInsuranceValuesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy> getAvailabilityPolicy() {
        return getAvailabilityPolicySet();
    }

    @Deprecated
    public boolean hasAnyAvailabilityPolicy() {
        return !getAvailabilityPolicySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.QueueJob> getQueueJobUndone() {
        return getQueueJobUndoneSet();
    }

    @Deprecated
    public boolean hasAnyQueueJobUndone() {
        return !getQueueJobUndoneSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Event> getAccountingEvents() {
        return getAccountingEventsSet();
    }

    @Deprecated
    public boolean hasAnyAccountingEvents() {
        return !getAccountingEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.QueueJob> getQueueJob() {
        return getQueueJobSet();
    }

    @Deprecated
    public boolean hasAnyQueueJob() {
        return !getQueueJobSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderSequenceNumberGenerator> getSantanderSequenceNumberGenerators() {
        return getSantanderSequenceNumberGeneratorsSet();
    }

    @Deprecated
    public boolean hasAnySantanderSequenceNumberGenerators() {
        return !getSantanderSequenceNumberGeneratorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CourseLoad> getCourseLoads() {
        return getCourseLoadsSet();
    }

    @Deprecated
    public boolean hasAnyCourseLoads() {
        return !getCourseLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.FakeShift> getFakeShifts() {
        return getFakeShiftsSet();
    }

    @Deprecated
    public boolean hasAnyFakeShifts() {
        return !getFakeShiftsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequests() {
        return getCompetenceCourseInformationChangeRequestsSet();
    }

    @Deprecated
    public boolean hasAnyCompetenceCourseInformationChangeRequests() {
        return !getCompetenceCourseInformationChangeRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PublicCandidacyHashCode> getCandidacyHashCodes() {
        return getCandidacyHashCodesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyHashCodes() {
        return !getCandidacyHashCodesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom> getMeasurementTestRooms() {
        return getMeasurementTestRoomsSet();
    }

    @Deprecated
    public boolean hasAnyMeasurementTestRooms() {
        return !getMeasurementTestRoomsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GenericEvent> getGenericEvents() {
        return getGenericEventsSet();
    }

    @Deprecated
    public boolean hasAnyGenericEvents() {
        return !getGenericEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ImportRegister> getImportRegister() {
        return getImportRegisterSet();
    }

    @Deprecated
    public boolean hasAnyImportRegister() {
        return !getImportRegisterSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionDegree> getExecutionDegrees() {
        return getExecutionDegreesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegrees() {
        return !getExecutionDegreesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.FunctionalityPrinters> getFunctionalityPrinters() {
        return getFunctionalityPrintersSet();
    }

    @Deprecated
    public boolean hasAnyFunctionalityPrinters() {
        return !getFunctionalityPrintersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication> getCareerWorkshopApplications() {
        return getCareerWorkshopApplicationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopApplications() {
        return !getCareerWorkshopApplicationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.classProperties.GeneralClassProperty> getGeneralClassPropertys() {
        return getGeneralClassPropertysSet();
    }

    @Deprecated
    public boolean hasAnyGeneralClassPropertys() {
        return !getGeneralClassPropertysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime> getPersonProfessionalRegimes() {
        return getPersonProfessionalRegimesSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalRegimes() {
        return !getPersonProfessionalRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getConnectionRules() {
        return getConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyConnectionRules() {
        return !getConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification> getExemptionJustifications() {
        return getExemptionJustificationsSet();
    }

    @Deprecated
    public boolean hasAnyExemptionJustifications() {
        return !getExemptionJustificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.PersonName> getPersonName() {
        return getPersonNameSet();
    }

    @Deprecated
    public boolean hasAnyPersonName() {
        return !getPersonNameSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdStudyPlan> getPhdStudyPlans() {
        return getPhdStudyPlansSet();
    }

    @Deprecated
    public boolean hasAnyPhdStudyPlans() {
        return !getPhdStudyPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.OldPublication> getOldPublications() {
        return getOldPublicationsSet();
    }

    @Deprecated
    public boolean hasAnyOldPublications() {
        return !getOldPublicationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime> getTeacherInstitutionWorkTimes() {
        return getTeacherInstitutionWorkTimesSet();
    }

    @Deprecated
    public boolean hasAnyTeacherInstitutionWorkTimes() {
        return !getTeacherInstitutionWorkTimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.messaging.ForumSubscription> getForumSubscriptions() {
        return getForumSubscriptionsSet();
    }

    @Deprecated
    public boolean hasAnyForumSubscriptions() {
        return !getForumSubscriptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PaymentCodeMapping> getPaymentCodeMappings() {
        return getPaymentCodeMappingsSet();
    }

    @Deprecated
    public boolean hasAnyPaymentCodeMappings() {
        return !getPaymentCodeMappingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer> getQuestionAnswers() {
        return getQuestionAnswersSet();
    }

    @Deprecated
    public boolean hasAnyQuestionAnswers() {
        return !getQuestionAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.RoomClassification> getRoomClassification() {
        return getRoomClassificationSet();
    }

    @Deprecated
    public boolean hasAnyRoomClassification() {
        return !getRoomClassificationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Job> getJobs() {
        return getJobsSet();
    }

    @Deprecated
    public boolean hasAnyJobs() {
        return !getJobsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularYear> getCurricularYears() {
        return getCurricularYearsSet();
    }

    @Deprecated
    public boolean hasAnyCurricularYears() {
        return !getCurricularYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail> getAccountingTransactionDetails() {
        return getAccountingTransactionDetailsSet();
    }

    @Deprecated
    public boolean hasAnyAccountingTransactionDetails() {
        return !getAccountingTransactionDetailsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPriceQuirk> getPhdGratuityPriceQuirks() {
        return getPhdGratuityPriceQuirksSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityPriceQuirks() {
        return !getPhdGratuityPriceQuirksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAttendss() {
        return getAttendssSet();
    }

    @Deprecated
    public boolean hasAnyAttendss() {
        return !getAttendssSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLogDay> getRequestLogDays() {
        return getRequestLogDaysSet();
    }

    @Deprecated
    public boolean hasAnyRequestLogDays() {
        return !getRequestLogDaysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Student> getStudents() {
        return getStudentsSet();
    }

    @Deprecated
    public boolean hasAnyStudents() {
        return !getStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch> getRectorateSubmissionBatch() {
        return getRectorateSubmissionBatchSet();
    }

    @Deprecated
    public boolean hasAnyRectorateSubmissionBatch() {
        return !getRectorateSubmissionBatchSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CaseStudy> getCaseStudys() {
        return getCaseStudysSet();
    }

    @Deprecated
    public boolean hasAnyCaseStudys() {
        return !getCaseStudysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationNumber> getRegistrationNumbers() {
        return getRegistrationNumbersSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationNumbers() {
        return !getRegistrationNumbersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement> getPhdCandidacyFeedbackElements() {
        return getPhdCandidacyFeedbackElementsSet();
    }

    @Deprecated
    public boolean hasAnyPhdCandidacyFeedbackElements() {
        return !getPhdCandidacyFeedbackElementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear> getStudentDataByExecutionYear() {
        return getStudentDataByExecutionYearSet();
    }

    @Deprecated
    public boolean hasAnyStudentDataByExecutionYear() {
        return !getStudentDataByExecutionYearSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryQuestionHeader> getInquiryQuestionHeaders() {
        return getInquiryQuestionHeadersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryQuestionHeaders() {
        return !getInquiryQuestionHeadersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftGroupingProperties> getShiftGroupingProperties() {
        return getShiftGroupingPropertiesSet();
    }

    @Deprecated
    public boolean hasAnyShiftGroupingProperties() {
        return !getShiftGroupingPropertiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipSummaryRelation> getTutorshipSummaryRelations() {
        return getTutorshipSummaryRelationsSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipSummaryRelations() {
        return !getTutorshipSummaryRelationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.Blueprint> getBlueprints() {
        return getBlueprintsSet();
    }

    @Deprecated
    public boolean hasAnyBlueprints() {
        return !getBlueprintsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramInformation> getPhdProgramInformations() {
        return getPhdProgramInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramInformations() {
        return !getPhdProgramInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.thesis.Thesis> getThesesPendingPublication() {
        return getThesesPendingPublicationSet();
    }

    @Deprecated
    public boolean hasAnyThesesPendingPublication() {
        return !getThesesPendingPublicationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.administrativeOffice.curriculumValidation.DocumentPrintRequest> getRequest() {
        return getRequestSet();
    }

    @Deprecated
    public boolean hasAnyRequest() {
        return !getRequestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFileTag> getUnitFileTags() {
        return getUnitFileTagsSet();
    }

    @Deprecated
    public boolean hasAnyUnitFileTags() {
        return !getUnitFileTagsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeCandidate> getMasterDegreeCandidates() {
        return getMasterDegreeCandidatesSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeCandidates() {
        return !getMasterDegreeCandidatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidenceMonth> getResidenceMonths0() {
        return getResidenceMonths0Set();
    }

    @Deprecated
    public boolean hasAnyResidenceMonths0() {
        return !getResidenceMonths0Set().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuitySituation> getGratuitySituations() {
        return getGratuitySituationsSet();
    }

    @Deprecated
    public boolean hasAnyGratuitySituations() {
        return !getGratuitySituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.Researcher> getResearchers() {
        return getResearchersSet();
    }

    @Deprecated
    public boolean hasAnyResearchers() {
        return !getResearchersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LessonInstance> getLessonInstances() {
        return getLessonInstancesSet();
    }

    @Deprecated
    public boolean hasAnyLessonInstances() {
        return !getLessonInstancesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ExternalPhdProgram> getExternalPhdPrograms() {
        return getExternalPhdProgramsSet();
    }

    @Deprecated
    public boolean hasAnyExternalPhdPrograms() {
        return !getExternalPhdProgramsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCreditsState> getTeacherCreditsState() {
        return getTeacherCreditsStateSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCreditsState() {
        return !getTeacherCreditsStateSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark> getApprovedTeacherEvaluationProcessMark() {
        return getApprovedTeacherEvaluationProcessMarkSet();
    }

    @Deprecated
    public boolean hasAnyApprovedTeacherEvaluationProcessMark() {
        return !getApprovedTeacherEvaluationProcessMarkSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ReceiptPrintVersion> getReceiptVersions() {
        return getReceiptVersionsSet();
    }

    @Deprecated
    public boolean hasAnyReceiptVersions() {
        return !getReceiptVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getInternshipCandidacy() {
        return getInternshipCandidacySet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacy() {
        return !getInternshipCandidacySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage> getPhdAlertMessages() {
        return getPhdAlertMessagesSet();
    }

    @Deprecated
    public boolean hasAnyPhdAlertMessages() {
        return !getPhdAlertMessagesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contacts.PartyContact> getPartyContacts() {
        return getPartyContactsSet();
    }

    @Deprecated
    public boolean hasAnyPartyContacts() {
        return !getPartyContactsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.RegistrationRegime> getRegistrationRegimes() {
        return getRegistrationRegimesSet();
    }

    @Deprecated
    public boolean hasAnyRegistrationRegimes() {
        return !getRegistrationRegimesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant> getThesisParticipations() {
        return getThesisParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyThesisParticipations() {
        return !getThesisParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod> getPhdProgramContextPeriods() {
        return getPhdProgramContextPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramContextPeriods() {
        return !getPhdProgramContextPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod> getCurricularPeriods() {
        return getCurricularPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyCurricularPeriods() {
        return !getCurricularPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonFunctionsAccumulation> getPersonFunctionsAccumulations() {
        return getPersonFunctionsAccumulationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonFunctionsAccumulations() {
        return !getPersonFunctionsAccumulationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.period.CandidacyPeriod> getCandidacyPeriods() {
        return getCandidacyPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPeriods() {
        return !getCandidacyPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublication> getPreferredPublication() {
        return getPreferredPublicationSet();
    }

    @Deprecated
    public boolean hasAnyPreferredPublication() {
        return !getPreferredPublicationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.FakeEnrollment> getFakeEnrollments() {
        return getFakeEnrollmentsSet();
    }

    @Deprecated
    public boolean hasAnyFakeEnrollments() {
        return !getFakeEnrollmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLogYear> getRequestLogYears() {
        return getRequestLogYearsSet();
    }

    @Deprecated
    public boolean hasAnyRequestLogYears() {
        return !getRequestLogYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport> getCoordinatorExecutionDegreeCoursesReports() {
        return getCoordinatorExecutionDegreeCoursesReportsSet();
    }

    @Deprecated
    public boolean hasAnyCoordinatorExecutionDegreeCoursesReports() {
        return !getCoordinatorExecutionDegreeCoursesReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionYear> getExecutionYears() {
        return getExecutionYearsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionYears() {
        return !getExecutionYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderEntry> getSantanderEntries() {
        return getSantanderEntriesSet();
    }

    @Deprecated
    public boolean hasAnySantanderEntries() {
        return !getSantanderEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.Modality> getModalitys() {
        return getModalitysSet();
    }

    @Deprecated
    public boolean hasAnyModalitys() {
        return !getModalitysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.SpaceInformation> getSpaceInformations() {
        return getSpaceInformationsSet();
    }

    @Deprecated
    public boolean hasAnySpaceInformations() {
        return !getSpaceInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation> getReimbursementGuideSituations() {
        return getReimbursementGuideSituationsSet();
    }

    @Deprecated
    public boolean hasAnyReimbursementGuideSituations() {
        return !getReimbursementGuideSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.CreditLine> getCreditLines() {
        return getCreditLinesSet();
    }

    @Deprecated
    public boolean hasAnyCreditLines() {
        return !getCreditLinesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Senior> getSeniors() {
        return getSeniorsSet();
    }

    @Deprecated
    public boolean hasAnySeniors() {
        return !getSeniorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.thesis.Thesis> getTheses() {
        return getThesesSet();
    }

    @Deprecated
    public boolean hasAnyTheses() {
        return !getThesesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial> getPresentationMaterials() {
        return getPresentationMaterialsSet();
    }

    @Deprecated
    public boolean hasAnyPresentationMaterials() {
        return !getPresentationMaterialsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DomainObjectActionLogEntry> getDomainObjectActionLogEntries() {
        return getDomainObjectActionLogEntriesSet();
    }

    @Deprecated
    public boolean hasAnyDomainObjectActionLogEntries() {
        return !getDomainObjectActionLogEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidenceYear> getResidenceYears() {
        return getResidenceYearsSet();
    }

    @Deprecated
    public boolean hasAnyResidenceYears() {
        return !getResidenceYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.ResidenceCandidacies> getResidenceCandidaciess() {
        return getResidenceCandidaciessSet();
    }

    @Deprecated
    public boolean hasAnyResidenceCandidaciess() {
        return !getResidenceCandidaciessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getRegistrations() {
        return getRegistrationsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrations() {
        return !getRegistrationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ImportRegisterLog> getImportRegisterLog() {
        return getImportRegisterLogSet();
    }

    @Deprecated
    public boolean hasAnyImportRegisterLog() {
        return !getImportRegisterLogSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup> getOutboundMobilityCandidacyContestGroup() {
        return getOutboundMobilityCandidacyContestGroupSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContestGroup() {
        return !getOutboundMobilityCandidacyContestGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituation> getAcademicServiceRequestSituations() {
        return getAcademicServiceRequestSituationsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequestSituations() {
        return !getAcademicServiceRequestSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Qualification> getQualifications() {
        return getQualificationsSet();
    }

    @Deprecated
    public boolean hasAnyQualifications() {
        return !getQualificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry> getShiftDistributionEntries() {
        return getShiftDistributionEntriesSet();
    }

    @Deprecated
    public boolean hasAnyShiftDistributionEntries() {
        return !getShiftDistributionEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.person.IdDocument> getIdDocuments() {
        return getIdDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyIdDocuments() {
        return !getIdDocumentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult> getGaugingTestResults() {
        return getGaugingTestResultsSet();
    }

    @Deprecated
    public boolean hasAnyGaugingTestResults() {
        return !getGaugingTestResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getEmployeeHistorics() {
        return getEmployeeHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyEmployeeHistorics() {
        return !getEmployeeHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewTestElement> getNewTestElements() {
        return getNewTestElementsSet();
    }

    @Deprecated
    public boolean hasAnyNewTestElements() {
        return !getNewTestElementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftProfessorship> getShiftProfessorships() {
        return getShiftProfessorshipsSet();
    }

    @Deprecated
    public boolean hasAnyShiftProfessorships() {
        return !getShiftProfessorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEventTicket> getInstitutionAffiliationEventTicket() {
        return getInstitutionAffiliationEventTicketSet();
    }

    @Deprecated
    public boolean hasAnyInstitutionAffiliationEventTicket() {
        return !getInstitutionAffiliationEventTicketSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingPartyHistory> getParkingPartyHistories() {
        return getParkingPartyHistoriesSet();
    }

    @Deprecated
    public boolean hasAnyParkingPartyHistories() {
        return !getParkingPartyHistoriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CandidateEnrolment> getCandidateEnrolments() {
        return getCandidateEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyCandidateEnrolments() {
        return !getCandidateEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission> getOutboundMobilityCandidacySubmission() {
        return getOutboundMobilityCandidacySubmissionSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacySubmission() {
        return !getOutboundMobilityCandidacySubmissionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidencePriceTable> getResidencePriceTables() {
        return getResidencePriceTablesSet();
    }

    @Deprecated
    public boolean hasAnyResidencePriceTables() {
        return !getResidencePriceTablesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity> getExtraCurricularActivity() {
        return getExtraCurricularActivitySet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularActivity() {
        return !getExtraCurricularActivitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ScientificCommission> getScientificCommissions() {
        return getScientificCommissionsSet();
    }

    @Deprecated
    public boolean hasAnyScientificCommissions() {
        return !getScientificCommissionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation> getPersonProfessionalRelations() {
        return getPersonProfessionalRelationsSet();
    }

    @Deprecated
    public boolean hasAnyPersonProfessionalRelations() {
        return !getPersonProfessionalRelationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.NewParkingDocument> getNewParkingDocuments() {
        return getNewParkingDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyNewParkingDocuments() {
        return !getNewParkingDocumentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationProcess> getPhdMigrationProcesses() {
        return getPhdMigrationProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationProcesses() {
        return !getPhdMigrationProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.Cooperation> getCooperations() {
        return getCooperationsSet();
    }

    @Deprecated
    public boolean hasAnyCooperations() {
        return !getCooperationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancy> getErasmusVacancy() {
        return getErasmusVacancySet();
    }

    @Deprecated
    public boolean hasAnyErasmusVacancy() {
        return !getErasmusVacancySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Curriculum> getCurriculums() {
        return getCurriculumsSet();
    }

    @Deprecated
    public boolean hasAnyCurriculums() {
        return !getCurriculumsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion> getInquiryGroupsQuestions() {
        return getInquiryGroupsQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGroupsQuestions() {
        return !getInquiryGroupsQuestionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation> getProjectEventAssociations() {
        return getProjectEventAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyProjectEventAssociations() {
        return !getProjectEventAssociationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getIndividualCandidacies() {
        return getIndividualCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacies() {
        return !getIndividualCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.DepartmentCreditsPool> getDepartmentCreditsPools() {
        return getDepartmentCreditsPoolsSet();
    }

    @Deprecated
    public boolean hasAnyDepartmentCreditsPools() {
        return !getDepartmentCreditsPoolsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PunctualRoomsOccupationStateInstant> getPunctualRoomsOccupationStateInstants() {
        return getPunctualRoomsOccupationStateInstantsSet();
    }

    @Deprecated
    public boolean hasAnyPunctualRoomsOccupationStateInstants() {
        return !getPunctualRoomsOccupationStateInstantsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Tutorship> getTutorships() {
        return getTutorshipsSet();
    }

    @Deprecated
    public boolean hasAnyTutorships() {
        return !getTutorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Degree> getDegrees() {
        return getDegreesSet();
    }

    @Deprecated
    public boolean hasAnyDegrees() {
        return !getDegreesSet().isEmpty();
    }

    @Deprecated
    public boolean hasInstitutionUnit() {
        return getInstitutionUnit() != null;
    }

    @Deprecated
    public boolean hasDeployNotifier() {
        return getDeployNotifier() != null;
    }

    @Deprecated
    public boolean hasRootPortal() {
        return getRootPortal() != null;
    }

    @Deprecated
    public boolean hasMostRecentRequestLogDay() {
        return getMostRecentRequestLogDay() != null;
    }

    @Deprecated
    public boolean hasDomainRoot() {
        return getDomainRoot() != null;
    }

    @Deprecated
    public boolean hasExternalInstitutionUnit() {
        return getExternalInstitutionUnit() != null;
    }

    @Deprecated
    public boolean hasIrsDeclarationLink() {
        return getIrsDeclarationLink() != null;
    }

    @Deprecated
    public boolean hasResidenceManagementUnit() {
        return getResidenceManagementUnit() != null;
    }

    @Deprecated
    public boolean hasLibraryCardSystem() {
        return getLibraryCardSystem() != null;
    }

    @Deprecated
    public boolean hasEarthUnit() {
        return getEarthUnit() != null;
    }

    @Deprecated
    public boolean hasRootModule() {
        return getRootModule() != null;
    }

    @Deprecated
    public boolean hasSystemSender() {
        return getSystemSender() != null;
    }

    @Deprecated
    public boolean hasDefaultAcademicCalendar() {
        return getDefaultAcademicCalendar() != null;
    }

    @Deprecated
    public boolean hasUsernameCounter() {
        return getUsernameCounter() != null;
    }

}
