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

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;


public interface IPersistentMWAluno extends IPersistentObject
{
    /**
     * 
     * @return All the STudents from the old Database
     * @throws PersistentMiddlewareSupportException
     */
    public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
    public List readAllBySpan(Integer spanNumber, Integer numberSpanOfElements) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public Integer countAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
    /**
     * 
     * @param Student Number
     * @return The Student corresponding to this number from the Old Database
     * @throws PersistentMiddlewareSupportException
     */
    public MWStudent readByNumber(Integer number)throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;

    public Iterator readAllBySpanIterator(Integer spanNumber, Integer numberOfElementsInSpan);
	
}
