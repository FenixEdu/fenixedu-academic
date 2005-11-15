/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;

import java.util.Collection;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.ICms;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
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
public class ReadAllMailingLists  extends CmsService
{
	public Collection<IMailingList> run() throws ExcepcaoPersistencia
	{
		return PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject().readAll(MailingList.class);
	}

	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response) throws FilterException, Exception
	{
		//	 	nothing to do		
	}
}
