
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWCurricularCourse;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWCurricularCourse  {
    
    /**
     * 
     * @param code
     * @return
     * @throws PersistentMiddlewareSupportException
     * @throws ExcepcaoPersistencia
     */
	public MWCurricularCourse readByCode(String code) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
    
    
	
}
