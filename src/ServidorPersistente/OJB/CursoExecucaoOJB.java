/*
 * CursoExecucaoOJB.java
 *
 * Created on 2 de Novembro de 2002, 21:17
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  rpfi
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;

public class CursoExecucaoOJB
	extends ObjectFenixOJB
	implements ICursoExecucaoPersistente {

	public ICursoExecucao readByDegreeAndExecutionYear(
		ICurso curso,
		String anoLectivo)
		throws ExcepcaoPersistencia {
		try {
			ICursoExecucao cursoExecucao = null;
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curso.sigla = $1 and anoLectivo = $2";
			query.create(oqlQuery);
			query.bind(curso.getSigla());
			query.bind(anoLectivo);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				cursoExecucao = (ICursoExecucao) result.get(0);
			return cursoExecucao;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia {
		super.lockWrite(cursoExecucao);
	}

	public void delete(ICursoExecucao cursoExecucao)
		throws ExcepcaoPersistencia {
		super.delete(cursoExecucao);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + CursoExecucao.class.getName();
		super.deleteAll(oqlQuery);
	}

	public ICursoExecucao readBySigla(String sigla)
		throws ExcepcaoPersistencia {
		try {
			ICursoExecucao cursoExecucao = null;
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curso.sigla = $1";
			query.create(oqlQuery);
			query.bind(sigla);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				cursoExecucao = (ICursoExecucao) result.get(0);
			return cursoExecucao;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByExecutionYear(Dominio.IExecutionYear)
	 */
	public List readByExecutionYear(IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {
			ICursoExecucao cursoExecucao = null;
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where executionYear.year = $1";
			query.create(oqlQuery);
			query.bind(executionYear.getYear());
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException e) {
			e.printStackTrace(System.out);
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

	/**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso, Dominio.IExecutionYear)
	 */
	public ICursoExecucao readByDegreeCurricularPlanAndExecutionYear(
		IPlanoCurricularCurso degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {
			ICursoExecucao cursoExecucao = null;
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where executionYear.year = $1"
				+ " and curricularPlan.name = $2 "
				+ " and curricularPlan.curso.sigla = $3";
			query.create(oqlQuery);

			query.bind(executionYear.getYear());
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getCurso().getSigla());

			List result = (List) query.execute();
			lockRead(result);
			return (ICursoExecucao) result.get(0);
		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

	/**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByCursoAndAnoLectivo(Dominio.ICurso, java.lang.String)
	 */
	public ICursoExecucao readByCursoAndAnoLectivo(
		ICurso curso,
		String anoLectivo)
		throws ExcepcaoPersistencia {
		return null;
	}

}
