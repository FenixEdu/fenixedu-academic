/**
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.cms.infrastructure.IMailAddressAlias;
import net.sourceforge.fenixedu.domain.cms.infrastructure.MailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 17:36:45,26/Out/2005
 * @version $Id$
 */
public class MailAdressAliasVO extends VersionedObjectsBase implements IPersistentMailAddressAlias
{

	public IMailAddressAlias readByAddress(String name)
	{
		Collection<IMailAddressAlias> aliases = readAll(MailAddressAlias.class);
		IMailAddressAlias result = null;
		for (IMailAddressAlias alias : aliases)
		{
			if (name.equals(alias.getAddress()))
			{
				result = alias;
				break;
			}
		}
		
		return result;
	}

}
