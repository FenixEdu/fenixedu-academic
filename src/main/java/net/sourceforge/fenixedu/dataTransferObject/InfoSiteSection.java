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
 * Created on 7/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 */
public class InfoSiteSection extends DataTranferObject implements ISiteComponent {

    private InfoSection section;

    private List items;

    public InfoSiteSection() {
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String result = "[INFOSITESECTION";
        result += " section=" + getSection();
        result += ", items=" + getItems();
        result += "]";
        return result;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteSection
                && (((((InfoSiteSection) objectToCompare).getSection() != null && this.getSection() != null && ((InfoSiteSection) objectToCompare)
                        .getSection().equals(this.getSection())) || ((InfoSiteSection) objectToCompare).getSection() == null
                        && this.getSection() == null))) {
            result = true;
        }

        if (((InfoSiteSection) objectToCompare).getItems() == null && this.getItems() == null && result == true) {
            return true;
        }
        if (((InfoSiteSection) objectToCompare).getItems() == null || this.getItems() == null
                || ((InfoSiteSection) objectToCompare).getItems().size() != this.getItems().size()) {

            return false;
        }
        ListIterator iter1 = ((InfoSiteSection) objectToCompare).getItems().listIterator();
        ListIterator iter2 = this.getItems().listIterator();
        while (result && iter1.hasNext()) {
            InfoItem infoItem1 = (InfoItem) iter1.next();
            InfoItem infoItem2 = (InfoItem) iter2.next();
            if (!infoItem1.equals(infoItem2)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public List getItems() {
        return items;
    }

    /**
     * @return
     */
    public InfoSection getSection() {
        return section;
    }

    /**
     * @param list
     */
    public void setItems(List list) {
        items = list;
    }

    /**
     * @param section
     */
    public void setSection(InfoSection section) {
        this.section = section;
    }

}