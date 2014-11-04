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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ContactTableRenderer extends AbstractContactRenderer {
    private String label;

    private String rowClasses;

    private String leftColumnClasses;

    private String rightColumnClasses;

    private String leftLineStyle;

    private String rightLineStyle;

    @Override
    protected Layout getLayout(Object unfiltered, Class type) {
        return new Layout() {
            @Override
            public HtmlComponent createComponent(Object unfiltered, Class type) {
                List<MetaObject> contacts = getFilteredContacts((Collection<PartyContact>) unfiltered);
                if (contacts.isEmpty()) {
                    return new HtmlText();
                }
                HtmlInlineContainer span = new HtmlInlineContainer();
                int rowIndex = 0;
                for (MetaObject meta : contacts) {
                    HtmlTableRow row = new HtmlTableRow();
                    row.setClasses(getRowClasses());
                    if (rowIndex == 0) {
                        HtmlTableCell left = row.createCell(CellType.HEADER);
                        left.setClasses(getLeftColumnClasses());
                        left.setStyle(getLeftLineStyle());
                        left.setRowspan(contacts.size());
                        left.setBody(new HtmlText(RenderUtils.getResourceString(getBundle(), getLabel()) + ":"));
                    }
                    HtmlTableCell right = row.createCell();
                    right.setClasses(getRightColumnClasses());
                    right.setStyle(getRightLineStyle());
                    right.setBody(getValue((PartyContact) meta.getObject()));
                    span.addChild(row);
                    rowIndex++;
                }
                return span;
            }
        };
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public String getLeftColumnClasses() {
        return leftColumnClasses;
    }

    public void setLeftColumnClasses(String leftColumnClasses) {
        this.leftColumnClasses = leftColumnClasses;
    }

    public String getRightColumnClasses() {
        return rightColumnClasses;
    }

    public void setRightColumnClasses(String rightColumnClasses) {
        this.rightColumnClasses = rightColumnClasses;
    }

    public String getLeftLineStyle() {
        return leftLineStyle;
    }

    public void setLeftLineStyle(String leftLineStyle) {
        this.leftLineStyle = leftLineStyle;
    }

    public String getRightLineStyle() {
        return rightLineStyle;
    }

    public void setRightLineStyle(String rightLineStyle) {
        this.rightLineStyle = rightLineStyle;
    }
}
