/*
 * Interface.java
 * 
 * Created on 19 de Agosto de 2002, 1:09
 */

package net.sourceforge.fenixedu.persistenceTier;

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
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentSmsTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;

/**
 * @author ars
 */
public interface ISuportePersistente {
    public void iniciarTransaccao() throws ExcepcaoPersistencia;

    public void confirmarTransaccao() throws ExcepcaoPersistencia;

    public void cancelarTransaccao() throws ExcepcaoPersistencia;

    public void clearCache();

    public Integer getNumberCachedItems();

    //public IPessoaPersistente getIPessoaPersistente();

    public IAulaPersistente getIAulaPersistente();

    public ISalaPersistente getISalaPersistente();

    public ITurmaPersistente getITurmaPersistente();

    public ITurnoPersistente getITurnoPersistente();

    public IFrequentaPersistente getIFrequentaPersistente();

    public IPersistentEnrollment getIPersistentEnrolment();

    public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation();

    public ITurmaTurnoPersistente getITurmaTurnoPersistente();

    public ITurnoAlunoPersistente getITurnoAlunoPersistente();

    //	public ITurnoAulaPersistente getITurnoAulaPersistente();

    public IPersistentCurricularCourse getIPersistentCurricularCourse();

    public IPersistentExecutionCourse getIPersistentExecutionCourse();

    public IPessoaPersistente getIPessoaPersistente();

    public IPersistentCountry getIPersistentCountry();

    public ICursoPersistente getICursoPersistente();

    public IPersistentExecutionDegree getIPersistentExecutionDegree();

    public IPersistentStudent getIPersistentStudent();
    
    public IPersistentSenior getIPersistentSenior();

    public IPersistentBibliographicReference getIPersistentBibliographicReference();

    public IPersistentDepartment getIDepartamentoPersistente();

    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente();

    public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan();

    public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente();

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate();

    public IPersistentCandidateSituation getIPersistentCandidateSituation();

    public IPersistentExecutionPeriod getIPersistentExecutionPeriod();

    public IPersistentExecutionYear getIPersistentExecutionYear();

    public IPersistentSite getIPersistentSite();

    public IPersistentSection getIPersistentSection();

    public IPersistentItem getIPersistentItem();

    public IPersistentAnnouncement getIPersistentAnnouncement();

    public IPersistentCurriculum getIPersistentCurriculum();

    public IPersistentEvaluationMethod getIPersistentEvaluationMethod();

    public IPersistentTeacher getIPersistentTeacher();

    public IPersistentExam getIPersistentExam();

    public IPersistentWrittenTest getIPersistentWrittenTest();

    public IPersistentWrittenEvaluationCurricularCourseScope getIPersistentWrittenEvaluationCurricularCourseScope();

    public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse();

    public IPersistentBranch getIPersistentBranch();

    public IPersistentCurricularYear getIPersistentCurricularYear();

    public IPersistentContributor getIPersistentContributor();

    public IPersistentCurricularSemester getIPersistentCurricularSemester();

    public IPersistentEnrolmentEquivalence getIPersistentEnrolmentEquivalence();

    public IPersistentProfessorship getIPersistentProfessorship();

    public IPersistentResponsibleFor getIPersistentResponsibleFor();

    public IPersistentPrice getIPersistentPrice();

    public IPersistentGuideEntry getIPersistentGuideEntry();

    public IPersistentGuide getIPersistentGuide();

    public IPersistentGuideSituation getIPersistentGuideSituation();

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope();

    public IPersistentRole getIPersistentRole();

    public IPersistentPersonRole getIPersistentPersonRole();

    public IPersistentPrecedence getIPersistentPrecedence();

    public IPersistentRestriction getIPersistentRestriction();

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod();

    public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo();

    public IPersistentStudentKind getIPersistentStudentKind();

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage();

    public IPersistentExamStudentRoom getIPersistentExamStudentRoom();

    public IPersistentMark getIPersistentMark();

