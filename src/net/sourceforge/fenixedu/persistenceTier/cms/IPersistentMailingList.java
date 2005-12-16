/**
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.cms;

import java.util.Collection;

import javax.mail.Address;

import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 1:47:48,16/Nov/2005
 * @version $Id$
 */
public interface IPersistentMailingList
{
	public Collection<IMailingList> readAllMailingListsWithOutgoingMails() throws ExcepcaoPersistencia;
	public Collection<IMailingList> readReceptorMailingListsForAddress(Collection<Address> addresses, String mailingListDomain) throws ExcepcaoPersistencia;	
}
