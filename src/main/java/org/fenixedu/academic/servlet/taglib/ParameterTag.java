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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

public class ParameterTag extends TagSupport {
    private String name;

    private String property;

    private String scope;

    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {
        ParameterLinkTag parent = (ParameterLinkTag) this.findAncestorWithClass(this, ParameterLinkTag.class);

        if (parent == null) {
            throw new JspException("this tag must have a paramenter link parent");
        }

        Object value = findValue();
        parent.addParameter(getId(), value);

        return SKIP_BODY;
    }

    private Object findValue() throws JspException {
        if (getName() != null) {
            return TagUtils.getInstance().lookup(pageContext, getName(), getProperty(), getScope());
        } else if (getValue() != null) {
            return getValue();
        } else {
            throw new JspException("must specify name or value attribute");
        }
    }

    @Override
    public int doEndTag() throws JspException {
        this.release();

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.property = null;
        this.scope = null;
        this.value = null;
    }

}
