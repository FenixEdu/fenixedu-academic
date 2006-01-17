/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:27:07,21/Set/2005
 * @version $Id$
 */
public class UserGroupsManagement extends FenixDispatchAction
{

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Person person = this.getLoggedPerson(request);
		request.setAttribute("person", person);
		return mapping.findForward("showPersonUserGroups");
	}

	public ActionForward selectUserGroupTypeToAdd(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward destiny = null;
		DynaActionForm addGroupForm = (DynaActionForm) actionForm;
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);

		destiny = mapping.findForward("showInitialUserGroupOptions");
		request.setAttribute("userGroupTypeToAdd", userGroupType);
		return destiny;
	}

	public ActionForward parameterizeGroup(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward destiny = null;
		DynaActionForm addGroupForm = (DynaActionForm) actionForm;
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);

		destiny = mapping.findForward("selectOptionsFor_" + userGroupType);
		request.setAttribute("userGroupTypeToAdd", userGroupType);
		return destiny;
	}

	public ActionForward viewElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException, FenixServiceException
	{
		Person person = this.getLoggedPerson(request);
		Integer groupId = new Integer((String) request.getParameter("groupId"));
		Group groupToShow = null;
// FIXME: needs to be implemented in a different way
//		for (Iterator<UserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
//		{
//			UserGroup group = (UserGroup) iter.next();
//
//			if (group.getIdInternal().equals(groupId))
//			{
//				groupToShow = group;
//				break;
//			}
//		}

		request.setAttribute("group", groupToShow);
		return mapping.findForward("showElements");
	}
}
