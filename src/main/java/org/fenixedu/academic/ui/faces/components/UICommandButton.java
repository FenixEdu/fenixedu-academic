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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
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
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        JsfRenderUtils.addEventHandlingHiddenFieldsIfNotExists(context, this);
        List<UIParameter> uiParameters = getParametersWithNameAttribute();
        JsfRenderUtils.addHiddenFieldsForParametersIfNotExists(context, this, uiParameters);

        writer.startElement("input", this);
        writer.writeAttribute("type", (this.getType() == null) ? INPUT_SUBMIT_TYPE : this.getType(), null);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("name", getClientId(context), null);
        writer.writeAttribute("style", (this.getStyle() != null) ? this.getStyle() : "", null);
        writer.writeAttribute("class", (this.getStyleClass() != null) ? this.getStyleClass() : "", null);
        writer.writeAttribute("alt", (this.getAlt() != null) ? this.getAlt() : "", null);
        writer.writeAttribute("value", (this.getValue() != null) ? this.getValue() : "", null);
        writer.writeAttribute("onclick", getOnClickEvent(context, uiParameters), null);

        if (this.getType() != null && this.getType().equalsIgnoreCase(INPUT_IMAGE_TYPE)) {
            writer.writeAttribute("src", (this.getImage() != null) ? this.getImage() : "", null);
        }

    }

    private String getOnClickEvent(FacesContext context, List<UIParameter> uiParameters) {
        StringBuilder onClickEvent = new StringBuilder();

        if (this.getOnclick() != null) {
            onClickEvent.append(this.getOnclick()).append(";");
        }

        onClickEvent.append(JsfRenderUtils.getSubmitJavaScriptWithParameters(context, this, uiParameters));

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

    private List<UIParameter> getParametersWithNameAttribute() {
        List<UIParameter> result = new ArrayList<UIParameter>();

        List children = this.getChildren();

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = (UIComponent) children.get(i);

            if (child instanceof UIParameter) {
                UIParameter parameter = (UIParameter) child;

                if (parameter.getName() != null) {
                    result.add(parameter);
                }
            }
        }

        return result;
    }

}
