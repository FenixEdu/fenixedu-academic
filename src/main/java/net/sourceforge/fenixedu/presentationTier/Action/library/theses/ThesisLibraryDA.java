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
package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.thesis.ThesisSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ThesisLibraryDA extends FenixDispatchAction {

    protected String buildSearchArgs(ThesisSearchBean search) {
        StringBuilder buffer = new StringBuilder();
        if (search.getText() != null && !search.getText().isEmpty()) {
            appendSearchArg(buffer, "text=" + search.getText());
        }
        if (search.getState() != null) {
            appendSearchArg(buffer, "state=" + search.getState().getName());
        }
        if (search.getYear() != null) {
            appendSearchArg(buffer, "year=" + search.getYear().getName());
        }
        return buffer.toString();
    }

    private void appendSearchArg(StringBuilder buffer, String arg) {
        if (buffer.length() == 0) {
            buffer.append('&');
        } else if (buffer.charAt(buffer.length() - 1) != '&') {
            buffer.append('&');
        }
        buffer.append(arg);
    }

    protected ActionForward forward(ActionMapping mapping, HttpServletRequest request, String name, String parameter) {
        ActionForward existing = mapping.findForward(name);
        ActionForward result = new FenixActionForward(request, existing);

        if (parameter != null) {
            String[] values = request.getParameterValues(parameter);

            if (values == null) {
                return result;
            }

            StringBuilder path = new StringBuilder(existing.getPath());
            for (String value : values) {
                path.append(String.format("&%s=%s", parameter, value));
            }

            result.setPath(path.toString());
        }

        return result;
    }

}
