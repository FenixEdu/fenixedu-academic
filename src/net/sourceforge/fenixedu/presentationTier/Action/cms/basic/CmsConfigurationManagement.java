/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.basic;


import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.basic.WriteCmsConfiguration;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.basic.WriteCmsConfiguration.CmsConfiguration;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.cms.Cms;
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
 *         <br/> Created on 9:57:29,15/Nov/2005
 * @version $Id$
 */
public class CmsConfigurationManagement extends FenixDispatchAction
{
	private final static String propertiesFilename = "/cms.properties";

	private final static String defaultFenixCMSName = "FenixCMS";

	private static Properties properties;

	private static String fenixCmsName;

	{
		CmsConfigurationManagement.fenixCmsName = CmsConfigurationManagement.defaultFenixCMSName;
		if (properties == null)
		{
			properties = new Properties();
			try
			{
				InputStream inputStream = getClass().getResourceAsStream(propertiesFilename);
				properties.load(inputStream);
				fenixCmsName = properties.getProperty("mailSender.server.url");
				if (fenixCmsName == null)
				{
					fenixCmsName = defaultFenixCMSName;
				}
			}

			catch (Exception e)
			{
				// the default cms instance name
				fenixCmsName = defaultFenixCMSName;
			}
		}
	}

	public ActionForward write(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm configurationForm = (DynaActionForm) actionForm;
		String smtpServerAddress = (String) configurationForm.get("smtpServerAddress");
		Boolean filterNonTextualAttachments = (Boolean) configurationForm.get("filterNonTextualAttachments");
		String mailingListsHost = (String) configurationForm.get("mailingListHost");
		String conversationReplyMarkers = (String) configurationForm.get("conversationReplyMarkers");
		Integer maxAttachmentSize = (Integer) configurationForm.get("maxAttachmentSize");
		Integer maxMessageSize = (Integer) configurationForm.get("maxMessageSize");

		try
		{
			CmsConfiguration config = new WriteCmsConfiguration.CmsConfiguration();
			config.smtpServerAddress = smtpServerAddress;
			config.filterNonTextualAttachments = filterNonTextualAttachments;
			config.mailingListsHost = mailingListsHost;
			config.conversationReplyMarkers = conversationReplyMarkers;
			config.maxAttachmentSize = maxAttachmentSize;
			config.maxMessageSize = maxMessageSize;
			Object[] args = new Object[]
			{ CmsConfigurationManagement.fenixCmsName, config };
			ServiceUtils.executeService(userView, "WriteCmsConfiguration", args);
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.writeConfiguration.sucessMessage.label"));
			saveMessages(request, messages);
		}
		catch (FenixServiceException e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.writeConfiguration.errorMessage.label"));
			saveErrors(request, messages);
		}

		return this.prepare(mapping, actionForm, request, response);
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm configurationForm = (DynaActionForm) actionForm;
		try
		{

			Object[] args = new Object[]
			{ CmsConfigurationManagement.fenixCmsName };
			Cms cms = (Cms) ServiceUtils.executeService(userView, "ReadCmsByName", args);
			if (cms != null)
			{
				configurationForm.set("smtpServerAddress", cms.getConfiguration().getSmtpServerAddressToUse());
				configurationForm.set("filterNonTextualAttachments", cms.getConfiguration().getFilterNonTextualAttachmentsToUse());
				configurationForm.set("mailingListHost", cms.getConfiguration().getMailingListsHostToUse());
				StringBuilder commaSeparatedConversationReplyMarkers = new StringBuilder();
				for (int i = 0; i < cms.getConfiguration().getConversationReplyMarkersToUse().length; i++)
				{
					if (i > 0) commaSeparatedConversationReplyMarkers.append(",");
					commaSeparatedConversationReplyMarkers.append(cms.getConfiguration().getConversationReplyMarkersToUse()[i]);
				}
				configurationForm.set("conversationReplyMarkers", commaSeparatedConversationReplyMarkers.toString());
				configurationForm.set("maxAttachmentSize", cms.getConfiguration().getMaxAttachmentSizeToUse());
				configurationForm.set("maxMessageSize", cms.getConfiguration().getMaxMessageSizeToUse());
				request.setAttribute("cmsExists", true);
			}
			else
			{
				request.setAttribute("cmsExists", false);
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("showConfiguration");
	}

	public ActionForward deleteCms(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		try
		{

			Object[] args = new Object[]
			{ CmsConfigurationManagement.fenixCmsName };
			ServiceUtils.executeService(userView, "DeleteCms", args);
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.deleteCms.sucessMessage.label"));
			saveMessages(request, messages);
		}
		catch (FenixServiceException e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.deleteCms.errorMessage.label"));
			saveErrors(request, messages);
			e.printStackTrace();
			throw new FenixActionException(e);
		}
		return mapping.findForward("cmsDeleted");
	}
	
	public ActionForward createCms(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		try
		{

			Object[] args = new Object[]
			{ CmsConfigurationManagement.fenixCmsName };
			ServiceUtils.executeService(userView, "CreateCms", args);
			request.setAttribute("cmsExists", true);
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.createCms.sucessMessage.label"));
			saveMessages(request, messages);
			
		}
		catch (FenixServiceException e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.configuration.viewConfiguration.createCms.errorMessage.label"));
			saveErrors(request, messages);
		}
		return mapping.findForward("cmsCreated");
	}
}
