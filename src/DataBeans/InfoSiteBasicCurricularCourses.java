/*
 * Created on 24/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author João Mota
 * 
 * 24/Jul/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteBasicCurricularCourses extends DataTranferObject implements ISiteComponent {

    private List basicCurricularCourses;

    private List nonBasicCurricularCourses;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    /**
     * @return
     */
    public List getNonBasicCurricularCourses() {
        return nonBasicCurricularCourses;
    }

    public List getBasicCurricularCoursesIds() {
        Iterator iter = basicCurricularCourses.iterator();
        List result = new ArrayList();
        while (iter.hasNext()) {
            result.add(((InfoCurricularCourse) iter.next()).getIdInternal());
        }
        return result;
    }

    /**
     * @param nonBasicCurricularCourses
     */
    public void setNonBasicCurricularCourses(List nonBasicCurricularCourses) {
        this.nonBasicCurricularCourses = nonBasicCurricularCourses;
    }

    /**
     * @return
     */
    public List getBasicCurricularCourses() {
        return basicCurricularCourses;
    }

    /**
     * @param curricularCourses
     */
    public void setBasicCurricularCourses(List curricularCourses) {
        this.basicCurricularCourses = curricularCourses;
        //TODO: uncomment the sort when the test data is fixed
        //Collections.sort(this.curricularCourses);
    }

    /**
     *  
     */
    public InfoSiteBasicCurricularCourses() {
    }

    /**
     * @return
     */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    /**
     * @param plan
     */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan) {
        infoDegreeCurricularPlan = plan;
    }

}