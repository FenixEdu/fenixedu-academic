/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.TagLib.cms.email;


import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 10:41:23,9/Nov/2005
 * @version $Id$
 */
public class EmailBodyDigestTag extends TagSupport
{
	protected String name;

	protected String property;

	protected String scope;

	protected String locale = Globals.LOCALE_KEY;

	protected String bundle;

	protected boolean filter = false;

	protected Integer chars = new Integer(15);

	protected MailMessage message;

	protected String allowedTags = "b i /b /i";

	protected String allowedTagsSeparator = " ";

	public String getAllowedTags()
	{
		return allowedTags;
	}

	public void setAllowedTags(String allowedTags)
	{
		this.allowedTags = allowedTags;
	}

	public boolean isFilter()
	{
		return filter;
	}

	public void setFilter(boolean filter)
	{
		this.filter = filter;
	}

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
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

	public int doStartTag() throws JspException
	{

		Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);

		if (value == null)
		{
			return (SKIP_BODY);
		}
		else
		{
			this.message = (MailMessage) value;
		}
		try
		{
			String body = this.buildBody();

			StringBuilder output = new StringBuilder();
			output.append(body).append("<br/>");			
			TagUtils.getInstance().write(pageContext, output.toString());

		}
		catch (MessagingException e)
		{
			new JspException(e);
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	private String getMessage(String key) throws JspException
	{
		return TagUtils.getInstance().message(this.pageContext, this.bundle, this.locale, key);
	}

	private String recoverAllowedTags(String filteredOutput)
	{
		StringBuilder toReplace = new StringBuilder();
		StringBuilder replacement = new StringBuilder();
		String[] allowedTags = this.getAllowedTags().split(this.getAllowedTagsSeparator());
		for (int i = 0; i < allowedTags.length; i++)
		{
			toReplace.delete(0, toReplace.length());
			replacement.delete(0, replacement.length());
			toReplace.append("&lt;").append("\\s*").append(allowedTags[i]).append("\\s*").append("/*&gt;");
			replacement.append("<").append(allowedTags[i]).append(">");
			filteredOutput = Pattern.compile(toReplace.toString()).matcher(filteredOutput).replaceAll(replacement.toString());
		}

		return filteredOutput;
	}

	private String buildBody() throws MessagingException, JspException
	{
		StringBuilder messageDigest = new StringBuilder();
		String originalMessageDigest = this.message.getFirstPlainTextContent();
		if (originalMessageDigest==null || originalMessageDigest.equalsIgnoreCase("") || originalMessageDigest.trim().equalsIgnoreCase(""))
		{
			return this.getMessage("cms.messaging.mailingList.viewMailingList.expand.link");
		}
		if (this.filter)
		{
			originalMessageDigest = TagUtils.getInstance().filter(originalMessageDigest);
			originalMessageDigest = this.recoverAllowedTags(originalMessageDigest);
		}
		String[] messageVector = originalMessageDigest.split(" ");

		for (int i = 0; i < messageVector.length && i < this.chars; i++)
			messageDigest.append(messageVector[i]).append(" ");
		if (messageVector.length > this.chars) messageDigest.append("[...]");

		return messageDigest.toString();
	}
	
	

	public Integer getChars()
	{
		return chars;
	}

	public void setChars(Integer chars)
	{
		this.chars = chars;
	}

	public String getAllowedTagsSeparator()
	{
		return allowedTagsSeparator;
	}

	public void setAllowedTagsSeparator(String allowedTagsSeparator)
	{
		this.allowedTagsSeparator = allowedTagsSeparator;
	}

	public String getBundle()
	{
		return bundle;
	}

	public void setBundle(String bundle)
	{
		this.bundle = bundle;
	}
}
