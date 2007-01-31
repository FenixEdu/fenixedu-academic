package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.Degree;

public class UIBreadCrumbs extends UIInput {

    private static final String CRUMB_SEPERATOR = "&nbsp;&gt;&nbsp;";
    private static final String DEGREE_SITE_LINK = "/publico/showDegreeSite.do?method=showDescription&degreeID=";

    public static final String COMPONENT_TYPE = UIBreadCrumbs.class.getName();
    public static final String COMPONENT_FAMILY = UIBreadCrumbs.class.getName();

    public UIBreadCrumbs() {
        super();
        this.setRendererType(null);
    }

    public String getFamily() {
        return UIBreadCrumbs.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        final Map attributes = getAttributes();

        final Degree degree = (Degree) attributes.get("degree");
        final String trailingCrumb = (String) attributes.get("trailingCrumb");

        final ResponseWriter responseWriter = context.getResponseWriter();

        final Locale locale = context.getViewRoot().getLocale();
        final ResourceBundle globalResourceBundle = ResourceBundle.getBundle("resources/GlobalResources", locale);
        final ResourceBundle publicDegreeInformationResourceBundle = ResourceBundle.getBundle("resources/PublicDegreeInformation", locale);

        final String institutionUrl = globalResourceBundle.getObject("institution.url").toString();
        final String institutionNameAbbreviation = globalResourceBundle.getObject("institution.name.abbreviation").toString();
        final String linkInstitution = globalResourceBundle.getObject("link.institution").toString();
        final String labelEducation = publicDegreeInformationResourceBundle.getObject("public.degree.information.label.education").toString();

        writeLink(responseWriter, institutionUrl, institutionNameAbbreviation);
        responseWriter.write(CRUMB_SEPERATOR);
        writeLink(responseWriter, institutionUrl + linkInstitution, labelEducation);
        responseWriter.write(CRUMB_SEPERATOR);
        if (degree != null) {
            writeLink(responseWriter, context.getExternalContext().getRequestContextPath() + DEGREE_SITE_LINK + degree.getIdInternal(), degree.getSigla());
            responseWriter.write(CRUMB_SEPERATOR);
            responseWriter.write(trailingCrumb);
        }
        //writeBR(responseWriter);
        //writeBR(responseWriter);
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
