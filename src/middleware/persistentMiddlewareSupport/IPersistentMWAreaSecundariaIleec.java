/*
 * Created on 8/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWAreaSecundariaIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWAreaSecundariaIleec
{
    public MWAreaSecundariaIleec readSecondaryAreaById(Integer areaId)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
        
	public MWAreaSecundariaIleec readByName(String name)
			throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
