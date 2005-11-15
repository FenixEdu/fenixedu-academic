/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;


import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:50:30,9/Nov/2005
 * @version $Id$
 */
public class ReadMailMessageById extends CmsService
{// /////////////////////
	public IMailMessage run(Integer id) throws ExcepcaoPersistencia
	{
		return (IMailMessage) PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject().readByOID(MailMessage.class, id);
	}

	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception
	{
		// nothing to do
	}
}
