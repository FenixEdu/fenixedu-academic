
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class MWBranchOJB extends ObjectFenixOJB implements IPersistentMWBranch {
    
    public MWBranchOJB() {
    }

	public MWBranch readByDegreeCodeAndBranchCode(Integer degreeCode, Integer branchCode) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
	
		criteria.addEqualTo("branchcode", branchCode);
		criteria.addEqualTo("degreecode", degreeCode);
		//Query query = new QueryByCriteria(MWBranch.class,criteria);
	
		return (MWBranch) queryObject(MWBranch.class, criteria);
	}
}