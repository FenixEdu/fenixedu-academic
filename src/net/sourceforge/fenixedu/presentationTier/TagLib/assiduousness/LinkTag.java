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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.logic.IterateTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * Generate a URL-encoded hyperlink to the specified URI.
 * 
 * @author Craig R. McClanahan
 * @author James Turner
 * @version $Revision$ $Date$
 */

public class LinkTag extends BaseHandlerTag {

    // ----------------------------------------------------- Instance Variables

    /**
     * The body content of this tag (if any).
     */
    protected String text = null;

    // ------------------------------------------------------------- Properties

    /**
     * The anchor to be added to the end of the generated hyperlink.
     */
    protected String anchor = null;

    public String getAnchor() {
        return (this.anchor);
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * The logical forward name from which to retrieve the hyperlink URI.
     */
    protected String forward = null;

    public String getForward() {
        return (this.forward);
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    /**
     * The hyperlink URI.
     */
    protected String href = null;

    public String getHref() {
        return (this.href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    /**
     * The link name for named links.
     */
    protected String linkName = null;

    public String getLinkName() {
        return (this.linkName);
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages = MessageResources.getMessageResources(Constants.Package
            + ".LocalStrings");

    /**
     * The JSP bean name for query parameters.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The module-relative page URL (beginning with a slash) to which this
     * hyperlink will be rendered.
     */
    protected String page = null;

    public String getPage() {
        return (this.page);
    }

    public void setPage(String page) {
        this.page = page;
    }

    /**
     * The module-relative action (beginning with a slash) which will be called
     * by this link
     */
    protected String action = null;

    public String getAction() {
        return (this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    /**
     * The single-parameter request parameter name to generate.
     */
    protected String paramId = null;

    public String getParamId() {
        return (this.paramId);
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    /**
     * The single-parameter JSP bean name.
     */
    protected String paramName = null;

    public String getParamName() {
        return (this.paramName);
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * The single-parameter JSP bean property.
     */
    protected String paramProperty = null;

    public String getParamProperty() {
        return (this.paramProperty);
    }

    public void setParamProperty(String paramProperty) {
        this.paramProperty = paramProperty;
    }

    /**
     * The single-parameter JSP bean scope.
     */
    protected String paramScope = null;

    public String getParamScope() {
        return (this.paramScope);
    }

    public void setParamScope(String paramScope) {
        this.paramScope = paramScope;
    }

    /**
     * The JSP bean property name for query parameters.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The scope of the bean specified by the name property, if any.
     */
    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * The window target.
     */
    protected String target = null;

    public String getTarget() {
        return (this.target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Include transaction token (if any) in the hyperlink?
     */
    protected boolean transaction = false;

    public boolean getTransaction() {
        return (this.transaction);
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    /**
     * Name of parameter to generate to hold index number
     */
    protected String indexId = null;

    public String getIndexId() {
        return (this.indexId);
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the beginning of the hyperlink.
     * <p>
     * Support for indexed property since Struts 1.1
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Special case for name anchors
        if (linkName != null) {
            StringBuffer results = new StringBuffer("<a name=\"");
            results.append(linkName);
            results.append("\">");
            ResponseUtils.write(pageContext, results.toString());
            return (EVAL_BODY_BUFFERED);
        }

        //acrescentado por Fernanda Quitério & Tânia Pousão
        // Generate the hyperlink URL
        Map params = RequestUtils.computeParameters(pageContext, paramId, paramName, paramProperty,
                paramScope, name, property, scope, transaction);

        String fwd = (String) pageContext.findAttribute(forward);
        if (fwd != null)
            forward = fwd;

        try {
            RequestUtils.computeURL(pageContext, forward, href, page, null, params, anchor, false);
        } catch (MalformedURLException e) {
            RequestUtils.saveException(pageContext, e);
            throw new JspException(messages.getMessage("rewrite.url", e.toString()));
        }
        //até aqui

        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a href=\"");
        // * @since Struts 1.1
        results.append(calculateURL());
        results.append("\"");
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(prepareStyles());
        results.append(prepareEventHandlers());
        results.append(">");

        // Print this element to our output writer
        ResponseUtils.write(pageContext, results.toString());

        // Evaluate the body of this tag
        this.text = null;
        return (EVAL_BODY_BUFFERED);

    }

    /**
     * Save the associated label from the body content.
     */
    public int doAfterBody() {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return (SKIP_BODY);

    }

    /**
     * Render the end of the hyperlink.
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Prepare the textual content and ending element of this hyperlink
        StringBuffer results = new StringBuffer();
        if (text != null)
            results.append(text);
        results.append("</a>");

        // Render the remainder to the output stream
        ResponseUtils.write(pageContext, results.toString());

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        anchor = null;
        forward = null;
        href = null;
        linkName = null;
        name = null;
        page = null;
        action = null;
        paramId = null;
        paramName = null;
        paramProperty = null;
        paramScope = null;
        property = null;
        scope = null;
        target = null;
        text = null;
        transaction = false;

    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the complete URL to which this hyperlink will direct the user.
     * Support for indexed property since Struts 1.1
     * 
     * @exception JspException
     *                if an exception is thrown calculating the value
     */
    protected String calculateURL() throws JspException {

        // Identify the parameters we will add to the completed URL
        Map params = RequestUtils.computeParameters(pageContext, paramId, paramName, paramProperty,
                paramScope, name, property, scope, transaction);

        // if "indexed=true", add "index=x" parameter to query string
        // * @since Struts 1.1
        if (indexed) {

            // look for outer iterate tag
            IterateTag iterateTag = (IterateTag) findAncestorWithClass(this, IterateTag.class);
            if (iterateTag == null) {
                // This tag should only be nested in an iterate tag
                // If it's not, throw exception
                JspException e = new JspException(messages.getMessage("indexed.noEnclosingIterate"));
                RequestUtils.saveException(pageContext, e);
                throw e;
            }

            //calculate index, and add as a parameter
            if (params == null) {
                params = new HashMap(); //create new HashMap if no other params
            }
            if (indexId != null) {
                params.put(indexId, Integer.toString(iterateTag.getIndex()));
            } else {
                params.put("index", Integer.toString(iterateTag.getIndex()));
            }
        }

        String url = null;
        try {
            url = RequestUtils.computeURL(pageContext, forward, href, page, action, params, anchor,
                    false);
        } catch (MalformedURLException e) {
            RequestUtils.saveException(pageContext, e);
            throw new JspException(messages.getMessage("rewrite.url", e.toString()));
        }
        return (url);

    }

}