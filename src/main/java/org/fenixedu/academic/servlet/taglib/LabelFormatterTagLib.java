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
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.presentationTier.util.struts.StrutsMessageResourceProvider;

import org.apache.struts.taglib.TagUtils;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class LabelFormatterTagLib extends BodyTagSupport implements PropertyContainerTag {

    private Properties properties;

    private String name;

    private String property;

    private String scope;

    public LabelFormatterTagLib() {

        this.properties = new Properties();
    }

    @Override
    public void addProperty(String name, String value) {
        this.properties.put(name, value);
    }

    @Override
    public int doEndTag() throws JspException {

        final LabelFormatter labelFormatter =
                (LabelFormatter) TagUtils.getInstance().lookup(this.pageContext, this.name, this.property, this.scope);

        final JspWriter out = this.pageContext.getOut();

        try {
            out.write(labelFormatter.toString(new StrutsMessageResourceProvider(this.properties, getUserLocale(),
                    this.pageContext.getServletContext(), (HttpServletRequest) this.pageContext.getRequest())));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private Locale getUserLocale() {
        return TagUtils.getInstance().getUserLocale(this.pageContext, null);
    }

    @Override
    public void release() {
        super.release();

        this.properties = new Properties();
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
