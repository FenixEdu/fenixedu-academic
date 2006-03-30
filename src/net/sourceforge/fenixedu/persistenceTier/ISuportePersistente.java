package net.sourceforge.fenixedu.persistenceTier;


import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
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
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;

public interface ISuportePersistente {
    
    public void iniciarTransaccao() throws ExcepcaoPersistencia;

    public void confirmarTransaccao() throws ExcepcaoPersistencia;

    public void cancelarTransaccao() throws ExcepcaoPersistencia;

    public void clearCache();

    public Integer getNumberCachedItems();

    public IPersistentExecutionCourse getIPersistentExecutionCourse();

    public IPersistentExecutionDegree getIPersistentExecutionDegree();

    public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente();

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate();

    public IPersistentCurriculum getIPersistentCurriculum();

    public IPersistentGuideEntry getIPersistentGuideEntry();

    public IPersistentGuide getIPersistentGuide();

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope();

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod();

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage();

    public IPersistentMetadata getIPersistentMetadata();

    public IPersistentQuestion getIPersistentQuestion();

    public IPersistentTestQuestion getIPersistentTestQuestion();

    public IPersistentDistributedTest getIPersistentDistributedTest();

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion();

    public IPersistentStudentTestLog getIPersistentStudentTestLog();

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory();

    public IPersistentGrantOwner getIPersistentGrantOwner();

    public IPersistentGrantContract getIPersistentGrantContract();

    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher();

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter();

    public IPersistentGrantPart getIPersistentGrantPart();

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity();

    public IPersistentGrantSubsidy getIPersistentGrantSubsidy();

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime();

    public IPersistentWebSiteSection getIPersistentWebSiteSection();

    public IPersistentWebSiteItem getIPersistentWebSiteItem();

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion();

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion();

    public IPersistentExternalPerson getIPersistentExternalPerson();

    public IPersistentShiftProfessorship getIPersistentShiftProfessorship();

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide();

    public IPersistentOrientation getIPersistentOrientation();

    public IPersistentPublicationsNumber getIPersistentPublicationsNumber();

    public IPersistentOldPublication getIPersistentOldPublication();

    public IPersistentSupportLesson getIPersistentSupportLesson();

    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime();

    public IPersistentGratuitySituation getIPersistentGratuitySituation();

    public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine();

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork();

    public IPersistentSentSms getIPersistentSentSms();

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute();

    public IPersistentPublicationFormat getIPersistentPublicationFormat();

    public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry();

    public IPersistentObject getIPersistentObject();

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction();

    public IPersistentPublicationTeacher getIPersistentPublicationTeacher();

    public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary();

    public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes();

    public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter();

    public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests();

    public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes();

    public IPersistentCMS getIPersistentCms();

    public IPersistentMailAddressAlias getIPersistentMailAdressAlias();

    public IPersistentMailingList getIPersistentMailingList();

}
