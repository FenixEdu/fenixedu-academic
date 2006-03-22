/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;

import org.apache.struts.action.ActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:52:10,15/Fev/2006
 * @version $Id: PersonalGroupsMailSender.java,v 1.1 2006/02/24 14:08:21 gedl
 *          Exp $
 */
public class PersonalGroupsMailSender extends MailSenderAction {
	@Override
	protected List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		IUserView userView = this.getUserView(request);
		Person person = userView.getPerson();
		ArrayList<IGroup> groups = new ArrayList<IGroup>();
		for (PersonalGroup group : person.getPersonalGroups()) {
			groups.add(group);
		}

		return groups;
	}

	@Override
	protected EMailAddress getFromAddress(HttpServletRequest request, ActionForm form)
			throws FenixFilterException, FenixServiceException {
		SendMailForm sendMailForm = (SendMailForm) form;
		EMailAddress address = new EMailAddress();
		if (EMailAddress.isValid(sendMailForm.getFromAddress())) {
			String[] components = sendMailForm.getFromAddress().split("@");
			address.setUser(components[0]);
			address.setDomain(components[1]);
			address.setPersonalName(sendMailForm.getFromPersonalName());
		}

		return address;
	}

	@Override
	protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] destinationGroups)
			throws FenixFilterException, FenixServiceException {
		IGroup[] allowedGroups = new IGroup[2];
		IGroup cmsManagerGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.CMS_MANAGER));
		IGroup managerGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER));

		allowedGroups[0] = cmsManagerGroup;
		allowedGroups[1] = managerGroup;

		return allowedGroups;
	}

	@Override
	protected IGroup[] getGroupsToSend(ActionForm form, HttpServletRequest request) throws FenixFilterException,
			FenixServiceException {

		Person person = this.getUserView(request).getPerson();
		SendMailForm sendMailForm = (SendMailForm) form;
		Integer[] ids = sendMailForm.getSelectedPersonalGroupsIds();
		if (ids ==null || ids.length==0)
		{
			return new PersonalGroup[0];
		}
		IGroup[] groupsToSend = new PersonalGroup[ids.length];
		
		for (PersonalGroup group : person.getPersonalGroups()) {
			for (int i = 0; i < ids.length; i++) {
				if (group.getIdInternal().equals(ids[i])) {
					groupsToSend[i] = group;
					break;
				}
			}
		}
		
		return groupsToSend;
	}
}
