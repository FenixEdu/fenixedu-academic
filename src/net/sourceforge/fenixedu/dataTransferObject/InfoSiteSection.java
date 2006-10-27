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

    public InfoSiteSection() {}
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOSITESECTION";
        result += " section=" + getSection();
        result += ", items=" + getItems();
        result += "]";
        return result;
    }

    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteSection
                && (((((InfoSiteSection) objectToCompare).getSection() != null
                        && this.getSection() != null && ((InfoSiteSection) objectToCompare).getSection()
                        .equals(this.getSection())) || ((InfoSiteSection) objectToCompare).getSection() == null
                        && this.getSection() == null))) {
            result = true;
        }

        if (((InfoSiteSection) objectToCompare).getItems() == null && this.getItems() == null
                && result == true) {
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