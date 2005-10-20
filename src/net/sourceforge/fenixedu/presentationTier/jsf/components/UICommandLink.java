package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfRenderUtils;

public class UICommandLink extends HtmlCommandLink {

    public UICommandLink() {
        super();
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        JsfRenderUtils.addEventSenderHiddenFieldIfNotExists(context, this);

        writer.startElement("a", this);
        writer.writeAttribute("type", (this.getType() != null) ? this.getType() : "", null);
        writer.writeAttribute("title", (this.getTitle() != null) ? this.getTitle() : "", null);
        writer.writeAttribute("target", (this.getTarget() != null) ? this.getTarget() : "", null);
        writer.writeAttribute("onclick", getOnClickEvent(context), null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("name", getClientId(context), null);
        writer.writeAttribute("style", (this.getStyle() != null) ? this.getStyle() : "", null);
        writer.writeAttribute("class", (this.getStyleClass() != null) ? this.getStyleClass() : "", null);

        writer.writeText(this.getValue(), null);

    }

    private String getOnClickEvent(FacesContext context) {
        StringBuilder onClickEvent = new StringBuilder();

        if (this.getOnclick() != null) {
            onClickEvent.append(this.getOnclick()).append(";");
        }

        onClickEvent.append(JsfRenderUtils.getSubmitJavaScript(context, this));

        return onClickEvent.toString();
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("a");
    }

    @Override
    public void decode(FacesContext context) {
        Map paramMap = context.getExternalContext().getRequestParameterMap();

        UIForm parentForm = JsfRenderUtils.findForm(this);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldId = JsfRenderUtils.getEventSenderHiddenFieldId(parentFormClientId);

        String eventSenderId = (String) paramMap.get(eventSenderHiddenFieldId);

        if (eventSenderId != null && eventSenderId.equals(this.getClientId(context))) {
            this.queueEvent(new ActionEvent(this));
        }

    }

}
