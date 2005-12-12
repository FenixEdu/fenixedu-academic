/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;


import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.INodeGroup;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import relations.GroupCreation;
import relations.GroupOwnership;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:57:56,3/Out/2005
 * @version $Id$
 */
public class WriteGroupAggregator implements IService
{
	public IUserGroup run(Collection<IUserGroup> parts, String name, String description, IPerson owner,
			UserGroupTypes type) throws FenixServiceException, InstantiationException,
			IllegalAccessException, ExcepcaoPersistencia
	{
		INodeGroup aggregator = (INodeGroup) type.getImplementationClass().newInstance();
		aggregator.setName(name);
		aggregator.setDescription(description);
		for (IUserGroup group : parts)
		{
			aggregator.addParts(group);
		}
		GroupOwnership.add(owner,aggregator);
		GroupCreation.add(owner,aggregator);

		return aggregator;
	}

}
