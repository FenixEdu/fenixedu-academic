
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWCurricularCourseScope  {
    
	/**
	 * 
	 * @return
	 * @throws PersistentMiddlewareSupportException
	 * @throws ExcepcaoPersistencia
	 */
    public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
    
    
	
}
