/*
 * Created on 10/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LEMOptionalGroupsEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    public LEMOptionalGroupsEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.degree.enrollment.rules.IEnrollmentRule#apply(java.util.List)
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {
    	List<CurricularCourseGroup> optionalCurricularCourseGroups = studentCurricularPlan.getBranch().getOptionalCurricularCourseGroups();
    	
    	CurricularCourseGroup optionalCurricularCourseGroup = (CurricularCourseGroup) CollectionUtils
    	.find(optionalCurricularCourseGroups, new Predicate() {
    		public boolean evaluate(Object arg0) {
    			CurricularCourseGroup ccg = (CurricularCourseGroup) arg0;
    			return ccg.getName().equalsIgnoreCase("Opções 4ºAno 2ºSem");
    		}
    	});
    	
    	CurricularCourse firstOptionalCurricularCourse = optionalCurricularCourseGroup
    	.getCurricularCourses().get(0);
    	CurricularCourse secondOptionalCurricularCourse = optionalCurricularCourseGroup
    	.getCurricularCourses().get(1);
    	
    	if (studentCurricularPlan.isCurricularCourseEnrolled(firstOptionalCurricularCourse)
    			|| studentCurricularPlan.isCurricularCourseApproved(firstOptionalCurricularCourse)) {
    		curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
    				secondOptionalCurricularCourse, executionSemester));
    	}
    	
    	else if (studentCurricularPlan.isCurricularCourseEnrolled(secondOptionalCurricularCourse)
    			|| studentCurricularPlan.isCurricularCourseApproved(secondOptionalCurricularCourse)) {
    		curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
    				firstOptionalCurricularCourse, executionSemester));
    	}

        return curricularCoursesToBeEnrolledIn;
    }

    protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(
            CurricularCourse curricularCourse, ExecutionSemester currentExecutionPeriod) {
        return new CurricularCourse2Enroll(curricularCourse, studentCurricularPlan
                .getCurricularCourseEnrollmentType(curricularCourse, currentExecutionPeriod),
                new Boolean(true));
    }

}
