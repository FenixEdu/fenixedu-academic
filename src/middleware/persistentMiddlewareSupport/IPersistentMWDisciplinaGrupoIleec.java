/*
 * Created on 9/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.middlewareDomain.MWGruposIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWDisciplinaGrupoIleec
{
	public List readByGrupo(MWGruposIleec grupoILeec) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	

}
