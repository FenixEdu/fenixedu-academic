/*
 * Created on 5/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWAreaEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWAreaEspecializacaoIleec
{
	public MWAreaEspecializacaoIleec readSpecializationAreaById(Integer areaId)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;

	public MWAreaEspecializacaoIleec readByName(String name)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
