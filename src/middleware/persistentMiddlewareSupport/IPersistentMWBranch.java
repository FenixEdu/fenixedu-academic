 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWBranch;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWBranch  {
    
    /**
     * 
     * @param degreeCode
     * @param branchCode
     * @return The corresponding Branch
     * @throws PersistentMiddlewareSupportException
     */
    public MWBranch readByDegreeCodeAndBranchCode(Integer degreeCode, Integer branchCode)throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public MWBranch readByDegreeCodeAndBranchName(Integer degreeCode, String branchName) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia; 
	
}
