/*
 * AulaOJB.java
 *
 * Created on 18 de Outubro de 2002, 00:34
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import Dominio.ITurnoAula;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import Util.DiaSemana;
import Util.TipoAula;

public class AulaOJB extends ObjectFenixOJB implements IAulaPersistente {

	public IAula readByDiaSemanaAndInicioAndFimAndSala(
		DiaSemana diaSemana,
		Calendar inicio,
		Calendar fim,
		ISala sala)
		throws ExcepcaoPersistencia {
		try {
			IAula aula = null;
			String oqlQuery =
				"select diasemanainiciofimsala from " + Aula.class.getName();
			oqlQuery
				+= " where diaSemana = $1 and inicio = $2 and fim = $3 and sala.nome = $4";
			query.create(oqlQuery);
			query.bind(diaSemana);
			query.bind(inicio);
			query.bind(fim);
			query.bind(sala.getNome());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				aula = (IAula) result.get(0);
			return aula;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IAula aula) throws ExcepcaoPersistencia {
		super.lockWrite(aula);
	}

	public void delete(IAula aula) throws ExcepcaoPersistencia {
		try {
			ITurnoAula turnoAula = null;
			TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
			String oqlQuery = "select all from " + TurnoAula.class.getName();
			oqlQuery += " where aula.diaSemana = $1"
				+ " and  aula.inicio = $2"
				+ " and aula.fim = $3"
				+ " and aula.sala.nome= $4";
			query.create(oqlQuery);
			query.bind(aula.getDiaSemana());
			query.bind(aula.getInicio());
			query.bind(aula.getFim());
			query.bind(aula.getSala().getNome());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turnoAula = (ITurnoAula) iterador.next();
				turnoAulaOJB.delete(turnoAula);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		super.delete(aula);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Aula.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readByDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao)
		throws ExcepcaoPersistencia {
		if (disciplinaExecucao == null)
			return new ArrayList();
		try {
			List aulas = new ArrayList();
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1";
			oqlQuery += " and disciplinaExecucao.licenciaturaExecucao.anoLectivo = $2";
			oqlQuery += " and disciplinaExecucao.licenciaturaExecucao.curso.nome = $3";
			oqlQuery += " and disciplinaExecucao.licenciaturaExecucao.curso.sigla = $4";
			query.create(oqlQuery);
			query.bind(disciplinaExecucao.getSigla());
			query.bind(disciplinaExecucao.getLicenciaturaExecucao().getAnoLectivo());
			query.bind(disciplinaExecucao.getLicenciaturaExecucao().getCurso().getNome());
			query.bind(disciplinaExecucao.getLicenciaturaExecucao().getCurso().getSigla());
			
			List result = (List) query.execute();
			lockRead(result);
			for (int i = 0; i != result.size(); i++)
				aulas.add((IAula) (result.get(i)));
			return aulas;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByDisciplinaExecucaoETipo(String sigla, TipoAula tipoAula)
		throws ExcepcaoPersistencia {
		try {
			//			List aulas = new ArrayList();
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where tipo = $1";
			oqlQuery += " and disciplinaExecucao.sigla = $2";
			query.create(oqlQuery);
			query.bind(tipoAula);
			query.bind(sigla);
			List result = (List) query.execute();
			lockRead(result);
			//			for (int i = 0; i != result.size(); i++)
			//				aulas.add((IAula) (result.get(i)));
			//			return aulas;
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByDisciplinaExecucaoETipo(
		IDisciplinaExecucao executionCourse,
		TipoAula tipoAula)
		throws ExcepcaoPersistencia {
		try {
			//			List aulas = new ArrayList();
			String oqlQuery =
				"select aulas from "
					+ Aula.class.getName()
					+ " where tipo = $1"
					+ " and disciplinaExecucao.sigla = $2"
					+ " and disciplinaExecucao.licenciaturaExecucao.curso.sigla = $3"
					+ " and disciplinaExecucao.licenciaturaExecucao.anoLectivo = $4";
			query.create(oqlQuery);
			query.bind(tipoAula);
			query.bind(executionCourse.getSigla());

			System.out.println(tipoAula);
			System.out.println(
				executionCourse
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
			System.out.println(
				executionCourse.getLicenciaturaExecucao().getAnoLectivo());

			query.bind(
				executionCourse
					.getLicenciaturaExecucao()
					.getCurso()
					.getSigla());
			query.bind(
				executionCourse.getLicenciaturaExecucao().getAnoLectivo());

			System.out.println(query.toString());

			List result = (List) query.execute();
			lockRead(result);

			System.out.println(result.size());

			//			for (int i = 0; i != result.size(); i++)
			//				aulas.add((IAula) (result.get(i)));
			//			return aulas;
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readBySalaEmSemestre(String nomeSala, Integer semestre)
		throws ExcepcaoPersistencia {
		try {
			List aulas = new ArrayList();
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where sala.nome = $1";
			// 
			//oqlQuery += " and disciplinaExecucao.semestre = $2";
			query.create(oqlQuery);
			query.bind(nomeSala);
			//query.bind(semestre);
			List result = (List) query.execute();
			lockRead(result);
			for (int i = 0; i != result.size(); i++)
				aulas.add((IAula) (result.get(i)));
			return aulas;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readLessonsInPeriod(IAula lesson) throws ExcepcaoPersistencia {
		try {
			List lessonList = null;
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where inicio >= $1 "
				+ "and fim <= $2 "
				+ "and diaSemana = $3";
			// 
			//oqlQuery += " and disciplinaExecucao.semestre = $2";
			query.create(oqlQuery);
			query.bind(lesson.getInicio());
			query.bind(lesson.getFim());
			query.bind(lesson.getDiaSemana());
			//query.bind(semestre);
			lessonList = (List) query.execute();
			lockRead(lessonList);
			return lessonList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
