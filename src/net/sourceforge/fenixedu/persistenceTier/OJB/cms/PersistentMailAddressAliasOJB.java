/**
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.cms;

import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 17:53:32,26/Out/2005
 * @version $Id$
 */
public class PersistentMailAddressAliasOJB extends PersistentObjectOJB implements IPersistentMailAddressAlias
{

	public MailAddressAlias readByAddress(String name) throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("address",name);
		
		return (MailAddressAlias) queryObject(MailAddressAlias.class,crit);
	}

}
