package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:41:37,3/Out/2005
 * @version $Id$
 */
public class UnsortedGroupOperationsManagement extends FenixDispatchAction
{
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		IPerson person = this.getLoggedPerson(request);
		ActionForward destiny = null;
		DynaActionForm addGroupForm = (DynaActionForm) actionForm;
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);

		destiny = mapping.findForward("showCurrentGroups");
		request.setAttribute("person", person);
		request.setAttribute("userGroupTypeToAdd", userGroupType);
		return destiny;

	}

	public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm addGroupForm = (DynaActionForm) form;
		IPerson person = this.getLoggedPerson(request);
		String name = (String) addGroupForm.get("name");
		String description = (String) addGroupForm.get("description");
		Integer[] groupIds = (Integer[]) addGroupForm.get("selectedGroups");
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);
		Collection<IUserGroup> groups = new HashSet<IUserGroup>();
		IUserGroup group=null;
		for (Iterator<IUserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
		{
			IUserGroup currentGroup = iter.next();
			for (int i = 0; i < groupIds.length; i++)
			{
				if (currentGroup.getIdInternal().equals(groupIds[i]))
				{
					groups.add(currentGroup);
				}
			}
		}			
	
		try
		{
			Object writeArgs[] =
			{ groups, name, description, person, userGroupType };
			group = (IUserGroup) ServiceUtils.executeService(userView, "WriteGroupAggregator", writeArgs);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("userGroupType", userGroupType);
		request.setAttribute("viewAction", mapping.getPath());
		request.setAttribute("group", group);
		return mapping.findForward("addGroup");

	}
}
