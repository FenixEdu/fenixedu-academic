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
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        JsfRenderUtils.addEventHandlingHiddenFieldsIfNotExists(context, this);
        List<UIParameter> uiParameters = getParametersWithNameAttribute();
        JsfRenderUtils.addHiddenFieldsForParametersIfNotExists(context, this, uiParameters);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("a", this);
        writer.writeAttribute("type", (this.getType() != null) ? this.getType() : "", null);
        writer.writeAttribute("title", (this.getTitle() != null) ? this.getTitle() : "", null);
        if (this.getTarget() != null) {
            writer.writeAttribute("target", this.getTarget(), null);
        }
        writer.writeAttribute("onclick", getOnClickEvent(context, uiParameters), null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("name", getClientId(context), null);
        writer.writeAttribute("style", (this.getStyle() != null) ? this.getStyle() : "", null);
        writer.writeAttribute("class", (this.getStyleClass() != null) ? this.getStyleClass() : "", null);

        writer.writeText(this.getValue(), null);

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
