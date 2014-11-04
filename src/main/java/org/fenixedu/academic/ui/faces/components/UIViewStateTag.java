/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.fenixedu.academic.ui.faces.components.util.JsfTagUtils;

public class UIViewStateTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "org.fenixedu.academic.ui.faces.components.UIViewState";

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        JsfTagUtils.setString(component, "value", this.value);

    }

    @Override
    public void release() {
        super.release();

        value = null;

    }

}
