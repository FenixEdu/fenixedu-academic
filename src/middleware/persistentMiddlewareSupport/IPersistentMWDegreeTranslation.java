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

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;


public interface IPersistentMWDegreeTranslation  {
    
    public MWDegreeTranslation readByDegreeCode(Integer degreeCode) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public MWDegreeTranslation readByDegree(ICurso degree) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	
}
