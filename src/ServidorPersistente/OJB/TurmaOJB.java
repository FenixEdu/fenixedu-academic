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

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.Turma;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class TurmaOJB extends ObjectFenixOJB implements ITurmaPersistente {

	public void lockWrite(ITurma classToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ITurma classFromDB = null;

		// If there is nothing to write, simply return.
		if (classToWrite == null)
			return;

		// Read class from database.
		classFromDB =
			this.readByNameAndExecutionDegreeAndExecutionPeriod(
				classToWrite.getNome(),
				classToWrite.getExecutionDegree(),
				classToWrite.getExecutionPeriod());

		// If class is not in database, then write it.
		if (classFromDB == null)
			super.lockWrite(classToWrite);
		// else If the class is mapped to the database, then write any existing changes.
		else if (
			(classToWrite instanceof Turma)
				&& ((Turma) classFromDB).getIdInternal().equals(
					((Turma) classToWrite).getIdInternal())) {
			super.lockWrite(classToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ITurma turma) throws ExcepcaoPersistencia {
		try {
			ITurmaTurno turmaTurno = null;
			TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 ";
			oqlQuery += " and turma.executionPeriod.name = $2 ";
			oqlQuery += " and turma.executionPeriod.executionYear.year = $3 ";
			oqlQuery += " and turma.executionDegree.executionYear.year = $4 ";
			oqlQuery += " and turma.executionDegree.curricularPlan.name = $5 ";
			oqlQuery
				+= " and turma.executionDegree.curricularPlan.degree.sigla = $6 ";

			query.create(oqlQuery);
			query.bind(turma.getNome());
			query.bind(turma.getExecutionPeriod().getName());
			query.bind(turma.getExecutionPeriod().getExecutionYear().getYear());
			query.bind(turma.getExecutionDegree().getExecutionYear().getYear());
			query.bind(
				turma.getExecutionDegree().getCurricularPlan().getName());
			query.bind(
				turma
					.getExecutionDegree()
					.getCurricularPlan()
					.getDegree()
					.getSigla());
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

	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Turma.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
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
				+ " and executionDegree.curricularPlan.degree.sigla = $6";

			query.create(oqlQuery);

			query.bind(executionPeriod.getExecutionYear().getYear());
			query.bind(executionPeriod.getName());

			query.bind(curricularYear);

			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(
				executionDegree.getCurricularPlan().getDegree().getSigla());

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
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();	
		criteria.addEqualTo("nome", className);	
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear().getYear());
		criteria.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear().getYear());
		criteria.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan().getName());
		criteria.addEqualTo("executionDegree.curricularPlan.degree.sigla", executionDegree.getCurricularPlan().getDegree().getSigla());
		
		return (ITurma) queryObject(Turma.class, criteria);
//		try {
//			String oqlQuery = "select turmas from " + Turma.class.getName();
//			//				oqlQuery += " where executionPeriod.executionYear.year = $1"
//			//					+ " and executionPeriod.name = $2"
//			//					+ " and nome = $3"
//			//					+ " and executionDegree.executionYear.year = $4"
//			//					+ " and executionDegree.curricularPlan.name = $5"
//			//					+ " and executionDegree.curricularPlan.degree.sigla = $6";
//
//			oqlQuery += " where nome = $1 ";
//			oqlQuery += " and executionPeriod.name = $2 ";
//			oqlQuery += " and executionPeriod.executionYear.year = $3 ";
//			oqlQuery += " and executionDegree.executionYear.year = $4 ";
//			oqlQuery += " and executionDegree.curricularPlan.name = $5 ";
//			oqlQuery
//				+= " and executionDegree.curricularPlan.degree.sigla = $6 ";
//			query.create(oqlQuery);
//			query.bind(className);
//			query.bind(executionPeriod.getName());
//			query.bind(executionPeriod.getExecutionYear().getYear());
//
//			query.bind(executionDegree.getExecutionYear().getYear());
//			query.bind(executionDegree.getCurricularPlan().getName());
//			query.bind(
//				executionDegree.getCurricularPlan().getDegree().getSigla());
//
//			List result = (List) query.execute();
//			lockRead(result);
//			ITurma iClass = null;
//			if (!result.isEmpty())
//				iClass = (ITurma) result.get(0);
//			return iClass;
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
	}

	public List readByDegreeNameAndDegreeCode(String name, String code)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery
				+= " where executionDegree.curricularPlan.degree.nome = $1"
				+ " and executionDegree.curricularPlan.degree.sigla = $2";

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

	public List readByExecutionDegree(ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select turmas from " + Turma.class.getName();
			oqlQuery += " where executionDegree.executionYear.year = $1"
				+ " and executionDegree.curricularPlan.name = $2"
				+ " and executionDegree.curricularPlan.degree.sigla = $3";

			query.create(oqlQuery);

			query.bind(executionDegree.getExecutionYear().getYear());
			query.bind(executionDegree.getCurricularPlan().getName());
			query.bind(
				executionDegree.getCurricularPlan().getDegree().getSigla());

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionPeriodAndDegreeType(
		IExecutionPeriod executionPeriod,
		TipoCurso tipoCurso)
		throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.executionYear.year",
			executionPeriod.getExecutionYear().getYear());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo(
			"executionDegree.curricularPlan.degree.tipoCurso",
			tipoCurso);
		//Query queryPB = new QueryByCriteria(Turma.class, criteria);
		return queryList(Turma.class, criteria);
	}

	public List readByExecutionDegreeAndExecutionPeriod(
		ICursoExecucao execucao,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionPeriod.executionYear.year",
			executionPeriod.getExecutionYear().getYear());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo(
			"executionDegree.executionYear.year",
			execucao.getExecutionYear().getYear());
		criteria.addEqualTo(
			"executionDegree.curricularPlan.name",
			execucao.getCurricularPlan().getName());
		criteria.addEqualTo(
			"executionDegree.curricularPlan.degree.sigla",
			execucao.getCurricularPlan().getDegree().getSigla());
		//Query queryPB = new QueryByCriteria(Turma.class, criteria);
		return queryList(Turma.class, criteria);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.ITurmaPersistente#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"associatedShifts.disciplinaExecucao.idInternal",
			executionCourse.getIdInternal());
		return queryList(Turma.class, criteria, true);
	}
}
