/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:43:14,9/Nov/2005
 * @version $Id$
 */
public class MailMessageManagement extends FenixDispatchAction
{
	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public ActionForward downloadAttachment(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException
	{
		String attachmentName = request.getParameter("attachmentName");
		Integer mailMessageId = new Integer(request.getParameter("mailMessageId"));

		try
		{
			MailMessage message = (MailMessage) rootDomainObject.readContentByOID(mailMessageId);
			InputStream contentStream = message.getAttachment(attachmentName).getInputStream();
			
			try {
				response.setHeader("Content-disposition", "attachment;filename=" + attachmentName);				
	            response.setContentType(message.getAttachment(attachmentName).getContentType());
	            copy(contentStream, response.getOutputStream());
	        } finally {
	            if (contentStream != null) {
	            	contentStream.close();
	            response.flushBuffer();
	            }
	        }

		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		return null;
	}

	private int copy(InputStream input, OutputStream output) throws IOException
	{
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer)))
		{
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
