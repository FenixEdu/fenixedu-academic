/*
 * TurnoAulaOJB.java
 *
 * Created on 22 de Outubro de 2002, 9:18
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.TurnoAluno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoAulaPersistente;
import Util.DiaSemana;

public class TurnoAulaOJB
	extends ObjectFenixOJB
	implements ITurnoAulaPersistente {

	public ITurnoAula readByTurnoAndAula(ITurno turno, IAula aula)
		throws ExcepcaoPersistencia {
		try {
			ITurnoAula turnoAula = null;
			String oqlQuery =
				"select turnoaula from " + TurnoAula.class.getName();
			oqlQuery += " where turno.nome = $1"
				+ " and turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2"
				+ " and turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $3"
				+ " and turno.disciplinaExecucao.sigla = $4"
				+ " and aula.diaSemana = $5"
				+ " and aula.inicio= $6"
				+ " and aula.fim = $7"
				+ " and aula.tipo = $8"
				+ " and aula.sala.nome = $9";

			query.create(oqlQuery);

			query.bind(turno.getNome());

			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getAnoLectivo());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());

			query.bind(turno.getDisciplinaExecucao().getSigla());

			query.bind(aula.getDiaSemana());
			query.bind(aula.getInicio());
			query.bind(aula.getFim());
			query.bind(aula.getTipo());
			query.bind(aula.getSala().getNome());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turnoAula = (ITurnoAula) result.get(0);
			return turnoAula;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurnoAula turnoAula) throws ExcepcaoPersistencia {
		super.lockWrite(turnoAula);
	}

	public void delete(ITurnoAula turnoAula) throws ExcepcaoPersistencia {
		super.delete(turnoAula);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + TurnoAula.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readLessonsByStudent(String username)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + TurnoAluno.class.getName();
			oqlQuery += " where aluno.person.username = $1";
			query.create(oqlQuery);
			query.bind(username);
			List studentShifts = (List) query.execute();
			lockRead(studentShifts);

			List lessons = null;
			for (int i = 0; i < studentShifts.size(); i++) {
				oqlQuery = "select all from " + TurnoAula.class.getName();
				oqlQuery += " where turno.nome = $1";
				query.create(oqlQuery);
				query.bind(
					((TurnoAluno) studentShifts.get(i)).getTurno().getNome());
				lessons = (List) query.execute();
				lockRead(lessons);
				//				for (int j = 0; j < studentShiftLessons.size(); j++)
				//					lessons.add(
				//						((TurnoAula) studentShiftLessons.get(j)).getAula());
			}

			return lessons;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

/**
 * 
 * @see ServidorPersistente.ITurnoAulaPersistente#readByShift(ITurno)
 */
	public List readByShift(ITurno shift) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from "
					+ TurnoAula.class.getName()
					+ " where turno.nome = $1"
					+ " and turno.disciplinaExecucao.executionPeriod.name = $2"
					+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $3"
					+ " and turno.disciplinaExecucao.sigla = $4";
			query.create(oqlQuery);

			
			IDisciplinaExecucao executionCourse = shift.getDisciplinaExecucao();
			
			query.bind(shift.getNome());
			query.bind(executionCourse.getExecutionPeriod().getName());
			
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			query.bind(executionCourse.getSigla());

			List shiftLessons = (List) query.execute();
			lockRead(shiftLessons);

			List lessons = new ArrayList();
			for (int j = 0; j < shiftLessons.size(); j++)
				lessons.add(((TurnoAula) shiftLessons.get(j)).getAula());
			return lessons;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
	/**
	 * @see ServidorPersistente.ITurnoAulaPersistente#delete(Dominio.ITurno, Util.DiaSemana, java.util.Calendar, java.util.Calendar, Dominio.ISala)
	 */
	public void delete(
		ITurno shift,
		DiaSemana diaSemana,
		Calendar inicio,
		Calendar fim,
		ISala sala)
		throws ExcepcaoPersistencia {
		try {
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

			IDisciplinaExecucao executionCourse = shift.getDisciplinaExecucao();
			
			query.bind(shift.getNome());
			

			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			query.bind(executionCourse.getSigla());

			query.bind(diaSemana);
			query.bind(inicio);
			query.bind(fim);
			query.bind(sala.getNome());

			List result = (List) query.execute();
			delete(result.get(0));
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
	/**
	 * @see ServidorPersistente.ITurnoAulaPersistente#readLessonsByShift(Dominio.ITurno)
	 */
	public List readLessonsByShift(ITurno shift) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from "
					+ TurnoAula.class.getName()
					+ " where turno.nome = $1"
					+ " and turno.disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2"
					+ " and turno.disciplinaExecucao.licenciaturaExecucao.curso.sigla = $3"
					+ " and turno.disciplinaExecucao.sigla = $4";

			query.create(oqlQuery);

			query.bind(shift.getNome());

			query.bind(
				shift
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getAnoLectivo());
			query.bind(
				shift
					.getDisciplinaExecucao()
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());

			query.bind(shift.getDisciplinaExecucao().getSigla());

			List shiftLessons = (List) query.execute();
			lockRead(shiftLessons);

			//			List lessons = new ArrayList();
			//			for (int j = 0; j < shiftLessons.size(); j++)
			//				lessons.add(((ITurnoAula) shiftLessons.get(j)).getAula());
			//
			//			return lessons;
			return shiftLessons;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}
