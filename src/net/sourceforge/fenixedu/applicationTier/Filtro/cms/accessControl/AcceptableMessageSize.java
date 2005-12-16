/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;


import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:39:11,14/Nov/2005
 * @version $Id$
 */
public class AcceptableMessageSize extends CmsAccessControlFilter
{
	public void execute(ServiceRequest arg0, ServiceResponse arg1, FilterParameters arg2)
			throws FilterException, Exception
	{
		MimeMessage message = (MimeMessage) arg0.getServiceParameters().getParameter(0);
		if (message.getSize() > this.readFenixCMS().getConfiguration().getMaxMessageSizeToUse())
		{
			throw new MessageTooLargeException();
		}		
		this.checkAttachmentSizes(message);
	}
	
	public void checkAttachmentSizes(MimeMessage message) throws MessagingException, AttachmentTooLargeException, ExcepcaoPersistencia
	{

		try
		{
			if (message.isMimeType("multipart/*"))
			{
				this.checkAttachmentSizes((Multipart) message.getContent());
			}

			else if (message.getDisposition() == null
					|| message.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
			{
				if (message.getSize() > this.readFenixCMS().getConfiguration().getMaxAttachmentSizeToUse())
					throw new AttachmentTooLargeException();
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("Error searching for attachments" + e);
		}
	}

	/**
	 * @param multipart
	 * @return
	 * @throws MessagingException
	 * @throws AttachmentTooLargeException 
	 * @throws ExcepcaoPersistencia 
	 */
	private void checkAttachmentSizes(Multipart multipart) throws MessagingException, AttachmentTooLargeException, ExcepcaoPersistencia
	{
		try
		{
			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("multipart/*"))
				{
					// recurse
					this.checkAttachmentSizes((Multipart) part.getContent());
				}
				else
				{
					if (part.getDisposition() == null
							|| part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
					{
						if (part.getSize()>this.readFenixCMS().getConfiguration().getMaxAttachmentSizeToUse())
							throw new AttachmentTooLargeException();
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("could not find first text plain content" + e);
		}
	}
}