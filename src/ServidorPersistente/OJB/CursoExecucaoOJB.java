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

import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class CursoExecucaoOJB
	extends ObjectFenixOJB
	implements ICursoExecucaoPersistente {

	public void lockWrite(ICursoExecucao cursoExecucaoToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ICursoExecucao cursoExecucaoFromDB = null;

		// If there is nothing to write, simply return.
		if (cursoExecucaoToWrite == null)
			return;

		// Read cursoExecucao from database.
		cursoExecucaoFromDB =
			this.readByDegreeCurricularPlanAndExecutionYear(
				cursoExecucaoToWrite.getCurricularPlan(),
				cursoExecucaoToWrite.getExecutionYear());

		// If cursoExecucao is not in database, then write it.
		if (cursoExecucaoFromDB == null)
			super.lockWrite(cursoExecucaoToWrite);
		// else If the cursoExecucao is mapped to the database, then write any existing changes.
		else if (
			(cursoExecucaoToWrite instanceof CursoExecucao)
				&& ((CursoExecucao) cursoExecucaoFromDB)
					.getCodigoInterno()
					.equals(
					((CursoExecucao) cursoExecucaoToWrite)
						.getCodigoInterno())) {
			super.lockWrite(cursoExecucaoToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia {
		// Delete all Classes associated
		List classes =
			SuportePersistenteOJB
				.getInstance()
				.getITurmaPersistente()
				.readByExecutionDegree(executionDegree);

		Iterator iterator = classes.iterator();
		while (iterator.hasNext()) {
			SuportePersistenteOJB.getInstance().getITurmaPersistente().delete(
				(ITurma) iterator.next());
		}
		super.delete(executionDegree);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				delete((ICursoExecucao) iterator.next());
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * 
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readBySigla(String)
	 * @deprecated 
	 */
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
		IDegreeCurricularPlan degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where executionYear.year = $1"
				+ " and curricularPlan.name = $2 "
				+ " and curricularPlan.degree.sigla = $3";
			query.create(oqlQuery);

			query.bind(executionYear.getYear());
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) {
				return (ICursoExecucao) result.get(0);
			} else {
				return null;
			}

		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

	/**
	 * @see ServidorPersistente.ICursoExecucaoPersistente#readByDegreeAndExecutionYear(Dominio.ICurso, Dominio.IExecutionYear)
	 */
	public ICursoExecucao readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
		String degreeInitials,
		String nameDegreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where executionYear.year = $1"
				+ " and curricularPlan.name = $2 "
				+ " and curricularPlan.degree.sigla = $3";
			query.create(oqlQuery);

			query.bind(executionYear.getYear());
			query.bind(nameDegreeCurricularPlan);
			query.bind(degreeInitials);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				return (ICursoExecucao) result.get(0);
			else
				return null;
		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

	public List readMasterDegrees(String executionYear)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery += " where executionYear.year = $1"
				+ " and curricularPlan.degree.tipoCurso = $2";
			query.create(oqlQuery);

			query.bind(executionYear);
			query.bind(new Integer(TipoCurso.MESTRADO));

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}

	}

	public ICursoExecucao readByDegreeNameAndExecutionYear(
		String degreeName,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from "
					+ CursoExecucao.class.getName()
					+ " where executionYear.year = $1"
					+ " and curricularPlan.degree.nome = $2";
			query.create(oqlQuery);

			query.bind(executionYear.getYear());
			query.bind(degreeName);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				return (ICursoExecucao) result.get(0);
			return null;
		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

}