    public IPersistentEvaluation getIPersistentEvaluation();

    public IPersistentEvaluationExecutionCourse getIPersistentEvaluationExecutionCourse();

    public IPersistentEmployee getIPersistentEmployee();

    public IPersistentEquivalentEnrolmentForEnrolmentEquivalence getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

    public IPersistentSummary getIPersistentSummary();

    public IPersistentQualification getIPersistentQualification();

    public IPersistentCandidateEnrolment getIPersistentCandidateEnrolment();

    public IPersistentGratuity getIPersistentGratuity();

    public IPersistentStudentGroup getIPersistentStudentGroup();

    public IPersistentStudentGroupAttend getIPersistentStudentGroupAttend();

    public IPersistentGroupProperties getIPersistentGroupProperties();

    public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence();

    //by gedl AT rnl DOT ist DOT utl DOT pt (July the 25th, 2003)
    public IPersistentSeminaryModality getIPersistentSeminaryModality();

    //by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminaryTheme getIPersistentSeminaryTheme();

    //    by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminary getIPersistentSeminary();

    //    by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy();

    //    by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
    public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy();

    //    by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
    public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice();

    //  by gedl AT rnl DOT ist DOT utl DOT pt (August the 4th, 2003)
    public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency();

    public IPersistentMetadata getIPersistentMetadata();

    public IPersistentQuestion getIPersistentQuestion();

    public IPersistentTest getIPersistentTest();

    public IPersistentTestQuestion getIPersistentTestQuestion();

    public IPersistentDistributedTest getIPersistentDistributedTest();

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion();

    public IPersistentStudentTestLog getIPersistentStudentTestLog();

    public IPersistentOnlineTest getIPersistentOnlineTest();

    public IPersistentTestScope getIPersistentTestScope();

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory();

    // by Barbosa (October 28th, 2003)
    public IPersistentGrantOwner getIPersistentGrantOwner();

    //by Barbosa (November 18th, 2003)
    public IPersistentGrantContract getIPersistentGrantContract();

    //by Barbosa (November 19th, 2003)
    public IPersistentGrantType getIPersistentGrantType();

    //by Barbosa (November 20th, 2003)
    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher();

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter();

    public IPersistentGrantPart getIPersistentGrantPart();

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity();

    public IPersistentGrantProject getIPersistentGrantProject();

    public IPersistentGrantSubsidy getIPersistentGrantSubsidy();

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime();

    public IPersistentGrantInsurance getIPersistentGrantInsurance();

    public IPersistentGrantContractMovement getIPersistentGrantContractMovement();

    public IPersistentAdvisory getIPersistentAdvisory();

    public IPersistentWebSite getIPersistentWebSite();

    public IPersistentWebSiteSection getIPersistentWebSiteSection();

    public IPersistentWebSiteItem getIPersistentWebSiteItem();

    public IPersistentMasterDegreeThesis getIPersistentMasterDegreeThesis();

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion();

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion();

    public IPersistentExternalPerson getIPersistentExternalPerson();

    public IPersistentCoordinator getIPersistentCoordinator();

    public IPersistentDegreeInfo getIPersistentDegreeInfo();

    public IPersistentUniversity getIPersistentUniversity();

    public IPersistentCourseReport getIPersistentCourseReport();

    public IPersistentCategory getIPersistentCategory();

    public IPersistentCareer getIPersistentCareer();

    public IPersistentWeeklyOcupation getIPersistentWeeklyOcupation();

    public IPersistentExternalActivity getIPersistentExternalActivity();

    public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime();

    public IPersistentShiftProfessorship getIPersistentShiftProfessorship();

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide();

    public IPersistentReimbursementGuideEntry getIPersistentReimbursementGuideEntry();

    public IPersistentOrientation getIPersistentOrientation();

    public IPersistentPublicationsNumber getIPersistentPublicationsNumber();

    public IPersistentOldPublication getIPersistentOldPublication();

