package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos in Jun 22, 2004
 */

public class MaximumNumberOfAcumulatedEnrollmentsRule implements IEnrollmentRule
{
	private IStudentCurricularPlan studentCurricularPlan;
	private IExecutionPeriod executionPeriod;

	public MaximumNumberOfAcumulatedEnrollmentsRule(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		this.studentCurricularPlan = studentCurricularPlan;
		this.executionPeriod = executionPeriod;
	}

	public List apply(List curricularCoursesToBeEnrolledIn) throws ExcepcaoPersistencia
	{
		List curricularCoursesToRemove = new ArrayList();
		List allStudentEnrolledEnrollments = this.studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(this.executionPeriod);

		int totalNAC = 0;
		int size = allStudentEnrolledEnrollments.size();

		for(int i = 0; i < size; i++)
        {
            IEnrollment enrollment = (IEnrollment) allStudentEnrolledEnrollments.get(i);
            totalNAC += enrollment.getAccumulatedWeight().intValue();
        }
		
		int maxNAC = this.studentCurricularPlan.getMaximumNumberOfAcumulatedEnrollments().intValue();
		
		if (totalNAC >= maxNAC)
		{
			return new ArrayList();
		}

		int availableNACToEnroll = maxNAC - totalNAC;

		size = curricularCoursesToBeEnrolledIn.size();
		for(int i = 0; i < size; i++)
        {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesToBeEnrolledIn.get(i);
            int ac = this.studentCurricularPlan.getCurricularCourseAcumulatedEnrollments(
                    curricularCourse2Enroll.getCurricularCourse()).intValue();
            if (ac > availableNACToEnroll)
            {
            	curricularCoursesToRemove.add(curricularCourse2Enroll);
            }
        }

   		curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToRemove);

		return curricularCoursesToBeEnrolledIn;
	}
}