/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.cms.INodeGroup;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import relations.CmsContents;
import relations.ContentOwnership;
import relations.GroupComposition;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 12:00:18,29/Set/2005
 * @version $Id$
 */
public class DeleteUserGroup  extends CmsService
{
	public IUserGroup run(IUserGroup group)
	{
		ContentOwnership.remove(group.getOwner(),group);
		// first lets notify all aggregators for this group that it is being deleted (and thus they must stop referencing it)
		for (INodeGroup aggregator : group.getAggregators())
		{
			GroupComposition.remove(group,aggregator);
		}
		//now if we are deleting a NodeGroup we must notify all parts that the reference should be removed too
		if (group instanceof INodeGroup)
		{
			INodeGroup aggregator = (INodeGroup) group;
			for (IUserGroup part : aggregator.getParts())
			{
				GroupComposition.remove(part,aggregator);
			}
		}
		
		return group;
	}

	@Override
	protected void updateRootObjectReferences(ServiceRequest request, ServiceResponse response) throws FilterException, Exception
	{
		IUserGroup group = (IUserGroup) response.getReturnObject();
		CmsContents.remove(this.readFenixCMS(),group);
	}
}
