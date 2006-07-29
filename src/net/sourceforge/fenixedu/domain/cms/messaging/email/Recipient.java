/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import javax.mail.Message;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 11:48:24,8/Fev/2006
 *         <br/>
 *         In the current version all recipients are added to BCC despite of the specified RecipientType
 * @version $Id$
 */
public abstract class Recipient
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

	public SendStatus getStatus()
	{
		return status;
	}

	void setStatus(SendStatus status)
	{
		this.status = status;
	}
	
	public abstract EMailAddress getAddress();

			
}