package ServidorAplicacao.strategy.enrolment.rules;

import java.util.Iterator;

import Dominio.ICurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to remove the training
 * curricular course in the LEQ degree from the list of curricular courses to
 * choose from.
 */

public class EnrolmentFilterLEQTrainingCourseRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {
        ICurricularCourse curricularCourseToRemove = null;
        Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
        while (iterator.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if (curricularCourse.getType().equals(CurricularCourseType.TRAINING_COURSE_OBJ))
            {
                curricularCourseToRemove = curricularCourse;
                break;
            }
        }
        if (curricularCourseToRemove != null)
        {
            enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().remove(
                curricularCourseToRemove);
        }
        return enrolmentContext;
    }
}