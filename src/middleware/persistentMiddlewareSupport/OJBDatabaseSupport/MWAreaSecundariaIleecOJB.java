/*
 * Created on 8/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWAreaSecundariaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaSecundariaIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWAreaSecundariaIleecOJB extends ObjectFenixOJB implements IPersistentMWAreaSecundariaIleec
{

	public MWAreaSecundariaIleecOJB()
	{
	}

	public MWAreaSecundariaIleec readSecondaryAreaById(Integer areaId)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("idAreaSecundaria", areaId);
		return (MWAreaSecundariaIleec) queryObject(MWAreaSecundariaIleec.class, criteria);
	}

	public MWAreaSecundariaIleec readByName(String name)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("nome", name);
		return (MWAreaSecundariaIleec) queryObject(MWAreaSecundariaIleec.class, criteria);
	}

}
