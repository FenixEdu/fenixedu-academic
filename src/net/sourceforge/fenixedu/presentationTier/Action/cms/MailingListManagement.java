

package net.sourceforge.fenixedu.presentationTier.Action.cms;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailConversation;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;
import net.sourceforge.fenixedu.domain.cms.messaging.IMailingList;
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
		IUserGroup group = null;
		try
		{
			IPerson person = this.getLoggedPerson(request);
			for (Iterator<IUserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
			{
				IUserGroup currentGroup = iter.next();
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
		Collection<IMailingList> mailingLists;
		try
		{
			mailingLists = (Collection<IMailingList>) ServiceManagerServiceFactory.executeService(userView, "ReadAllMailingLists", new Object[] {});
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		IMailingList mailingListToView = null;
		for (IMailingList mailingList : mailingLists)
		{
			if (mailingList.getIdInternal().equals(mailingListID))
			{
				mailingListToView = mailingList;
				break;
			}
		}
		
		int a = mailingListToView.getMailConversationsCount(); 
		int b = mailingListToView.getMailMessagesCount();
		int c = mailingListToView.getAliasesCount();
		
		int d = mailingListToView.getChildrenCount();

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
		IMailingList mailingList = null;

		try
		{
			IPerson person = this.getLoggedPerson(request);
			Collection<IUserGroup> mailingListUserGroups = new ArrayList<IUserGroup>();

			for (Iterator<IUserGroup> iter = person.getUserGroupsIterator(); iter.hasNext();)
			{
				IUserGroup group = iter.next();
				if (group.getIdInternal().equals(groupId))
				{
					mailingListUserGroups.add(group);
				}

			}
			Object writeArgs[] =
			{ name, description, address, aliases, mailingListUserGroups,true,true, person };
			mailingList = (IMailingList) ServiceUtils.executeService(userView, "WriteMailingList", writeArgs);
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
		Collection<IMailingList> mailingLists;
		try
		{
			mailingLists = (Collection<IMailingList>) ServiceManagerServiceFactory.executeService(userView, "ReadAllMailingLists", new Object[] {});
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		IMailingList mailingListToView = null;
		for (IMailingList mailingList : mailingLists)
		{
			if (mailingList.getIdInternal().equals(mailingListID))
			{
				mailingListToView = mailingList;
				break;
			}
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
			for (Iterator<IMailConversation> conversationsIterator = mailingListToView.getMailConversationsIterator(); conversationsIterator.hasNext();)
			{
				IMailConversation currentConversation = conversationsIterator.next();
				if (threaded.booleanValue())
				{
					zos.putNextEntry(new ZipEntry(currentConversation.getSubject() + "|"+currentConversation.getIdInternal()+"|.txt"));
				}
				for (Iterator<IMailMessage> messagesIterator = currentConversation.getMailMessagesIterator(); messagesIterator.hasNext();)
				{
					IMailMessage currentMessage = messagesIterator.next();
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
