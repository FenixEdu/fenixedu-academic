package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 */
public class EnrolmentFilterSemesterRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        List curricularCoursesFromActualExecutionPeriod = new ArrayList();

        Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
        while (iterator.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if (hasSemester(curricularCourse.getScopes(), enrolmentContext.getSemester()))
            {
                curricularCoursesFromActualExecutionPeriod.add(curricularCourse);
            }
        }

        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(
            curricularCoursesFromActualExecutionPeriod);
        return enrolmentContext;
    }

    /**
	 * @param scopes
	 * @param integer
	 * @return
	 */
    private boolean hasSemester(List scopes, Integer integer)
    {
        boolean result = false;
        Iterator iter = scopes.iterator();
        while (iter.hasNext() && !result)
        {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            result = scope.getCurricularSemester().getSemester().equals(integer);
        }
        return result;
    }
}
