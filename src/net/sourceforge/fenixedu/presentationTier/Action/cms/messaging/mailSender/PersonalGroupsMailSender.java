/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;

import org.apache.struts.action.ActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:52:10,15/Fev/2006
 * @version $Id$
 */
public class PersonalGroupsMailSender extends MailSenderAction
{
	@Override
	protected List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException
	{
		IUserView userView = this.getUserView(request);
		Person person = userView.getPerson();
		ArrayList<IGroup> groups = new ArrayList<IGroup>();
		for (PersonalGroup group : person.getPersonalGroups())
		{
			groups.add(group);
		}

		return groups;
	}

	@Override
	protected List<EMailAddress> getFromAddresses(HttpServletRequest request, ActionForm form)
			throws FenixFilterException, FenixServiceException
	{
		List<EMailAddress> addresses = new ArrayList<EMailAddress>();
		SendMailForm sendMailForm = (SendMailForm) form;
		EMailAddress address = new EMailAddress();
		if (EMailAddress.isValid(sendMailForm.getFromAddress()))
		{
			String[] components = sendMailForm.getFromAddress().split("@");
			address.setUser(components[0]);
			address.setDomain(components[1]);
			address.setPersonalName(sendMailForm.getFromPersonalName());
			addresses.add(address);
		}

		return addresses;
	}

	@Override
	protected boolean isUserAllowedToSendMail(HttpServletRequest request,
			Collection<IGroup> destinationGroups) throws FenixFilterException, FenixServiceException
	{
		IUserView userView = this.getUserView(request);
		return userView.hasRoleType(RoleType.CMS_MANAGER) || userView.hasRoleType(RoleType.MANAGER);
	}

}
