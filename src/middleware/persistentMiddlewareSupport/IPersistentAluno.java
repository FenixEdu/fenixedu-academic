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

import middleware.middlewareDomain.MWAluno;
import middleware.persistentMiddlewareSupport.exceptions.*;


public interface IPersistentAluno  {
    
    /**
     * 
     * @return All the STudents from the old Database
     * @throws PersistentMiddlewareSupportException
     */
    public List readAll() throws PersistentMiddlewareSupportException;
    
    
    /**
     * 
     * @param Student Number
     * @return The Student corresponding to this number from the Old Database
     * @throws PersistentMiddlewareSupportException
     */
    public MWAluno readByNumber(Integer number)throws PersistentMiddlewareSupportException; 
	
}
