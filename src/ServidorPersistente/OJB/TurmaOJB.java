/*
 * TurmaOJB.java
 *
 * Created on 17 de Outubro de 2002, 18:44
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.Turma;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaPersistente;

public class TurmaOJB extends ObjectFenixOJB implements ITurmaPersistente {


	public void lockWrite(ITurma turma) throws ExcepcaoPersistencia {
		super.lockWrite(turma);
	}

	public void delete(ITurma turma) throws ExcepcaoPersistencia {
		try {
			ITurmaTurno turmaTurno = null;
			TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 ";

			query.create(oqlQuery);
			query.bind(turma.getNome());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turmaTurno = (ITurmaTurno) iterador.next();
				turmaTurnoOJB.delete(turmaTurno);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		super.delete(turma);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Turma.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Turma.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readBySemestreAndAnoCurricularAndSiglaLicenciatura(
		Integer semestre,
		Integer anoCurricular,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery += " where semestre = $1";
			oqlQuery += " and anoCurricular = $2";
			oqlQuery += " and licenciatura.sigla = $3";
			query.create(oqlQuery);
			query.bind(semestre);
			query.bind(anoCurricular);
			query.bind(siglaLicenciatura);
			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionPeriodAndCurricularYearAndExecutionDegree(Dominio.IExecutionPeriod, java.lang.Integer, Dominio.ICursoExecucao)
	 */
	public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(
		IExecutionPeriod executionPeriod,
		Integer curricularYear,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery += " where executionPeriod.executionYear.year = $1"
				+ " and executionPeriod.name = $2"
				+ " and anoCurricular = $3"
				+ " and executionDegree.executionYear.year = $4"
				+ " and executionDegree.curricularPlan.name = $5"
				+ " and executionDegree.curricularPlan.curso.sigla = $6";

			
			query.create(oqlQuery);

			query.bind(executionPeriod.getExecutionYear().getYear());
			query.bind(executionPeriod.getName());

			query.bind(curricularYear);

			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(
				executionDegree.getCurricularPlan().getCurso().getSigla());
			
			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.ITurmaPersistente#readByNameAndExecutionDegreeAndExecutionPeriod(java.lang.String, Dominio.ICursoExecucao, Dominio.IExecutionPeriod)
	 */
	public ITurma readByNameAndExecutionDegreeAndExecutionPeriod(
		String className,
		ICursoExecucao executionDegree,
		IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
			try {
				String oqlQuery = "select turmas from " + Turma.class.getName();
				oqlQuery += " where executionPeriod.executionYear.year = $1"
					+ " and executionPeriod.name = $2"
					+ " and nome = $3"
					+ " and executionDegree.executionYear.year = $4"
					+ " and executionDegree.curricularPlan.name = $5"
					+ " and executionDegree.curricularPlan.curso.sigla = $6";

			
				query.create(oqlQuery);

				query.bind(executionPeriod.getExecutionYear().getYear());
				query.bind(executionPeriod.getName());

				query.bind(className);

				query.bind(executionDegree.getExecutionYear().getYear());
				query.bind(executionDegree.getCurricularPlan().getName());
				
				query.bind(
					executionDegree.getCurricularPlan().getCurso().getSigla());
				
				List result = (List) query.execute();
				lockRead(result);
				ITurma iClass = null; 
				if (!result.isEmpty())
					iClass = (ITurma) result.get(0);
				return iClass;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	}

}
