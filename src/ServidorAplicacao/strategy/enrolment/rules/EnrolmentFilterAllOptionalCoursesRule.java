package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to obtain ALL the curricular
 * courses of one degree, so that one of them can be chosen as an optional
 * course to enroll.
 */

public class EnrolmentFilterAllOptionalCoursesRule //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        int min_year_of_optional_courses =
            enrolmentContext
                .getStudentActiveCurricularPlan()
                .getDegreeCurricularPlan()
                .getMinimalYearForOptionalCourses()
                .intValue();

        List degreeCurricularPlanList =
            enrolmentContext.getChosenOptionalDegree().getDegreeCurricularPlans();

        List activeDegreeCurricularPlanList =
            (List) CollectionUtils.select(degreeCurricularPlanList, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
                return degreeCurricularPlan.getState().equals(
                    new DegreeCurricularPlanState(DegreeCurricularPlanState.ACTIVE));
            }
        });

        List finalCurricularCourseList = new ArrayList();
        if (activeDegreeCurricularPlanList != null && !activeDegreeCurricularPlanList.isEmpty())
        {
            List curricularCoursesFromDegreeList = new ArrayList();
            Iterator iterator = activeDegreeCurricularPlanList.iterator();
            while (iterator.hasNext())
            {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
                try
                {
                    ISuportePersistente sp = SuportePersistenteOJB.getInstance();
                    IPersistentCurricularCourse persistentCurricularCourse =
                        sp.getIPersistentCurricularCourse();
                    List curricularCoursesList =
                        persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(
                            degreeCurricularPlan);
                    curricularCoursesFromDegreeList.addAll(curricularCoursesList);
                }
                catch (ExcepcaoPersistencia e)
                {
                    e.printStackTrace(System.out);
                    throw new IllegalStateException("Cannot read from data base" + e.toString());
                }
            }

            List curricularCoursesEnroledByStudent =
                enrolmentContext.getCurricularCoursesAutomaticalyEnroled();
            List curricularCoursesFromFinalSpan =
                enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled();
            List opionalCurricularCoursesEnrolmentsList =
                (
                    List) CollectionUtils
                        .collect(
                            enrolmentContext.getOptionalCurricularCoursesEnrolments(),
                            new Transformer()
            {
                public Object transform(Object obj)
                {
                    IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse =
                        (IEnrolmentInOptionalCurricularCourse) obj;
                    return enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();
                }
            });

            Iterator iterator3 = curricularCoursesFromDegreeList.iterator();
            while (iterator3.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator3.next();
                List scopes = curricularCourse.getScopes();
                //pay attention
                if (hasScopeInSemester(scopes, enrolmentContext.getSemester())
                    && hasMinimalYearAllowed(scopes, min_year_of_optional_courses)
                    && (!finalCurricularCourseList.contains(curricularCourse))
                    && (!curricularCourse
                        .getType()
                        .equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE)))
                    && (!curricularCourse
                        .getType()
                        .equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE)))
                    && (!enrolmentContext.isCurricularCourseDone(curricularCourse))
                    && (!curricularCoursesEnroledByStudent.contains(curricularCourse))
                    && (!curricularCoursesFromFinalSpan.contains(curricularCourse))
                    && (!opionalCurricularCoursesEnrolmentsList.contains(curricularCourse)))
                {
                    finalCurricularCourseList.add(curricularCourse);
                }
            }
        }

        enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(finalCurricularCourseList);
        return enrolmentContext;
    }

    /**
	 * @param scopes
	 * @param min_year_of_optional_courses
	 * @return
	 */
    private boolean hasMinimalYearAllowed(List scopes, int min_year_of_optional_courses)
    {

        Iterator iter = scopes.iterator();
        boolean result = false;
        while (iter.hasNext() && !result)
        {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            result =
                scope.getCurricularSemester().getCurricularYear().getYear().intValue()
                    >= min_year_of_optional_courses;

        }

        return result;
    }

    /**
	 * @param scopes
	 * @param integer
	 * @return
	 */
    private boolean hasScopeInSemester(List scopes, Integer integer)
    {
        Iterator iter = scopes.iterator();
        boolean result = false;
        while (iter.hasNext() && !result)
        {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            result = scope.getCurricularSemester().getSemester().equals(integer);
        }
        return result;
    }

}