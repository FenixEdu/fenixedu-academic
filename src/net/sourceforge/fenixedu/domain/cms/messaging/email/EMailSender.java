/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.RecipientType;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;

import org.apache.commons.collections.iterators.IteratorChain;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:48:52,7/Fev/2006
 * @version $Id$
 */
public class EMailSender
{
	private static String bundleFile = new String("SMTPConfiguration");

	private static ResourceBundle bundle = null;

	private static String mailServer = null;

	static
	{
		if (bundle == null)
		{
			try
			{
				EMailSender.bundle = ResourceBundle.getBundle(EMailSender.bundleFile);
				EMailSender.mailServer = EMailSender.bundle.getString("mail.smtp.host");
				if (EMailSender.mailServer == null)
				{
					EMailSender.mailServer = "mail.adm";
				}
			}
			catch (Exception e)
			{
				// the default server
				EMailSender.mailServer = "mail.adm";
			}
		}
	}

	public class InvalidFromAddress extends Exception
	{
		private static final long serialVersionUID = 589679694753025475L;

		private EMailAddress offendingAddress;

		public EMailAddress getOffendingAddress()
		{
			return offendingAddress;
		}

		public InvalidFromAddress(EMailAddress offendingAddress)
		{
			super();
			this.offendingAddress = offendingAddress;
		}
	}

	public class InvalidPersonalNameEncoding extends Exception
	{
		private static final long serialVersionUID = 653152446837125007L;

		private EMailAddress offendingAddress;

		public EMailAddress getOffendingAddress()
		{
			return offendingAddress;
		}

		public InvalidPersonalNameEncoding(EMailAddress offendingAddress)
		{
			super();
			this.offendingAddress = offendingAddress;
		}
	}

	private Collection<Recipient> recipients = new ArrayList<Recipient>();

	private Collection<EMailMessage> messages = new ArrayList<EMailMessage>();

	public void addMessage(EMailMessage message)
	{
		this.messages.add(message);
	}

	public void addRecipient(RecipientType type, Person... persons)
	{
		for (Person person : persons)
		{
			Recipient recipient = new Recipient();
			recipient.setType(type);
			recipient.setSubject(person);
			this.recipients.add(recipient);
		}

	}

	public void addRecipient(RecipientType type, Collection<Recipient> recipients)
	{
		this.recipients.addAll(recipients);
	}

	public void addRecipient(RecipientType type, IGroup... groups)
	{
		IteratorChain iteratorChain = new IteratorChain();
		for (IGroup group : groups)
		{
			iteratorChain.addIterator(group.getElementsIterator());
		}
		while (iteratorChain.hasNext())
			this.addRecipient(type, (Person) iteratorChain.next());
	}

	public void addMessage(EMailMessage... messages)
	{
		for (EMailMessage message : messages)
		{
			this.messages.add(message);
		}
	}

	public void send(EMailAddress... from) throws InvalidFromAddress, InvalidPersonalNameEncoding
	{
		for (EMailMessage message : this.messages)
		{
			this.sendToAll(message, from);
		}
	}

	private void sendToAll(EMailMessage message, EMailAddress... from) throws InvalidFromAddress,
			InvalidPersonalNameEncoding
	{
		Session session = getSession();

		for (Recipient recipient : this.recipients)
		{
			MimeMessage mimeMessage = new MimeMessage(session);
			InternetAddress[] addresses = new InternetAddress[from.length];
			for (int i = 0; i < from.length; i++)
			{
				try
				{
					addresses[i] = from[i].getInternetAddress();
				}
				catch (AddressException e)
				{
					throw new InvalidFromAddress(from[i]);
				}
				catch (UnsupportedEncodingException e)
				{
					throw new InvalidPersonalNameEncoding(from[i]);
				}

				InternetAddress internetAddress;
				try
				{
					String subjectEmailAddress = recipient.getSubject().getEmail();
					if (EMailAddress.isValid(subjectEmailAddress))
					{
						internetAddress = new InternetAddress(subjectEmailAddress);
						if (recipient.getSubject().getNome() != null)
						{
							internetAddress.setPersonal(recipient.getSubject().getNome());
						}
						mimeMessage.addRecipient(recipient.getType().getJavaxRecipientType(), internetAddress);
						mimeMessage.setText(message.getText());
						mimeMessage.setFrom(new InternetAddress("hello@hello.com"));
						Transport.send(mimeMessage);
						recipient.setStatus(SendStatus.SENT);
					}
					else
					{
						recipient.setStatus(SendStatus.INVALID_ADDRESS);
					}
				}
				catch (AddressException e)
				{
					recipient.setStatus(SendStatus.INVALID_ADDRESS);
				}
				catch (UnsupportedEncodingException e)
				{
					recipient.setStatus(SendStatus.INVALID_PERSONAL_NAME);
				}
				catch (MessagingException e)
				{
					recipient.setStatus(SendStatus.TRANSPORT_ERROR);
				}

			}
		}
	}

	private Session getSession()
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", EMailSender.mailServer);
		Session session = Session.getDefaultInstance(props, null);
		return session;
	}

	public Collection<Recipient> getRecipients()
	{
		return recipients;
	}
}
