package ServidorAplicacao.strategy.enrolment.rules.depercated;

import Dominio.ICurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 */

public class EnrolmentFilterTFCRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        int size1 = enrolmentContext.getEnrolmentsAprovedByStudent().size();
        int size2 = enrolmentContext.getCurricularCoursesFromStudentCurricularPlan().size();

        if ((size2 - size1) > 14)
        {
            for (int i = 0;
                i < enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().size();
                i++)
            {
                ICurricularCourse curricularCourse =
                    (ICurricularCourse) enrolmentContext
                        .getFinalCurricularCoursesSpanToBeEnrolled()
                        .get(
                        i);
                if (curricularCourse
                    .getType()
                    .equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE)))
                {
                    enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().remove(
                        curricularCourse);
                }
            }
        }

        return enrolmentContext;
    }
}