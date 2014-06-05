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
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class UIBreadCrumbs extends UIInput {

    private static final String CRUMB_SEPERATOR = "&nbsp;&gt;&nbsp;";
    private static final String DEGREE_SITE_LINK = "/publico/showDegreeSite.do?method=showDescription&degreeID=";

    public static final String COMPONENT_TYPE = UIBreadCrumbs.class.getName();
    public static final String COMPONENT_FAMILY = UIBreadCrumbs.class.getName();

    public UIBreadCrumbs() {
        super();
        this.setRendererType(null);
    }

    @Override
    public String getFamily() {
        return UIBreadCrumbs.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        final Map attributes = getAttributes();

        final Degree degree = (Degree) attributes.get("degree");
        final String trailingCrumb = (String) attributes.get("trailingCrumb");

        final ResponseWriter responseWriter = context.getResponseWriter();

        final String institutionUrl = Installation.getInstance().getInstituitionURL();
        final String institutionNameAbbreviation = Unit.getInstitutionAcronym();
        final String linkInstitution = BundleUtil.getString(Bundle.GLOBAL, "link.institution");
        final String labelEducation = BundleUtil.getString(Bundle.DEGREE, "public.degree.information.label.education").toString();

        writeLink(responseWriter, institutionUrl, institutionNameAbbreviation);
        responseWriter.write(CRUMB_SEPERATOR);
        writeLink(responseWriter, institutionUrl + linkInstitution, labelEducation);
        responseWriter.write(CRUMB_SEPERATOR);
        if (degree != null) {
            writeLink(responseWriter,
                    context.getExternalContext().getRequestContextPath() + DEGREE_SITE_LINK + degree.getExternalId(),
                    degree.getSigla());
            responseWriter.write(CRUMB_SEPERATOR);
            responseWriter.write(trailingCrumb);
        }
        // writeBR(responseWriter);
        // writeBR(responseWriter);
    }

    private void writeLink(final ResponseWriter responseWriter, final String link, final String text) throws IOException {
        responseWriter.startElement("a", this);
        responseWriter.writeAttribute("href", link, null);
        responseWriter.write(text);
        responseWriter.endElement("a");
    }

    private void writeBR(final ResponseWriter responseWriter) throws IOException {
        responseWriter.startElement("br", this);
        responseWriter.endElement("br");
    }

}
