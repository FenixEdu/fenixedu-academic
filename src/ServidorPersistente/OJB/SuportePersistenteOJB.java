/*
 * SuportePersistenteOJB.java
 *
 * Created on 19 de Agosto de 2002, 1:18
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.odmg.Transaction;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.IPersistentChosenCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentDegreeCurricularPlanEnrolmentInfo;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentEquivalence;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.IPersistentGuideSituation;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IPersistentPrice;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentKind;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTeacherShiftPercentage;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;

public class SuportePersistenteOJB implements ISuportePersistente {
	Implementation _odmg = null;
	private static SuportePersistenteOJB _instance = null;

	protected Implementation getImplementation() {
		return _odmg;
	}

	public static synchronized SuportePersistenteOJB getInstance()
		throws ExcepcaoPersistencia {
		if (_instance == null) {
			_instance = new SuportePersistenteOJB();
		}
		return _instance;
	}

	public static synchronized void resetInstance() {
		if (_instance != null) {
			PersistenceBroker broker =
				PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			_instance = null;
		}
	}

	/** Creates a new instance of SuportePersistenteOJB */
	private SuportePersistenteOJB() throws ExcepcaoPersistencia {
		init();
	}

	private void init() throws ExcepcaoPersistencia {
		
		_odmg = OJB.getInstance();
		try {
			openDatabase();
		} catch (ODMGException e) {
			throw new ExcepcaoPersistencia();
		}
	}

	protected void finalize() throws Throwable {
	}

	public void iniciarTransaccao() throws ExcepcaoPersistencia {
		try {
			openDatabase();
			Transaction tx = _odmg.newTransaction();
			tx.begin();
		} catch (ODMGException ex1) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.OPEN_DATABASE,
				ex1);
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.BEGIN_TRANSACTION,
				ex);
		}
	}

	private void openDatabase() throws ODMGException {
		Database db = _odmg.getDatabase(null);
		if (db == null) {
			db = _odmg.newDatabase();
			db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
		}
	}

	public void confirmarTransaccao() throws ExcepcaoPersistencia {
		try {
			openDatabase();
			Transaction tx = _odmg.currentTransaction();

			if (tx == null)
				System.out.println(
					"SuportePersistente.OJB - Nao ha transaccao activa");
			else {
				tx.commit();
				//				Database db =_odmg.getDatabase(null); 
				//				if (db!= null) db.close();
			}
		} catch (ODMGException ex1) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.CLOSE_DATABASE,
				ex1);
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.COMMIT_TRANSACTION,
				ex);
		}
	}

	public void cancelarTransaccao() throws ExcepcaoPersistencia {
		try {
			openDatabase();
			Transaction tx = _odmg.currentTransaction();

			if (tx != null) {
				tx.abort();
				//					_odmg.getDatabase(null).close();
			}

		} catch (ODMGException ex1) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.CLOSE_DATABASE,
				ex1);
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.ABORT_TRANSACTION,
				ex);
		}
	}

	public IAulaPersistente getIAulaPersistente() {
		return new AulaOJB();
	}
	public ISalaPersistente getISalaPersistente() {
		return new SalaOJB();
	}
	public ITurmaPersistente getITurmaPersistente() {
		return new TurmaOJB();
	}
	public ITurnoPersistente getITurnoPersistente() {
		return new TurnoOJB();
	}

	public IFrequentaPersistente getIFrequentaPersistente() {
		return new FrequentaOJB();
	}
	public IPersistentEnrolment getIPersistentEnrolment() {
		return new EnrolmentOJB();
	}
	public ITurmaTurnoPersistente getITurmaTurnoPersistente() {
		return new TurmaTurnoOJB();
	}
	public ITurnoAlunoPersistente getITurnoAlunoPersistente() {
		return new TurnoAlunoOJB();
	}
	public ITurnoAulaPersistente getITurnoAulaPersistente() {
		return new TurnoAulaOJB();
	}

	public IPersistentCurricularCourse getIPersistentCurricularCourse() {
		return new CurricularCourseOJB();
	}
	public IDisciplinaExecucaoPersistente getIDisciplinaExecucaoPersistente() {
		return new DisciplinaExecucaoOJB();
	}
	public IPersistentCountry getIPersistentCountry() {
		return new CountryOJB();
	}
	public IPessoaPersistente getIPessoaPersistente() {
		return new PessoaOJB();
	}

	public ICursoPersistente getICursoPersistente() {
		return new CursoOJB();
	}
	public ICursoExecucaoPersistente getICursoExecucaoPersistente() {
		return new CursoExecucaoOJB();
	}

	public IPersistentStudent getIPersistentStudent() {
		return new StudentOJB();
	}

	public IDepartamentoPersistente getIDepartamentoPersistente() {
		return new DepartamentoOJB();
	}
	public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente() {
		return new DisciplinaDepartamentoOJB();
	}
	public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan() {
		return new DegreeCurricularPlanOJB();
	}
	public IStudentCurricularPlanPersistente getIStudentCurricularPlanPersistente() {
		return new StudentCurricularPlanOJB();
	}

	public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
		return new MasterDegreeCandidateOJB();
	}
	public IPersistentCandidateSituation getIPersistentCandidateSituation() {
		return new CandidateSituationOJB();
	}

	public IPersistentBibliographicReference getIPersistentBibliographicReference() {
		return new BibliographicReferenceOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionPeriod()
	 */
	public IPersistentExecutionPeriod getIPersistentExecutionPeriod() {
		return new ExecutionPeriodOJB();
	}
	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionYear()
	 */

	public IPersistentExecutionYear getIPersistentExecutionYear() {
		return new ExecutionYearOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSite()
	 */
	public IPersistentSite getIPersistentSite() {
		return new SiteOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSection()
	 */
	public IPersistentSection getIPersistentSection() {
		return new SectionOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentItem()
	 */
	public IPersistentItem getIPersistentItem() {
		return new ItemOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentAnnouncement()
	 */
	public IPersistentAnnouncement getIPersistentAnnouncement() {
		return new AnnouncementOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentCurriculum()
	 */
	public IPersistentCurriculum getIPersistentCurriculum() {
		return new CurriculumOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacher()
	 */
	public IPersistentTeacher getIPersistentTeacher() {
		return new TeacherOJB();
	}

	public IPersistentExam getIPersistentExam() {
		return new ExamOJB();
	}

	public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse() {
		return new ExamExecutionCourseOJB();
	}

	public IPersistentBranch getIPersistentBranch() {
		return new BranchOJB();
	}

	public IPersistentCurricularYear getIPersistentCurricularYear() {
		return new CurricularYearOJB();
	}

	public IPersistentContributor getIPersistentContributor() {
		return new ContributorOJB();
	}

	public IPersistentCurricularSemester getIPersistentCurricularSemester() {
		return new CurricularSemesterOJB();
	}

	public IPersistentEquivalence getIPersistentEquivalence() {
		return new EquivalenceOJB();
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentProfessorship()
	 */
	public IPersistentProfessorship getIPersistentProfessorship() {
		return new ProfessorshipOJB();
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentResponsibleFor()
	 */
	public IPersistentResponsibleFor getIPersistentResponsibleFor() {
		return new ResponsibleForOJB();
	}

	public IPersistentPrice getIPersistentPrice() {
		return new PriceOJB();
	}

	public IPersistentGuideEntry getIPersistentGuideEntry() {
		return new GuideEntryOJB();
	}

	public IPersistentGuide getIPersistentGuide() {
		return new GuideOJB();
	}

	public IPersistentGuideSituation getIPersistentGuideSituation() {
		return new GuideSituationOJB();
	}

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
		return new CurricularCourseScopeOJB();
	}

	public IPersistentRole getIPersistentRole() {
		return new RoleOJB();
	}

	public IPersistentPersonRole getIPersistentPersonRole() {
		return new PersonRoleOJB();
	}

	public IPersistentPrecedence getIPersistentPrecedence() {
		return new PrecedenceOJB();
	}

	public IPersistentRestriction getIPersistentRestriction() {
		return new PersistentRestriction();
	}

	public IPersistentEvaluationMethod getIPersistentEvaluation() {
		return new EvaluationMethodOJB();
	}

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
		return new PersistentEnrolmentPeriod();
	}

	public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo() {
		return new DegreeCurricularPlanEnrolmentInfoOJB();
	}

	public IPersistentChosenCurricularCourseForOptionalCurricularCourse getIPersistentChosenCurricularCourseForOptionalCurricularCourse() {
		return new ChosenCurricularCourseForOptionalCurricularCourseOJB();
	}

	public IPersistentStudentKind getIPersistentStudentKind() {
		return new StudentKindOJB();
	}

	public IPersistentTeacherShiftPercentage getIPersistentTeacherShiftPercentage() {
		return new TeacherShiftPercentageOJB();
	}

	public IPersistentCreditsTeacher getIPersistentCreditsTeacher() {
		return new CreditsTeacherOJB();
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExamStudentRoom()
	 */
	public IPersistentExamStudentRoom getIPersistentExamStudentRoom() {
		return new ExamStudentRoomOJB();
	}

	public IPersistentMark getIPersistentMark() {
		return new MarkOJB();
	}
}