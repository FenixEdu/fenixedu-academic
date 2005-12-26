/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadCurricularCoursesToEnrollSuperUser extends ReadCurricularCoursesToEnroll {

    protected List findCurricularCourses(List curricularCourses, IStudentCurricularPlan studentCurricularPlan,
    		IExecutionPeriod executionPeriod) {
    	final List result = new ArrayList();
    	for (final ICurricularCourse curricularCourse : (List<ICurricularCourse>) curricularCourses) {
    		if (!studentCurricularPlan
    				.isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
                            (ICurricularCourse) curricularCourse, executionPeriod)
                    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(
                            (ICurricularCourse) curricularCourse, executionPeriod)) {
    			result.add(curricularCourse);
    		}
    	}
    	return result;
    }

}