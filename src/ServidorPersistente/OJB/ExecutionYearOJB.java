package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * Package  ServidorPersistente.OJB
 */
public class ExecutionYearOJB
	extends ObjectFenixOJB
	implements IPersistentExecutionYear {

	/**
	 * Constructor for ExecutionYearOJB.
	 */
	public ExecutionYearOJB() {
		super();
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionYear#readExecutionYearByName(java.lang.String)
	 */
	public IExecutionYear readExecutionYearByName(String year)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("year", year);
		return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
	}
		
	/**
	 * @see ServidorPersistente.IPersistentExecutionYear#readAllExecutionYear()
	 */
	public ArrayList readAllExecutionYear() throws ExcepcaoPersistencia {
		try {

			String oqlQuery = "select all from " + ExecutionYear.class.getName()
						    + " order by year desc";
			query.create(oqlQuery);
			Collection result = (Collection) query.execute();
			lockRead((List) result);
			ArrayList teste = new ArrayList();
			teste.addAll(result);
			return teste;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionYear#writeExecutionYear(Dominio.IExecutionYear)
	 */
	public void writeExecutionYear(IExecutionYear executionYearToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

			IExecutionYear executionYearFromDB = null;

		// If there is nothing to write, simply return.
		if (executionYearToWrite == null)
			return;

		// Read execution Year from database.
		executionYearFromDB =
			this.readExecutionYearByName(executionYearToWrite.getYear());

		// If execution Year is not in database, then write it.
		if (executionYearFromDB == null)
			super.lockWrite(executionYearToWrite);
		// else If the execution Year is mapped to the database, then write any existing changes.
		else if (
			(executionYearToWrite instanceof ExecutionYear)
				&& executionYearFromDB.getIdInternal().equals(executionYearToWrite.getIdInternal())) {
			super.lockWrite(executionYearToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionYear#deleteAll()
	 */
	public boolean deleteAll() {
		try {

			ArrayList list = readAllExecutionYear();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				IExecutionYear executionYear = (IExecutionYear) iter.next();
				delete(executionYear);
			}
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}

	public boolean delete(IExecutionYear executionYear) {
		try {
			
			String oqlQuery =
				"select all from " + ExecutionPeriod.class.getName();
			oqlQuery += " where executionYear.year= $1 ";
			query.create(oqlQuery);
			query.bind(executionYear.getYear());
			List executionPeriods = (List) query.execute();
			
			
			String oqlQuery1 =
				"select all from " + CursoExecucao.class.getName();
			oqlQuery1 += " where executionYear.year= $1 ";
			query.create(oqlQuery1);
			query.bind(executionYear.getYear());
			List executionDegrees = (List) query.execute();

			if (executionPeriods.isEmpty() && executionDegrees.isEmpty())
				super.delete(executionYear);
			else return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.CURRENT);
		return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
	}
	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentExecutionYear#readNotClosedExecutionYears()
	 */
	public List readNotClosedExecutionYears() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addNotEqualTo("state", PeriodState.CLOSED);
		return queryList(ExecutionYear.class, criteria);
	}
}
