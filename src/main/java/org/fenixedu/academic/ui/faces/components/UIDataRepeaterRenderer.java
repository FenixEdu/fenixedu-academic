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
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class UIDataRepeaterRenderer extends BaseRenderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public String convertClientId(FacesContext context, String clientId) {
        return clientId;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

        UIDataRepeater dataRepeater = (UIDataRepeater) component;

        int first = dataRepeater.getFirst();
        int rows = dataRepeater.getRows();
        int rowCount = dataRepeater.getRowCount();

        if (rows <= 0) {
            rows = rowCount - first;
        }

        int last = first + rows;

        if (last > rowCount) {
            last = rowCount;
        }

        for (int i = first; i < last; i++) {
            dataRepeater.setRowIndex(i);
            if (dataRepeater.isRowAvailable()) {
                if (dataRepeater.getChildCount() > 0) {
                    for (Iterator it = dataRepeater.getChildren().iterator(); it.hasNext();) {
                        UIComponent child = (UIComponent) it.next();
                        // For some reason its necessary to touch Id property,
                        // otherwise
                        // the child control will not call getClientId on parent
                        // (NamingContainer)
                        child.setId(child.getId());
                        encodeRecursive(context, child);
                    }
                }

            }
        }
    }

}
