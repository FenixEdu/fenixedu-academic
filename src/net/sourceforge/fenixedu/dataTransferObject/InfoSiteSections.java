package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda Quitério
 *  
 */
public class InfoSiteSections extends DataTranferObject implements ISiteComponent {

    private InfoSection section;

    private List sections;

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOSITESECTIONS";
        result += " section=" + getSection();
        result += ", sections=" + getSections();
        result += "]";
        return result;
    }

    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteSections
                && (((((InfoSiteSections) objectToCompare).getSection() != null
                        && this.getSection() != null && ((InfoSiteSections) objectToCompare)
                        .getSection().equals(this.getSection())) || ((InfoSiteSections) objectToCompare)
                        .getSection() == null
                        && this.getSection() == null))) {
            result = true;
        }

        if (((InfoSiteSections) objectToCompare).getSections() == null && this.getSections() == null
                && result == true) {
            return true;
        }
        if (((InfoSiteSections) objectToCompare).getSections() == null
                || this.getSections() == null
                || ((InfoSiteSections) objectToCompare).getSections().size() != this.getSections()
                        .size()) {

            return false;
        }
        ListIterator iter1 = ((InfoSiteSections) objectToCompare).getSections().listIterator();
        ListIterator iter2 = this.getSections().listIterator();
        while (result && iter1.hasNext()) {
            InfoSection infoSection1 = (InfoSection) iter1.next();
            InfoSection infoSection2 = (InfoSection) iter2.next();
            if (!infoSection1.equals(infoSection2)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public List getSections() {
        return sections;
    }

    /**
     * @param list
     */
    public void setSections(List list) {
        sections = list;
    }

    /**
     * @return
     */
    public InfoSection getSection() {
        return section;
    }

    /**
     * @param section
     */
    public void setSection(InfoSection section) {
        this.section = section;
    }

}