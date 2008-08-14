package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class CreateOptionalEnrolment extends Service {

    public void run(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
	    CurriculumGroup curriculumGroup, Context context, CurricularCourse curricularCourse,
	    EnrollmentCondition enrollmentCondition) throws FenixServiceException {
	studentCurricularPlan.createOptionalEnrolment(curriculumGroup, executionSemester, (OptionalCurricularCourse) context
		.getChildDegreeModule(), curricularCourse, enrollmentCondition);
    }

}
