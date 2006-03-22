/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.email.SendEMail.SendEMailParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.EMailAddress;
import net.sourceforge.fenixedu.domain.cms.messaging.email.SendMailReport;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;
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
public abstract class MailSenderAction extends FenixDispatchAction {
	protected abstract List<IGroup> loadPersonalGroupsToChooseFrom(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException;

	protected abstract EMailAddress getFromAddress(HttpServletRequest request, ActionForm form)
			throws FenixFilterException, FenixServiceException;

	protected abstract IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups)
			throws FenixFilterException, FenixServiceException;

	protected abstract IGroup[] getGroupsToSend(ActionForm form, HttpServletRequest request)
			throws FenixFilterException, FenixServiceException, FenixActionException;

	public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException,
			FenixServiceException {
		Person person = this.getUserView(request).getPerson();
		SendMailForm form = (SendMailForm) actionForm;
		List<IGroup> groups = this.loadPersonalGroupsToChooseFrom(request);

		form.setGroupsToChooseFrom(groups);
		String referer = request.getHeader("Referer");
		if (referer == null || referer.equals("")) {
			referer = this.getReturnUrl(request);
		}
		form.setReturnURL(referer);

		form.setFromAddress(person.getEmail());
		form.setFromPersonalName(person.getNome());

		return mapping.findForward("sendMail");
	}

	private String getReturnUrl(HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(request.getRequestURI()).append("?").append(request.getQueryString());

		return buffer.toString();
	}

	public ActionForward send(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException,
			FenixServiceException {
		MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		IUserView userView = this.getUserView(request);

		SendMailForm sendMailForm = (SendMailForm) actionForm;

		IGroup[] groupsToSend = this.getGroupsToSend(actionForm, request);

		if (groupsToSend == null || groupsToSend.length == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.selectAtLeastOneGroup"));
			saveErrors(request, errors);
			return this.start(mapping, actionForm, request, response);
		}

		SendEMailParameters parameters = new SendEMailParameters();
		parameters.subject = sendMailForm.getSubject();
		parameters.message = sendMailForm.getMessage();
		parameters.toRecipients = groupsToSend;
		parameters.allowedSenders = this.getAllowedGroups(request, groupsToSend);
		EMailAddress fromAddress = this.getFromAddress(request, actionForm);
		if (fromAddress == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.provideValidFromEMailAddress"));
			saveErrors(request, errors);
			return this.start(mapping, actionForm, request, response);
		}

		parameters.from = fromAddress;

		try {
			SendMailReport result = (SendMailReport) ServiceManagerServiceFactory.executeService(userView, "SendEMail", new Object[] { parameters });

			StringBuffer sent = new StringBuffer();
			StringBuffer invalidAddress = new StringBuffer();
			StringBuffer transportError = new StringBuffer();
			StringBuffer invalidName = new StringBuffer();

			int invalidAddressCount = 0;
			int invalidPersonalNameCount = 0;
			int transportErrorCount = 0;
			int sentCount = 0;

			if (result.get(SendStatus.INVALID_ADDRESS) != null) {
				for (Person person : result.get(SendStatus.INVALID_ADDRESS)) {
					if (invalidAddressCount == 0) invalidAddress.append("<ul>");
					invalidAddressCount++;
					String addressToShow = person.getEmail();
					if (addressToShow == null || addressToShow.equals("")) {
						addressToShow = resources.getMessage(this.getLocale(request), "cms.mailSender.emptyAddress");
					}
					invalidAddress.append("<li>").append(person.getNome()).append(" (").append(addressToShow).append(") </li>");
				}
			}

			if (result.get(SendStatus.INVALID_PERSONAL_NAME) != null) {
				for (Person person : result.get(SendStatus.INVALID_PERSONAL_NAME)) {
					if (invalidPersonalNameCount == 0) invalidName.append("<ul> ");
					invalidPersonalNameCount++;
					invalidName.append("<li>").append(person.getNome()).append("</li>");
				}
			}

			if (result.get(SendStatus.TRANSPORT_ERROR) != null) {
				for (Person person : result.get(SendStatus.TRANSPORT_ERROR)) {

					if (transportErrorCount == 0) transportError.append("<ul>");
					transportErrorCount++;
					transportError.append("<li>").append(person.getNome()).append(" (").append(person.getEmail()).append(") </li>");
				}
			}

			if (result.get(SendStatus.SENT) != null) {
				for (Person person : result.get(SendStatus.SENT)) {
					if (sentCount == 0) sent.append("<ul>");
					sentCount++;
					sent.append("<li>").append(person.getNome()).append(" (").append(person.getEmail()).append(")</li>");
					break;
				}
			}

			invalidAddress.append("</ul>");
			invalidName.append("</ul>");
			transportError.append("</ul>");
			sent.append("</ul>");
			ActionMessages errors = new ActionMessages();
			if (invalidAddressCount > 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.invalidAddress", invalidAddressCount, invalidAddress));
			}
			if (invalidPersonalNameCount > 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.invalidPersonalName", invalidPersonalNameCount, invalidName));
			}
			if (transportErrorCount > 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.transportError", transportErrorCount, transportError));
			}
			saveErrors(request, errors);

			ActionMessages messages = new StartHiddenActionMessages();
			if (sentCount > 0) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.mailSender.sent", sentCount, sent));
			}
			saveMessages(request, messages);
		}
		catch (Exception e) {
			throw new FenixActionException(e);
		}

		return this.start(mapping, actionForm, request, response);
	}

	public ActionForward goBack(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException, FenixServiceException {
		SendMailForm sendMailForm = (SendMailForm) actionForm;
		try {
			response.sendRedirect(response.encodeRedirectURL(sendMailForm.getReturnURL()));
		}
		catch (IOException e) {
			throw new FenixActionException();
		}
		return null;
	}
}
