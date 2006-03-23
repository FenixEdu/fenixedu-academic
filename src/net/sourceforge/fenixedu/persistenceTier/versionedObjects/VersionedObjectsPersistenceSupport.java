package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrice;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentOnlineTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestScope;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.CMSVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.MailAdressAliasVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.MailingListVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AuthorshipVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CandidateSituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CoordinatorVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseGroupVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseScopeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurriculumVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CursoExecucaoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EnrolmentPeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExamVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExecutionCourseVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExecutionPeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExecutionYearVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExternalPersonVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GratuitySituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GratuityTransactionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GuideVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.InsuranceTransactionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeProofVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeThesisDataVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PessoaVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PriceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationTypeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationsNumberVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ReimbursementGuideVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ReimbursementTransactionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.RestrictionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SentSmsVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SibsPaymentFileEntryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SibsPaymentFileVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.StudentCurricularPlanVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.StudentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SummaryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SupportLessonVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits.ManagementPositionCreditLineVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits.OtherTypeCreditLineVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.degree.finalProject.TeacherDegreeFinalProjectStudentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractMovementVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractRegimeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantCostCenterVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantInsuranceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantOrientationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPartVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPaymentEntityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantSubsidyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantTypeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.owner.GrantOwnerVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.InquiriesRegistryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesCoursesResVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesSummaryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesTeachersResVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestAdvisoryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.MetadataVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.OnlineTestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.QuestionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.StudentTestLogVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.StudentTestQuestionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.TestQuestionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.TestScopeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.TestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.projectsManagement.ProjectAccessVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.CandidacyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.CaseStudyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.EquivalencyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.ModalityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.SeminaryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher.ExternalActivityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher.OldPublicationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher.OrientationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher.ServiceProviderRegimeVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

    private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();

    static {
    }

    private VersionedObjectsPersistenceSupport() {
    }

    public static VersionedObjectsPersistenceSupport getInstance() {
        return instance;
    }

    public IPersistentGratuityTransaction getIPersistentGratuityTransaction() {
        return new GratuityTransactionVO();
    }

    public IPersistentTestScope getIPersistentTestScope() {
        return new TestScopeVO();
    }

    public IPessoaPersistente getIPessoaPersistente() {
        return new PessoaVO();
    }

    public IPersistentPrice getIPersistentPrice() {
        return new PriceVO();
    }

    public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine() {
        return new OtherTypeCreditLineVO();
    }

    public IPersistentStudentTestLog getIPersistentStudentTestLog() {
        return new StudentTestLogVO();
    }

    public IPersistentPublicationsNumber getIPersistentPublicationsNumber() {
        return new PublicationsNumberVO();
    }

    public IPersistentExam getIPersistentExam() {
        return new ExamVO();
    }

    public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction() {
        return new ReimbursementTransactionVO();
    }

    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher() {
        return new GrantOrientationTeacherVO();
    }

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide() {
        return new ReimbursementGuideVO();
    }

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion() {
        return new StudentTestQuestionVO();
    }

    public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente() {
        return new StudentCurricularPlanVO();
    }

    public IPersistentOrientation getIPersistentOrientation() {
        return new OrientationVO();
    }

    public IPersistentGrantContractMovement getIPersistentGrantContractMovement() {
        return new GrantContractMovementVO();
    }

    public IPersistentPublicationType getIPersistentPublicationType() {
        return new PublicationTypeVO();
    }

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
        return null;
    }

    public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy() {
        return new CaseStudyVO();
    }

    public IPersistentTutor getIPersistentTutor() {
        return null;
    }

    public IPersistentSeminaryModality getIPersistentSeminaryModality() {
        return new ModalityVO();
    }

    public IPersistentGrantInsurance getIPersistentGrantInsurance() {
        return new GrantInsuranceVO();
    }

    public IPersistentDelegate getIPersistentDelegate() {
        return null;
    }

    public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup() {
        return new CurricularCourseGroupVO();
    }

    public IPersistentPaymentTransaction getIPersistentPaymentTransaction() {
        return null;
    }

    public IPersistentCostCenter getIPersistentCostCenter() {
        return null;
    }

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter() {
        return new GrantCostCenterVO();
    }

    public IFrequentaPersistente getIFrequentaPersistente() {
        return null;
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentPeriod getIPersistentPeriod() {
        return new PeriodVO();
    }

    public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric() {
        return null;
    }

    public IPersistentExternalPerson getIPersistentExternalPerson() {
        return new ExternalPersonVO();
    }

    public IPersistentStudent getIPersistentStudent() {
        return new StudentVO();
    }

    public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile() {
        return new SibsPaymentFileVO();
    }

    public IPersistentGrantSubsidy getIPersistentGrantSubsidy() {
        return new GrantSubsidyVO();
    }

    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime() {
        return null;
    }

    public IPersistentProjectAccess getIPersistentProjectAccess() {
        return new ProjectAccessVO();
    }

    public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine() {
        return new ManagementPositionCreditLineVO();
    }

    public IPersistentShiftProfessorship getIPersistentShiftProfessorship() {
        return null;
    }

    public IPersistentMetadata getIPersistentMetadata() {
        return new MetadataVO();
    }

    public IPersistentSentSms getIPersistentSentSms() {
        return new SentSmsVO();
    }

    public IPersistentGaugingTestResult getIPersistentGaugingTestResult() {
        return null;
    }

    public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary() {
        return new OldInquiriesSummaryVO();
    }

    public IPersistentGrantOwner getIPersistentGrantOwner() {
        return new GrantOwnerVO();
    }

    public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime() {
        return new ServiceProviderRegimeVO();
    }

    public IPersistentGuide getIPersistentGuide() {
        return new GuideVO();
    }

    public IPersistentSeminaryTheme getIPersistentSeminaryTheme() {
        return null;
    }

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory() {
        return new DistributedTestAdvisoryVO();
    }

    public IPersistentTest getIPersistentTest() {
        return new TestVO();
    }

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity() {
        return new GrantPaymentEntityVO();
    }

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute() {
        return null;
    }

    public IPersistentPublicationTeacher getIPersistentPublicationTeacher() {
        return new PublicationTeacherVO();
    }

    public IPersistentCurricularCourse getIPersistentCurricularCourse() {
        return new CurricularCourseVO();
    }

    public IPersistentGuideEntry getIPersistentGuideEntry() {
        return null;
    }

    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractVO();
    }

    public IPersistentSummary getIPersistentSummary() {
        return new SummaryVO();
    }

    public IPersistentWebSiteItem getIPersistentWebSiteItem() {
        return null;
    }

    public IPersistentGratuitySituation getIPersistentGratuitySituation() {
        return new GratuitySituationVO();
    }

    public IPersistentOnlineTest getIPersistentOnlineTest() {
        return new OnlineTestVO();
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
        return new EnrolmentPeriodVO();
    }

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime() {
        return new GrantContractRegimeVO();
    }

    public IPersistentRestriction getIPersistentRestriction() {
        return new RestrictionVO();
    }

    public IPersistentQuestion getIPersistentQuestion() {
        return new QuestionVO();
    }

    public IPersistentCandidateSituation getIPersistentCandidateSituation() {
        return new CandidateSituationVO();
    }

    public Integer getNumberCachedItems() {
        return null;
    }

    public IPersistentSeminary getIPersistentSeminary() {
        return new SeminaryVO();
    }

    public IPersistentGrantType getIPersistentGrantType() {
        return new GrantTypeVO();
    }

    public IPersistentExportGrouping getIPersistentExportGrouping() {
        return null;
    }

    public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry() {
        return new SibsPaymentFileEntryVO();
    }

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork() {
        return null;
    }

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction() {
        return new InsuranceTransactionVO();
    }

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion() {
        return new MasterDegreeProofVersionVO();
    }

    public IPersistentCurriculum getIPersistentCurriculum() {
        return new CurriculumVO();
    }

    public IPersistentPublicationFormat getIPersistentPublicationFormat() {
        return null;
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion() {
        return new MasterDegreeThesisDataVersionVO();
    }

    public void clearCache() {
        return;
    }

    public IPersistentExternalActivity getIPersistentExternalActivity() {
        return new ExternalActivityVO();
    }

    public IPersistentGrantPart getIPersistentGrantPart() {
        return new GrantPartVO();
    }

    public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy() {
        return new CandidacyVO();
    }

    public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency() {
        return new EquivalencyVO();
    }

    public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter() {
        return null;
    }

    public IPersistentSupportLesson getIPersistentSupportLesson() {
        return new SupportLessonVO();
    }

    public IPersistentCoordinator getIPersistentCoordinator() {
        return new CoordinatorVO();
    }

    public IPersistentWebSiteSection getIPersistentWebSiteSection() {
        return null;
    }

    public IPersistentExecutionYear getIPersistentExecutionYear() {
        return new ExecutionYearVO();
    }

    public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes() {
        return new OldInquiriesTeachersResVO();
    }

    public IPersistentObject getIPersistentObject() {
        return null;
    }

    public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests() {
        return null;
    }

    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent() {
        return new TeacherDegreeFinalProjectStudentVO();
    }

    public IPersistentMark getIPersistentMark() {
        return null;
    }

    public IPersistentExecutionPeriod getIPersistentExecutionPeriod() {
        return new ExecutionPeriodVO();
    }

    public IPersistentExecutionCourse getIPersistentExecutionCourse() {
        return new ExecutionCourseVO();
    }

    public IPersistentExecutionDegree getIPersistentExecutionDegree() {
        return new CursoExecucaoVO();
    }

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage() {
        return null;
    }

    public IPersistentDistributedTest getIPersistentDistributedTest() {
        return new DistributedTestVO();
    }

    public IPersistentOldPublication getIPersistentOldPublication() {
        return new OldPublicationVO();
    }

    public IPersistentTestQuestion getIPersistentTestQuestion() {
        return new TestQuestionVO();
    }

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
        return new CurricularCourseScopeVO();
    }

    public IPersistentCategory getIPersistentCategory() {
        return null;
    }

    public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes() {
        return new OldInquiriesCoursesResVO();
    }

    public IPersistentInquiriesRegistry getIPersistentInquiriesRegistry() {
        return new InquiriesRegistryVO();
    }

    public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher() {
        return null;
    }
    
	public IPersistentAuthorship getIPersistentAuthorship() {
        return new AuthorshipVO();
    }
   
	public IPersistentCMS getIPersistentCms()
	{
		return new CMSVO();
	}

	public IPersistentMailAddressAlias getIPersistentMailAdressAlias()
	{
		return new MailAdressAliasVO();
	}
	
	public IPersistentMailingList getIPersistentMailingList()
	{
		return new MailingListVO();
	}	
	    
}
