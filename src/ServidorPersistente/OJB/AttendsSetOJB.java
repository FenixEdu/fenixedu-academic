/*
 * Created on Jul 31, 2004
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.AttendsSet;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAttendsSet;

/**
 * @author joaosa-rmalo
 */

public class AttendsSetOJB extends ObjectFenixOJB implements IPersistentAttendsSet
{

	public List readAll()  throws ExcepcaoPersistencia
	{	
	    return queryList(AttendsSet.class, new Criteria());
	}
	
	
	public List readAttendsSetByName(String attendsSetName)  throws ExcepcaoPersistencia
	{	
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", attendsSetName);
	    return queryList(AttendsSet.class, criteria);
	
	}
	
}