package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda Quitério
 *  
 */
public class InfoSiteTeachers extends DataTranferObject implements ISiteComponent {

    private List infoTeachers;

    private Boolean isResponsible;

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFOSITETEACHERS";
        result += " teachers=" + getInfoTeachers();
        result += ", isResponsible=" + getIsResponsible();
        result += "]";
        return result;
    }

    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteTeachers
                && (((((InfoSiteTeachers) objectToCompare).getIsResponsible() != null
                        && this.getIsResponsible() != null && ((InfoSiteTeachers) objectToCompare)
                        .getIsResponsible().equals(this.getIsResponsible())) || ((InfoSiteTeachers) objectToCompare)
                        .getIsResponsible() == null
                        && this.getIsResponsible() == null))) {
            result = true;
        }

        if (((InfoSiteTeachers) objectToCompare).getInfoTeachers() == null
                && this.getInfoTeachers() == null && result == true) {
            return true;
        }
        if (((InfoSiteTeachers) objectToCompare).getInfoTeachers() == null
                || this.getInfoTeachers() == null
                || ((InfoSiteTeachers) objectToCompare).getInfoTeachers().size() != this
                        .getInfoTeachers().size()) {

            return false;
        }
        ListIterator iter1 = ((InfoSiteTeachers) objectToCompare).getInfoTeachers().listIterator();
        ListIterator iter2 = this.getInfoTeachers().listIterator();
        while (result && iter1.hasNext()) {
            InfoTeacher infoTeacher1 = (InfoTeacher) iter1.next();
            InfoTeacher infoTeacher2 = (InfoTeacher) iter2.next();
            if (!infoTeacher1.equals(infoTeacher2)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public List getInfoTeachers() {
        return infoTeachers;
    }

    /**
     * @param list
     */
    public void setInfoTeachers(List list) {
        infoTeachers = list;
    }

    /**
     * @return
     */
    public Boolean getIsResponsible() {
        return isResponsible;
    }

    /**
     * @param boolean1
     */
    public void setIsResponsible(Boolean boolean1) {
        isResponsible = boolean1;
    }

}