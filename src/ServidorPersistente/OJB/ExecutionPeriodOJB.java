package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Site;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;
import Util.TipoCurso;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
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
		// else If the execution period is mapped to the database, then write
		// any existing changes.
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
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readByNameAndExecutionYear(java.lang.String,
	 *      Dominio.IExecutionYear)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readBySemesterAndExecutionYear(java.lang.String,
	 *      Dominio.IExecutionYear)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readPublic()
	 */
	public List readPublic() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
		criteria.addGreaterThan("semester", new Integer(0));
		return queryList(ExecutionPeriod.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionPeriod#transferData(Dominio.IExecutionPeriod,
	 *      Dominio.IExecutionPeriod)
	 */
	public void transferData(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		// Clear all data from executionPeriodToImportDataTo.
		deleteAllDataRelatedToExecutionPeriod(executionPeriodToImportDataTo);

		//try {
		transferExecutionDegrees(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating execution degrees.");

		transferExecutionCourses(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating execution courses.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.gc();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		
		transferClasses(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating classes.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.gc();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferShifts(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);
		System.out.println("Finished creating shifts.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.gc();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferLessons(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);
		System.out.println("Finished creating lessons.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.gc();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferShiftLessonAssociations(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating shiftLessons.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.gc();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferClassShiftAssociations(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating classShifts.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferSites(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating sites.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferProfessorships(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating professorships.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		transferResponsibleFors(
			executionPeriodToImportDataTo,
			executionPeriodToExportDataFrom);

		System.out.println("Finished creating responsiblefor.");

		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		System.out.println("Clearing cache.");
		SuportePersistenteOJB.getInstance().clearCache();
		System.out.println("Confirming transaction.");
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Starting transaction.");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();

		cleanUpLessons(executionPeriodToImportDataTo);
		cleanUpShifts(executionPeriodToImportDataTo);
		System.out.println("Completed cleanup.");
	}

	/**
	 * @param executionPeriodToImportDataTo
	 */
	private void cleanUpShifts(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		int numberOfShifts = count(Turno.class, criteria);
		for (int i = 0; i < numberOfShifts; i++) {
			ITurno shift =
				(ITurno) readSpan(
					Turno.class,
					criteria,
					new Integer(1),
					new Integer(i + 1)).get(
					0);
			if (shift.getAssociatedLessons() == null
				|| shift.getAssociatedLessons().isEmpty()) {
				delete(shift);
			}
		}
	}

	/**
	 * 
	 */
	private void cleanUpLessons(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriod.getIdInternal());

		int numberOfLessons = count(Aula.class, criteria);
		for (int i = 0; i < numberOfLessons; i++) {
			IAula lesson =
				(IAula) readSpan(
					Aula.class,
					criteria,
					new Integer(1),
					new Integer(i + 1)).get(
					0);
			Criteria criteriaShifts = new Criteria();
			criteriaShifts.addEqualTo(
				"associatedLessons.idInternal",
				lesson.getIdInternal());
			if (count(Turno.class, criteriaShifts) == 0) {
				delete(lesson);
			}
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferClassShiftAssociations(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfClassShiftAssociations = count(TurmaTurno.class, criteria);

		for (int i = 0; i < numberOfClassShiftAssociations; i++) {
			ITurmaTurno classShiftAssociationToTransfer =
				(ITurmaTurno) readSpan(
					TurmaTurno.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createClassShift(
				classShiftAssociationToTransfer,
				executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferShiftLessonAssociations(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfShiftLessonAssociations = count(TurnoAula.class, criteria);

		for (int i = 0; i < numberOfShiftLessonAssociations; i++) {
			ITurnoAula shiftLessonAssociationToTransfer =
				(ITurnoAula) readSpan(
					TurnoAula.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createShiftLesson(
				shiftLessonAssociationToTransfer,
				executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferShifts(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfShifts = count(Turno.class, criteria);

		for (int i = 0; i < numberOfShifts; i++) {
			ITurno shiftToTransfer =
				(ITurno) readSpan(
					Turno.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createShift(shiftToTransfer, executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferLessons(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfLessons = count(Aula.class, criteria);

		for (int i = 0; i < numberOfLessons; i++) {
			IAula lessonToTransfer =
				(IAula) readSpan(
					Aula.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createLesson(lessonToTransfer, executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferClasses(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfClassesToTransfer = count(Turma.class, criteria);

		for (int i = 0; i < numberOfClassesToTransfer; i++) {
			ITurma schoolClassToTransfer =
				(ITurma) readSpan(
					Turma.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createClass(schoolClassToTransfer, executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferExecutionCourses(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfExecutionCoursesToTransfer =
			count(DisciplinaExecucao.class, criteria);

		for (int i = 0; i < numberOfExecutionCoursesToTransfer; i++) {
			IDisciplinaExecucao executionCourseToTransfer =
				(IDisciplinaExecucao) readSpan(
					DisciplinaExecucao.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createExecutionCourse(
				executionCourseToTransfer,
				executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferExecutionDegrees(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"academicYear",
			executionPeriodToExportDataFrom.getExecutionYear().getIdInternal());
		criteria.addEqualTo(
			"curricularPlan.degree.tipoCurso",
			new TipoCurso(TipoCurso.LICENCIATURA));

		int numberOfExecutionDegreesToTransfer =
			count(CursoExecucao.class, criteria);

		for (int i = 0; i < numberOfExecutionDegreesToTransfer; i++) {
			ICursoExecucao executionDegreeToTransfer =
				(ICursoExecucao) readSpan(
					CursoExecucao.class,
					criteria,
					new Integer(1),
					new Integer(i+1)).get(
					0);

			createExecutionDegree(
				executionDegreeToTransfer,
				executionPeriodToImportDataTo);
		}

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
			"curricularPlan.degree.idInternal",
			executionDegreeToTransfer
				.getDegreeCurricularPlan()
				.getDegree()
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
			executionDegreeToCreate.setCampus(
				executionDegreeToTransfer.getCampus());

			store(executionDegreeToCreate);
			return executionDegreeToCreate;
		} else {
			return (CursoExecucao) queryObject(CursoExecucao.class, criteria);
		}
	}

	private DisciplinaExecucao createExecutionCourse(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
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
			criteriaExecutionDegree.addEqualTo(
				"executionYear.idInternal",
				executionPeriodToImportDataTo
					.getExecutionYear()
					.getIdInternal());
			criteriaExecutionDegree.addEqualTo(
				"curricularPlan.degree.idInternal",
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getIdInternal());
			CursoExecucao executionDegree =
				(CursoExecucao) queryObject(CursoExecucao.class,
					criteriaExecutionDegree);

			if (executionDegree != null) {
				Criteria criteriaCurricularCourse = new Criteria();
				criteriaCurricularCourse.addEqualTo(
					"code",
					curricularCourse.getCode());
				criteriaCurricularCourse.addEqualTo(
					"degreeCurricularPlan.idInternal",
					executionDegree.getDegreeCurricularPlan().getIdInternal());
				ICurricularCourse curricularCourseInNewExecutionPeriod =
					(ICurricularCourse) queryObject(CurricularCourse.class,
						criteriaCurricularCourse);
				if (curricularCourseInNewExecutionPeriod != null) {
					curricularCourses.add(curricularCourseInNewExecutionPeriod);
				}
			}
		}

		if (curricularCourses != null && !curricularCourses.isEmpty()) {

			executionCourseToCreate.setAssociatedCurricularCourses(
				curricularCourses);
			executionCourseToCreate.setAssociatedEvaluations(new ArrayList());
			executionCourseToCreate.setAssociatedExams(new ArrayList());
			executionCourseToCreate.setComment(" ");
			executionCourseToCreate.setExecutionPeriod(
				executionPeriodToImportDataTo);
			executionCourseToCreate.setIdInternal(null);
			executionCourseToCreate.setLabHours(
				executionCourseToTransfer.getLabHours());
			executionCourseToCreate.setNome(
				executionCourseToTransfer.getNome());
			executionCourseToCreate.setPraticalHours(
				executionCourseToTransfer.getPraticalHours());
			executionCourseToCreate.setSigla(
				executionCourseToTransfer.getSigla());
			executionCourseToCreate.setTheoPratHours(
				executionCourseToTransfer.getTheoPratHours());
			executionCourseToCreate.setTheoreticalHours(
				executionCourseToTransfer.getTheoreticalHours());

			store(executionCourseToCreate);
		}

		return executionCourseToCreate;
	}

	private Turma createClass(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		Turma classToTransfer = (Turma) arg0;
		Turma classToCreate = new Turma();

		CursoExecucao executionDegree =
			findCorrespondingExecutionDegree(
				executionPeriodToImportDataTo,
				classToTransfer);

		classToCreate.setAnoCurricular(classToTransfer.getAnoCurricular());
		classToCreate.setExecutionDegree(executionDegree);
		classToCreate.setExecutionPeriod(executionPeriodToImportDataTo);
		classToCreate.setIdInternal(null);
		classToCreate.setNome(classToTransfer.getNome());

		try {
			store(classToCreate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classToCreate;
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param classToTransfer
	 * @return
	 */
	private CursoExecucao findCorrespondingExecutionDegree(
		IExecutionPeriod executionPeriodToImportDataTo,
		Turma classToTransfer)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"curricularPlan.degree.idInternal",
			classToTransfer
				.getExecutionDegree()
				.getCurricularPlan()
				.getDegree()
				.getIdInternal());
		criteria.addEqualTo(
			"executionYear.idInternal",
			executionPeriodToImportDataTo.getExecutionYear().getIdInternal());
		return (CursoExecucao) queryObject(CursoExecucao.class, criteria);
	}

	private IAula createLesson(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		IAula lessonToTransfer = (IAula) arg0;
		Aula lessonToCreate = new Aula();

		DisciplinaExecucao executionCourse =
			findCorrespondingExecutionCourse(
				executionPeriodToImportDataTo,
				lessonToTransfer.getDisciplinaExecucao());

		if (executionCourse != null) {
			lessonToCreate.setDiaSemana(lessonToTransfer.getDiaSemana());
			lessonToCreate.setDisciplinaExecucao(executionCourse);
			lessonToCreate.setExecutionPeriod(executionPeriodToImportDataTo);
			lessonToCreate.setFim(lessonToTransfer.getFim());
			lessonToCreate.setIdInternal(null);
			lessonToCreate.setInicio(lessonToTransfer.getInicio());
			lessonToCreate.setSala(lessonToTransfer.getSala());
			lessonToCreate.setTipo(lessonToTransfer.getTipo());

			try {
				store(lessonToCreate);
			} catch (Exception e) {
			e.printStackTrace();
			}
		}

		return lessonToCreate;
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param execucao
	 * @return
	 */
	private DisciplinaExecucao findCorrespondingExecutionCourse(
		IExecutionPeriod executionPeriodToImportDataTo,
		IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", executionCourse.getSigla());
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriodToImportDataTo.getIdInternal());
		return (DisciplinaExecucao) queryObject(
			DisciplinaExecucao.class,
			criteria);
	}

	private ITurno createShift(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		ITurno shiftToTransfer = (ITurno) arg0;
		ITurno shiftToCreate = new Turno();

		DisciplinaExecucao executionCourse =
			findCorrespondingExecutionCourse(
				executionPeriodToImportDataTo,
				shiftToTransfer.getDisciplinaExecucao());

		if (executionCourse != null) {
			shiftToCreate.setAssociatedLessons(new ArrayList());
			shiftToCreate.setAssociatedTeacherProfessorShipPercentage(
				new ArrayList());
			shiftToCreate.setDisciplinaExecucao(executionCourse);
			shiftToCreate.setIdInternal(null);
			shiftToCreate.setLotacao(shiftToTransfer.getLotacao());
			shiftToCreate.setNome(shiftToTransfer.getNome());
			shiftToCreate.setTipo(shiftToTransfer.getTipo());
			shiftToCreate.setAvailabilityFinal(new Integer(0));

			try {
				store(shiftToCreate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return shiftToCreate;
	}

	private ITurnoAula createShiftLesson(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		ITurnoAula shiftLessonToTransfer = (ITurnoAula) arg0;
		ITurnoAula shiftLessonToCreate = new TurnoAula();

		ITurno shift =
			findCorrespondingShift(
				executionPeriodToImportDataTo,
				shiftLessonToTransfer.getTurno());

		IAula lesson =
			findCorrespondingLesson(
				executionPeriodToImportDataTo,
				shiftLessonToTransfer.getAula());

		if (shift != null && lesson != null) {
			shiftLessonToCreate.setAula(lesson);
			shiftLessonToCreate.setIdInternal(null);
			shiftLessonToCreate.setTurno(shift);

			try {
				store(shiftLessonToCreate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return shiftLessonToCreate;
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param aula
	 * @return
	 */
	private IAula findCorrespondingLesson(
		IExecutionPeriod executionPeriodToImportDataTo,
		IAula lesson)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToImportDataTo.getIdInternal());
		criteria.addEqualTo(
			"disciplinaExecucao.sigla",
			lesson.getDisciplinaExecucao().getSigla());
		criteria.addEqualTo("diaSemana", lesson.getDiaSemana());
		criteria.addEqualTo("inicio", lesson.getInicio());
		criteria.addEqualTo("fim", lesson.getFim());
		criteria.addEqualTo("sala.nome", lesson.getSala().getNome());
		return (IAula) queryObject(Aula.class, criteria);
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param shiftLessonToTransfer
	 * @return
	 */
	private ITurno findCorrespondingShift(
		IExecutionPeriod executionPeriodToImportDataTo,
		ITurno shift)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"disciplinaExecucao.executionPeriod.idInternal",
			executionPeriodToImportDataTo.getIdInternal());
		criteria.addEqualTo(
			"disciplinaExecucao.idInternal",
			shift.getDisciplinaExecucao().getIdInternal());
		criteria.addEqualTo(
			"disciplinaExecucao.sigla",
			shift.getNome());
		criteria.addEqualTo("nome", shift.getNome());
		return (ITurno) queryObject(Turno.class, criteria);
	}

	private ITurmaTurno createClassShift(
		Object arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		ITurmaTurno classShiftToTransfer = (ITurmaTurno) arg0;
		ITurmaTurno classShiftToCreate = new TurmaTurno();

		ITurma ourClass =
			findCorrespondingClass(
				executionPeriodToImportDataTo,
				classShiftToTransfer.getTurma());

		ITurno shift =
			findCorrespondingShift(
				executionPeriodToImportDataTo,
				classShiftToTransfer.getTurno());

		if (ourClass != null && shift != null) {
			classShiftToCreate.setIdInternal(null);
			classShiftToCreate.setTurma(ourClass);
			classShiftToCreate.setTurno(shift);

			try {
				store(classShiftToCreate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return classShiftToCreate;
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param turno
	 * @return
	 */
	private ITurma findCorrespondingClass(
		IExecutionPeriod executionPeriodToImportDataTo,
		ITurma turma)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.idInternal",
			executionPeriodToImportDataTo.getIdInternal());
		criteria.addEqualTo("nome", turma.getNome());
		return (ITurma) queryObject(Turma.class, criteria);
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

	public List readByExecutionYear(IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionYear", executionYear.getIdInternal());
		return queryList(ExecutionPeriod.class, criteria);
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferResponsibleFors(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionCourse.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfResponsibleFors = count(ResponsibleFor.class, criteria);

		for (int i = 0; i < numberOfResponsibleFors; i++) {
			IResponsibleFor responsibleForToTransfer =
				(IResponsibleFor) readSpan(
					ResponsibleFor.class,
					criteria,
					new Integer(1),
					new Integer(i + 1)).get(
					0);

			createResponsibleFor(responsibleForToTransfer, executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param responsibleForToTransfer
	 * @param executionPeriodToImportDataTo
	 */
	private IResponsibleFor createResponsibleFor(
		IResponsibleFor arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
			IResponsibleFor responsibleForToTransfer = (IResponsibleFor) arg0;
			IResponsibleFor responsibleForToCreate = new ResponsibleFor();

			DisciplinaExecucao executionCourse =
				findCorrespondingExecutionCourse(
					executionPeriodToImportDataTo,
					responsibleForToTransfer.getExecutionCourse());

			if (executionCourse != null) {
				responsibleForToCreate.setExecutionCourse(executionCourse);
				responsibleForToCreate.setTeacher(responsibleForToTransfer.getTeacher());

				try {
					store(responsibleForToCreate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return responsibleForToCreate;
		}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferProfessorships(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
			Criteria criteria = new Criteria();
			criteria.addEqualTo(
				"executionCourse.executionPeriod.idInternal",
				executionPeriodToExportDataFrom.getIdInternal());

			int numberOfProfessorships = count(Professorship.class, criteria);

			for (int i = 0; i < numberOfProfessorships; i++) {
				IProfessorship professorshipToTransfer =
					(IProfessorship) readSpan(
						Professorship.class,
						criteria,
						new Integer(1),
						new Integer(i + 1)).get(
						0);

				createProfessorship(professorshipToTransfer, executionPeriodToImportDataTo);
			}
	}

	/**
	 * @param professorshipToTransfer
	 * @param executionPeriodToImportDataTo
	 */
	private IProfessorship createProfessorship(
		IProfessorship arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		IProfessorship professorshipForToTransfer = (IProfessorship) arg0;
		IProfessorship professorshipForToCreate = new Professorship();

		DisciplinaExecucao executionCourse =
			findCorrespondingExecutionCourse(
				executionPeriodToImportDataTo,
				professorshipForToTransfer.getExecutionCourse());

		if (executionCourse != null) {
			professorshipForToCreate.setExecutionCourse(executionCourse);
			professorshipForToCreate.setTeacher(
			professorshipForToTransfer.getTeacher());

			try {
				store(professorshipForToCreate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return professorshipForToCreate;
	}

	/**
	 * @param executionPeriodToImportDataTo
	 * @param executionPeriodToExportDataFrom
	 */
	private void transferSites(
		IExecutionPeriod executionPeriodToImportDataTo,
		IExecutionPeriod executionPeriodToExportDataFrom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionCourse.executionPeriod.idInternal",
			executionPeriodToExportDataFrom.getIdInternal());

		int numberOfSites = count(Site.class, criteria);

		for (int i = 0; i < numberOfSites; i++) {
			ISite siteToTransfer =
				(ISite) readSpan(
					Site.class,
					criteria,
					new Integer(1),
					new Integer(i + 1)).get(
					0);

			createSite(siteToTransfer, executionPeriodToImportDataTo);
		}
	}

	/**
	 * @param siteToTransfer
	 * @param executionPeriodToImportDataTo
	 */
	private ISite createSite(
		ISite arg0,
		IExecutionPeriod executionPeriodToImportDataTo)
		throws ExcepcaoPersistencia {
		ISite siteToTransfer = (ISite) arg0;
		ISite siteToCreate = new Site();

		DisciplinaExecucao executionCourse =
			findCorrespondingExecutionCourse(
				executionPeriodToImportDataTo,
				siteToTransfer.getExecutionCourse());

		if (executionCourse != null) {
			siteToCreate.setAlternativeSite(
				siteToTransfer.getAlternativeSite());
			siteToCreate.setExecutionCourse(executionCourse);
			siteToCreate.setInitialStatement(
				siteToTransfer.getInitialStatement());
			siteToCreate.setIntroduction(siteToTransfer.getIntroduction());
			siteToCreate.setMail(siteToTransfer.getMail());
			siteToCreate.setStyle(siteToTransfer.getStyle());

			try {
				store(siteToCreate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return siteToCreate;
	}

}