/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.TagLib.cms.email;


import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:20:41,10/Nov/2005
 * @version $Id$
 */
public class EmailSenderDigestTag extends TagSupport
{
	protected String name;

	protected String property;

	protected String scope;

	protected String locale = Globals.LOCALE_KEY;

	protected String bundle;
	
	protected String addressSeparator=",";

	protected MailMessage message;

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public int doStartTag() throws JspException
	{

		// Look up the requested property value
		Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);

		if (value == null)
		{
			return (SKIP_BODY);
		}
		else
		{
			this.message = (MailMessage) value;
		}
		// Convert value to the String with some formatting
		try
		{
			String from = this.buildSender();

			StringBuilder output = new StringBuilder();
			output.append(from);
			TagUtils.getInstance().write(pageContext, output.toString());

		}
		catch (MessagingException e)
		{
			new JspException(e);
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	private String buildSender() throws MessagingException, JspException
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.messageSenders());

		return buffer.toString();
	}

	private String getMessage(String key) throws JspException
	{
		return TagUtils.getInstance().message(this.pageContext, this.bundle, this.locale, key);
	}

	private String messageSenders() throws MessagingException, JspException
	{
		StringBuilder buffer = new StringBuilder();
		boolean firstAddress = true;
		for (Iterator<InternetAddress> fromIterator = this.message.getFrom().iterator(); fromIterator.hasNext();)
		{
			InternetAddress from = fromIterator.next();
			if (from.getPersonal() != null)
			{
				if (!firstAddress) buffer.append(this.addressSeparator);
				buffer.append(TagUtils.getInstance().filter(from.getPersonal()));
				firstAddress = false;
			}
			if (from.getAddress() != null && from.getPersonal()==null)
			{
				if (from.getPersonal() != null) buffer.append(" ");
				if (from.getPersonal() == null && !firstAddress) buffer.append(this.addressSeparator);
				buffer.append("&lt;").append(TagUtils.getInstance().filter(from.getAddress())).append("&gt;");
				firstAddress = false;
			}
			if ((from.getAddress() == null || from.getAddress().equals(""))
					&& (from.getPersonal() == null || from.getPersonal().equals("")))
			{
				if (!firstAddress) buffer.append(this.addressSeparator);
				buffer.append(this.getMessage("cms.messaging.mailingList.mailMessage.unknownSenders.label"));
				firstAddress = false;
			}
			if (!firstAddress)
			{
				if (fromIterator.hasNext()) buffer.append(" [...]");
				break;
			}
		}
		return buffer.toString();

	}

	public String getBundle()
	{
		return bundle;
	}

	public void setBundle(String bundle)
	{
		this.bundle = bundle;
	}

	public String getAddressSeparator()
	{
		return addressSeparator;
	}

	public void setAddressSeparator(String addressSeparator)
	{
		this.addressSeparator = addressSeparator;
	}

}
