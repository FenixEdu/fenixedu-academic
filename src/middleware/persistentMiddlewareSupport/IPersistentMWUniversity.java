package middleware.persistentMiddlewareSupport;

import java.util.List;

import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos
 * 28/Out/2003
 */

public interface IPersistentMWUniversity {

	public MWUniversity readByCode(String code) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia;
}