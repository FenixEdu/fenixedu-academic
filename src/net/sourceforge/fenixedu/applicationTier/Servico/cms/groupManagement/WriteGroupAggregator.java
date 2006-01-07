/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;


import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.NodeGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
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
	public UserGroup run(Collection<UserGroup> parts, String name, String description, Person owner,
			UserGroupTypes type) throws FenixServiceException, InstantiationException,
			IllegalAccessException, ExcepcaoPersistencia
	{
		NodeGroup aggregator = (NodeGroup) type.getImplementationClass().newInstance();
		aggregator.setName(name);
		aggregator.setDescription(description);
		for (UserGroup group : parts)
		{
			aggregator.addParts(group);
		}
		GroupOwnership.add(owner,aggregator);
		GroupCreation.add(owner,aggregator);

		return aggregator;
	}

}
