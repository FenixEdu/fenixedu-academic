/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;


import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:34:31,29/Set/2005
 * @version $Id$
 */
public class DeleteUserGroup extends FenixAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		Integer groupId = new Integer((String) request.getParameter("groupId"));
		IUserView userView = SessionUtils.getUserView(request);
		try
		{
			IPerson person = (IPerson) ServiceManagerServiceFactory.executeService(userView, "ReadDomainPersonByUsername", new Object[]
			{ userView.getUtilizador() });
			IUserGroup toDelete = null;
			for (Iterator<IUserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
			{
				IUserGroup group = iter.next();
				if (group.getIdInternal().equals(groupId))
				{
					toDelete = group;
					break;
				}			
			}			
			ServiceManagerServiceFactory.executeService(userView, "DeleteUserGroup", new Object[]{toDelete});
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		return mapping.findForward("userGroupsManagement");
	}
}
