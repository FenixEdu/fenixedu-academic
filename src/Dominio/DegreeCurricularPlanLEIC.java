package Dominio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEIC extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEIC() {
        ojbConcreteClass = getClass().getName();
    }

    public List getSpecialListOfCurricularCourses() {

        List allDegreeCurricularPlans = this.getDegree().getDegreeCurricularPlans();

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) CollectionUtils.find(
                allDegreeCurricularPlans, new Predicate() {
                    public boolean evaluate(Object obj) {
                        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
                        return degreeCurricularPlan.getName().equals("LEIC - Currículo Antigo");
                    }
                });

        List curricularCourses = new ArrayList();

        int curricularCoursesSize = degreeCurricularPlan.getCurricularCourses().size();
        for (int i = 0; i < curricularCoursesSize; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) degreeCurricularPlan.getCurricularCourses().get(i);
            
            int curricularCourseScopesSize = curricularCourse.getScopes().size();
            for (int j = 0; j < curricularCourseScopesSize; j++) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(j);
                if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() == 5) {
                    curricularCourses.add(curricularCourse);
                }
            }
        }
        
        return curricularCourses;
    }
}