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

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.ICursoExecucao;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class TurmaTurnoOJB
	extends ObjectFenixOJB
	implements ITurmaTurnoPersistente {

	public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno)
		throws ExcepcaoPersistencia {
		try {
			ITurmaTurno turmaTurno = null;
			String oqlQuery =
				"select turmaturno from " + TurmaTurno.class.getName()
				// Unique from Class
	+" where turma.nome = $1"
		+ " and turma.executionPeriod.name = $2"
		+ " and turma.executionPeriod.executionYear.year = $3"
		+ " and turma.executionDegree.executionYear.year = $4"
		+ " and turma.executionDegree.curricularPlan.name = $5"
		+ " and turma.executionDegree.curricularPlan.degree.sigla = $6"
				// Unique from Shift
	+" and turno.nome = $7"
		+ " and turno.disciplinaExecucao.sigla = $8"
		+ " and turno.disciplinaExecucao.executionPeriod.name = $9"
		+ " and turno.disciplinaExecucao.executionPeriod.executionYear.year = $10";

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
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				turmaTurno = (ITurmaTurno) result.get(0);
			return turmaTurno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ITurmaTurno classShiftToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ITurmaTurno classShiftFromDB = null;

		// If there is nothing to write, simply return.
		if (classShiftToWrite == null)
			return;

		// Read classShift from database.
		classShiftFromDB =
			this.readByTurmaAndTurno(
				classShiftToWrite.getTurma(),
				classShiftToWrite.getTurno());

		// If classShift is not in database, then write it.
		if (classShiftFromDB == null)
			super.lockWrite(classShiftToWrite);
		// else If the classShift is mapped to the database, then write any existing changes.
		else if (
			(classShiftToWrite instanceof TurmaTurno)
				&& ((TurmaTurno) classShiftFromDB).getCodigoInterno().equals(
					((TurmaTurno) classShiftToWrite).getCodigoInterno())) {
			super.lockWrite(classShiftToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
		super.delete(turmaTurno);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + TurmaTurno.class.getName();
		super.deleteAll(oqlQuery);
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
				+ " and turma.executionDegree.curricularPlan.degree.sigla = $6 ";
			query.create(oqlQuery);

			query.bind(group.getNome());
			query.bind(group.getExecutionPeriod().getName());
			query.bind(group.getExecutionPeriod().getExecutionYear().getYear());

			ICursoExecucao executionDegree = group.getExecutionDegree();
			query.bind(executionDegree.getExecutionYear().getYear());

			query.bind(executionDegree.getCurricularPlan().getName());

			query.bind(
				executionDegree.getCurricularPlan().getDegree().getSigla());

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
					+ " and turno.disciplinaExecucao.sigla = $2"
					+ " and turno.disciplinaExecucao.executionPeriod.name = $3"
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

	public List readByShiftAndExecutionDegree(
		ITurno turno,
		ICursoExecucao execucao)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("turno.nome", turno.getNome());
		criteria.addEqualTo(
			"turma.executionDegree.curricularPlan.degree.sigla",
			execucao.getCurricularPlan().getDegree().getSigla());
		return queryList(TurmaTurno.class, criteria);
	}

}
