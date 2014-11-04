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

public class UIDataRepeaterTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIDataRepeater";

    private static final String RENDERER_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIDataRepeaterRenderer";

    // UIData attributes
    private String rows;

    private String var;

    private String first;

    // HtmlDataList attributes
    private String layout;

    private String rowIndexVar;

    private String rowCountVar;

    private String value;

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        JsfTagUtils.setInteger(component, "rows", this.rows);
        JsfTagUtils.setString(component, "var", this.var);
        JsfTagUtils.setString(component, "layout", this.layout);
        JsfTagUtils.setString(component, "rowIndexVar", this.rowIndexVar);
        JsfTagUtils.setString(component, "rowCountVar", this.rowCountVar);
        JsfTagUtils.setString(component, "value", this.value);

    }

    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getRowCountVar() {
        return rowCountVar;
    }

    public void setRowCountVar(String rowCountVar) {
        this.rowCountVar = rowCountVar;
    }

    public String getRowIndexVar() {
        return rowIndexVar;
    }

    public void setRowIndexVar(String rowIndexVar) {
        this.rowIndexVar = rowIndexVar;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void release() {
        super.release();
        rows = null;
        var = null;
        first = null;
        layout = null;
        rowIndexVar = null;
        rowCountVar = null;
    }

}
