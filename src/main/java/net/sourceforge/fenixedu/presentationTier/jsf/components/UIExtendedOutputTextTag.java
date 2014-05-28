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
/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

import com.sun.faces.taglib.html_basic.OutputTextTag;

public class UIExtendedOutputTextTag extends OutputTextTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIExtendedOutputText";

    private String linebreak;

    @Override
    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getLinebreak() {
        return linebreak;
    }

    public void setLinebreak(String linebreak) {
        this.linebreak = linebreak;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        JsfTagUtils.setBoolean(component, "linebreak", getLinebreak());
        Boolean lineBreakBoolean = Boolean.valueOf(getLinebreak() != null ? getLinebreak() : "false");
        if (lineBreakBoolean.booleanValue()) {
            JsfTagUtils.setBoolean(component, "escape", "false");
        }
    }

    @Override
    public void release() {
        super.release();
        setLinebreak(null);
    }
}
