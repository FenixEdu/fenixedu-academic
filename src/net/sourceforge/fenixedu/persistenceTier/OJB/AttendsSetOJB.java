/*
 * Created on Jul 31, 2004
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;

import org.apache.ojb.broker.query.Criteria;

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