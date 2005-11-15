/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.TagLib.cms.email;


import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import net.sourceforge.fenixedu.domain.cms.messaging.IMailMessage;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:27:42,11/Nov/2005
 * @version $Id$
 */
public class EmailAttachmentTag extends TagSupport
{
	protected String action = "/mailMessageManagement";

	protected String module = "/cms";

	protected String method = "downloadAttachment";

	protected boolean filter = false;

	protected String attachmentParamId = "attachmentName";

	protected String mailMessageParamId = "mailMessageId";

	protected String paramName;

	protected String paramProperty = "idInternal";

	protected IMailMessage message;

	protected String bundle;

	protected String locale;

	protected String name;

	protected String property;

	protected String scope;

	protected String attachmentSeparator = ", ";

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
			this.message = (IMailMessage) value;
		}
		// Convert value to the String with some formatting
		try
		{
			String attachments = this.buildAttachments();

			StringBuffer output = new StringBuffer();
			if (this.message.getAllAttachmentsNames().size() > 0)
			{
				output.append(attachments);
			}
			TagUtils.getInstance().write(pageContext, output.toString());

		}
		catch (MessagingException e)
		{
			new JspException(e);
			e.printStackTrace();
		}

		return (SKIP_BODY);
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

	private String messageAttachments() throws MessagingException, JspException
	{
		StringBuffer buffer = new StringBuffer();
		boolean firstAddress = true;
		for (String attachmentName : this.message.getAllAttachmentsNames())
		{
			if (!firstAddress) buffer.append(this.attachmentSeparator);
			this.buildAttachmentLink(buffer, this.filter ? TagUtils.getInstance().filter(attachmentName)
					: attachmentName);
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
					throw new JspException("EmailAttachmentTag: could not generate attachment download URL");
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

	private String getMessage(String key) throws JspException
	{
		return TagUtils.getInstance().message(this.pageContext, this.bundle, this.locale, key);
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getAttachmentParamId()
	{
		return attachmentParamId;
	}

	public void setAttachmentParamId(String attachmentParamId)
	{
		this.attachmentParamId = attachmentParamId;
	}

	public boolean isFilter()
	{
		return filter;
	}

	public void setFilter(boolean filter)
	{
		this.filter = filter;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getModule()
	{
		return module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public String getBundle()
	{
		return bundle;
	}

	public void setBundle(String bundle)
	{
		this.bundle = bundle;
	}

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

	public String getAttachmentSeparator()
	{
		return attachmentSeparator;
	}

	public void setAttachmentSeparator(String attachmentSeparator)
	{
		this.attachmentSeparator = attachmentSeparator;
	}

	public String getMailMessageParamId()
	{
		return mailMessageParamId;
	}

	public void setMailMessageParamId(String mailMessageParamId)
	{
		this.mailMessageParamId = mailMessageParamId;
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

	public void setScope(String scope)
	{
		this.scope = scope;
	}
}
