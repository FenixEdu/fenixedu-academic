/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package ServidorApresentacao.TagLib.assiduousness;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * Tag that retrieves the specified property of the specified bean, converts
 * it to a String representation (if necessary), and writes it to the current
 * output stream, optionally filtering characters that are sensitive in HTML.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class WriteTag extends TagSupport {

	/**
	 * The key to search default format string for 
	 * java.sql.Timestamp in resources.
	 */
	public static final String SQL_TIMESTAMP_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.timestamp";

	/**
	 * The key to search default format string for 
	 * java.sql.Date in resources.
	 */
	public static final String SQL_DATE_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.date";

	/**
	 * The key to search default format string for 
	 * java.sql.Time in resources.
	 */
	public static final String SQL_TIME_FORMAT_KEY = "org.apache.struts.taglib.bean.format.sql.time";

	/**
	 * The key to search default format string for 
	 * java.util.Date in resources.
	 */
	public static final String DATE_FORMAT_KEY = "org.apache.struts.taglib.bean.format.date";

	/**
	 * The key to search default format string for int
	 * (byte, short, etc.) in resources.
	 */
	public static final String INT_FORMAT_KEY = "org.apache.struts.taglib.bean.format.int";

	/**
	 * The key to search default format string for float
	 * (double, BigDecimal) in resources.
	 */
	public static final String FLOAT_FORMAT_KEY = "org.apache.struts.taglib.bean.format.float";

	/**
	 * The message resources for this package.
	 */
	protected static MessageResources messages = MessageResources.getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

	// ------------------------------------------------------------- Properties

	/**
	 * Filter the rendered output for characters that are sensitive in HTML?
	 */
	protected boolean filter = true;

	public boolean getFilter() {
		return (this.filter);
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	/**
	 * Should we ignore missing beans and simply output nothing?
	 */
	protected boolean ignore = false;

	public boolean getIgnore() {
		return (this.ignore);
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	/**
	 * Name of the bean that contains the data we will be rendering.
	 */
	protected String name = null;

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Name of the property to be accessed on the specified bean.
	 */
	protected String property = null;

	public String getProperty() {
		return (this.property);
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * The scope to be searched to retrieve the specified bean.
	 */
	protected String scope = null;

	public String getScope() {
		return (this.scope);
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * The format string to be used as format to convert 
	 * value to String.
	 */
	protected String formatStr = null;

	public String getFormat() {
		return (this.formatStr);
	}

	public void setFormat(String formatStr) {
		this.formatStr = formatStr;
	}

	/**
	 * The key to search format string in applciation resources
	 */
	protected String formatKey = null;

	public String getFormatKey() {
		return (this.formatKey);
	}

	public void setFormatKey(String formatKey) {
		this.formatKey = formatKey;
	}

	/**
	 * The session scope key under which our Locale is stored.
	 */
	protected String localeKey = null;

	public String getLocale() {
		return (this.localeKey);
	}

	public void setLocale(String localeKey) {
		this.localeKey = localeKey;
	}

	/**
	 * The servlet context attribute key for our resources.
	 */
	protected String bundle = null;

	public String getBundle() {
		return (this.bundle);
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Process the start tag.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		// Look up the requested bean (if necessary)
		if (ignore) {
			if (RequestUtils.lookup(pageContext, name, scope) == null)
				return (SKIP_BODY); // Nothing to output
		}

		// Look up the requested property value
		Object value = RequestUtils.lookup(pageContext, name, property, scope);
		if (value == null)
			return (SKIP_BODY); // Nothing to output

		// Convert value to the String with some formatting
		String output = formatValue(value);

		// Print this property value to our output writer, suitably filtered
		if (filter)
			ResponseUtils.write(pageContext, ResponseUtils.filter(output));
		else
			ResponseUtils.write(pageContext, output);

		// Continue processing this page
		return (SKIP_BODY);

	}

	/**
	 * Retrieve format string from message bundle and return null if
	 * message not found or message string.
	 * 
	 * @param formatKey value to use as key to search message in bundle
	 * @exception JspException if a JSP exception has occurred
	 */
	protected String retrieveFormatString(String formatKey) throws JspException {
		String result = RequestUtils.message(pageContext, this.bundle, this.localeKey, formatKey);
		if ((result != null) && !(result.startsWith("???") && result.endsWith("???")))
			return result;
		else
			return null;
	}

	/**
	 * Format value according to specified format string (as tag attribute or
	 * as string from message resources) or to current user locale.
	 * 
	 * @param valueToFormat value to process and convert to String
	 * @exception JspException if a JSP exception has occurred
	 */
	protected String formatValue(Object valueToFormat) throws JspException {
		Format format = null;
		Object value = valueToFormat;
		Locale locale = RequestUtils.retrieveUserLocale(pageContext, this.localeKey);
		boolean formatStrFromResources = false;
		String formatString = formatStr;

		// Return String object as is.
		if (value instanceof java.lang.String) {
			return (String) value;
		} else {

			// Try to retrieve format string from resources by the key from formatKey.
			if ((formatString == null) && (formatKey != null)) {
				formatString = retrieveFormatString(this.formatKey);
				if (formatString != null)
					formatStrFromResources = true;
			}

			// Prepare format object for numeric values.
			if (value instanceof Number) {

				if (formatString == null) {
					if ((value instanceof Byte)
						|| (value instanceof Short)
						|| (value instanceof Integer)
						|| (value instanceof Long)
						|| (value instanceof BigInteger))
						formatString = retrieveFormatString(INT_FORMAT_KEY);
					else if ((value instanceof Float) || (value instanceof Double) || (value instanceof BigDecimal))
						formatString = retrieveFormatString(FLOAT_FORMAT_KEY);
					if (formatString != null)
						formatStrFromResources = true;
				}

				if (formatString != null) {
					try {
						format = NumberFormat.getNumberInstance(locale);
						if (formatStrFromResources)
							 ((DecimalFormat) format).applyLocalizedPattern(formatString);
						else
							 ((DecimalFormat) format).applyPattern(formatString);
					} catch (IllegalArgumentException _e) {
						JspException e = new JspException(messages.getMessage("write.format", formatString));
						RequestUtils.saveException(pageContext, e);
						throw e;
					}
				}

			} else if (value instanceof java.util.Date) {

				if (formatString == null) {

					if (value instanceof java.sql.Timestamp) {
						formatString = retrieveFormatString(SQL_TIMESTAMP_FORMAT_KEY);
					} else if (value instanceof java.sql.Date) {
						formatString = retrieveFormatString(SQL_DATE_FORMAT_KEY);
					} else if (value instanceof java.sql.Time) {
						formatString = retrieveFormatString(SQL_TIME_FORMAT_KEY);
					} else if (value instanceof java.util.Date) {
						formatString = retrieveFormatString(DATE_FORMAT_KEY);
					}

					if (formatString != null)
						formatStrFromResources = true;

				}

				if (formatString != null) {
					if (formatStrFromResources) {
						format = new SimpleDateFormat(formatString, locale);
					} else {
						format = new SimpleDateFormat(formatString);
					}
				}

			}
		}

		if (format != null)
			return format.format(value);
		else
			return value.toString();

	}

	/**
	 * Release all allocated resources.
	 */
	public void release() {

		super.release();
		filter = true;
		ignore = false;
		name = null;
		property = null;
		scope = null;
		formatStr = null;
		formatKey = null;
		localeKey = null;
		bundle = null;

	}

}
