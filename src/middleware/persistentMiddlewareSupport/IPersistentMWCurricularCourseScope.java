
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWCurricularCourseScope  {
    
    public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public List readAllBySpan(Integer spanNumber, Integer numberSpanOfElements) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public Integer countAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
