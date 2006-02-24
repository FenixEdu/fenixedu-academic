/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.StartHiddenActionMessages;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:36:35,15/Fev/2006
 * @version $Id$
 */
public abstract class MailSenderAction extends FenixDispatchAction
{
	protected abstract List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException;

	protected abstract List<EMailAddress> getFromAddresses(HttpServletRequest request, ActionForm form)
			throws FenixFilterException, FenixServiceException;
	
	protected abstract boolean isUserAllowedToSendMail(HttpServletRequest request, Collection<IGroup> destinationGroups) throws FenixFilterException, FenixServiceException;

	public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException,
			FenixServiceException
	{
		Person person = this.getUserView(request).getPerson();
		SendMailForm form = (SendMailForm) actionForm;
		List<IGroup> groups = this.loadPersonalGroupsToChooseFrom(request);

		form.setGroupsToChooseFrom(groups);
		String referer = request.getHeader("Referer");
		if (referer == null || referer.equals(""))
		{
			referer = this.getReturnUrl(request);
		}
		form.setReturnURL(referer);

		form.setFromAddress(person.getEmail());
		form.setFromPersonalName(person.getNome());

		return mapping.findForward("sendMail");
	}

	private String getReturnUrl(HttpServletRequest request)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(request.getRequestURI()).append("?").append(request.getQueryString());

		return buffer.toString();
	}

	public ActionForward send(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException,
			FenixServiceException
	{
		MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		IUserView userView = this.getUserView(request);
		Person person = this.getUserView(request).getPerson();
		SendMailForm sendMailForm = (SendMailForm) actionForm;
		Integer[] ids = sendMailForm.getSelectedPersonalGroupsIds();
		if (ids==null)
		{
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.selectAtLeastOneGroup"));
			saveErrors(request,errors);
			return this.start(mapping,actionForm,request,response);
		}
		IGroup[] selectedGroups = new PersonalGroup[ids.length];

		for (PersonalGroup group : person.getPersonalGroups())
		{
			for (int i = 0; i < ids.length; i++)
			{
				if (group.getIdInternal().equals(ids[i]))
				{
					selectedGroups[i] = group;
					break;
				}
			}
		}

		SendEMailParameters parameters = new SendEMailParameters();
		parameters.subject = sendMailForm.getSubject();
		parameters.message = sendMailForm.getMessage();
		parameters.toRecipients = selectedGroups;
		List<EMailAddress> fromAddresses = this.getFromAddresses(request,actionForm);
		if (fromAddresses==null || fromAddresses.size()==0)
		{
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.provideValidFromEMailAddress"));
			saveErrors(request,errors);
			return this.start(mapping,actionForm,request,response);			
		}
		EMailAddress[] addresses = new EMailAddress[fromAddresses.size()];
		int i=0;
		for (Iterator iter = fromAddresses.iterator(); iter.hasNext();)
		{
			addresses[i]=(EMailAddress) iter.next();
			parameters.from=addresses;			
		}
		
		try
		{
			Collection<Recipient> result = (Collection<Recipient>) ServiceManagerServiceFactory.executeService(userView, "SendEMail", new Object[]
			{ parameters });

			StringBuffer sent = new StringBuffer();
			StringBuffer invalidAddress = new StringBuffer();
			StringBuffer transportError = new StringBuffer();
			StringBuffer invalidName = new StringBuffer();

			int invalidAddressCount = 0;
			int invalidPersonalNameCount = 0;
			int transportErrorCount = 0;
			int sentCount = 0;

			for (Recipient recipient : result)
			{
				switch (recipient.getStatus())
				{
				case INVALID_ADDRESS:
					if (invalidAddressCount == 0) invalidAddress.append("<ul>");
					invalidAddressCount++;
					String addressToShow = recipient.getSubject().getEmail();
					if (addressToShow == null || addressToShow.equals(""))
					{
						addressToShow = resources.getMessage(this.getLocale(request), "cms.mailSender.emptyAddress");
					}
					invalidAddress.append("<li>").append(recipient.getSubject().getNome()).append(" (").append(addressToShow).append(") </li>");
					break;
				case INVALID_PERSONAL_NAME:
					if (invalidPersonalNameCount == 0) invalidName.append("<ul> ");
					invalidPersonalNameCount++;
					invalidName.append("<li>").append(recipient.getSubject().getNome()).append("</li>");
					break;
				case TRANSPORT_ERROR:
					if (transportErrorCount == 0) transportError.append("<ul>");
					transportErrorCount++;
					transportError.append("<li>").append(recipient.getSubject().getEmail()).append("</li>");
					break;
				case SENT:
					if (sentCount == 0) sent.append("<ul>");
					sentCount++;
					sent.append("<li>").append(recipient.getSubject().getNome()).append(" (").append(recipient.getSubject().getEmail()).append(")</li>");
					break;
				}
			}
			invalidAddress.append("</ul>");
			invalidName.append("</ul>");
			transportError.append("</ul>");
			sent.append("</ul>");
			ActionMessages errors = new ActionMessages();
			if (invalidAddressCount > 0)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.invalidAddress", invalidAddressCount, invalidAddress));
			}
			if (invalidPersonalNameCount > 0)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.invalidPersonalName", invalidPersonalNameCount, invalidName));
			}
			if (transportErrorCount > 0)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.transportError", transportErrorCount, transportError));
			}
			saveErrors(request, errors);

			ActionMessages messages = new StartHiddenActionMessages();
			if (sentCount > 0)
			{
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.sent", sentCount, sent));
			}
			saveMessages(request, messages);
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		return this.start(mapping, actionForm, request, response);
	}

	public ActionForward goBack(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException, FenixServiceException
	{
		SendMailForm sendMailForm = (SendMailForm) actionForm;
		try
		{
			response.sendRedirect(response.encodeRedirectURL(sendMailForm.getReturnURL()));
		}
		catch (IOException e)
		{
			throw new FenixActionException();
		}
		return null;
	}
}
