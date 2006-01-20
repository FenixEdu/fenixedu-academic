/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 14:53:53,20/Out/2005
 * @version $Id$
 */
public class ReadMailingListsWithOutgoingMails extends CmsService
{
	public Collection<MailingList> run() throws ExcepcaoPersistencia
	{
		return persistentSupport.getIPersistentMailingList().readAllMailingListsWithOutgoingMails();
	}
}
