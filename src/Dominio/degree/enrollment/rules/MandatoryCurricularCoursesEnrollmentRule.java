/*
 * Created on Jun 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MandatoryCurricularCoursesEnrollmentRule implements IEnrollmentRule {
    
    public MandatoryCurricularCoursesEnrollmentRule(IStudentCurricularPlan studentCurricularPlan)
    {}

    public List apply(List curricularCoursesToBeEnrolledIn) {
        
        ArrayList mandatoryCurricularCourses = new ArrayList();
        
        for(int iter=0; iter < curricularCoursesToBeEnrolledIn.size(); iter++)
        {
            ICurricularCourse curricularCourse = (ICurricularCourse)curricularCoursesToBeEnrolledIn.get(iter);
            if(curricularCourse.curricularCourseIsMandatory())
                mandatoryCurricularCourses.add(curricularCourse);
        }
        return mandatoryCurricularCourses;
    }

}
