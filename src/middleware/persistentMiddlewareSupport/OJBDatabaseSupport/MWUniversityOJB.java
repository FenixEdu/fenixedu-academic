
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class MWUniversityOJB extends ObjectFenixOJB implements IPersistentMWUniversity {
    
    public MWUniversityOJB() {
    }


	public MWUniversity readByCode(String code) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("universityCode", code);
		return (MWUniversity) queryObject(MWUniversity.class, criteria);
	}
    
	public List readAll() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWUniversity.class, criteria);
	}

}