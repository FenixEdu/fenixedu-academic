package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

public class UIContentLink extends UIOutput {

    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIContentLink";
    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIContentLink";

    public UIContentLink() {
	super();
	this.setRendererType(null);
    }

    public String getFamily() {
	return UIContentLink.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

	if (!isRendered()) {
	    return;
	}

	ResponseWriter writer = context.getResponseWriter();
	Integer contentIdInternal = (Integer) this.getAttributes().get("contentIdInternal");
	String label = (String) this.getAttributes().get("label");
	Content content = RootDomainObject.getInstance().readContentByOID(Integer.valueOf(contentIdInternal));

	writer.append(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	writer.append("<a href=\"");
	writer.append(getContextPath(context));
	writer.append(content.getReversePath());
	writer.append("\">").append(label).append("</a>");

    }

    private static String getContextPath(FacesContext facesContext) {
	return ((HttpServletRequest) facesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
    }
}
