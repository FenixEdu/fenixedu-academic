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
package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

public class JsfRenderUtils {

    // TODO: Refactor this method and addHiddenFieldForParameterIfNotExists
    public static void addEventHandlingHiddenFieldsIfNotExists(FacesContext context, UIComponent component) throws IOException {

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldFinalId = getEventSenderHiddenFieldId(parentFormClientId);
        String eventSenderHiddenFieldRenderedAttribute = getEventSenderHiddenFieldRenderedAttributeName(parentFormClientId);
        String eventArgumentHiddenFieldFinalId = getEventArgumentHiddenFieldId(parentFormClientId);
        String eventArgumentHiddenFieldRenderedAttribute = getEventArgumentHiddenFieldRenderedAttributeName(parentFormClientId);
        ResponseWriter writer = context.getResponseWriter();

        if (request.getAttribute(eventSenderHiddenFieldRenderedAttribute) == null) {
            renderEmptyHiddenField(eventSenderHiddenFieldFinalId, writer);
            request.setAttribute(eventSenderHiddenFieldRenderedAttribute, true);
        }

        if (request.getAttribute(eventArgumentHiddenFieldRenderedAttribute) == null) {
            renderEmptyHiddenField(eventArgumentHiddenFieldFinalId, writer);
            request.setAttribute(eventArgumentHiddenFieldRenderedAttribute, true);
        }
    }

    private static void renderEmptyHiddenField(String clientId, ResponseWriter writer) throws IOException {
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", "", null);
        writer.endElement("input");
    }

    public static String getEventSenderHiddenFieldId(String formClientId) {

        String eventSenderHiddenFieldFinalId = formClientId + JsfRenderConstants.EVENT_SENDER_HIDDEN_FIELD_SUFFIX;
        return eventSenderHiddenFieldFinalId;
    }

    public static String getEventArgumentHiddenFieldId(String formClientId) {

        String eventArgumentHiddenFieldFinalId = formClientId + JsfRenderConstants.EVENT_ARGUMENT_HIDDEN_FIELD_SUFFIX;
        return eventArgumentHiddenFieldFinalId;
    }

    public static String getEventSenderHiddenFieldRenderedAttributeName(String formClientId) {

        String eventSenderHiddenFieldRenderedAttributeName =
                formClientId + JsfRenderConstants.EVENT_SENDER_HIDDEN_FIELD_RENDERED_SUFFIX;
        return eventSenderHiddenFieldRenderedAttributeName;
    }

    public static String getEventArgumentHiddenFieldRenderedAttributeName(String formClientId) {

        String eventArgumentHiddenFieldRenderedAttributeName =
                formClientId + JsfRenderConstants.EVENT_ARGUMENT_HIDDEN_FIELD_RENDERED_SUFFIX;
        return eventArgumentHiddenFieldRenderedAttributeName;
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

    public static String getSubmitJavaScriptWithArgument(FacesContext context, UIComponent component, String eventArgument) {
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String componentClientId = component.getClientId(context);
        String eventSenderHiddenFieldId = getEventSenderHiddenFieldId(parentFormClientId);
        String eventArgumentHiddenFieldId = getEventArgumentHiddenFieldId(parentFormClientId);

        StringBuilder onClickEvent = new StringBuilder();
        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].elements['");
        onClickEvent.append(eventSenderHiddenFieldId).append("'].value='").append(componentClientId).append("';");

        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].elements['");
        onClickEvent.append(eventArgumentHiddenFieldId).append("'].value='").append(eventArgument).append("';");

        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].submit();");

        return onClickEvent.toString();
    }

    /**
     * 
     * @param context
     * @param component
     * @param uiParameterList
     * @return
     * @throws IOException
     */
    public static String getSubmitJavaScriptWithParameters(FacesContext context, UIComponent component,
            List<UIParameter> uiParameterList) {
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String componentClientId = component.getClientId(context);
        String eventSenderHiddenFieldId = getEventSenderHiddenFieldId(parentFormClientId);

        StringBuilder setParametersJavaScript = new StringBuilder();

        for (int i = 0; i < uiParameterList.size(); i++) {
            UIParameter parameter = uiParameterList.get(i);
            setParametersJavaScript.append("document.forms['").append(parentFormClientId).append("'].elements['")
                    .append(parameter.getName()).append("'].value='").append(parameter.getValue()).append("';");

        }

        StringBuilder onClickEvent = new StringBuilder();
        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].elements['");
        onClickEvent.append(eventSenderHiddenFieldId).append("'].value='").append(componentClientId).append("';");
        onClickEvent.append(setParametersJavaScript.toString());
        onClickEvent.append("document.forms['").append(parentFormClientId).append("'].submit();");

        return onClickEvent.toString();
    }

    // TODO: Refactor this method and addEventHandlingHiddenFieldsIfNotExists to
    // a single method (addHiddenFieldIfNotExists)
    private static void addHiddenFieldForParameterIfNotExists(String parentFormClientId, HttpServletRequest request,
            ResponseWriter writer, String parameterName) throws IOException {

        String renderedAttribute = parentFormClientId + parameterName + JsfRenderConstants.PARAMETER_HIDDEN_FIELD_RENDERED_SUFFIX;

        if (request.getAttribute(renderedAttribute) == null) {
            renderEmptyHiddenField(parameterName, writer);
            request.setAttribute(renderedAttribute, true);
        }

    }

    public static void addHiddenFieldsForParametersIfNotExists(FacesContext context, UIComponent parentComponent,
            List<UIParameter> uiParameters) throws IOException {

        UIForm parentForm = findForm(parentComponent);
        String parentFormClientId = parentForm.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        for (int i = 0; i < uiParameters.size(); i++) {
            UIParameter parameter = uiParameters.get(i);
            addHiddenFieldForParameterIfNotExists(parentFormClientId, request, writer, parameter.getName());
        }
    }

    public static String getClearEventSenderFieldJavaScript(FacesContext context, UIComponent component) {
        UIForm parentForm = findForm(component);
        String parentFormClientId = parentForm.getClientId(context);
        String eventSenderHiddenFieldId = getEventSenderHiddenFieldId(parentFormClientId);

        StringBuilder clearEventSenderFieldJavaScript = new StringBuilder();

        clearEventSenderFieldJavaScript.append("document.forms['").append(parentFormClientId).append("'].elements['")
                .append(eventSenderHiddenFieldId).append("'].value='';");

        return clearEventSenderFieldJavaScript.toString();

    }

}
