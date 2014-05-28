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
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;

public class ContactListRenderer extends AbstractContactRenderer {
    private String itemClasses;

    @Override
    protected Layout getLayout(Object unfiltered, Class type) {
        return new Layout() {
            @Override
            public HtmlComponent createComponent(Object unfiltered, Class type) {
                List<MetaObject> contacts = getFilteredContacts((Collection<PartyContact>) unfiltered);
                if (contacts.isEmpty()) {
                    return new HtmlText();
                }
                HtmlList list = new HtmlList();
                list.setClasses(getClasses());
                for (MetaObject meta : contacts) {
                    HtmlListItem item = list.createItem();
                    item.setClasses(getItemClasses());
                    item.setBody(getValue((PartyContact) meta.getObject()));
                }
                return list;
            }
        };
    }

    public String getItemClasses() {
        return itemClasses;
    }

    public void setItemClasses(String itemClasses) {
        this.itemClasses = itemClasses;
    }
}
