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

package net.sourceforge.fenixedu.presentationTier.TagLib.assiduousness;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * Custom tag that retrieves an internationalized messages string (with optional
 * parametric replacement) from the <code>ActionResources</code> object stored
 * as a context attribute by our associated <code>ActionServlet</code>
 * implementation.
 * 
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class MessageTag extends TagSupport {

    // ------------------------------------------------------------- Properties

    /**
     * The first optional argument.
     */
    protected String arg0 = null;

    public String getArg0() {
        return (this.arg0);
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * The second optional argument.
     */
    protected String arg1 = null;

    public String getArg1() {
        return (this.arg1);
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * The third optional argument.
     */
    protected String arg2 = null;

    public String getArg2() {
        return (this.arg2);
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * The fourth optional argument.
     */
    protected String arg3 = null;

    public String getArg3() {
        return (this.arg3);
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    /**
     * The fifth optional argument.
     */
    protected String arg4 = null;

    public String getArg4() {
        return (this.arg4);
    }

    public void setArg4(String arg4) {
        this.arg4 = arg4;
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

    /**
     * The default Locale for our server.
     */
    protected static final Locale defaultLocale = Locale.getDefault();

    /**
     * The message key of the message to be retrieved.
     */
    protected String key = null;

    public String getKey() {
        return (this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Name of the bean that contains the message key.
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
     * The session scope key under which our Locale is stored.
     */
    protected String localeKey = Globals.LOCALE_KEY;

    public String getLocale() {
        return (this.localeKey);
    }

    public void setLocale(String localeKey) {
        this.localeKey = localeKey;
    }

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    // --------------------------------------------------------- Public Methods

    /**
     * Process the start tag.
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String key = this.key;
        if (key == null) {
            // Look up the requested property value
            Object value = RequestUtils.lookup(pageContext, name, property, scope);
            if (value != null && !(value instanceof String)) {
                JspException e = new JspException(messages.getMessage("message.property", key));
                RequestUtils.saveException(pageContext, e);
                throw e;
            }
            key = (String) value;
        }

        // Construct the optional arguments array we will be using
        Object args[] = new Object[5];
        args[0] = arg0;
        args[1] = arg1;
        args[2] = arg2;
        args[3] = arg3;
        args[4] = arg4;

        // acrescentado por Fernanda Quitério & Tânia Pousão
        String k = (String) pageContext.findAttribute(key);
        if (k != null)
            key = k;
        //até aqui

        // Retrieve the message string we are looking for
        String message = RequestUtils.message(pageContext, this.bundle, this.localeKey, key, args);
        if (message == null) {
            JspException e = new JspException(messages.getMessage("message.message", key));
            RequestUtils.saveException(pageContext, e);
            throw e;
        }

        // Print the retrieved message to our output writer
        ResponseUtils.write(pageContext, message);

        // Continue processing this page
        return (SKIP_BODY);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        arg0 = null;
        arg1 = null;
        arg2 = null;
        arg3 = null;
        arg4 = null;
        bundle = Globals.MESSAGES_KEY;
        key = null;
        name = null;
        property = null;
        scope = null;
        localeKey = Globals.LOCALE_KEY;

    }

}