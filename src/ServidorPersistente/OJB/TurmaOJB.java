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
			oqlQuery += " and turma.executionPeriod.name = $2 ";
			oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
			oqlQuery += " and turma.cursoExecucao.executionYear.year = $4 ";
			oqlQuery += " and turma.cursoExecucao.degreeCurricularPlan.nome = $5 ";
			oqlQuery += " and turma.cursoExecucao.degreeCurricularPlan.curso.sigla = $6 ";

			query.create(oqlQuery);
			query.bind(turma.getNome());
			query.bind(turma.getExecutionPeriod().getName());
			query.bind(turma.getExecutionPeriod().getExecutionYear().getYear());
			query.bind(turma.getExecutionDegree().getExecutionYear().getYear());
			query.bind(turma.getExecutionDegree().getCurricularPlan().getName());
			query.bind(turma.getExecutionDegree().getCurricularPlan().getCurso().getSigla());			
			List result = (List) query.execute();
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turmaTurno = (ITurmaTurno) iterador.next();
				turmaTurnoOJB.delete(turmaTurno);
			}
			super.delete(turma);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		super.delete(turma);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Turma.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while(iter.hasNext()){
				delete((ITurma) iter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
		} 
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
	/**
	 * @deprecated
	 * @see ServidorPersistente.ITurmaPersistente#readBySemestreAndAnoCurricularAndSiglaLicenciatura(Integer, Integer, String)
	 */
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


	/**
	 * 
	 * @see ServidorPersistente.ITurmaPersistente#readByNameAndExecutionDegreeAndExecutionPeriod(String, ICursoExecucao, IExecutionPeriod)
	 *  
	 */
	public ITurma readByNameAndExecutionDegreeAndExecutionPeriodAndCurricularYear(
		String className,
		ICursoExecucao executionDegree,
		IExecutionPeriod executionPeriod, 
		Integer curricularYear) throws ExcepcaoPersistencia {
			try {
				String oqlQuery = "select turmas from " + Turma.class.getName();
				oqlQuery += " where executionPeriod.executionYear.year = $1"
					+ " and executionPeriod.name = $2"
					+ " and nome = $3"
					+ " and anoCurricular = $4"
					+ " and executionDegree.executionYear.year = $5"
					+ " and executionDegree.curricularPlan.name = $6"
					+ " and executionDegree.curricularPlan.curso.sigla = $7";

			
				query.create(oqlQuery);

				query.bind(executionPeriod.getExecutionYear().getYear());
				query.bind(executionPeriod.getName());

				query.bind(className);
				query.bind(curricularYear);

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

	public List readByDegreeNameAndDegreeCode(String name, String code) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery += " where executionDegree.curricularPlan.curso.nome = $1"
				+ " and executionDegree.curricularPlan.curso.sigla = $2";

			query.create(oqlQuery);
			query.bind(name);
			query.bind(code);
			
			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public List readByExecutionPeriod(IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
				try {
				String oqlQuery = "select turmas from " + Turma.class.getName();
				oqlQuery += " where executionPeriod.executionYear.year = $1"
					+ " and executionPeriod.name = $2";
						
				query.create(oqlQuery);

				query.bind(executionPeriod.getExecutionYear().getYear());
				query.bind(executionPeriod.getName());

				List result = (List) query.execute();
				lockRead(result);

				return result;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	}

}
