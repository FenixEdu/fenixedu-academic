package net.sourceforge.fenixedu.presentationTier.TagLib.content;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.domain.contents.Content;

import org.apache.commons.lang.StringUtils;

public class DefineContentPathTag extends BodyTagSupport {

    protected String body = null;
    protected String id = null;
    protected String name = null;
    protected String property = null;
    protected String scope = null;
    protected String toScope = null;

    public String getId() {
	return (this.id);
    }

    public void setId(final String id) {
	this.id = id;
    }

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

    public String getScope() {
	return (this.scope);
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public String getToScope() {
	return (this.toScope);
    }

    public void setToScope(String toScope) {
	this.toScope = toScope;
    }


    public int doStartTag() throws JspException {
	return (BodyTagSupport.EVAL_BODY_TAG);
    }

    public int doAfterBody() throws JspException {
	if (bodyContent != null) {
	    body = bodyContent.getString();
	    if (body != null) {
		body = body.trim();
	    }
	    if (body.length() < 1) {
		body = null;
	    }
	}
	return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
	final String id = getId();
	final String name = getName();

	if (id == null || name == null) {
	    throw new JspException("Both id and name are required for define content path.");
	}

	final Content content = getContent(name, pageContext, getScope(), getProperty());
	final int toScope = getPageToScope();
	final String path = content.getReversePath();

	pageContext.setAttribute(id, path, toScope);

	return (EVAL_PAGE);
    }

    public static Content getContent(final String name, final PageContext pageContext, final String scope, final String property) {
	final Object object = getObject(name, pageContext, scope);
	return getContent(object, property);
    }

    public static Object getObject(final String name, final PageContext pageContext, final String scope) {
	final int pageScope = getPageScope(scope);
	return pageScope == -1 ? pageContext.getAttribute(name) : pageContext.getAttribute(name, pageScope);
    }

    protected int getPageToScope() {
	final String scope = getScope();
	final int pageScope = getPageScope(scope);
	return pageScope == -1 ? PageContext.PAGE_SCOPE : pageScope;
    }

    public static int getPageScope(final String scope) {
	if (scope == null) {
	    return -1;
	} else if (scope.equalsIgnoreCase("page")) {
	    return PageContext.PAGE_SCOPE;
	} else if (scope.equalsIgnoreCase("request")) {
	    return PageContext.REQUEST_SCOPE;
	} else if (scope.equalsIgnoreCase("session")) {
	    return PageContext.SESSION_SCOPE;
	} else {
	    return -1;
	}
    }

    public static Content getContent(final Object object, final String property) {
	if (property == null) {
	    return (Content) object;
	}
	final String[] properties = property.split("\\.");
	Object currentObject = object;
	for (final String p : properties) {
	    currentObject = getObject(currentObject, p);
	}
	return (Content) currentObject;
    }

    public static Object getObject(final Object object, final String property) {
	final String methodName = "get" + StringUtils.capitalize(property);
	try {
	    final Method method = object.getClass().getMethod(methodName);
	    return method.invoke(object);
	} catch (final NoSuchMethodException e) {
	    throw new Error(e);
	} catch (final IllegalArgumentException e) {
	    throw new Error(e);
	} catch (final IllegalAccessException e) {
	    throw new Error(e);
	} catch (final InvocationTargetException e) {
	    throw new Error(e);
	}
    }

    public void release() {
	super.release();
	body = null;
	id = null;
	name = null;
	property = null;
	scope = null;
	toScope = null;
    }

}
