/*
 * Interface.java
 *
 * Created on 19 de Agosto de 2002, 1:09
 */

package ServidorPersistente;

import ServidorPersistente.OJB.IPersistentChosenCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.OJB.IPersistentEnrolmentPeriod;


/**
 *
 * @author  ars
 */
public interface ISuportePersistente {
	public void iniciarTransaccao() throws ExcepcaoPersistencia;
	public void confirmarTransaccao() throws ExcepcaoPersistencia;
	public void cancelarTransaccao() throws ExcepcaoPersistencia;

	//public IPessoaPersistente getIPessoaPersistente();

	public IAulaPersistente getIAulaPersistente();
	public ISalaPersistente getISalaPersistente();
	public ITurmaPersistente getITurmaPersistente();
	public ITurnoPersistente getITurnoPersistente();

	public IFrequentaPersistente getIFrequentaPersistente();
	public IPersistentEnrolment getIPersistentEnrolment();
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

	public IDepartamentoPersistente getIDepartamentoPersistente();
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

	public IPersistentTeacher getIPersistentTeacher();

	public IPersistentExam getIPersistentExam();
	public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse();

	public IPersistentBranch getIPersistentBranch();

	public IPersistentCurricularYear getIPersistentCurricularYear();
	public IPersistentContributor getIPersistentContributor();

	public IPersistentCurricularSemester getIPersistentCurricularSemester();
	
	public IPersistentEquivalence getIPersistentEquivalence();
	
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
	
	public IPersistentEvaluation getIPersistentEvaluation();

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod();
	public IPersistentDegreeEnrolmentInfo getIPersistentDegreeEnrolmentInfo();
	public IPersistentChosenCurricularCourseForOptionalCurricularCourse getIPersistentChosenCurricularCourseForOptionalCurricularCourse();
}