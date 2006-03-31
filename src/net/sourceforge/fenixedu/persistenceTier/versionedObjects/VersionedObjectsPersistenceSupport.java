package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.CMSVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.MailAdressAliasVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.MailingListVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseScopeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CursoExecucaoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EnrolmentPeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExternalPersonVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GratuitySituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GuideVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.InsuranceTransactionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeProofVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeThesisDataVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ReimbursementGuideVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SentSmsVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SibsPaymentFileEntryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SupportLessonVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractRegimeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantCostCenterVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantOrientationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPartVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPaymentEntityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantSubsidyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.owner.GrantOwnerVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesCoursesResVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesSummaryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries.OldInquiriesTeachersResVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestAdvisoryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.MetadataVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.QuestionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.StudentTestLogVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.StudentTestQuestionVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

    private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();

    static {
    }

    private VersionedObjectsPersistenceSupport() {
    }

    public static VersionedObjectsPersistenceSupport getInstance() {
        return instance;
    }

    public IPersistentStudentTestLog getIPersistentStudentTestLog() {
        return new StudentTestLogVO();
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

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
        return null;
    }

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter() {
        return new GrantCostCenterVO();
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentExternalPerson getIPersistentExternalPerson() {
        return new ExternalPersonVO();
    }

    public IPersistentGrantSubsidy getIPersistentGrantSubsidy() {
        return new GrantSubsidyVO();
    }

    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime() {
        return null;
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

    public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary() {
        return new OldInquiriesSummaryVO();
    }

    public IPersistentGrantOwner getIPersistentGrantOwner() {
        return new GrantOwnerVO();
    }

    public IPersistentGuide getIPersistentGuide() {
        return new GuideVO();
    }

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory() {
        return new DistributedTestAdvisoryVO();
    }

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity() {
        return new GrantPaymentEntityVO();
    }

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute() {
        return null;
    }

    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractVO();
    }

    public IPersistentWebSiteItem getIPersistentWebSiteItem() {
        return null;
    }

    public IPersistentGratuitySituation getIPersistentGratuitySituation() {
        return new GratuitySituationVO();
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
        return new EnrolmentPeriodVO();
    }

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime() {
        return new GrantContractRegimeVO();
    }

    public IPersistentQuestion getIPersistentQuestion() {
        return new QuestionVO();
    }

    public Integer getNumberCachedItems() {
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

    public IPersistentGrantPart getIPersistentGrantPart() {
        return new GrantPartVO();
    }

    public IPersistentSupportLesson getIPersistentSupportLesson() {
        return new SupportLessonVO();
    }

    public IPersistentWebSiteSection getIPersistentWebSiteSection() {
        return null;
    }

    public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes() {
        return new OldInquiriesTeachersResVO();
    }

    public IPersistentObject getIPersistentObject() {
        return null;
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

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
        return new CurricularCourseScopeVO();
    }

    public IPersistentCategory getIPersistentCategory() {
        return null;
    }

    public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes() {
        return new OldInquiriesCoursesResVO();
    }

    public IPersistentCMS getIPersistentCms() {
        return new CMSVO();
    }

    public IPersistentMailAddressAlias getIPersistentMailAdressAlias() {
        return new MailAdressAliasVO();
    }

    public IPersistentMailingList getIPersistentMailingList() {
        return new MailingListVO();
    }

}
