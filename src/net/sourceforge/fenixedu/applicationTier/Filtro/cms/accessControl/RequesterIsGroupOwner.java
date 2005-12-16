/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 20:59:29,14/Nov/2005
 * @version $Id$
 */
public class RequesterIsGroupOwner extends CmsAccessControlFilter
{

	public void execute(ServiceRequest request, ServiceResponse response, FilterParameters filterParameters) throws FilterException, Exception
	{
//		IUserView userView = (IUserView) request.getRequester();
//		IUserGroup userGroup = (IUserGroup) request.getServiceParameters().getParameter(0);
//		if (!userGroup.getOwners().contains(userView.getPerson()))
//		{
//			throw new RequesterNotAllowedToDelete("UserGroup");
//		}
	}

}
