/*
 * Created on 5/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import middleware.middlewareDomain.MWAreasEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWAreasEspecializacaoIleec
{
	public MWAreasEspecializacaoIleec readSpecializationAreaById(Integer areaId)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;

	public MWAreasEspecializacaoIleec readByName(String name)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}
