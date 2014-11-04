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

import javax.faces.component.UIOutput;

public class UIExtendedOutputText extends UIOutput {

    public UIExtendedOutputText() {
        super();
    }

    @Override
    public Object getValue() {
        final Boolean linebreak = (Boolean) getAttributes().get("linebreak");
        Object value = super.getValue();
        if (value != null && linebreak != null && linebreak.booleanValue()) {
            value = super.getValue().toString().replaceAll("(\r\n)|(\n)", "<br />");
        }
        return value;
    }
}
