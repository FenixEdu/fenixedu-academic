/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.TagLib.cms.email;


import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.cms.messaging.MailMessage;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:18:44,8/Nov/2005
 * @version $Id$
 */
public class EmailBodyWriteTag extends TagSupport
{
	protected String locale = Globals.LOCALE_KEY;;

	protected MailMessage message;

	protected String bundle;
	
	protected String name;

	protected String property;

	protected String scope;

	protected boolean filter = false;

	protected String attachmentParamId = "attachmentName";

	protected String mailMessageParamId = "mailMessageId";

	protected String paramName;

	protected String paramProperty = "idInternal";

	protected String allowedTags = "b i br /b /i /br br/";

	protected String allowedTagsSeparator = " ";

	protected String action = "/mailMessageManagement";
	
	protected String module = "/cms";

	protected String method = "downloadAttachment";

	public String getAllowedTags()
	{
		return allowedTags;
	}

	public void setAllowedTags(String allowedTags)
	{
		this.allowedTags = allowedTags;
	}

	public String getAllowedTagsSeparator()
	{
		return allowedTagsSeparator;
	}

	public void setAllowedTagsSeparator(String allowedTagsSeparator)
	{
		this.allowedTagsSeparator = allowedTagsSeparator;
	}

	public String getModule()
	{
		return module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	/**
	 * Process the start tag.
	 * 
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
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
			String subject = this.buildSubject();
			String attachments = this.buildAttachments();
			String body = this.buildBody();
			
			StringBuffer output = new StringBuffer();
			output.append(subject);
			if (this.message.getAllAttachmentsNames().size() > 0)
			{
				output.append(attachments);
			}
			output.append(body);
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
		return
            TagUtils.getInstance().message(
                this.pageContext,
                this.bundle,
                this.locale,
                key);
	}
	/**
	 * @return
	 * @throws MessagingException 
	 */
	private String buildBody() throws MessagingException
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div>").append(this.filter?TagUtils.getInstance().filter(this.messagePlainTextComponents()):this.messagePlainTextComponents()).append("</div>");

		return this.recoverAllowedTags(buffer.toString().replaceAll("\n","<br/>"));
	}

	/**
	 * @return
	 * @throws JspException
	 * @throws MessagingException
	 */
	private String buildAttachments() throws MessagingException, JspException
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<p><b>").append(this.getMessage("cms.messaging.mailingConversation.mailMessage.attachments.label")).append(":</b>").append(this.messageAttachments());

		return buffer.toString();
	}

	/**
	 * @return
	 * @throws MessagingException
	 * @throws JspException 
	 */
	private String buildSubject() throws MessagingException, JspException
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<p><b>").append(this.getMessage("cms.messaging.mailingConversation.mailMessage.subject.label")).append(":</b>").append(this.filter?TagUtils.getInstance().filter(this.messageSubject()):this.messageSubject());

		return buffer.toString();
	}

	private String recoverAllowedTags(String filteredOutput)
	{
		StringBuffer toReplace = new StringBuffer();
		StringBuffer replacement = new StringBuffer();
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

	private String messagePlainTextComponents() throws MessagingException
	{
		StringBuffer buffer = new StringBuffer();
		for (String string : this.message.getAllPlainTextContents())
		{
			buffer.append(string);
		}
		return buffer.toString();
	}

	private String messageAttachments() throws MessagingException, JspException
	{
		StringBuffer buffer = new StringBuffer();
		boolean firstAddress = true;
		for (String attachmentName : this.message.getAllAttachmentsNames())
		{
			if (!firstAddress) buffer.append(",");
			this.buildAttachmentLink(buffer, this.filter?TagUtils.getInstance().filter(attachmentName):attachmentName);
		}
		return buffer.toString();
	}

	/**
	 * @param buffer
	 * @param attachmentName
	 * @throws JspException
	 */
	private void buildAttachmentLink(StringBuffer buffer, String attachmentName) throws JspException
	{
		Object value = null;
		if (paramName == null)
		{
			paramName = name;
		}
		if (paramName != null)
		{
			value = TagUtils.getInstance().lookup(pageContext, paramName, paramProperty, scope);
		}
		Map params = TagUtils.getInstance().computeParameters(pageContext, mailMessageParamId, paramName, paramProperty, null, null, null, scope, false);
		if (params == null)
		{
			params = new HashMap();
		}

		params.put(attachmentParamId, attachmentName);
		params.put("method", method);

		String url = null;
		if (value != null)
		{
			if (this.action != null)
			{
				try
				{
					url = TagUtils.getInstance().computeURLWithCharEncoding(pageContext, null, null, null, action, module, params, null, false, false);
				}
				catch (MalformedURLException e)
				{
					TagUtils.getInstance().saveException(pageContext, e);
					throw new JspException("EmailWriteTag: could not generate attachment download URL");
				}
			}
		}
		if (url != null)
		{
			buffer.append("<a href=\"").append(url).append("\">");
		}
		buffer.append(attachmentName);
		if (url != null)
		{
			buffer.append("</a>");
		}
	}

	private String messageSubject() throws MessagingException, JspException
	{
		StringBuffer buffer = new StringBuffer();
		String subject = this.message.getSubject();
		if (subject ==null)
			subject = this.getMessage("cms.messaging.mailingConversation.mailMessage.noSubject.label");
		buffer.append(subject);
		return buffer.toString();
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

	public boolean getFilter()
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

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getParamName()
	{
		return paramName;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	public String getParamProperty()
	{
		return paramProperty;
	}

	public void setParamProperty(String paramProperty)
	{
		this.paramProperty = paramProperty;
	}

	public String getAttachmentParamId()
	{
		return attachmentParamId;
	}

	public void setAttachmentParamId(String attachmentParamId)
	{
		this.attachmentParamId = attachmentParamId;
	}

	public String getMailMessageParamId()
	{
		return mailMessageParamId;
	}

	public void setMailMessageParamId(String mailMessageParamId)
	{
		this.mailMessageParamId = mailMessageParamId;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
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
