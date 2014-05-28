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

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class UIHtmlEditorTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIHtmlEditor";

    private String width;

    private String height;

    private String value;

    private String showButtons;

    private String required;

    private String maxLength;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getShowButtons() {
        return showButtons;
    }

    public void setShowButtons(String designMode) {
        this.showButtons = designMode;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getComponentType() {

        return COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        JsfTagUtils.setInteger(component, "width", this.width);
        JsfTagUtils.setInteger(component, "height", this.height);
        JsfTagUtils.setString(component, "value", this.value);
        JsfTagUtils.setBoolean(component, "showButtons", this.showButtons);
        JsfTagUtils.setBoolean(component, "required", this.required);
        JsfTagUtils.setInteger(component, "maxLength", this.maxLength);

    }

    @Override
    public void release() {
        super.release();

        width = null;

        height = null;

        value = null;

        showButtons = null;

        required = null;

        maxLength = null;

    }

}
