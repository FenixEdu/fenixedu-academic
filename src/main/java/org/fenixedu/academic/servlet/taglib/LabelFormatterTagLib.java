/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.servlet.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.fenixedu.academic.util.LabelFormatter;

public class LabelFormatterTagLib extends BodyTagSupport implements PropertyContainerTag {

    private String name;

    private String property;

    private String scope;

    public LabelFormatterTagLib() {
    }

    @Override
    public void addProperty(String name, String value) {
    }

    @Override
    public int doEndTag() throws JspException {

        final LabelFormatter labelFormatter =
                (LabelFormatter) TagUtils.getInstance().lookup(this.pageContext, this.name, this.property, this.scope);

        final JspWriter out = this.pageContext.getOut();

        try {
            out.write(labelFormatter.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.property = null;
        this.scope = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
