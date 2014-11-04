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

public class BreadCrumbsTag extends UIComponentTag {

    private String degree;
    private String trailingCrumb;

    @Override
    public String getComponentType() {
        return UIBreadCrumbs.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);
        JsfTagUtils.setString(component, "degree", this.degree);
        JsfTagUtils.setString(component, "trailingCrumb", this.trailingCrumb);
    }

    @Override
    public void release() {
        super.release();
        setDegree(null);
        setTrailingCrumb(null);
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(final String degree) {
        this.degree = degree;
    }

    public String getTrailingCrumb() {
        return trailingCrumb;
    }

    public void setTrailingCrumb(String trailingCrumb) {
        this.trailingCrumb = trailingCrumb;
    }

}
