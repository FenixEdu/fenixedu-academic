/*
 * Created on 5/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWAreasEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreasEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWAreasEspecializacaoIleecOJB
	extends ObjectFenixOJB
	implements IPersistentMWAreasEspecializacaoIleec
{

	public MWAreasEspecializacaoIleecOJB()
	{
	}

	public MWAreasEspecializacaoIleec readSpecializationAreaById(Integer areaId)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("idAreaEspecializacao", areaId);
		return (MWAreasEspecializacaoIleec) queryObject(MWAreasEspecializacaoIleec.class, criteria);
	}

	public MWAreasEspecializacaoIleec readByName(String name)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("nome", name);
		return (MWAreasEspecializacaoIleec) queryObject(MWAreasEspecializacaoIleec.class, criteria);
	}

}
