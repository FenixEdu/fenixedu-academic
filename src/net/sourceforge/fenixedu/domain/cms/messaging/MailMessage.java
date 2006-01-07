

package net.sourceforge.fenixedu.domain.cms.messaging;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;

import net.sourceforge.fenixedu.domain.cms.predicates.ContentAssignableClassPredicate;

import org.apache.commons.collections.iterators.FilterIterator;

public class MailMessage extends MailMessage_Base implements MimePart, Part
{

	public MailMessage()
	{
		super();
	}

	private MimeMessage message;

	/**
	 * @throws MessagingException
	 * 
	 */
	private synchronized void init() throws MessagingException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(this.getBody().getBytes());
		this.message = new MimeMessage(Session.getDefaultInstance(System.getProperties()), bais);
	}

	private synchronized void updateBody() throws MessagingException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			this.message.writeTo(baos);
		}
		catch (IOException e)
		{
			throw new MessagingException("MailMessage.java: error while updatingBody", e);
		}
		this.setBody(baos.toString());
		this.message = null;
	}

	public String getFirstPlainTextContent() throws MessagingException
	{
		String result = null;
		Collection<String> allTextContent = this.getAllPlainTextContents();
		if (allTextContent.size() > 0) result = allTextContent.iterator().next();

		return result;
	}

	public Collection<String> getAllPlainTextContents() throws MessagingException
	{
		Collection<String> result = new ArrayList<String>();

		try
		{
			if (this.isMimeType("multipart/*"))
			{
				result.addAll(this.getAllPlainTextContents((Multipart) this.getContent()));
			}
			else if (this.isMimeType("text/plain"))
			{
				Object content = this.getContent();
				if (content instanceof String)
				{
					result.add((String) content);
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("Error searching for plain text content" + e);
		}

		return result;
	}

	/**
	 * @param multipart
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	private Collection<String> getAllPlainTextContents(Multipart multipart) throws MessagingException
	{
		Collection<String> result = new ArrayList<String>();
		try
		{
			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("multipart/*"))
				{
					// recurse
					result.addAll(getAllPlainTextContents((Multipart) part.getContent()));
				}
				else
				{
					if (part.isMimeType("text/plain"))
					{
						Object content = part.getContent();
						if (content instanceof String)
						{
							result.add((String) content);
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("could not find first text plain content" + e);
		}

		return result;
	}

	public Collection<String> getAllAttachmentsNames() throws MessagingException
	{
		Collection<String> result = new ArrayList<String>();

		try
		{
			if (this.isMimeType("multipart/*"))
			{
				result.addAll(this.getAllAttachmentsNames((Multipart) this.getContent()));
			}

			else if (this.getDisposition() == null
					|| this.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
			{
				if (this.getFileName() != null) result.add(this.getFileName());
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("Error searching for attachments" + e);
		}

		return result;
	}

	/**
	 * @param multipart
	 * @return
	 * @throws MessagingException
	 */
	private Collection<String> getAllAttachmentsNames(Multipart multipart) throws MessagingException
	{
		Collection<String> result = new ArrayList<String>();
		try
		{
			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("multipart/*"))
				{
					// recurse
					result.addAll(getAllAttachmentsNames((Multipart) part.getContent()));
				}
				else
				{
					if (part.getDisposition() == null
							|| part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
					{
						if (part.getFileName() != null) result.add(part.getFileName());
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("could not find first text plain content" + e);
		}

		return result;
	}

	public Iterator<MailConversation> getMailConversationsIterator()
	{
		return new FilterIterator(this.getParentsIterator(), new ContentAssignableClassPredicate(MailConversation.class));
	}

	public Collection<InternetAddress> getFrom() throws MessagingException
	{
		Collection<InternetAddress> from = new ArrayList<InternetAddress>();
		if (this.message == null) this.init();
		for (int i = 0; i < this.message.getFrom().length; i++)
		{
			if (this.message.getFrom()[i] instanceof InternetAddress)
			{
				from.add((InternetAddress) this.message.getFrom()[i]);
			}
		}

		return from;

	}

	public Part getAttachment(String attachmentName) throws MessagingException
	{
		Part result = null;

		try
		{
			if (this.isMimeType("multipart/*"))
			{
				result = this.getAttachment(attachmentName, (Multipart) this.getContent());
			}

			else if (this.getDisposition() == null
					|| this.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
			{
				if (this.getFileName() != null && this.getFileName().equals(attachmentName))
				{
					result = this;
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("Error searching for attachments" + e);
		}

		return result;
	}

	private Part getAttachment(String attachmentName, Multipart multipart) throws MessagingException
	{
		Part result=null;
		try
		{
			for (int i = 0; i < multipart.getCount(); i++)
			{
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("multipart/*"))
				{
					// recurse
					result = getAttachment(attachmentName, (Multipart) part.getContent());
				}
				else
				{
					if (part.getDisposition() == null
							|| part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT))
					{
						if (part.getFileName() != null)
						{
							result=part;
							break;
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new MessagingException("could not find first text plain content" + e);
		}

		return result;
	}

	public int getMailConversationsCount()
	{
		int count =0;
		Iterator<MailConversation> conversationsIterator = this.getMailConversationsIterator();
		while (conversationsIterator.hasNext())
		{
			count++;
			conversationsIterator.next();
		}
		return count;
	}
	
	public String getSubject() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getSubject();
	}

	public String getHeader(String arg0, String arg1) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getHeader(arg0, arg1);
	}

	public void addHeaderLine(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.addHeaderLine(arg0);
		this.updateBody();
	}

	public Enumeration getAllHeaderLines() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getAllHeaderLines();
	}

	public Enumeration getMatchingHeaderLines(String[] arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getMatchingHeaderLines(arg0);
	}

	public Enumeration getNonMatchingHeaderLines(String[] arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getNonMatchingHeaderLines(arg0);
	}

	public String getEncoding() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getEncoding();
	}

	public String getContentID() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getContentID();
	}

	public String getContentMD5() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getContentMD5();
	}

	public void setContentMD5(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setContentMD5(arg0);
		this.updateBody();
	}

	public String[] getContentLanguage() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getContentLanguage();
	}

	public void setContentLanguage(String[] arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setContentLanguage(arg0);
		this.updateBody();
	}

	public void setText(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setText(arg0);
		this.updateBody();
	}

	public void setText(String arg0, String arg1) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setText(arg0, arg1);
		this.updateBody();
	}

	public int getSize() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getSize();
	}

	public int getLineCount() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getLineCount();
	}

	public String getContentType() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getContentType();
	}

	public boolean isMimeType(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.isMimeType(arg0);
	}

	public String getDisposition() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getDisposition();
	}

	public void setDisposition(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setDisposition(arg0);
		this.updateBody();
	}

	public String getFileName() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getFileName();
	}

	public void setFileName(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setFileName(arg0);
		this.updateBody();
	}

	public InputStream getInputStream() throws IOException, MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getInputStream();
	}

	public DataHandler getDataHandler() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getDataHandler();
	}

	public Object getContent() throws IOException, MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getContent();
	}

	public void setDataHandler(DataHandler arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setDataHandler(arg0);
		this.updateBody();
	}

	public void setContent(Object arg0, String arg1) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setContent(arg0, arg1);
		this.updateBody();
	}

	public void setContent(Multipart arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setContent(arg0);
		this.updateBody();
	}

	public void writeTo(OutputStream arg0) throws IOException, MessagingException
	{
		if (this.message == null) this.init();
		this.message.writeTo(arg0);
		this.updateBody();
	}

	public String[] getHeader(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getHeader(arg0);
	}

	public void setHeader(String arg0, String arg1) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.setHeader(arg0, arg1);
		this.updateBody();
	}

	public void addHeader(String arg0, String arg1) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.addHeader(arg0, arg1);
		this.updateBody();
	}

	public void removeHeader(String arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		this.message.removeHeader(arg0);
		this.updateBody();
	}

	public Enumeration getAllHeaders() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getAllHeaders();
	}

	public Enumeration getMatchingHeaders(String[] arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getMatchingHeaderLines(arg0);
	}

	public Enumeration getNonMatchingHeaders(String[] arg0) throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getNonMatchingHeaderLines(arg0);
	}

	public Date getReceivedDate() throws MessagingException
	{
		if (this.message == null) this.init();
		return this.message.getReceivedDate();
	}
}
