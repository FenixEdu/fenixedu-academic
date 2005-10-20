package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

public class JsfRenderUtils {

    public static void addEventSenderHiddenFieldIfNotExists(FacesContext context, UIComponent component)
            throws IOException {

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldFinalId = getEventSenderHiddenFieldId(parentFormClientId);
        String eventSenderHiddenFieldRenderedAttribute = getEventSenderHiddenFieldRenderedAttributeName(parentFormClientId);

        if (request.getAttribute(eventSenderHiddenFieldRenderedAttribute) == null) {

            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("input", null);
            writer.writeAttribute("type", "hidden", null);
            writer.writeAttribute("id", eventSenderHiddenFieldFinalId, null);
            writer.writeAttribute("name", eventSenderHiddenFieldFinalId, null);
            writer.writeAttribute("value", "", null);
            writer.endElement("input");

            request.setAttribute(eventSenderHiddenFieldRenderedAttribute, true);

        }
    }

    public static String getEventSenderHiddenFieldId(String formClientId) {

        String eventSenderHiddenFieldFinalId = formClientId
                + JsfRenderConstants.EVENT_SENDER_HIDDEN_FIELD_SUFFIX;
        return eventSenderHiddenFieldFinalId;
    }

    public static String getEventSenderHiddenFieldRenderedAttributeName(String formClientId) {

        String eventSenderHiddenFieldRenderedAttributeName = formClientId
                + JsfRenderConstants.EVENT_SENDER_HIDDEN_FIELD_RENDERED_SUFFIX;
        return eventSenderHiddenFieldRenderedAttributeName;
    }

    public static UIForm findForm(UIComponent component) {
        UIComponent formCandidate = component.getParent();

        while (formCandidate != null && !(formCandidate instanceof UIForm)) {
            formCandidate = formCandidate.getParent();
        }

        return (UIForm) formCandidate;
    }

    public static String getSubmitJavaScript(FacesContext context, UIComponent component) {
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String componentClientId = component.getClientId(context);
        String eventSenderHiddenFieldId = getEventSenderHiddenFieldId(parentFormClientId);

        StringBuilder onClickEvent = new StringBuilder();
        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].elements['");
        onClickEvent.append(eventSenderHiddenFieldId).append("'].value='").append(componentClientId);

        onClickEvent.append("';document.forms['").append(parentFormClientId).append("'].submit();");

        return onClickEvent.toString();
    }

    public static String getClearEventSenderFieldJavaScript(FacesContext context, UIComponent component) {
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldId = getEventSenderHiddenFieldId(parentFormClientId);

        StringBuilder clearEventSenderFieldJavaScript = new StringBuilder();

        clearEventSenderFieldJavaScript.append("document.forms['").append(parentFormClientId).append(
                "'].elements['").append(eventSenderHiddenFieldId).append("'].value='';");

        return clearEventSenderFieldJavaScript.toString();

    }

}
