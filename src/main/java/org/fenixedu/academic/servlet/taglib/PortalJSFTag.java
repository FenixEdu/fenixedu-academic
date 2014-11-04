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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.fenixedu.bennu.struts.portal.StrutsPortalBackend;

public class PortalJSFTag extends TagSupport {

    private static final long serialVersionUID = 7598687970107322370L;
    private String actionClass;

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Class<?> actionClass = Class.forName(this.actionClass);
            StrutsPortalBackend.chooseSelectedFunctionality((HttpServletRequest) pageContext.getRequest(), actionClass);
            return SKIP_BODY;
        } catch (ClassNotFoundException e) {
            throw new JspException("Cannot find action class", e);
        }
    }

}
