

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.MailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 8:55:20,18/Out/2005
 * @version $Id$
 */
public class MailingListManagement extends FenixDispatchAction
{
	public ActionForward prepareCreate(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		StringBuffer mailingListDefaultName = new StringBuffer(resources.getMessage(this.getLocale(request), "cms.messaging.mailingList.abbreviation.label"));
		StringBuffer mailingListDefaultDescription = new StringBuffer(resources.getMessage(this.getLocale(request), "cms.messaging.mailingList.mailingList.label"));
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer groupID = new Integer((String) request.getParameter("groupId"));
		mailingListForm.set("groupID", groupID);
		UserGroup group = null;
		try
		{
			Person person = this.getLoggedPerson(request);
			for (Iterator<UserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
			{
				UserGroup currentGroup = iter.next();
				if (currentGroup.getIdInternal().equals(groupID))
				{
					group = currentGroup;
					break;
				}
			}
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}
		mailingListDefaultName.append("_").append(group.getName());
		mailingListForm.set("name", mailingListDefaultName.toString());
		mailingListDefaultDescription.append(" ").append(resources.getMessage(this.getLocale(request), "cms.messaging.mailingList.for.label")).append(" ").append(group.getName());
		mailingListForm.set("description", mailingListDefaultDescription.toString());
		mailingListForm.set("address", mailingListDefaultName.toString().replace(" ", "_"));

		request.setAttribute("group", group);
		return mapping.findForward("showPrepareCreateMailingList");
	}

	public ActionForward viewMailingList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer mailingListID = (Integer) mailingListForm.get("mailingListID");
		MailingList mailingListToView = null;
		try
		{
			mailingListToView = (MailingList) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailingList.class, mailingListID});
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}
		
		request.setAttribute("mailingList", mailingListToView);
		return mapping.findForward("showMailingList");
	}

	public ActionForward create(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm addGroupForm = (DynaActionForm) actionForm;
		Collection<String> aliases = new ArrayList<String>();
		String name = (String) addGroupForm.get("name");
		String description = (String) addGroupForm.get("description");
		String address = (String) addGroupForm.get("address");
		Integer groupId = (Integer) addGroupForm.get("groupID");
		MailingList mailingList = null;

		try
		{
			Person person = this.getLoggedPerson(request);
			Collection<UserGroup> mailingListUserGroups = new ArrayList<UserGroup>();

			for (Iterator<UserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
			{
				UserGroup group = iter.next();
				if (group.getIdInternal().equals(groupId))
				{
					mailingListUserGroups.add(group);
				}

			}
			Object writeArgs[] =
			{ name, description, address, aliases, mailingListUserGroups,true,true, person };
			mailingList = (MailingList) ServiceUtils.executeService(userView, "WriteMailingList", writeArgs);
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("mailingList", mailingList);
		return null;
	}

	public ActionForward downloadMLArchive(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm mailingListForm = (DynaActionForm) actionForm;
		Integer mailingListID = (Integer) mailingListForm.get("mailingListID");
		Boolean threaded = (Boolean) mailingListForm.get("threaded");
		MailingList mailingListToView = null;
		try
		{
			mailingListToView = (MailingList) ServiceManagerServiceFactory.executeService(userView, "ReadDomainObject", new Object[] {MailingList.class,mailingListID});
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
		if (now.get(Calendar.MINUTE)<10)
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
			zos.setComment(resources.getMessage(this.getLocale(request), "cms.messaging.mailingList.zipArchive.comment.label",new Date()));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(9);
			if (!threaded.booleanValue())
			{
				zos.putNextEntry(new ZipEntry(entryName + ".txt"));
			}
			for (Iterator<MailConversation> conversationsIterator = mailingListToView.getMailConversationsIterator(); conversationsIterator.hasNext();)
			{
				MailConversation currentConversation = conversationsIterator.next();
				if (threaded.booleanValue())
				{
					zos.putNextEntry(new ZipEntry(currentConversation.getSubject() + "|"+currentConversation.getIdInternal()+"|.txt"));
				}
				for (Iterator<MailMessage> messagesIterator = currentConversation.getMailMessagesIterator(); messagesIterator.hasNext();)
				{
					MailMessage currentMessage = messagesIterator.next();
					zos.write(currentMessage.getBody().getBytes());
				}
				if (threaded.booleanValue())
				{
					zos.closeEntry();
				}
			}
			if (!threaded.booleanValue())
			{
				zos.closeEntry();
			}

			
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
}
