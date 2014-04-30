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
