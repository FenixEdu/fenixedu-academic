package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Price;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrice;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PriceOJB extends ObjectFenixOJB implements IPersistentPrice {
    
	public PriceOJB() {
	}
    	
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Price.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}		
	}

	public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Price.class.getName()
						    + " where graduationType = $1";
			
			query.create(oqlQuery);
			query.bind(graduationType.getType());
			
			List result = (List) query.execute();
			super.lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}		
	}

}
