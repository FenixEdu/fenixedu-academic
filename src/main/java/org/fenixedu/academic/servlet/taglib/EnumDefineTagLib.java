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
/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 16:13:53,23/Set/2005
 * @version $Id: EnumDefineTagLib.java 45653 2010-01-19 18:39:54Z
 *          ist148357@IST.UTL.PT $
 */
public class EnumDefineTagLib extends BodyTagSupport {
    private String id;

    private String enumerationString;

    private String locale = Globals.LOCALE_KEY;

    private String bundle;

    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getEnumeration() {
        return enumerationString;
    }

    public void setEnumeration(String enumeration) {
        this.enumerationString = enumeration;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int doStartTag() throws JspException {
        pageContext.getRequest().setAttribute(id, getLabel());

        return super.doStartTag();
    }

    /**
     * @return
     */
    private String getLabel() {
        String result = "variable undefined";
        Enum enumeration = (Enum) pageContext.findAttribute(this.enumerationString);
        try {
            String message = RequestUtils.message(pageContext, this.bundle, this.locale, enumeration.toString(), null);
            if (message != null) {
                result = message;
            }
        } catch (JspException e) {
            throw new IllegalArgumentException("Expected an enum type, got: " + enumeration);
        }

        return result;
    }
}
