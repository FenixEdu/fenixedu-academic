/*
 * Created on 21/Out/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IPeriod;
import Dominio.Period;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPeriod;

/**
 * @author Ana e Ricardo
 *
 */
public class PeriodOJB extends ObjectFenixOJB implements IPersistentPeriod{

	public List readBy(Calendar startDate)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("startDate", startDate);
		return queryList(Period.class, criteria);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Period.class.getName();
//			oqlQuery += " order by season asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void delete(IPeriod period) throws ExcepcaoPersistencia {
		// TO DO falta apagar as ligações a outras tabelas 
		super.delete(period);

	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Period.class.getName();
		super.deleteAll(oqlQuery);
	}

}
