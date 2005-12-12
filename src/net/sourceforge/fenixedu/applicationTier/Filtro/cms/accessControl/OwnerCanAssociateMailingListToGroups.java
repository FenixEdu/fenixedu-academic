/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:08:23,14/Nov/2005
 * @version $Id$
 */
public class OwnerCanAssociateMailingListToGroups extends CmsAccessControlFilter
{

	public void execute(ServiceRequest arg0, ServiceResponse arg1, FilterParameters arg2) throws FilterException, Exception
	{
		Collection<IUserGroup> groups = (Collection<IUserGroup>)arg0.getServiceParameters().getParameter(4);
		IPerson owner = (IPerson) arg0.getServiceParameters().getParameter(7);
		for (IUserGroup group : groups)
		{
//			if (!group.getOwners().contains(owner))
//			{
//				throw new MailingListGroupAssociationNotAllowed();
//			}
		}
	}		
}
