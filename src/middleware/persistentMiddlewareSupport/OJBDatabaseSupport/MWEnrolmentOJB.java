
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class MWEnrolmentOJB extends ObjectFenixOJB implements IPersistentMWEnrolment {
    
    public MWEnrolmentOJB() {
    }


	public List readByStudentNumber(Integer number) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", number);
		return queryList(MWEnrolment.class, criteria);
	}
    
}