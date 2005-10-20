package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfRenderUtils;

public class UICommandButton extends HtmlCommandButton {

    private static final String INPUT_SUBMIT_TYPE = "submit";

    private static final String INPUT_RESET_TYPE = "reset";

    private static final String INPUT_IMAGE_TYPE = "image";

    public UICommandButton() {
        super();
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        JsfRenderUtils.addEventSenderHiddenFieldIfNotExists(context, this);

        String onClickEvent = getOnClickEvent(context);

        writer.startElement("input", this);
        writer.writeAttribute("type", (this.getType() == null) ? INPUT_SUBMIT_TYPE : this.getType(),
                null);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("name", getClientId(context), null);
        writer.writeAttribute("style", (this.getStyle() != null) ? this.getStyle() : "", null);
        writer.writeAttribute("class", (this.getStyleClass() != null) ? this.getStyleClass() : "", null);
        writer.writeAttribute("alt", (this.getAlt() != null) ? this.getAlt() : "", null);
        writer.writeAttribute("value", (this.getValue() != null) ? this.getValue() : "", null);
        writer.writeAttribute("onclick", onClickEvent, null);

        if (this.getType() != null && this.getType().equalsIgnoreCase(INPUT_IMAGE_TYPE)) {
            writer.writeAttribute("src", (this.getImage() != null) ? this.getImage() : "", null);
        }

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

        writer.endElement("input");
    }

    @Override
    public void decode(FacesContext context) {
        Map paramMap = context.getExternalContext().getRequestParameterMap();

        UIForm parentForm = JsfRenderUtils.findForm(this);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldId = JsfRenderUtils.getEventSenderHiddenFieldId(parentFormClientId);

        String eventSenderId = (String) paramMap.get(eventSenderHiddenFieldId);
        boolean isReset = false;

        if (this.getType() != null && this.getType().equalsIgnoreCase(INPUT_RESET_TYPE)) {
            isReset = true;
        }

        if (!isReset && (eventSenderId != null) && eventSenderId.equals(this.getClientId(context))) {
            this.queueEvent(new ActionEvent(this));
        }

    }

}