    public IPersistentGaugingTestResult getIPersistentGaugingTestResult();

    public IPersistentSupportLesson getIPersistentSupportLesson();

    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent();

    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime();

    /**
     * @return
     */
    public IPersistentCampus getIPersistentCampus();

    public IPersistentWorkLocation getIPersistentWorkLocation();

    //Nuno Correia & Ricardo Rodrigues
    public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup();

    public IPersistentScientificArea getIPersistentScientificArea();

    public IPersistentGratuityValues getIPersistentGratuityValues();

    public IPersistentGratuitySituation getIPersistentGratuitySituation();

    public IPersistentPaymentPhase getIPersistentPaymentPhase();

    public IPersistentCreditsInAnySecundaryArea getIPersistentCreditsInAnySecundaryArea();

    public IPersistentCreditsInSpecificScientificArea getIPersistentCreditsInSpecificScientificArea();

    public IPersistentTutor getIPersistentTutor();

    public IPersistentCourseHistoric getIPersistentCourseHistoric();

    public IPersistentStudentCourseReport getIPersistentStudentCourseReport();

    public IPersistentDelegate getIPersistentDelegate();

    public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine();

    public IPersistentServiceExemptionCreditLine getIPersistentServiceExemptionCreditLine();

    public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine();

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork();

    public IPersistentSentSms getIPersistentSentSms();

    //	Ana e Ricardo
    public IPersistentRoomOccupation getIPersistentRoomOccupation();

    public IPersistentPeriod getIPersistentPeriod();

    // Nuno Correia & Ricardo Rodrigues
    public IPersistentPersonalDataUseInquiryAnswers getIPersistentPersonalDataUseInquiryAnswers();

    //	TJBF & PFON
    public IPersistentPublication getIPersistentPublication();

    public IPersistentPublicationType getIPersistentPublicationType();

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute();

    public IPersistentPublicationFormat getIPersistentPublicationFormat();

    public IPersistentAuthor getIPersistentAuthor();

    public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile();

    public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry();

    public IPersistentResidenceCandidacies getIPersistentResidenceCandidacies();

    public IPersistentGratuityTransaction getIPersistentGratuityTransaction();

    public IPersistentObject getIPersistentObject();

    public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction();

    public IPersistentSmsTransaction getIPersistentSmsTransaction();

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction();

    public IPersistentPersonAccount getIPersistentPersonAccount();

    public IPersistentTransaction getIPersistentTransaction();
    
    public IPersistentPaymentTransaction getIPersistentPaymentTransaction();

    public IPersistentInsuranceValue getIPersistentInsuranceValue();

    public IPersistentFAQSection getIPersistentFAQSection();

    public IPersistentFAQEntries getIPersistentFAQEntries();

    public IPersistentGlossaryEntries getIPersistentGlossaryEntries();
    
    // Ricardo Rodrigues
    public IPersistentPublicationAuthor getIPersistentPublicationAuthor();

	public IPersistentPublicationTeacher getIPersistentPublicationTeacher();

    public IPersistentAttendInAttendsSet getIPersistentAttendInAttendsSet();

    public IPersistentAttendsSet getIPersistentAttendsSet();

    public IPersistentGroupPropertiesExecutionCourse getIPersistentGroupPropertiesExecutionCourse();
    
    // Rita Ferreira e João Fialho
    public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary();
    
    public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes();
        
    //
    public IPersistentCostCenter getIPersistentCostCenter();
    
    public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter();
    
    public IPersistentExtraWork getIPersistentExtraWork();
        
    public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests();
    
    public IPersistentExtraWorkCompensation getIPersistentExtraWorkCompensation();

    public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric();
    
    public IPersistentSecretaryEnrolmentStudent getIPersistentSecretaryEnrolmentStudent();
    
    public IPersistentProjectAccess getIPersistentProjectAccess();

    public IPersistentBuilding getIPersistentBuilding();
}