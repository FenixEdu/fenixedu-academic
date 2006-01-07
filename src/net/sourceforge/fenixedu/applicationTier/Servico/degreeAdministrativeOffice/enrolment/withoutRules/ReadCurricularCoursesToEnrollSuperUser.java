/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadCurricularCoursesToEnrollSuperUser extends ReadCurricularCoursesToEnroll {

    protected List findCurricularCourses(List curricularCourses, StudentCurricularPlan studentCurricularPlan,
    		ExecutionPeriod executionPeriod) {
    	final List result = new ArrayList();
    	for (final CurricularCourse curricularCourse : (List<CurricularCourse>) curricularCourses) {
    		if (!studentCurricularPlan
    				.isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
                            (CurricularCourse) curricularCourse, executionPeriod)
                    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(
                            (CurricularCourse) curricularCourse, executionPeriod)) {
    			result.add(curricularCourse);
    		}
    	}
    	return result;
    }

}