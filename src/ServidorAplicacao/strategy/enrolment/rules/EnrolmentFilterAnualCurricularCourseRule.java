package ServidorAplicacao.strategy.enrolment.rules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import Util.CurricularCourseExecutionScope;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to remove from the possible
 * choices, ALL the curricular courses scopes that are related to an anual
 * curricular course, except the first one.
 */

public class EnrolmentFilterAnualCurricularCourseRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

       

        List anualCurricularCourses =
            (
                List) CollectionUtils
                    .select(enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled(), new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) obj;
                return curricularCourse.getCurricularCourseExecutionScope().equals(
                    new CurricularCourseExecutionScope(CurricularCourseExecutionScope.ANUAL));
            }
        });

        if (enrolmentContext.getSemester().intValue() == 2)
        {
            enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().removeAll(
                anualCurricularCourses);
        }

        return enrolmentContext;
    }
}