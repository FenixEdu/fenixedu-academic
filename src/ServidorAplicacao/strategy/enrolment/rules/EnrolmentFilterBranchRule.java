package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 */
public class EnrolmentFilterBranchRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        List curricularCoursesFromBranch = new ArrayList();

        IBranch studentBranch = enrolmentContext.getStudentActiveCurricularPlan().getBranch();

        Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
        while (iterator.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if (hasBranch(curricularCourse.getScopes(), studentBranch)
                || hasNullBranch(curricularCourse.getScopes()))
            {
                curricularCoursesFromBranch.add(curricularCourse);
            }
        }

        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(curricularCoursesFromBranch);
        return enrolmentContext;
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
}