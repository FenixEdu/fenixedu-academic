package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;

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
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readExecutionPeriodByNameAndExecutionYear(java.lang.String, Dominio.IExecutionYear)
	 */
	public IExecutionPeriod readExecutionPeriodByNameAndExecutionYear(
		String name,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia {
		try {

			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			oqlQuery += " where name = $1 and executionYear.year= $2 ";
			query.create(oqlQuery);
			query.bind(name);
			query.bind(executionYear.getYear());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				executionPeriod = (IExecutionPeriod) result.get(0);
			return executionPeriod;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readAllExecutionPeriod()
	 */
	public ArrayList readAllExecutionPeriod() throws ExcepcaoPersistencia {
		try {

			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();

			query.create(oqlQuery);

			List result = (List) query.execute();
			lockRead(result);

			return (ArrayList) executionPeriod;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#writeExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public boolean writeExecutionPeriod(IExecutionPeriod executionPeriod) {
		try {
			super.lockWrite(executionPeriod);
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#delete(Dominio.IExecutionPeriod)
	 */
	public boolean delete(IExecutionPeriod executionPeriod) {
		try {
			super.delete(executionPeriod);
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionPeriod#deleteAll()
	 */
	public boolean deleteAll() {
		try {

			ArrayList list = readAllExecutionPeriod();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				IExecutionPeriod executionPeriod =
					(IExecutionPeriod) iter.next();
				super.delete(executionPeriod);
			}
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}

	/**
	 * :FIXME: this is wrong if we have more than one EXECUTION_PERIOD
	 * @see ServidorPersistente.IPersistentExecutionPeriod#readActualExecutionPeriod()
	 */
	public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia{
		try {
			IExecutionPeriod executionPeriod = null;
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			
			query.create(oqlQuery);
			
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
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
		IExecutionYear executionYear) throws ExcepcaoPersistencia {
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

}
