/**
 * Jul 27, 2005
 */
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan extends
        InfoStudentCurricularPlanWithInfoDegreeCurricularPlan {

    private List infoNotNeedToEnrollCurricularCourses;
    
    public InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan() {
    }

    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            List infoNotNeedToEnrollInCurricularCourses = new ArrayList();
            for (Iterator iter = studentCurricularPlan.getNotNeedToEnrollCurricularCourses().iterator(); iter
                    .hasNext();) {
                INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (INotNeedToEnrollInCurricularCourse) iter
                        .next();
                infoNotNeedToEnrollInCurricularCourses.add(InfoNotNeedToEnrollInCurricularCourse
                        .newInfoFromDomain(notNeedToEnrollInCurricularCourse));
            }
            setInfoNotNeedToEnrollCurricularCourses(infoNotNeedToEnrollInCurricularCourses);
        }
    }

    public static InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }

    public List getInfoNotNeedToEnrollCurricularCourses() {
        return infoNotNeedToEnrollCurricularCourses;
    }

    public void setInfoNotNeedToEnrollCurricularCourses(List infoNotNeedToEnrollCurricularCourses) {
        this.infoNotNeedToEnrollCurricularCourses = infoNotNeedToEnrollCurricularCourses;
    }
}
