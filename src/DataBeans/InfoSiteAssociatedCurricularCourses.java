/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteAssociatedCurricularCourses extends DataTranferObject implements ISiteComponent {

    private List associatedCurricularCourses;

    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteAssociatedCurricularCourses) {
            result = true;
        }

        if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
                && this.getAssociatedCurricularCourses() == null) {
            return true;
        }
        if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
                || this.getAssociatedCurricularCourses() == null
                || ((InfoSiteAssociatedCurricularCourses) objectToCompare)
                        .getAssociatedCurricularCourses().size() != this
                        .getAssociatedCurricularCourses().size()) {
            return false;
        }
        ListIterator iter1 = ((InfoSiteAssociatedCurricularCourses) objectToCompare)
                .getAssociatedCurricularCourses().listIterator();
        ListIterator iter2 = this.getAssociatedCurricularCourses().listIterator();
        while (result && iter1.hasNext()) {
            InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) iter1.next();
            InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) iter2.next();
            if (!infoCurricularCourse1.equals(infoCurricularCourse2)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getAssociatedCurricularCourses() {
        return associatedCurricularCourses;
    }

    /**
     * @param list
     */
    public void setAssociatedCurricularCourses(List list) {
        associatedCurricularCourses = list;
    }

}