package net.sourceforge.fenixedu.presentationTier.TagLib.content;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

public class ContentLinkTag extends BodyTagSupport {

    protected String body = null;
    protected String name = null;
    protected String property = null;
    protected String scope = null;

    public String getName() {
	return (this.name);
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getProperty() {
	return (this.property);
    }

    public void setProperty(String property) {
	this.property = property;
    }

    @Override
    public int doStartTag() throws JspException {
	return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doAfterBody() throws JspException {
	return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
	try {
	    writeStartTag();
	    write(getBodyContent().getString().trim());
	    writeEndTag();
	} catch (IOException e) {
	    throw new JspException(e);
	}

	this.release();

	return EVAL_PAGE;
    }

    protected void writeStartTag() throws IOException, JspException {
	write(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	write("<a href=\"");
	final Content content = DefineContentPathTag.getContent(name, pageContext, getScope(), getProperty());
	write(getContextPath());
	write(content.getReversePath());
	write("\">");
    }

    protected void writeEndTag() throws IOException {
	write("</a>");
    }

    protected void write(final String text) throws IOException {
	pageContext.getOut().write(text);
    }

    protected String getContextPath() {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) pageContext.getRequest();
	return httpServletRequest.getContextPath();
    }

    @Override
    public void release() {
	super.release();
	body = null;
	name = null;
	property = null;
	scope = null;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
