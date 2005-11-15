/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms;


import net.sourceforge.fenixedu.applicationTier.Filtro.cms.UpdateCmsReferencesFilter;
import net.sourceforge.fenixedu.domain.cms.ICms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:11:04,25/Out/2005
 * @version $Id$
 */
public abstract class CmsService extends UpdateCmsReferencesFilter implements IService
{
	protected ICms readFenixCMS() throws ExcepcaoPersistencia
	{
		return PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentCms().readFenixCMS();
	}

	/**
	 * This method shall update all references to and from the CMS root object
	 * 
	 * Its ok to be empty as long as the service does not insert or remove any
	 * content to the system (tipically read services)
	 */
	abstract protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception;

	public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
			Exception
	{
		Object returnObject = response.getReturnObject();
		if (returnObject != null)
		{
			this.updateRootObjectReferences(request, response);
		}
	}
}
