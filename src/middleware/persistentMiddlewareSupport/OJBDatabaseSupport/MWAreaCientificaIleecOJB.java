/*
 * Created on 18/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaCientificaIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWAreaCientificaIleecOJB extends ObjectFenixOJB implements IPersistentMWAreaCientificaIleec
{
    public MWAreaCientificaIleecOJB()
    {
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(MWAreaCientificaIleec.class, criteria);
    }

	public MWAreaCientificaIleec readById(Integer id) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idAreaCientifica", id);
		
		return (MWAreaCientificaIleec) queryObject(MWAreaCientificaIleec.class, criteria);
	}

}
