/*
 * IPersistentCountry.java
 *
 * Created on 28 of December 2002, 10:11
 */
 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWStudentClass  {
    
    /**
     * 
     * @return All List of @see middleware.middlewareDomain.MWStudentClass
     */
    public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	
}
