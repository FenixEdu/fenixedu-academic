package ServidorAplicacao.strategy.enrolment.rules.depercated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 * 
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to check, if there is a
 * curricular course from a previous year that the student cannot be enrolled
 * in, and remove all the curricular courses from that year beyond.
 */

public class EnrolmentFilterCurricularYearPrecedence //implements IEnrolmentRule
{

    public EnrolmentContext apply(EnrolmentContext enrolmentContext)
    {

        List curricularCoursesNeverEnroled = null;
        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
            final List studentEnrolments =
                persistentEnrolment.readAllByStudentCurricularPlan(
                    enrolmentContext.getStudentActiveCurricularPlan());
            final List studentEnrolmentsWithStateDiferentOfTemporarilyEnroled =
                (List) CollectionUtils.select(studentEnrolments, new Predicate()
            {
                public boolean evaluate(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return !enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED);
                }
            });
            List correspondingCurricularCourses =
                (
                    List) CollectionUtils
                        .collect(
                            studentEnrolmentsWithStateDiferentOfTemporarilyEnroled,
                            new Transformer()
            {
                public Object transform(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return enrolment.getCurricularCourse();
                }
            });
            curricularCoursesNeverEnroled = new ArrayList();
            curricularCoursesNeverEnroled.addAll(
                enrolmentContext.getCurricularCoursesFromStudentCurricularPlan());
            curricularCoursesNeverEnroled.removeAll(correspondingCurricularCourses);
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw new IllegalStateException("Cannot read from database");
        }

       

        // ------------------------------------------------------------
        // NOTE [DAVID]: Este pedaço de código é uma maneira de filtrar pelo
        // ramo e semestre de modo a tornar esta regra independente.
        List finalSpanBackup = new ArrayList();
        finalSpanBackup.addAll(enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled());
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(curricularCoursesNeverEnroled);

//        IEnrolmentRule enrolmentRule = null;
//        enrolmentRule = new EnrolmentFilterBranchRule();
//        enrolmentContext = enrolmentRule.apply(enrolmentContext);
//        enrolmentRule = new EnrolmentFilterSemesterRule();
//        enrolmentContext = enrolmentRule.apply(enrolmentContext);

        curricularCoursesNeverEnroled = new ArrayList();
        curricularCoursesNeverEnroled.addAll(
            enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled());
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(finalSpanBackup);
        // ------------------------------------------------------------

        List aux = new ArrayList();
        Iterator iterator1 = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
        while (iterator1.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
            ICurricularCourseScope leastScope = getMinimumScope(curricularCourse);
            Iterator iterator2 = curricularCoursesNeverEnroled.iterator();
            while (iterator2.hasNext())
            {
                ICurricularCourse curricularCourse2 = (ICurricularCourse) iterator2.next();
                ICurricularCourseScope lestScope2Compare = getMinimumScope(curricularCourse2);
                if (lestScope2Compare.getCurricularSemester().getCurricularYear().getYear().intValue()
                    < leastScope.getCurricularSemester().getCurricularYear().getYear().intValue()
                    && (!enrolmentContext
                        .getFinalCurricularCoursesSpanToBeEnrolled()
                        .contains(curricularCourse2))
                    && (!enrolmentContext
                        .getCurricularCoursesAutomaticalyEnroled()
                        .contains(curricularCourse2)))
                {
                    aux.add(curricularCourse);
                    break;
                }

            }
        }
        enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().removeAll(aux);
        return enrolmentContext;
    }

    private ICurricularCourseScope getMinimumScope(ICurricularCourse curricularCourse)
    {
        List scopes = curricularCourse.getScopes();
        Iterator iter = scopes.iterator();
        ICurricularCourseScope scope = null;
        while (iter.hasNext())
        {
            ICurricularCourseScope scope2Compare = (ICurricularCourseScope) iter.next();
            if (scope == null
                || (scope.getCurricularSemester().getCurricularYear().getYear().intValue()
                    > scope2Compare.getCurricularSemester().getCurricularYear().getYear().intValue()))
            {
                scope = scope2Compare;
            }
        }
        return scope;
    }
}