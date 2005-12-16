/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
import net.sourceforge.fenixedu.domain.cms.messaging.MailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:34:45,3/Nov/2005
 * @version $Id$
 */
public class MailingListThreadManagement extends FenixDispatchAction
{
	public ActionForward viewThread(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer conversationId = (Integer) mailingListForm.get("threadId");
		Integer mailingListID = (Integer) mailingListForm.get("mailingListID");
		String messageIdString = request.getParameter("messageId");
		IMailConversation mailConversationToView = null;
		IMailingList mailingListToView = null; 
		try
		{
			mailConversationToView = (IMailConversation) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailConversation.class,conversationId});
			mailingListToView = (IMailingList) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailingList.class,mailingListID});
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}
	
		
	
		if (mailConversationToView != null)
		{
			if (messageIdString != null)
			{
				Integer[] newExpandedMessages = new Integer[]
				{ new Integer(messageIdString) };
				request.setAttribute("expandedMessages", newExpandedMessages);
			}
			else
			{
				IMailMessage mailMessage = null;
				Iterator<IMailMessage> messagesIterator = mailConversationToView.getMailMessagesIterator();
				while (messagesIterator.hasNext())
				{
					mailMessage = messagesIterator.next();
				}
				if (mailMessage != null)
				{
					Integer[] newExpandedMessages = new Integer[]
					{ mailMessage.getIdInternal() };
					if (request.getAttribute("expandedMessages") == null)
					{
						request.setAttribute("expandedMessages", newExpandedMessages);
					}
				}
			}
		}

		request.setAttribute("mailingList", mailingListToView);
		request.setAttribute("mailConversation", mailConversationToView);
		return mapping.findForward("showThread");
	}

	public ActionForward downloadConversationArchive(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer mailingListID = (Integer) mailingListForm.get("mailingListID");
		Integer threadID = (Integer) mailingListForm.get("threadId");
		IMailConversation mailConversationToView = null;
		IMailingList mailingListToView = null; 
		try
		{
			mailConversationToView = (IMailConversation) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailConversation.class,threadID});
			mailingListToView = (IMailingList) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailingList.class,mailingListID});
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}
		
		StringBuffer fileNameBuffer = new StringBuffer();
		Calendar now = new GregorianCalendar();
		fileNameBuffer.append(mailingListToView.getAddress()).append("_").append(now.get(Calendar.DAY_OF_MONTH));
		fileNameBuffer.append("-").append(now.get(Calendar.MONTH)).append("-").append(now.get(Calendar.YEAR));
		fileNameBuffer.append("_").append(now.get(Calendar.HOUR_OF_DAY)).append("-");
		if (now.get(Calendar.MINUTE) < 10)
		{
			fileNameBuffer.append('0');
		}
		fileNameBuffer.append(now.get((Calendar.MINUTE)));
		String entryName = fileNameBuffer.toString()+".txt";
		String fileName = fileNameBuffer.toString()+".zip";
		
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		
		try
		{
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			zos.setComment(resources.getMessage(this.getLocale(request), "cms.messaging.mailingList.zipArchive.comment.label", new Date()));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(9);
			zos.putNextEntry(new ZipEntry(entryName));
			for (Iterator<IMailMessage> messagesIterator = mailConversationToView.getMailMessagesIterator(); messagesIterator.hasNext();)
			{
				IMailMessage currentMessage = messagesIterator.next();
				zos.write(currentMessage.getBody().getBytes());
			}

			zos.closeEntry();
			zos.flush();
			response.flushBuffer();
			zos.close();
		}
		catch (IOException e)
		{
			throw new FenixActionException(e);
		}

		return null;
	}

	public ActionForward messagesVisibilityManagement(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer[] expandedMessages = (Integer[]) mailingListForm.get("expandedMessages");
		Integer[] newExpandedMessages = new Integer[expandedMessages.length];
		// for some reason the original array is lost when control is returned
		// to jsp
		for (int i = 0; i < expandedMessages.length; i++)
		{
			newExpandedMessages[i] = expandedMessages[i];
		}
		request.setAttribute("expandedMessages", newExpandedMessages);
		return this.viewThread(mapping, mailingListForm, request, response);
	}

}
