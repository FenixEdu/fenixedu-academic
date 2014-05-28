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
package net.sourceforge.fenixedu.presentationTier.TagLib.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;

public class ParameterLinkTag extends BodyTagSupport {
    private String page;

    private String module;

    private String anchor;

    private Map<String, String> parameters = new HashMap<String, String>();

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    @Override
    public void release() {
        super.release();

        this.page = null;
        this.module = null;
        this.anchor = null;
        this.parameters = new HashMap<String, String>();
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

    private void writeStartTag() throws IOException, JspException {
        write("<a href=\"" + calculateUrl() + "\">");
    }

    private String calculateUrl() throws JspException {
        try {
            return TagUtils.getInstance().computeURL(pageContext, null, null, getPage(), null, getModule(), this.parameters,
                    getAnchor(), false);
        } catch (MalformedURLException e) {
            throw new JspException("could not compute the requested url with encoding");
        }
    }

    private void writeEndTag() throws IOException {
        write("</a>");
    }

    private void write(String text) throws IOException {
        pageContext.getOut().write(text);
    }

    public void addParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    public void addParameter(String name, Object value) {
        addParameter(name, value == null ? "" : value.toString());
    }
}
