package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * Package ServidorPersistente.OJB
 * 
 */
public class ExecutionPeriodOJB
	extends ObjectFenixOJB
	implements IPersistentExecutionPeriod {

	/**
	 * Constructor for ExecutionPeriodOJB.
	 */
	public ExecutionPeriodOJB() {
		super();
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readAllExecutionPeriod()
	 */
	public List readAllExecutionPeriod() throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();

			query.create(oqlQuery);

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#writeExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public void writeExecutionPeriod(IExecutionPeriod executionPeriodToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IExecutionPeriod executionPeriodFromDB = null;

		// If there is nothing to write, simply return.
		if (executionPeriodToWrite == null)
			return;

		// Read execution period from database.
		executionPeriodFromDB =
			this.readByNameAndExecutionYear(
				executionPeriodToWrite.getName(),
				executionPeriodToWrite.getExecutionYear());

		// If execution period is not in database, then write it.
		if (executionPeriodFromDB == null)
			super.lockWrite(executionPeriodToWrite);
		// else If the execution period is mapped to the database, then write any existing changes.
		else if (
			(executionPeriodToWrite instanceof ExecutionPeriod)
				&& ((ExecutionPeriod) executionPeriodFromDB)
					.getIdInternal()
					.equals(
					((ExecutionPeriod) executionPeriodToWrite)
						.getIdInternal())) {
			super.lockWrite(executionPeriodToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#delete(Dominio.IExecutionPeriod)
	 */
	public boolean delete(IExecutionPeriod executionPeriod) {
		List executionCourses = new ArrayList();
		List classes = new ArrayList();
		try {

			executionCourses =
				SuportePersistenteOJB
					.getInstance()
					.getIDisciplinaExecucaoPersistente()
					.readByExecutionPeriod(executionPeriod);
			classes =
				SuportePersistenteOJB
					.getInstance()
					.getITurmaPersistente()
					.readByExecutionPeriod(executionPeriod);

			if (classes.isEmpty() && executionCourses.isEmpty()) {
				super.delete(executionPeriod);
			} else
				return false;

		} catch (ExcepcaoPersistencia e) {
			return false;
		}
		return true;
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#deleteAll()
	 */
	public boolean deleteAll() {
		try {
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IExecutionPeriod executionPeriod =
					(IExecutionPeriod) iter.next();
				delete(executionPeriod);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readActualExecutionPeriod()
	 */
	public IExecutionPeriod readActualExecutionPeriod()
		throws ExcepcaoPersistencia {
		try {
			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from "
					+ ExecutionPeriod.class.getName()
					+ " where state = $1";

			query.create(oqlQuery);
			query.bind(PeriodState.CURRENT);

			List result = (List) query.execute();
			lockRead(result);
			if ((result != null) && (!result.isEmpty()))
				executionPeriod = (IExecutionPeriod) result.get(0);
			return executionPeriod;
		} catch (QueryException e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readByNameAndExecutionYear(java.lang.String, Dominio.IExecutionYear)
	 */
	public IExecutionPeriod readByNameAndExecutionYear(
		String executionPeriodName,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {

			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			oqlQuery += " where name = $1 and executionYear.year= $2 ";
			query.create(oqlQuery);
			query.bind(executionPeriodName);
			query.bind(executionYear.getYear());

			List result = (List) query.execute();

			lockRead(result);

			if (result.size() != 0)
				return (IExecutionPeriod) result.get(0);

			return executionPeriod;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readBySemesterAndExecutionYear(java.lang.String, Dominio.IExecutionYear)
	 */
	public IExecutionPeriod readBySemesterAndExecutionYear(
		Integer semester,
		IExecutionYear year)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("semester", semester);
		criteria.addEqualTo("executionYear.year", year.getYear());
		return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);
	}

}
