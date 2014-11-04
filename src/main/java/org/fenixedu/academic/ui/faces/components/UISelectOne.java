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

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfRenderUtils;

public class UISelectOne extends javax.faces.component.UISelectOne {

    public UISelectOne() {
        super();
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        JsfRenderUtils.addEventHandlingHiddenFieldsIfNotExists(context, this);

        String clearEventSenderFieldJavaScript = JsfRenderUtils.getClearEventSenderFieldJavaScript(context, this);

        StringBuilder newOnChangeEvent = new StringBuilder(clearEventSenderFieldJavaScript);

        String onChangeEvent = (String) this.getAttributes().get("onchange");

        if (onChangeEvent != null && onChangeEvent.length() != 0) {
            if (onChangeEvent.indexOf(clearEventSenderFieldJavaScript) == 0) {
                newOnChangeEvent.append(onChangeEvent);
            } else {
                newOnChangeEvent.append(clearEventSenderFieldJavaScript).append(onChangeEvent);
            }

        } else {
            newOnChangeEvent.append(clearEventSenderFieldJavaScript);
        }

        this.getAttributes().put("onchange", newOnChangeEvent.toString());

        super.encodeBegin(context);

    }

}
