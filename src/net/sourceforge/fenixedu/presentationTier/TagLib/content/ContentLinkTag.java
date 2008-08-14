package net.sourceforge.fenixedu.presentationTier.TagLib.content;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

import org.apache.struts.util.RequestUtils;

public class ContentLinkTag extends BodyTagSupport {

    protected String body = null;
    protected String name = null;
    protected String property = null;
    protected String scope = null;
    protected String target = null;
    protected String title = null;
    protected Boolean hrefInBody = null;
    protected String styleClass = null;

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

    public String getTarget() {
	return target;
    }

    public void setTarget(String target) {
	this.target = target;
    }

    public String getTitle() {
	return title;
    }

    public Boolean getHrefInBody() {
	return hrefInBody;
    }

    public void setHrefInBody(Boolean hrefInBody) {
	this.hrefInBody = hrefInBody;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getStyleClass() {
	return styleClass;
    }

    public void setStyleClass(String styleClass) {
	this.styleClass = styleClass;
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
	    if (getHrefInBody() != null && getHrefInBody()) {
		final Content content = DefineContentPathTag.getContent(name, pageContext, getScope(), getProperty());
		final String path = content.getReversePath();
		write(RequestUtils.absoluteURL((HttpServletRequest) pageContext.getRequest(), path).toString());
	    } else {
		write(getBodyContent().getString().trim());
	    }
	    writeEndTag();
	} catch (IOException e) {
	    throw new JspException(e);
	}

	this.release();

	return EVAL_PAGE;
    }

    protected void writeStartTag() throws IOException, JspException {
	final Content content = DefineContentPathTag.getContent(name, pageContext, getScope(), getProperty());
	if (content.isPublic()) {
	    write(ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
	} else {
	    write(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	}
	write("<a href=\"");
	write(getContextPath());
	write(content.getReversePath());
	write("\"");
	if (getTarget() != null) {
	    write(" target=\"" + getTarget() + "\"");
	}
	if (getTitle() != null) {
	    write(" title=\"" + getTitle() + "\"");
	}
	if (getStyleClass() != null) {
	    write(" class=\"" + getStyleClass() + "\"");
	}
	write(">");
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
	target = null;
	hrefInBody = null;
    }

    public String getScope() {
	return scope;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }
}
