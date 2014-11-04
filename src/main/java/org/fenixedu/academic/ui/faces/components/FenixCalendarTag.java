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

public class FenixCalendarTag extends UIComponentTag {

    private String begin;
    private String end;
    private String createLink;
    private String editLinkPage;
    private String editLinkParameters;
    private String extraLines;

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreateLink() {
        return createLink;
    }

    public void setCreateLink(String createLink) {
        this.createLink = createLink;
    }

    public String getEditLinkPage() {
        return editLinkPage;
    }

    public void setEditLinkPage(String editLinkPage) {
        this.editLinkPage = editLinkPage;
    }

    public String getEditLinkParameters() {
        return editLinkParameters;
    }

    public void setEditLinkParameters(String editLinkParameters) {
        this.editLinkParameters = editLinkParameters;
    }

    public String getExtraLines() {
        return extraLines;
    }

    public void setExtraLines(String extraLines) {
        this.extraLines = extraLines;
    }

    @Override
    public String getComponentType() {
        return UIFenixCalendar.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        JsfTagUtils.setString(component, "begin", this.begin);
        JsfTagUtils.setString(component, "end", this.end);
        JsfTagUtils.setString(component, "createLink", this.createLink);
        JsfTagUtils.setString(component, "editLinkPage", this.editLinkPage);
        JsfTagUtils.setString(component, "editLinkParameters", this.editLinkParameters);
        JsfTagUtils.setString(component, "extraLines", this.extraLines);
    }

    @Override
    public void release() {
        super.release();
        begin = null;
        end = null;
        createLink = null;
        editLinkPage = null;
        editLinkParameters = null;
        extraLines = null;
    }

}
