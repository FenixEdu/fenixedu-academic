

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl.MailerNotAuthenticated;
import net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl.MessageTooLargeException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 0:32:09,17/Out/2005
 * @version $Id$
 */
public class ReceiveEmail extends FenixAction
{
	MimeMessage message = null;

	final static String allowedProtocol = "https";

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		boolean result = false;
		MimeMessage message = null;
		final DynaActionForm containerForm = (DynaActionForm) actionForm;
		final FormFile formFile = (FormFile) containerForm.get("theFile");
		final MessageResources resources = this.getResources(request, "CMS_RESOURCES");
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		try
		{
			String scheme = request.getScheme();

			if (allowedProtocol.equalsIgnoreCase(scheme))
			{
				InputStream source = formFile.getInputStream();
				message = new MimeMessage(Session.getDefaultInstance(System.getProperties()), source);
				String messageName = resources.getMessage(this.getLocale(request), "cms.messaging.mailMessage.received.name");
				String messageDescription = resources.getMessage(this.getLocale(request), "cms.messaging.mailMessage.received.description", new Date(System.currentTimeMillis()));
				Object args[] =
				{ message, messageName, messageDescription,username,password };
				ServiceUtils.executeService(null, "ProcessReceivedMail", args);
				result = true;
			}
			
		}
		catch (MailerNotAuthenticated e)
		{
			result = false;
		}
		catch (MessageTooLargeException e)
		{
			try
			{
				sendExplanationToSenders(message, e);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
				result = false;
			}
			result = true;
		}
		catch (Exception e)
		{
			result = false;
		}

		try
		{
			this.sendAnswer(response, result);
		}
		catch (IOException e)
		{
			throw new FenixActionException(e);
		}

		return null;
	}

	private void sendExplanationToSenders(MimeMessage message, FenixFilterException e) throws Exception
	{
		// TODO Auto-generated method stub

	}

	private void sendAnswer(HttpServletResponse response, boolean result) throws IOException
	{
		ServletOutputStream writer = response.getOutputStream();
		response.setContentType("text/plain");
		writer.print(result);
		writer.flush();
		response.flushBuffer();
	}

}
