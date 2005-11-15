/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;


import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.NodeGroup;
import net.sourceforge.fenixedu.domain.cms.UserGroupTypes;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;
import relations.CmsContents;
import relations.CmsUsers;
import relations.ContentOwnership;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:57:56,3/Out/2005
 * @version $Id$
 */
public class WriteGroupAggregator extends CmsService
{
	public IUserGroup run(Collection<IUserGroup> parts, String name, String description, IPerson owner,
			UserGroupTypes type) throws FenixServiceException, InstantiationException,
			IllegalAccessException
	{
		NodeGroup aggregator = (NodeGroup) type.getImplementationClass().newInstance();
		aggregator.setName(name);
		aggregator.setDescription(description);
		for (IUserGroup group : parts)
		{
			aggregator.addParts(group);
		}
		ContentOwnership.add(owner, aggregator);

		return aggregator;
	}

	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response)
			throws FilterException, Exception
	{
		IUserGroup aggregator = (IUserGroup) response.getReturnObject();
		IPerson owner = (IPerson) request.getServiceParameters().getParameter(3);
		Collection<IUserGroup> parts = (Collection<IUserGroup>) request.getServiceParameters().getParameter(0);

		for (IUserGroup part : parts)
		{
			CmsContents.add(this.readFenixCMS(), part);
		}

		CmsContents.add(this.readFenixCMS(), aggregator);
		CmsUsers.add(this.readFenixCMS(), owner);
	}
}
