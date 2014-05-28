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
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class UIContentLink extends UIOutput {

    public static final String COMPONENT_TYPE = UIContentLink.class.getName();
    public static final String COMPONENT_FAMILY = UIContentLink.class.getName();

    public UIContentLink() {
        super();
        this.setRendererType(null);
    }

    @Override
    public String getFamily() {
        return UIContentLink.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        if (!isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        Site content = (Site) this.getAttributes().get("content");
        String label = (String) this.getAttributes().get("label");

        if (content != null) {
            writer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            writer.append("<a href=\"");
            writer.append(content.getFullPath());
            writer.append("\">").append(label).append("</a>");
        }
    }

    private static String getContextPath() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
    }
}
