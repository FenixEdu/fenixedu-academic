/*
 * TurmaTurnoOJB.java
 *
 * Created on 19 de Outubro de 2002, 15:23
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ICursoExecucao;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaTurnoPersistente;

public class TurmaTurnoOJB
	extends ObjectFenixOJB
	implements ITurmaTurnoPersistente {

	public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno)
		throws ExcepcaoPersistencia {
		try {
			ITurmaTurno turmaTurno = null;
			String oqlQuery =
				"select turmaturno from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 and turno.nome = $2";
			query.create(oqlQuery);
			query.bind(turma.getNome());
			query.bind(turno.getNome());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turmaTurno = (ITurmaTurno) result.get(0);
			return turmaTurno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
		super.lockWrite(turmaTurno);
	}

	public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
		super.delete(turmaTurno);
	}

	public void delete(String nomeTurma, String nomeTurno)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 and turno.nome = $2";
			query.create(oqlQuery);
			query.bind(nomeTurma);
			query.bind(nomeTurno);
			List result = (List) query.execute();
			delete(((ITurmaTurno) (result.get(0))));
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + TurmaTurno.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readTurnosDeTurma(String nomeTurma)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1";
			query.create(oqlQuery);
			query.bind(nomeTurma);
			List result = (List) query.execute();
			lockRead(result);

			List finalResult = new ArrayList();
			for (int i = 0; i != result.size(); i++)
				finalResult.add(((ITurmaTurno) (result.get(i))).getTurno());

			return finalResult;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * Returns a shift list
	 * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ITurma)
	 */
	public List readByClass(ITurma group) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select turnos from " + TurmaTurno.class.getName();
			oqlQuery += " where turma.nome = $1 "
				+ " and turma.executionPeriod.name = $2 "
				+ " and turma.executionPeriod.executionYear.year = $3"
				+ " and turma.executionDegree.executionYear.year = $4"
				+ " and turma.executionDegree.curricularPlan.name = $5"
				+ " and turma.executionDegree.curricularPlan.curso.sigla = $6 ";
			query.create(oqlQuery);

			query.bind(group.getNome());
			query.bind(group.getExecutionPeriod().getName());
			query.bind(group.getExecutionPeriod().getExecutionYear().getYear());
			
			ICursoExecucao executionDegree = group.getExecutionDegree();
			query.bind(executionDegree.getExecutionYear().getYear());
			
			
			query.bind(executionDegree.getCurricularPlan().getName());
			
			query.bind(executionDegree.getCurricularPlan().getCurso().getSigla());

			List result = (List) query.execute();
			lockRead(result);
			
			List shiftList = new ArrayList();
			Iterator resultIterator = result.iterator();
			while (resultIterator.hasNext()) {
				ITurmaTurno classShift = (ITurmaTurno) resultIterator.next();
				shiftList.add(classShift.getTurno());
			}
			return shiftList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readClassesWithShift(ITurno turno)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from "
					+ TurmaTurno.class.getName()
					+ " where turno.nome = $1"
					+ " and  turno.disciplinaExecucao.sigla = $2"
					+ " and  turno.disciplinaExecucao.executionPeriod.name = $3"
					+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $4";
			query.create(oqlQuery);
			query.bind(turno.getNome());
			query.bind(turno.getDisciplinaExecucao().getSigla());
			query.bind(
				turno.getDisciplinaExecucao().getExecutionPeriod().getName());
			query.bind(
				turno
					.getDisciplinaExecucao()
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List classesList = (List) query.execute();
			lockRead(classesList);

			return classesList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
