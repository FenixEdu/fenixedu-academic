package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;

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
		try {

			IExecutionYear executionYear = null;
			String oqlQuery =
				"select all from " + ExecutionYear.class.getName();
			oqlQuery += " where year = $1 ";
			query.create(oqlQuery);
			query.bind(year);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				executionYear = (IExecutionYear) result.get(0);
			return executionYear;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/**
	 * @see ServidorPersistente.IPersistentExecutionYear#readAllExecutionYear()
	 */
	public ArrayList readAllExecutionYear() throws ExcepcaoPersistencia {
		try {

			String oqlQuery =
				"select all from " + ExecutionYear.class.getName();
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
	public boolean writeExecutionYear(IExecutionYear executionYear) {
		try {
			super.lockWrite(executionYear);
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}

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
				super.delete(executionYear);
			}
			return true;
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}
}
