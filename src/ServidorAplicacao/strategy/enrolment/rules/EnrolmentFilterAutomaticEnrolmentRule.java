package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to remove from the list of
 * curricular courses to be chosen for enrollment, the ANUAL and MANDATORY
 * curricular courses and put them in the list of curricular courses in which
 * the student will be automatically enrolled.
 */

public class EnrolmentFilterAutomaticEnrolmentRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        //	Anual curricular course filter

        Iterator iteratorAnualCurricularCourses =
            enrolmentContext.getCurricularCoursesAutomaticalyEnroled().iterator();
        List anualCurricularCourses = new ArrayList();
        while (iteratorAnualCurricularCourses.hasNext())
        {
            ICurricularCourse curricularCourse =
                (ICurricularCourse) iteratorAnualCurricularCourses.next();
            List scopes = curricularCourse.getScopes();
            if (hasBranch(scopes, enrolmentContext.getStudentActiveCurricularPlan().getBranch())
                || hasNullBranch(scopes))
            {
                if (hasSemester(scopes, enrolmentContext.getSemester()))
                {
                    anualCurricularCourses.add(curricularCourse);
                }
            }
        }
        enrolmentContext.getCurricularCoursesAutomaticalyEnroled().clear();
        enrolmentContext.getCurricularCoursesAutomaticalyEnroled().addAll(anualCurricularCourses);

        // ----------------------------------------------------------------------------
        // //

        //	Mandatory curricular course filter
        final Integer semester = enrolmentContext.getSemester();
        List alternativeSemesterCourses =
            (
                List) CollectionUtils
                    .select(enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled(), new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) obj;
                return (
                    (curricularCourse.curricularCourseIsMandatory())
                        && (hasSemester(curricularCourse.getScopes(), semester)));
            }
        });

        enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().removeAll(
            alternativeSemesterCourses);

        Iterator iteratorAlternative = alternativeSemesterCourses.iterator();
        while (iteratorAlternative.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iteratorAlternative.next();
            if ((hasBranch(curricularCourse.getScopes(),
                enrolmentContext.getStudentActiveCurricularPlan().getBranch()))
                || hasNullBranch(curricularCourse.getScopes()))
            {
                enrolmentContext.getCurricularCoursesAutomaticalyEnroled().add(curricularCourse);
            }
        }

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

    /**
	 * @param scopes
	 * @return
	 */
    private boolean hasNullBranch(List scopes)
    {

        boolean result = false;
        Iterator iter = scopes.iterator();
        while (iter.hasNext() && !result)
        {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            result = scope.getBranch().getName().equals("");
        }
        return result;
    }

    /**
	 * @param scopes
	 * @param branch
	 * @return
	 */
    private boolean hasBranch(List scopes, IBranch branch)
    {

        boolean result = false;
        Iterator iter = scopes.iterator();
        while (iter.hasNext() && !result)
        {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            result = scope.getBranch().getName().equals(branch);
        }
        return result;
    }
}