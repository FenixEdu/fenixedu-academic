/*
 * FrequentaOJB.java
 *
 * Created on 20 de Outubro de 2002, 15:36
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.odmg.QueryException;

import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class FrequentaOJB
	extends ObjectFenixOJB
	implements IFrequentaPersistente {

	public IFrequenta readByAlunoAndDisciplinaExecucao(
		IStudent aluno,
		IDisciplinaExecucao disciplinaExecucao)
		throws ExcepcaoPersistencia {
		try {
			IFrequenta frequenta = null;
			String oqlQuery =
				"select alunodisciplinaexecucao from "
					+ Frequenta.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1"
				+ " and aluno.number = $2";
			query.create(oqlQuery);
			query.bind(disciplinaExecucao.getSigla());
			query.bind(aluno.getNumber());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				frequenta = (IFrequenta) result.get(0);
			return frequenta;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IFrequenta attendanceToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IFrequenta attendanceFromDB = null;

		// If there is nothing to write, simply return.
		if (attendanceToWrite == null)
			return;

		// Read attendance from database.
		attendanceFromDB =
			this.readByAlunoAndDisciplinaExecucao(
				attendanceToWrite.getAluno(),
				attendanceToWrite.getDisciplinaExecucao());

		// If attendance is not in database, then write it.
		if (attendanceFromDB == null)
			super.lockWrite(attendanceToWrite);
		// else If the attendance is mapped to the database, then write any existing changes.
		else if (
			(attendanceToWrite instanceof Frequenta)
				&& ((Frequenta) attendanceFromDB).getCodigoInterno().equals(
					((Frequenta) attendanceToWrite).getCodigoInterno())) {
			super.lockWrite(attendanceToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IFrequenta frequenta) throws ExcepcaoPersistencia {
		super.delete(frequenta);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Frequenta.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readByStudentId(Integer id) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select frequentas from " + Frequenta.class.getName();
			oqlQuery += " where aluno.number = $1";
			query.create(oqlQuery);
			query.bind(id);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select frequentas from " + Frequenta.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1 "
				+ " and disciplinaExecucao.executionPeriod.name = $2 "
				+ " and disciplinaExecucao.executionPeriod.executionYear.year = $3 ";
			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public Integer countStudentsAttendingExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
			Criteria criteria = new Criteria();
			criteria.addEqualTo(
				"disciplinaExecucao.sigla",
				executionCourse.getSigla());
			criteria.addEqualTo(
				"disciplinaExecucao.executionPeriod.name",
				executionCourse.getExecutionPeriod().getName());
			criteria.addEqualTo(
				"disciplinaExecucao.executionPeriod.executionYear.year",
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());
			return new Integer(queryList(Frequenta.class, criteria).size());
	}

}
