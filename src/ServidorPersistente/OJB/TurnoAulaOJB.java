/*
 * TurnoAulaOJB.java
 * 
 * Created on 22 de Outubro de 2002, 9:18
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.ShiftStudent;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;

public class TurnoAulaOJB extends ObjectFenixOJB implements ITurnoAulaPersistente
{

	public ITurnoAula readByShiftAndLesson(ITurno shift, IAula lesson) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("turno.nome", shift.getNome());
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.name",
			shift.getDisciplinaExecucao().getExecutionPeriod().getName());
		criteria.addEqualTo(
			"turno.disciplinaExecucao.executionPeriod.executionYear.year",
			shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
		criteria.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
		criteria.addEqualTo("aula.diaSemana", lesson.getDiaSemana());
		criteria.addEqualTo("aula.inicio", lesson.getInicio());
		criteria.addEqualTo("aula.fim", lesson.getFim());
		criteria.addEqualTo("aula.tipo", lesson.getTipo());
		criteria.addEqualTo("aula.sala.nome", lesson.getSala().getNome());

		List result = queryList(TurnoAula.class, criteria);
		if (result.size() != 0)
			return (ITurnoAula) result.get(0);
		return null;
	}

	public void lockWrite(ITurnoAula shiftLessonToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException
	{

		ITurnoAula shiftLessonFromDB = null;

		// If there is nothing to write, simply return.
		if (shiftLessonToWrite == null)
			return;

		// Read shiftLesson from database.
		shiftLessonFromDB =
			this.readByShiftAndLesson(shiftLessonToWrite.getTurno(), shiftLessonToWrite.getAula());

		// If shiftLesson is not in database, then write it.
		if (shiftLessonFromDB == null)
			super.lockWrite(shiftLessonToWrite);
		// else If the shiftLesson is mapped to the database, then write any existing changes.
		else if (
			(shiftLessonToWrite instanceof TurnoAula)
				&& ((TurnoAula) shiftLessonFromDB).getIdInternal().equals(
					((TurnoAula) shiftLessonToWrite).getIdInternal()))
		{
			super.lockWrite(shiftLessonToWrite);
			// else Throw an already existing exception
		}
		else
			throw new ExistingPersistentException();
	}

	public void delete(ITurnoAula turnoAula) throws ExcepcaoPersistencia
	{
		super.delete(turnoAula);
	}

	public void deleteAll() throws ExcepcaoPersistencia
	{
		String oqlQuery = "select all from " + TurnoAula.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readLessonsByStudent(String username) throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery = "select all from " + ShiftStudent.class.getName();
			oqlQuery += " where aluno.person.username = $1";
			query.create(oqlQuery);
			query.bind(username);
			List studentShifts = (List) query.execute();
			lockRead(studentShifts);

			List lessons = new ArrayList();
			for (int i = 0; i < studentShifts.size(); i++)
			{
				oqlQuery = "select all from " + TurnoAula.class.getName();
				oqlQuery += " where turno.nome = $1";
				query.create(oqlQuery);
				query.bind(((ShiftStudent) studentShifts.get(i)).getShift().getNome());
				List auxLessons = (List) query.execute();

				lockRead(lessons);
				lessons.addAll(auxLessons);
				//				for (int j = 0; j < studentShiftLessons.size(); j++)
				//					lessons.add(
				//						((TurnoAula) studentShiftLessons.get(j)).getAula());
			}

			return lessons;
		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * @see ServidorPersistente.ITurnoAulaPersistente#readByShift(ITurno)
	 */
	public List readByShift(ITurno shift) throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery =
				"select all from "
					+ TurnoAula.class.getName()
					+ " where turno.nome = $1"
					+ " and turno.disciplinaExecucao.idInternal = $2";
			//+ " and turno.disciplinaExecucao.executionPeriod.name = $2"
			//+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $3"
			//+ " and turno.disciplinaExecucao.sigla = $4";
			query.create(oqlQuery);

			IExecutionCourse executionCourse = shift.getDisciplinaExecucao();

			query.bind(shift.getNome());
			query.bind(executionCourse.getIdInternal());
			//query.bind(executionCourse.getExecutionPeriod().getName());

			//query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			//query.bind(executionCourse.getSigla());

			List shiftLessons = (List) query.execute();
			lockRead(shiftLessons);

			List lessons = new ArrayList();
			for (int j = 0; j < shiftLessons.size(); j++)
				lessons.add(((TurnoAula) shiftLessons.get(j)).getAula());
			return lessons;

		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
	/**
	 * @see ServidorPersistente.ITurnoAulaPersistente#delete(Dominio.ITurno, Util.DiaSemana,
	 *      java.util.Calendar, java.util.Calendar, Dominio.ISala)
	 */
	public void delete(ITurno shift, DiaSemana diaSemana, Calendar inicio, Calendar fim, ISala sala)
		throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery =
				"select turnoAula from "
					+ TurnoAula.class.getName()
					+ " where turno.nome = $1"
					+ " and turno.disciplinaExecucao.executionPeriod.name = $2"
					+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $3"
					+ " and turno.disciplinaExecucao.sigla = $4"
					+ " and aula.diaSemana = $5"
					+ " and aula.inicio= $6"
					+ " and aula.fim = $7"
					+ " and aula.sala.nome = $8";

			query.create(oqlQuery);

			IExecutionCourse executionCourse = shift.getDisciplinaExecucao();

			query.bind(shift.getNome());

			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			query.bind(executionCourse.getSigla());

			query.bind(diaSemana);
			query.bind(inicio);
			query.bind(fim);
			query.bind(sala.getNome());

			List result = (List) query.execute();
			if (!result.isEmpty())
				delete((ITurnoAula) result.get(0));
		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
	/**
	 * @see ServidorPersistente.ITurnoAulaPersistente#readLessonsByShift(Dominio.ITurno)
	 */
	public List readLessonsByShift(ITurno shift) throws ExcepcaoPersistencia
	{
		try
		{
			String oqlQuery =
				"select all from "
					+ TurnoAula.class.getName()
					+ " where turno.nome = $1"
					+ " and turno.disciplinaExecucao.executionPeriod.name = $2"
					+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $3"
					+ " and turno.disciplinaExecucao.sigla = $4";

			//					+ " and turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2"
			//					+ " and turno.disciplinaExecucao.licenciaturaExecucao.degree.sigla = $3"
			//					+ " and turno.disciplinaExecucao.sigla = $4";

			query.create(oqlQuery);

			query.bind(shift.getNome());

			query.bind(shift.getDisciplinaExecucao().getExecutionPeriod().getName());
			query.bind(shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());

			//			query.bind(
			//				shift
			//					.getDisciplinaExecucao()
			//					.getLicenciaturaExecucao()
			//					.getAnoLectivo());
			//			query.bind(
			//				shift
			//					.getDisciplinaExecucao()
			//					.getLicenciaturaExecucao()
			//					.getCurso()
			//					.getSigla());

			query.bind(shift.getDisciplinaExecucao().getSigla());

			List shiftLessons = (List) query.execute();
			lockRead(shiftLessons);

			//			List lessons = new ArrayList();
			//			for (int j = 0; j < shiftLessons.size(); j++)
			//				lessons.add(((ITurnoAula) shiftLessons.get(j)).getAula());
			//
			//			return lessons;
			return shiftLessons;

		}
		catch (QueryException ex)
		{
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}
