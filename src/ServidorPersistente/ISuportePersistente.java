/*
 * Interface.java
 * 
 * Created on 19 de Agosto de 2002, 1:09
 */

package ServidorPersistente;

import ServidorPersistente.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import ServidorPersistente.Seminaries.IPersistentSeminary;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;
import ServidorPersistente.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantOwner;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;
import ServidorPersistente.grant.IPersistentGrantType;
import ServidorPersistente.guide.IPersistentReimbursementGuide;
import ServidorPersistente.guide.IPersistentReimbursementGuideSituation;
import ServidorPersistente.teacher.IPersistentCareer;
import ServidorPersistente.teacher.IPersistentCategory;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import ServidorPersistente.teacher.IPersistentOldPublication;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

/**
 * @author ars
 */
public interface ISuportePersistente
{
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
    public IPersistentEnrolment getIPersistentEnrolment();
    public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation();
    public ITurmaTurnoPersistente getITurmaTurnoPersistente();
    public ITurnoAlunoPersistente getITurnoAlunoPersistente();
    public ITurnoAulaPersistente getITurnoAulaPersistente();

    public IPersistentCurricularCourse getIPersistentCurricularCourse();
    public IDisciplinaExecucaoPersistente getIDisciplinaExecucaoPersistente();
    public IPessoaPersistente getIPessoaPersistente();
    public IPersistentCountry getIPersistentCountry();

    public ICursoPersistente getICursoPersistente();
    public ICursoExecucaoPersistente getICursoExecucaoPersistente();

    public IPersistentStudent getIPersistentStudent();
    public IPersistentBibliographicReference getIPersistentBibliographicReference();

    public IPersistentDepartment getIDepartamentoPersistente();
    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente();
    public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan();
    public IStudentCurricularPlanPersistente getIStudentCurricularPlanPersistente();

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
    public IPersistentPossibleCurricularCourseForOptionalCurricularCourse getIPersistentChosenCurricularCourseForOptionalCurricularCourse();

    public IPersistentStudentKind getIPersistentStudentKind();

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage();
    public IPersistentCreditsTeacher getIPersistentCreditsTeacher();

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

    public IPersistentCurricularCourseEquivalenceRestriction getIPersistentCurricularCourseEquivalenceRestriction();

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

    // by Barbosa (October 28th, 2003)
    public IPersistentGrantOwner getIPersistentGrantOwner();
    //by Barbosa (November 18th, 2003)
    public IPersistentGrantContract getIPersistentGrantContract();
    //by Barbosa (November 19th, 2003)
    public IPersistentGrantType getIPersistentGrantType();
    //by Barbosa (November 20th, 2003)
    public IPersistentGrantResponsibleTeacher getIPersistentGrantResponsibleTeacher();
    //by Barbosa (November 20th, 2003)
    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher();

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
    public IPersistentReimbursementGuideSituation getIPersistentReimbursementGuideSituation();
    public IPersistentOrientation getIPersistentOrientation();
    public IPersistentPublicationsNumber getIPersistentPublicationsNumber();
    public IPersistentOldPublication getIPersistentOldPublication();
    public IPersistentGaugingTestResult getIPersistentGaugingTestResult();

    public IPersistentSupportLesson getIPersistentSupportLesson();

    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent();
	public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime();
  
}