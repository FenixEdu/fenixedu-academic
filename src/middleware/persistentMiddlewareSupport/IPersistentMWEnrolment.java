
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWEnrolment  {
    
    /**
     * 
     * @param number
     * @return All of The Student Enrolments
     * @throws PersistentMiddlewareSupportException
     * @throws ExcepcaoPersistencia
     */
    
    public List readByStudentNumber(Integer number) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
    
}