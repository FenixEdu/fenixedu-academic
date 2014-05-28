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
package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class FenixActionForward extends ActionForward {

    private static final long serialVersionUID = 1L;
    private final HttpServletRequest request;

    public FenixActionForward(HttpServletRequest request, ActionForward forward) {
        super(forward);

        this.request = request;
    }

    @Override
    public String getPath() {
        String current = super.getPath();

        String mark = "";
        if (current.indexOf("?") == -1) {
            mark = "?";
        }

        String amp = "";
        if (mark.length() == 0) {
            amp = "&";
        }

        String module = getPathModule();

        String context = request.getContextPath();
        String checksum = GenericChecksumRewriter.calculateChecksum(context + module + current, request.getSession(false));

        return String.format("%s%s%s%s=%s", current, mark, amp, GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME, checksum);
    }

    private String getPathModule() {
        String currentModule = getModule();

        if (currentModule != null) {
            return currentModule;
        }

        ModuleConfig module = ModuleUtils.getInstance().getModuleConfig(this.request);
        if (module == null) {
            return "";
        }

        return module.getPrefix();
    }

}
