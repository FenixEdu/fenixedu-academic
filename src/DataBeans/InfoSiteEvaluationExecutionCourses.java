/*
 * Created on Nov 24, 2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class InfoSiteEvaluationExecutionCourses extends DataTranferObject implements ISiteComponent {
    protected List infoExecutionCourses;

    /**
     * @return Returns the infoExecutionCourses.
     */
    public List getInfoExecutionCourses() {
        return infoExecutionCourses;
    }

    /**
     * @param infoExecutionCourses
     *            The infoExecutionCourses to set.
     */
    public void setInfoExecutionCourses(List infoExecutionCourses) {
        this.infoExecutionCourses = infoExecutionCourses;
    }

}