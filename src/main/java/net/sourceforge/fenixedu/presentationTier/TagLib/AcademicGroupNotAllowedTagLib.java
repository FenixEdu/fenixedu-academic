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

import javax.servlet.jsp.JspException;

public class AcademicGroupNotAllowedTagLib extends AcademicGroupTagLib {

    /**
     * 
     */
    private static final long serialVersionUID = 1529594302892469648L;

    @Override
    public int doStartTag() throws JspException {
        int result = super.doStartTag();

        if (result == EVAL_BODY_INCLUDE) {
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }
}
