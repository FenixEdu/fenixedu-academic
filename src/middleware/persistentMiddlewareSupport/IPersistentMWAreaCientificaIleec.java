/*
 * Created on 18/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.middlewareDomain.MWAreaCientificaIleec;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentMWAreaCientificaIleec
{
	public List readAll() throws ExcepcaoPersistencia;
	public MWAreaCientificaIleec readById(Integer id) throws ExcepcaoPersistencia;
}
