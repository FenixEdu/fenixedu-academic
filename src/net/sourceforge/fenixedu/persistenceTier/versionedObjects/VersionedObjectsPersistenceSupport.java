package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import sun.security.krb5.internal.ktab.ay;
import net.sourceforge.fenixedu.persistenceTier.*;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentStudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantProject;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWork;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkCompensation;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentSmsTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AdvisoryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AnnouncementVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AttendInAttendsSetVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AttendsSetVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AulaVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.BibliographicReferenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SummaryVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

	private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();


	static {
	}

	private VersionedObjectsPersistenceSupport() {}

	public static VersionedObjectsPersistenceSupport getInstance() {
		return instance;
	}

	public IPersistentBibliographicReference getIPersistentBibliographicReference()  {
		return new BibliographicReferenceVO();
	}

	public IPersistentStudentGroup getIPersistentStudentGroup()  {
		return null;
	}

	public IPersistentGratuityTransaction getIPersistentGratuityTransaction()  {
		return null;
	}

	public IPersistentTestScope getIPersistentTestScope()  {
		return null;
	}

	public IPersistentCareer getIPersistentCareer()  {
		return null;
	}

	public IPessoaPersistente getIPessoaPersistente()  {
		return null;
	}

	public IPersistentStudentGroupAttend getIPersistentStudentGroupAttend()  {
		return null;
	}

	public IPersistentPrice getIPersistentPrice()  {
		return null;
	}

	public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine()  {
		return null;
	}

	public IPersistentScientificArea getIPersistentScientificArea()  {
		return null;
	}

	public IPersistentStudentTestLog getIPersistentStudentTestLog()  {
		return null;
	}

	public IPersistentPublicationsNumber getIPersistentPublicationsNumber()  {
		return null;
	}

	public IPersistentEvaluationMethod getIPersistentEvaluationMethod()  {
		return null;
	}

	public IPersistentResponsibleFor getIPersistentResponsibleFor()  {
		return null;
	}

	public IPersistentWrittenEvaluationCurricularCourseScope getIPersistentWrittenEvaluationCurricularCourseScope()  {
		return null;
	}

	public IPersistentPublicationAuthor getIPersistentPublicationAuthor()  {
		return null;
	}

	public IPersistentExam getIPersistentExam()  {
		return null;
	}

	public IPersistentServiceExemptionCreditLine getIPersistentServiceExemptionCreditLine()  {
		return null;
	}

	public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse()  {
		return null;
	}

	public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction()  {
		return null;
	}

	public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher()  {
		return null;
	}

	public IPersistentReimbursementGuide getIPersistentReimbursementGuide()  {
		return null;
	}

	public IPersistentExamStudentRoom getIPersistentExamStudentRoom()  {
		return null;
	}

	public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion()  {
		return null;
	}

	public IPersistentPublication getIPersistentPublication()  {
		return new PublicationVO();
	}

	public IPersistentMasterDegreeThesis getIPersistentMasterDegreeThesis()  {
		return null;
	}

	public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente()  {
		return null;
	}

	public IPersistentBranch getIPersistentBranch()  {
		return null;
	}

	public IPersistentCountry getIPersistentCountry()  {
		return null;
	}

	public IPersistentOrientation getIPersistentOrientation()  {
		return null;
	}

	public IPersistentGrantContractMovement getIPersistentGrantContractMovement()  {
		return null;
	}

	public IPersistentWrittenTest getIPersistentWrittenTest()  {
		return null;
	}

	public IPersistentPublicationType getIPersistentPublicationType()  {
		return null;
	}

	public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice()  {
		return null;
	}

	public IPersistentAdvisory getIPersistentAdvisory()  {
		return new AdvisoryVO();
	}

	public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate()  {
		return null;
	}

	public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy()  {
		return null;
	}

	public IPersistentEvaluation getIPersistentEvaluation()  {
		return null;
	}

	public IPersistentSmsTransaction getIPersistentSmsTransaction()  {
		return null;
	}

	public IPersistentTutor getIPersistentTutor()  {
		return null;
	}

	public IPersistentWebSite getIPersistentWebSite()  {
		return null;
	}

	public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente()  {
		return null;
	}

	public IPersistentSeminaryModality getIPersistentSeminaryModality()  {
		return null;
	}

	public IPersistentAttendsSet getIPersistentAttendsSet()  {
		return new AttendsSetVO();
	}

	public IPersistentRole getIPersistentRole()  {
		return null;
	}

	public IPersistentCreditsInAnySecundaryArea getIPersistentCreditsInAnySecundaryArea()  {
		return null;
	}

	public IPersistentGrantInsurance getIPersistentGrantInsurance()  {
		return null;
	}

	public ITurnoPersistente getITurnoPersistente()  {
		return null;
	}

	public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan()  {
		return null;
	}

	public IPersistentDelegate getIPersistentDelegate()  {
		return null;
	}

	public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup()  {
		return null;
	}

	public ITurmaTurnoPersistente getITurmaTurnoPersistente()  {
		return null;
	}

	public IPersistentWorkLocation getIPersistentWorkLocation()  {
		return null;
	}

	public IPersistentUniversity getIPersistentUniversity()  {
		return null;
	}

	public IPersistentCourseHistoric getIPersistentCourseHistoric()  {
		return null;
	}

	public IPersistentGrantProject getIPersistentGrantProject()  {
		return null;
	}

	public IPersistentPaymentTransaction getIPersistentPaymentTransaction()  {
		return null;
	}

	public IPersistentCostCenter getIPersistentCostCenter()  {
		return null;
	}

	public IPersistentGrantCostCenter getIPersistentGrantCostCenter()  {
		return null;
	}

	public IPersistentInsuranceValue getIPersistentInsuranceValue()  {
		return null;
	}

	public IPersistentGlossaryEntries getIPersistentGlossaryEntries()  {
		return null;
	}

	public IPersistentCurricularSemester getIPersistentCurricularSemester()  {
		return null;
	}

	public IFrequentaPersistente getIFrequentaPersistente()  {
		return null;
	}

	public ISalaPersistente getISalaPersistente()  {
		return null;
	}

	public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation()  {
		return null;
	}

	public void confirmarTransaccao() throws ExcepcaoPersistencia {
		return ;
	}

	public void iniciarTransaccao() throws ExcepcaoPersistencia {
		return ;
	}

	public IPersistentPeriod getIPersistentPeriod()  {
		return null;
	}

	public IPersistentCreditsInSpecificScientificArea getIPersistentCreditsInSpecificScientificArea()  {
		return null;
	}

	public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric()  {
		return null;
	}

	public ITurnoAlunoPersistente getITurnoAlunoPersistente()  {
		return null;
	}

	public IPersistentExternalPerson getIPersistentExternalPerson()  {
		return null;
	}

	public IPersistentPaymentPhase getIPersistentPaymentPhase()  {
		return null;
	}

	public IPersistentStudent getIPersistentStudent()  {
		return null;
	}

	public IPersistentResidenceCandidacies getIPersistentResidenceCandidacies()  {
		return null;
	}

	public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile()  {
		return null;
	}

	public IPersistentGrantSubsidy getIPersistentGrantSubsidy()  {
		return null;
	}

	public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime()  {
		return null;
	}

	public IPersistentProjectAccess getIPersistentProjectAccess()  {
		return null;
	}

	public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine()  {
		return null;
	}

	public IPersistentShiftProfessorship getIPersistentShiftProfessorship()  {
		return null;
	}

	public IPersistentMetadata getIPersistentMetadata()  {
		return null;
	}

	public IPersistentBuilding getIPersistentBuilding()  {
		return null;
	}

	public IPersistentSentSms getIPersistentSentSms()  {
		return null;
	}

	public IPersistentGaugingTestResult getIPersistentGaugingTestResult()  {
		return null;
	}

	public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary()  {
		return null;
	}

	public IPersistentGrantOwner getIPersistentGrantOwner()  {
		return null;
	}

	public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime()  {
		return null;
	}

	public IPersistentGuide getIPersistentGuide()  {
		return null;
	}

	public IPersistentSeminaryTheme getIPersistentSeminaryTheme()  {
		return null;
	}

	public IPersistentGuideSituation getIPersistentGuideSituation()  {
		return null;
	}

	public IPersistentGratuityValues getIPersistentGratuityValues()  {
		return null;
	}

	public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory()  {
		return null;
	}

	public IPersistentTest getIPersistentTest()  {
		return null;
	}

	public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity()  {
		return null;
	}

	public IPersistentPersonalDataUseInquiryAnswers getIPersistentPersonalDataUseInquiryAnswers()  {
		return null;
	}

	public IPersistentPublicationAttribute getIPersistentPublicationAttribute()  {
		return null;
	}

	public IPersistentPublicationTeacher getIPersistentPublicationTeacher()  {
		return null;
	}

	public IPersistentCampus getIPersistentCampus()  {
		return null;
	}

	public IPersistentCurricularCourse getIPersistentCurricularCourse()  {
		return null;
	}

	public IPersistentGuideEntry getIPersistentGuideEntry()  {
		return null;
	}

	public IPersistentGrantContract getIPersistentGrantContract()  {
		return null;
	}

	public IPersistentTeacher getIPersistentTeacher()  {
		return null;
	}

	public IPersistentEnrolmentEquivalence getIPersistentEnrolmentEquivalence()  {
		return null;
	}

	public ITurmaPersistente getITurmaPersistente()  {
		return null;
	}

	public IPersistentSummary getIPersistentSummary()  {
		return new SummaryVO();
	}

	public IPersistentSite getIPersistentSite()  {
		return null;
	}

	public IPersistentFAQSection getIPersistentFAQSection()  {
		return null;
	}

	public IPersistentWebSiteItem getIPersistentWebSiteItem()  {
		return null;
	}

	public IPersistentQualification getIPersistentQualification()  {
		return null;
	}

	public IPersistentGratuitySituation getIPersistentGratuitySituation()  {
		return null;
	}

	public IPersistentOnlineTest getIPersistentOnlineTest()  {
		return null;
	}

	public IPersistentWeeklyOcupation getIPersistentWeeklyOcupation()  {
		return null;
	}

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod()  {
		return null;
	}

	public IPersistentExtraWorkCompensation getIPersistentExtraWorkCompensation()  {
		return null;
	}

	public IPersistentGrantContractRegime getIPersistentGrantContractRegime()  {
		return null;
	}

	public IPersistentPersonRole getIPersistentPersonRole()  {
		return null;
	}

	public IPersistentItem getIPersistentItem()  {
		return null;
	}

	public IPersistentRestriction getIPersistentRestriction()  {
		return null;
	}

	public IPersistentQuestion getIPersistentQuestion()  {
		return null;
	}

	public IPersistentCourseReport getIPersistentCourseReport()  {
		return null;
	}

	public IPersistentCandidateSituation getIPersistentCandidateSituation()  {
		return new CandidateSituationVO();
	}

	public Integer getNumberCachedItems()  {
		return null;
	}

	public IPersistentStudentKind getIPersistentStudentKind()  {
		return null;
	}

	public IPersistentSeminary getIPersistentSeminary()  {
		return null;
	}

	public IPersistentGrantType getIPersistentGrantType()  {
		return null;
	}

	public IPersistentGroupPropertiesExecutionCourse getIPersistentGroupPropertiesExecutionCourse()  {
		return null;
	}

	public IAulaPersistente getIAulaPersistente()  {
		return new AulaVO();
	}

	public IPersistentAttendInAttendsSet getIPersistentAttendInAttendsSet()  {
		return new AttendInAttendsSetVO();
	}

	public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry()  {
		return null;
	}

	public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork()  {
		return null;
	}

	public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction()  {
		return null;
	}

	public IPersistentEquivalentEnrolmentForEnrolmentEquivalence getIPersistentEquivalentEnrolmentForEnrolmentEquivalence()  {
		return null;
	}

	public IPersistentGroupProperties getIPersistentGroupProperties()  {
		return null;
	}

	public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion()  {
		return null;
	}

	public IPersistentCurriculum getIPersistentCurriculum()  {
		return null;
	}

	public IPersistentPublicationFormat getIPersistentPublicationFormat()  {
		return null;
	}

	public void cancelarTransaccao() throws ExcepcaoPersistencia {
		return ;
	}

	public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion()  {
		return null;
	}

	public IPersistentEnrollment getIPersistentEnrolment()  {
		return null;
	}

	public IPersistentSection getIPersistentSection()  {
		return null;
	}

	public void clearCache()  {
		return ;
	}

	public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo()  {
		return null;
	}

	public IPersistentExternalActivity getIPersistentExternalActivity()  {
		return null;
	}

	public IPersistentReimbursementGuideEntry getIPersistentReimbursementGuideEntry()  {
		return null;
	}

	public IPersistentGrantPart getIPersistentGrantPart()  {
		return null;
	}

	public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy()  {
		return null;
	}

	public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency()  {
		return null;
	}

	public IPersistentStudentCourseReport getIPersistentStudentCourseReport()  {
		return null;
	}

	public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter()  {
		return null;
	}

	public IPersistentSupportLesson getIPersistentSupportLesson()  {
		return null;
	}

	public IPersistentTransaction getIPersistentTransaction()  {
		return null;
	}

	public IPersistentCoordinator getIPersistentCoordinator()  {
		return null;
	}

	public ICursoPersistente getICursoPersistente()  {
		return null;
	}

	public IPersistentCurricularYear getIPersistentCurricularYear()  {
		return null;
	}

	public IPersistentWebSiteSection getIPersistentWebSiteSection()  {
		return null;
	}

	public IPersistentPersonAccount getIPersistentPersonAccount()  {
		return null;
	}

	public IPersistentExecutionYear getIPersistentExecutionYear()  {
		return null;
	}

	public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes()  {
		return null;
	}

	public IPersistentFAQEntries getIPersistentFAQEntries()  {
		return null;
	}

	public IPersistentObject getIPersistentObject()  {
		return null;
	}

	public IPersistentProfessorship getIPersistentProfessorship()  {
		return null;
	}

	public IPersistentSenior getIPersistentSenior()  {
		return null;
	}

	public IPersistentEvaluationExecutionCourse getIPersistentEvaluationExecutionCourse()  {
		return null;
	}

	public IPersistentExtraWork getIPersistentExtraWork()  {
		return null;
	}

	public IPersistentAnnouncement getIPersistentAnnouncement()  {
		return new AnnouncementVO();
	}

	public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests()  {
		return null;
	}

	public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent()  {
		return null;
	}

	public IPersistentContributor getIPersistentContributor()  {
		return null;
	}

	public IPersistentSecretaryEnrolmentStudent getIPersistentSecretaryEnrolmentStudent()  {
		return null;
	}

	public IPersistentDepartment getIDepartamentoPersistente()  {
		return null;
	}

	public IPersistentMark getIPersistentMark()  {
		return null;
	}

	public IPersistentExecutionPeriod getIPersistentExecutionPeriod()  {
		return null;
	}

	public IPersistentExecutionCourse getIPersistentExecutionCourse()  {
		return null;
	}

	public IPersistentExecutionDegree getIPersistentExecutionDegree()  {
		return null;
	}

	public IPersistentPrecedence getIPersistentPrecedence()  {
		return null;
	}

	public IPersistentAuthor getIPersistentAuthor()  {
		return null;
	}

	public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage()  {
		return null;
	}

	public IPersistentDistributedTest getIPersistentDistributedTest()  {
		return null;
	}

	public IPersistentOldPublication getIPersistentOldPublication()  {
		return null;
	}

	public IPersistentCandidateEnrolment getIPersistentCandidateEnrolment()  {
		return null;
	}

	public IPersistentDegreeInfo getIPersistentDegreeInfo()  {
		return new DegreeInfoVO();
	}

	public IPersistentEmployee getIPersistentEmployee()  {
		return null;
	}

	public IPersistentTestQuestion getIPersistentTestQuestion()  {
		return null;
	}

	public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence()  {
		return null;
	}

	public IPersistentRoomOccupation getIPersistentRoomOccupation()  {
		return null;
	}

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope()  {
		return null;
	}

	public IPersistentCategory getIPersistentCategory()  {
		return null;
	}

    public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes() {
        return null;
    }

    public IPersistentInstitution getIPersistentInstitution() {
        return null;
    }
    
    public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher() {
        return null;
    }

}
