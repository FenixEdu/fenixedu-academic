
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
	
	/**
	 * @author Nuno Correia
	 * @author Ricardo Rodrigues
	 * 
	 * @param degreeCode
	 * @param branchName
	 * @return
	 * @throws PersistentMiddlewareSupportException
	 * @throws ExcepcaoPersistencia
	 */

	public MWBranch readByDegreeCodeAndBranchName(Integer degreeCode, String branchName) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
	
		criteria.addEqualTo("description", branchName);
		criteria.addEqualTo("degreeCode", degreeCode);
	
		return (MWBranch) queryObject(MWBranch.class, criteria);
	}
    
}