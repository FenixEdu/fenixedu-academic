/**
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.cms;

import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 17:37:14,26/Out/2005
 * @version $Id$
 */
public interface IPersistentMailAddressAlias
{
	public MailAddressAlias readByAddress(String name) throws ExcepcaoPersistencia;
}
