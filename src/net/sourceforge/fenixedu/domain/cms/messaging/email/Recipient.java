/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import javax.mail.Message;

import net.sourceforge.fenixedu.domain.Person;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:48:24,8/Fev/2006
 * @version $Id$
 */
public class Recipient
{
	public enum RecipientType
	{
		TO(Message.RecipientType.TO), CC(Message.RecipientType.CC), BCC(Message.RecipientType.BCC);

		private Message.RecipientType javaxRecipientType;

		private RecipientType(Message.RecipientType recipientType)
		{
			this.javaxRecipientType = recipientType;
		}

		public Message.RecipientType getJavaxRecipientType()
		{
			return this.javaxRecipientType;
		}
	}

	public enum SendStatus
	{
		SENT,
		QUEUED,
		TRANSPORT_ERROR,
		INVALID_PERSONAL_NAME,
		INVALID_ADDRESS;
	}
	
	private Person subject;
	private SendStatus status;
	private RecipientType type;
	
	public Recipient()
	{
		
	}
	
	public RecipientType getType()
	{
		return type;
	}

	public void setType(RecipientType type)
	{
		this.type = type;
	}

	public Recipient(Person person)
	{
		this.subject = person;
	}

	public SendStatus getStatus()
	{
		return status;
	}

	void setStatus(SendStatus status)
	{
		this.status = status;
	}

	public Person getSubject()
	{
		return subject;
	}

	public void setSubject(Person subject)
	{
		this.subject = subject;
	}		
}