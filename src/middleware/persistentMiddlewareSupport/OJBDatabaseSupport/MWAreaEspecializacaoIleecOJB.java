/*
 * Created on 5/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWAreaEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaEspecializacaoIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWAreaEspecializacaoIleecOJB
	extends ObjectFenixOJB
	implements IPersistentMWAreaEspecializacaoIleec
{

	public MWAreaEspecializacaoIleecOJB()
	{
	}

	public MWAreaEspecializacaoIleec readSpecializationAreaById(Integer areaId)
		throws ExcepcaoPersistencia
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("idAreaEspecializacao", areaId);
		return (MWAreaEspecializacaoIleec) queryObject(MWAreaEspecializacaoIleec.class, criteria);
	}

	public MWAreaEspecializacaoIleec readByName(String name)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("nome", name);
		return (MWAreaEspecializacaoIleec) queryObject(MWAreaEspecializacaoIleec.class, criteria);
	}

}
