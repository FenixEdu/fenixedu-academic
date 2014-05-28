/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.content;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.domain.Site;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

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
                final Site content = getContent(name, pageContext, getScope(), getProperty());
                write(content.getFullPath());
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
        final Site content = getContent(name, pageContext, getScope(), getProperty());
        write(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
        write("<a href=\"");
        write(content.getFullPath());
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

    public static Site getContent(final String name, final PageContext pageContext, final String scope, final String property) {
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

    public static Site getContent(final Object object, final String property) {
        if (property == null) {
            return (Site) object;
        }
        final String[] properties = property.split("\\.");
        Object currentObject = object;
        for (final String p : properties) {
            currentObject = getObject(currentObject, p);
        }
        return (Site) currentObject;
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
