package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.Exam;
import Dominio.ExecutionPeriod;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * Package ServidorPersistente.OJB
 * 
 */
public class ExecutionPeriodOJB
	extends ObjectFenixOJB
	implements IPersistentExecutionPeriod {

	/**
	 * Constructor for ExecutionPeriodOJB.
	 */
	public ExecutionPeriodOJB() {
		super();
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readAllExecutionPeriod()
	 */
	public List readAllExecutionPeriod() throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();

			query.create(oqlQuery);

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#writeExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public void writeExecutionPeriod(IExecutionPeriod executionPeriodToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IExecutionPeriod executionPeriodFromDB = null;

		// If there is nothing to write, simply return.
		if (executionPeriodToWrite == null)
			return;

		// Read execution period from database.
		executionPeriodFromDB =
			this.readByNameAndExecutionYear(
				executionPeriodToWrite.getName(),
				executionPeriodToWrite.getExecutionYear());

		// If execution period is not in database, then write it.
		if (executionPeriodFromDB == null)
			super.lockWrite(executionPeriodToWrite);
		// else If the execution period is mapped to the database, then write any existing changes.
		else if (
			(executionPeriodToWrite instanceof ExecutionPeriod)
				&& ((ExecutionPeriod) executionPeriodFromDB)
					.getIdInternal()
					.equals(
					((ExecutionPeriod) executionPeriodToWrite)
						.getIdInternal())) {
			super.lockWrite(executionPeriodToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#delete(Dominio.IExecutionPeriod)
	 */
	public boolean delete(IExecutionPeriod executionPeriod) {
		List executionCourses = new ArrayList();
		List classes = new ArrayList();
		try {

			executionCourses =
				SuportePersistenteOJB
					.getInstance()
					.getIDisciplinaExecucaoPersistente()
					.readByExecutionPeriod(executionPeriod);
			classes =
				SuportePersistenteOJB
					.getInstance()
					.getITurmaPersistente()
					.readByExecutionPeriod(executionPeriod);

			if (classes.isEmpty() && executionCourses.isEmpty()) {
				super.delete(executionPeriod);
			} else
				return false;

		} catch (ExcepcaoPersistencia e) {
			return false;
		}
		return true;
	}

	public boolean deleteWorkingArea(IExecutionPeriod executionPeriod) {
		if (executionPeriod.getSemester().intValue() > 0) {
			return false;
		}

		try {
			deleteAllDataRelatedToExecutionPeriod(executionPeriod);

			super.delete(executionPeriod);
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
		return true;
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#deleteAll()
	 */
	public boolean deleteAll() {
		try {
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IExecutionPeriod executionPeriod =
					(IExecutionPeriod) iter.next();
				delete(executionPeriod);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readActualExecutionPeriod()
	 */
	public IExecutionPeriod readActualExecutionPeriod()
		throws ExcepcaoPersistencia {
//		try {
//			IExecutionPeriod executionPeriod = null;
//			String oqlQuery =
//				"select all from "
//					+ ExecutionPeriod.class.getName()
//					+ " where state = $1";
//
//			query.create(oqlQuery);
//			query.bind(PeriodState.CURRENT);
//
//			List result = (List) query.execute();
//			lockRead(result);
//			if ((result != null) && (!result.isEmpty()))
//				executionPeriod = (IExecutionPeriod) result.get(0);
//			return executionPeriod;
//		} catch (QueryException e) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
//		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.CURRENT);
		return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);

	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readByNameAndExecutionYear(java.lang.String, Dominio.IExecutionYear)
	 */
	public IExecutionPeriod readByNameAndExecutionYear(
		String executionPeriodName,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {

			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			oqlQuery += " where name = $1 and executionYear.year= $2 ";
			query.create(oqlQuery);
			query.bind(executionPeriodName);
			query.bind(executionYear.getYear());

			List result = (List) query.execute();

			lockRead(result);

			if (result.size() != 0)
				return (IExecutionPeriod) result.get(0);

			return executionPeriod;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readBySemesterAndExecutionYear(java.lang.String, Dominio.IExecutionYear)
	 */
	public IExecutionPeriod readBySemesterAndExecutionYear(
		Integer semester,
		IExecutionYear year)
		throws ExcepcaoPersistencia {
		if (year == null) {
			return null;
		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("semester", semester);
		criteria.addEqualTo("executionYear.year", year.getYear());
		return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readPublic()
	 */
	public List readPublic() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
		criteria.addGreaterThan("semester", new Integer(0));
		return queryList(ExecutionPeriod.class, criteria);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExecutionPeriod#transferData(Dominio.IExecutionPeriod, Dominio.IExecutionPeriod)
	 */
	public void transferData(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		// Clear all data from executionPeriodToImportDataTo.
		// TODO : Presume that the execution period is empty
		deleteAllDataRelatedToExecutionPeriod(executionPeriodToImportDataTo);

		// Transfer data
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"academicYear",
			executionPeriodToExportDataFrom.getExecutionYear().getIdInternal());

		// Execution Degrees
		List executionDegreesToTransfer =
			queryList(CursoExecucao.class, criteria);
		List executionDegrees = new ArrayList();
		for (int i = 0; i < executionDegreesToTransfer.size(); i++) {
			executionDegrees.add(
				createExecutionDegree(
					executionDegreesToTransfer.get(i),
					executionPeriodToImportDataTo));
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		
		criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		// Execution Courses
		List executionCoursesToTransfer =
			queryList(DisciplinaExecucao.class, criteria);
		List executionCourses = new ArrayList();
		for (int i = 0; i < executionCoursesToTransfer.size(); i++) {
			executionCourses.add(
				createExecutionCourse(
					executionCoursesToTransfer.get(i),
					executionPeriodToImportDataTo));
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		// Classes
		List classesToTransfer = queryList(Turma.class, criteria);
		List classes = new ArrayList();
		for (int i = 0; i < classesToTransfer.size(); i++) {
			classes.add(
				createClass(
					classesToTransfer.get(i),
					executionPeriodToImportDataTo,
					executionDegrees));
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		// Lessons
		List lessonsToTransfer = queryList(Aula.class, criteria);
		List lessons = new ArrayList();
		for (int i = 0; i < lessonsToTransfer.size(); i++) {
			lessons.add(
				createLesson(
					lessonsToTransfer.get(i),
					executionPeriodToImportDataTo,
					executionCourses));
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		// Shifts
		List shiftsToTransfer = queryList(Turno.class, criteria);
		List shifts = new ArrayList();
		for (int i = 0; i < shiftsToTransfer.size(); i++) {
			shifts.add(
				createShift(
					shiftsToTransfer.get(i),
					executionPeriodToImportDataTo,
					executionCourses));
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		criteria = new Criteria();
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		// Shifts-Lessons
		List shiftsLessonsToTransfer = queryList(TurnoAula.class, criteria);
		for (int i = 0; i < shiftsLessonsToTransfer.size(); i++) {
			createShiftLesson(
				shiftsLessonsToTransfer.get(i),
				executionPeriodToImportDataTo,
				shifts,
				lessons);
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		// Classes-Shifts
		List classesShiftsToTransfer = queryList(TurmaTurno.class, criteria);
		for (int i = 0; i < classesShiftsToTransfer.size(); i++) {
			createClassShift(
				classesShiftsToTransfer.get(i),
				executionPeriodToImportDataTo,
				classes,
				shifts);
		}

		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

//		criteria = new Criteria();
//		criteria.addEqualTo(
//			"associatedExecutionCourses.executionPeriod.idInternal",
//			executionPeriodToExportDataFrom.getIdInternal());

		// Whatever else needs to be transfered
	}

	private void deleteAllDataRelatedToExecutionPeriod(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
			"turma.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		// Classes-Shifts
		List classesShifts = queryList(TurmaTurno.class, criteria);
		for (int i = 0; i < classesShifts.size(); i++) {
			ITurmaTurno classeShiftToDelete =
				(ITurmaTurno) classesShifts.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getITurmaTurnoPersistente()
				.deleteByOID(
				TurmaTurno.class,
				classeShiftToDelete.getIdInternal());
		}

		criteria = new Criteria();
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		// Shifts-Lessons
		List shiftsLessons = queryList(TurnoAula.class, criteria);
		for (int i = 0; i < shiftsLessons.size(); i++) {
			ITurnoAula shiftLessonToDelete = (ITurnoAula) shiftsLessons.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getITurnoAulaPersistente()
				.deleteByOID(
				TurnoAula.class,
				shiftLessonToDelete.getIdInternal());
		}

		criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		// Shifts
		List shifts = queryList(Turno.class, criteria);
		for (int i = 0; i < shifts.size(); i++) {
			ITurno shiftToDelete = (ITurno) shifts.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getITurnoPersistente()
				.deleteByOID(
				Turno.class,
				shiftToDelete.getIdInternal());
		}

		// Lessons
		List lessons = queryList(Aula.class, criteria);
		for (int i = 0; i < lessons.size(); i++) {
			IAula lessonToDelete = (IAula) lessons.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getIAulaPersistente()
				.deleteByOID(
				Aula.class,
				lessonToDelete.getIdInternal());
		}

		criteria = new Criteria();
		criteria.addEqualTo(
			"associatedExecutionCourses.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		// Exams
		List exams = queryList(Exam.class, criteria);
		for (int i = 0; i < exams.size(); i++) {
			IExam examToDelete = (IExam) exams.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getIAulaPersistente()
				.deleteByOID(
				Exam.class,
				examToDelete.getIdInternal());
		}

		// Whatever else needs to be deleted

		criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		// Execution Courses
		List executionCourses = queryList(DisciplinaExecucao.class, criteria);
		for (int i = 0; i < executionCourses.size(); i++) {
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) executionCourses.get(i);
			SuportePersistenteOJB
				.getInstance()
				.getIDisciplinaExecucaoPersistente()
				.deleteExecutionCourse(executionCourse);
		}

		// Classes
		List classes = queryList(Turma.class, criteria);
		for (int i = 0; i < classes.size(); i++) {
			ITurma classToDelete = (ITurma) classes.get(i);
			SuportePersistenteOJB.getInstance().getITurmaPersistente().delete(
				classToDelete);
		}

	}

	private CursoExecucao createExecutionDegree(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		CursoExecucao executionDegreeToTransfer = (CursoExecucao) arg0;
		CursoExecucao executionDegreeToCreate = new CursoExecucao();

		// check if exists
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"academicYear",
			executionPeriodToImportDataTo.getExecutionYear().getIdInternal());
		criteria.addEqualTo(
			"keyCurricularPlan",
			executionDegreeToTransfer
				.getDegreeCurricularPlan()
				.getIdInternal());
		if (queryObject(CursoExecucao.class, criteria) == null) {

			executionDegreeToCreate.setCoordinator(
				executionDegreeToTransfer.getCoordinator());
			executionDegreeToCreate.setCurricularPlan(
				executionDegreeToTransfer.getCurricularPlan());
			executionDegreeToCreate.setDegreeCurricularPlan(
				executionDegreeToTransfer.getDegreeCurricularPlan());
			executionDegreeToCreate.setExecutionYear(
				executionPeriodToImportDataTo.getExecutionYear());
			executionDegreeToCreate.setIdInternal(null);
			executionDegreeToCreate.setTemporaryExamMap(new Boolean(true));
			executionDegreeToCreate.setKeyCampus(new Integer(1));

			try {
				// It's Ok to just write it with no verification because:
				//   - all data from this execution period has been cleared
				//   - suposedly all data related to the other execution period
				//     is correct.
				lockWrite(executionDegreeToCreate);
				return executionDegreeToCreate;
			} catch (ExcepcaoPersistencia e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return (CursoExecucao) queryObject(CursoExecucao.class, criteria);
		}

		return null;
	}

	private DisciplinaExecucao createExecutionCourse(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo) throws ExcepcaoPersistencia {
		DisciplinaExecucao executionCourseToTransfer =
			(DisciplinaExecucao) arg0;
		DisciplinaExecucao executionCourseToCreate = new DisciplinaExecucao();
		List curricularCourses = new ArrayList();

		for (int i = 0;
			i
				< executionCourseToTransfer
					.getAssociatedCurricularCourses()
					.size();
			i++) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) executionCourseToTransfer
					.getAssociatedCurricularCourses()
					.get(i);
			Criteria criteriaExecutionDegree = new Criteria();
			criteriaExecutionDegree.addEqualTo("executionYear.idInternal", executionPeriodToImportDataTo.getExecutionYear().getIdInternal());
			criteriaExecutionDegree.addEqualTo("curricularPlan.degree.idInternal", curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal());
			CursoExecucao executionDegree = (CursoExecucao) queryObject(CursoExecucao.class, criteriaExecutionDegree);

			if (executionDegree != null) {
				Criteria criteriaCurricularCourse = new Criteria();
				criteriaCurricularCourse.addEqualTo("code", curricularCourse.getCode());
				criteriaCurricularCourse.addEqualTo("degreeCurricularPlan.idInternal", executionDegree.getDegreeCurricularPlan().getIdInternal());
				ICurricularCourse curricularCourseInNewExecutionPeriod = (ICurricularCourse) queryObject(CurricularCourse.class, criteriaCurricularCourse);
				if (curricularCourseInNewExecutionPeriod != null) {
					curricularCourses.add(curricularCourseInNewExecutionPeriod);
				}
			}
		}

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			simpleLockWrite(executionCourseToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executionCourseToCreate.setAssociatedCurricularCourses(curricularCourses);
		executionCourseToCreate.setAssociatedEvaluations(new ArrayList());
		executionCourseToCreate.setAssociatedExams(new ArrayList());
		executionCourseToCreate.setComment(" ");
		executionCourseToCreate.setExecutionPeriod(
			executionPeriodToImportDataTo);
		executionCourseToCreate.setIdInternal(null);
		executionCourseToCreate.setLabHours(
			executionCourseToTransfer.getLabHours());
		executionCourseToCreate.setNome(executionCourseToTransfer.getNome());
		executionCourseToCreate.setPraticalHours(
			executionCourseToTransfer.getPraticalHours());
		executionCourseToCreate.setSigla(executionCourseToTransfer.getSigla());
		executionCourseToCreate.setTheoPratHours(
			executionCourseToTransfer.getTheoPratHours());
		executionCourseToCreate.setTheoreticalHours(
			executionCourseToTransfer.getTheoreticalHours());

//		for (int i = 0;
//			i
//				< executionCourseToTransfer
//					.getAssociatedCurricularCourses()
//					.size();
//			i++) {
//			ICurricularCourse curricularCourse =
//				(ICurricularCourse) executionCourseToTransfer
//					.getAssociatedCurricularCourses()
//					.get(i);
//			executionCourseToCreate.getAssociatedCurricularCourses().add(
//				curricularCourse);
//		}

		return executionCourseToCreate;
	}

	private Turma createClass(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo,
		List executionDegrees)
		throws ExcepcaoPersistencia {
		Turma classToTransfer = (Turma) arg0;
		Turma classToCreate = new Turma();

		CursoExecucao executionDegree =
			(CursoExecucao) CollectionUtils.find(
				executionDegrees,
				new PREDICATE_EXECUTION_DEGREE(
					executionPeriodToImportDataTo,
					classToTransfer.getExecutionDegree().getCurricularPlan()));

		classToCreate.setAnoCurricular(classToTransfer.getAnoCurricular());
		classToCreate.setExecutionDegree(executionDegree);
		classToCreate.setExecutionPeriod(executionPeriodToImportDataTo);
		classToCreate.setIdInternal(null);
		classToCreate.setNome(classToTransfer.getNome());

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			lockWrite(classToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classToCreate;
	}

	private IAula createLesson(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo,
		List executionCourses)
		throws ExcepcaoPersistencia {
		IAula lessonToTransfer = (IAula) arg0;
		Aula lessonToCreate = new Aula();

		DisciplinaExecucao executionCourse =
			(DisciplinaExecucao) CollectionUtils.find(
				executionCourses,
				new PREDICATE_EXECUTION_COURSE(
					lessonToTransfer.getDisciplinaExecucao()));

		lessonToCreate.setDiaSemana(lessonToTransfer.getDiaSemana());
		lessonToCreate.setDisciplinaExecucao(executionCourse);
		lessonToCreate.setExecutionPeriod(executionPeriodToImportDataTo);
		lessonToCreate.setFim(lessonToTransfer.getFim());
		lessonToCreate.setIdInternal(null);
		lessonToCreate.setInicio(lessonToTransfer.getInicio());
		lessonToCreate.setSala(lessonToTransfer.getSala());
		lessonToCreate.setTipo(lessonToTransfer.getTipo());

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			lockWrite(lessonToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lessonToCreate;
	}

	private ITurno createShift(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo,
		List executionCourses)
		throws ExcepcaoPersistencia {
		ITurno shiftToTransfer = (ITurno) arg0;
		ITurno shiftToCreate = new Turno();

		DisciplinaExecucao executionCourse =
			(DisciplinaExecucao) CollectionUtils.find(
				executionCourses,
				new PREDICATE_EXECUTION_COURSE(
					shiftToTransfer.getDisciplinaExecucao()));

		shiftToCreate.setAssociatedLessons(new ArrayList());
		// TODO : find out what this is, and decide what to do with it.
		shiftToCreate.setAssociatedTeacherProfessorShipPercentage(
			new ArrayList());
		shiftToCreate.setDisciplinaExecucao(executionCourse);
		shiftToCreate.setIdInternal(null);
		shiftToCreate.setLotacao(shiftToTransfer.getLotacao());
		shiftToCreate.setNome(shiftToTransfer.getNome());
		shiftToCreate.setTipo(shiftToTransfer.getTipo());
		shiftToCreate.setAvailabilityFinal(new Integer(0));

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			lockWrite(shiftToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shiftToCreate;
	}

	private ITurnoAula createShiftLesson(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo,
		List shifts,
		List lessons)
		throws ExcepcaoPersistencia {
		ITurnoAula shiftLessonToTransfer = (ITurnoAula) arg0;
		ITurnoAula shiftLessonToCreate = new TurnoAula();

		ITurno shift =
			(ITurno) CollectionUtils.find(
				shifts,
				new PREDICATE_SHIFT(shiftLessonToTransfer.getTurno()));

		IAula lesson =
			(IAula) CollectionUtils.find(
				lessons,
				new PREDICATE_LESSON(shiftLessonToTransfer.getAula()));

		shiftLessonToCreate.setAula(lesson);
		shiftLessonToCreate.setIdInternal(null);
		shiftLessonToCreate.setTurno(shift);

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			lockWrite(shiftLessonToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shiftLessonToCreate;
	}

	private ITurmaTurno createClassShift(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo,
		List classes,
		List shifts)
		throws ExcepcaoPersistencia {
		ITurmaTurno classShiftToTransfer = (ITurmaTurno) arg0;
		ITurmaTurno classShiftToCreate = new TurmaTurno();

		ITurma ourClass =
			(ITurma) CollectionUtils.find(
				classes,
				new PREDICATE_CLASS(classShiftToTransfer.getTurma()));

		ITurno shift =
			(ITurno) CollectionUtils.find(
				shifts,
				new PREDICATE_SHIFT(classShiftToTransfer.getTurno()));

		classShiftToCreate.setIdInternal(null);
		classShiftToCreate.setTurma(ourClass);
		classShiftToCreate.setTurno(shift);

		try {
			// It's Ok to just write it with no verification because:
			//   - all data from this execution period has been cleared
			//   - suposedly all data related to the other execution period
			//     is correct.
			lockWrite(classShiftToCreate);
		} catch (ExcepcaoPersistencia e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classShiftToCreate;
	}

	private class PREDICATE_EXECUTION_DEGREE implements Predicate {

		private IExecutionPeriod executionPeriod;
		private IDegreeCurricularPlan degreeCurricularPlan;

		public PREDICATE_EXECUTION_DEGREE(
			IExecutionPeriod executionPeriod,
			IDegreeCurricularPlan degreeCurricularPlan) {
			this.executionPeriod = executionPeriod;
			this.degreeCurricularPlan = degreeCurricularPlan;
		}

		public boolean evaluate(Object arg0) {
			ICursoExecucao executionDegree = (ICursoExecucao) arg0;

			return this.executionPeriod.getExecutionYear().getYear().equals(
				executionDegree.getExecutionYear().getYear())
				&& this.degreeCurricularPlan.getDegree().getSigla().equals(
					executionDegree.getCurricularPlan().getDegree().getSigla());
		}
	}

	private class PREDICATE_EXECUTION_COURSE implements Predicate {

		private IDisciplinaExecucao executionCourse;

		public PREDICATE_EXECUTION_COURSE(IDisciplinaExecucao executionCourse) {
			this.executionCourse = executionCourse;
		}

		public boolean evaluate(Object arg0) {
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) arg0;

			return this.executionCourse.getSigla().equals(
				executionCourse.getSigla());
		}
	}

//	private class PREDICATE_ANY_EXECUTION_COURSE implements Predicate {
//
//		private List executionCourses;
//
//		public PREDICATE_ANY_EXECUTION_COURSE(List executionCourses) {
//			this.executionCourses = executionCourses;
//		}
//
//		public boolean evaluate(Object arg0) {
//			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) arg0;
//
//			return CollectionUtils.find(
//				this.executionCourses,
//				new PREDICATE_EXECUTION_COURSE(executionCourse))
//				!= null;
//		}
//	}

	private class PREDICATE_LESSON implements Predicate {

		private IAula lesson;

		public PREDICATE_LESSON(IAula lesson) {
			this.lesson = lesson;
		}

		public boolean evaluate(Object arg0) {
			IAula lesson = (IAula) arg0;

			return this.lesson.getDiaSemana().getDiaSemana().equals(
				lesson.getDiaSemana().getDiaSemana())
				&& this.lesson.getInicio().equals(lesson.getInicio())
				&& this.lesson.getFim().equals(lesson.getFim())
				&& this.lesson.getSala().getNome().equals(
					lesson.getSala().getNome());
		}
	}

	private class PREDICATE_SHIFT implements Predicate {

		private ITurno shift;

		public PREDICATE_SHIFT(ITurno shift) {
			this.shift = shift;
		}

		public boolean evaluate(Object arg0) {
			ITurno shift = (ITurno) arg0;

			return this.shift.getNome().equals(shift.getNome());
		}
	}

	private class PREDICATE_CLASS implements Predicate {

		private ITurma ourClass;

		public PREDICATE_CLASS(ITurma ourClass) {
			this.ourClass = ourClass;
		}

		public boolean evaluate(Object arg0) {
			ITurma ourClass = (ITurma) arg0;

			return this.ourClass.getNome().equals(ourClass.getNome())
				&& this
					.ourClass
					.getExecutionDegree()
					.getCurricularPlan()
					.getDegree()
					.getSigla()
					.equals(
						ourClass
							.getExecutionDegree()
							.getCurricularPlan()
							.getDegree()
							.getSigla());
		}
	}

	public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionYear", executionYear.getIdInternal());
		return queryList(ExecutionPeriod.class, criteria);
	}

}
