/*
 * Created on 24/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 * 
 * 24/Jul/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteCurricularCourses extends DataTranferObject implements ISiteComponent {

    private List curricularCourses;

    /**
     * @return
     */
    public List getCurricularCourses() {
        return curricularCourses;
    }

    /**
     * @param curricularCourses
     */
    public void setCurricularCourses(List curricularCourses) {
        this.curricularCourses = curricularCourses;
        //TODO: uncomment the sort when the test data is fixed
        //Collections.sort(this.curricularCourses);
    }

    /**
     *  
     */
    public InfoSiteCurricularCourses() {
    }

}