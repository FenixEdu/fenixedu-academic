package ServidorAplicacao.strategy.enrolment.strategys;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos in Jan 16, 2004
 */

public abstract class EnrolmentStrategy implements IEnrolmentStrategy
{

    protected StudentEnrolmentContext studentEnrolmentContext = null;

    /**
     * @return Returns the studentEnrolmentContext.
     */
    public StudentEnrolmentContext getStudentEnrolmentContext()
    {
    	return studentEnrolmentContext;
    }

    /**
     * @param studentEnrolmentContext The studentEnrolmentContext to set.
     */
    public void setStudentEnrolmentContext(StudentEnrolmentContext studentEnrolmentContext)
    {
    	this.studentEnrolmentContext = studentEnrolmentContext;
    }

    public abstract StudentEnrolmentContext getAvailableCurricularCourses() throws ExcepcaoPersistencia;

    public abstract StudentEnrolmentContext validateEnrolment();

    public abstract StudentEnrolmentContext getOptionalCurricularCourses();

    public abstract StudentEnrolmentContext getDegreesForOptionalCurricularCourses();

//    protected EnrolmentContext filterByExecutionCourses(EnrolmentContext enrolmentContext)
//    {
//
//        final IExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
//
//        List curricularCoursesToRemove = new ArrayList();
//        // Não se pode filtar as opções porque elas não têm
//        // EXECUTION_COURSE associados
//        Iterator spanIterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
//        while (spanIterator.hasNext())
//        {
//            ICurricularCourse curricularCourse = (ICurricularCourse) spanIterator.next();
//            List executionCourseList = curricularCourse.getAssociatedExecutionCourses();
//
//            final List executionCourseInExecutionPeriod =
//                (List) CollectionUtils.select(executionCourseList, new Predicate()
//            {
//                public boolean evaluate(Object obj)
//                {
//                    IExecutionCourse executionCourse = (IExecutionCourse) obj;
//                    return executionCourse.getExecutionPeriod().equals(executionPeriod);
//                }
//            });
//
//            if (executionCourseInExecutionPeriod.isEmpty())
//            {
//                curricularCoursesToRemove.add(curricularCourse);
//            }
//            else
//            {
//                executionCourseInExecutionPeriod.clear();
//            }
//        }
//
//        enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().removeAll(
//            curricularCoursesToRemove);
//
//        return enrolmentContext;
//    }
//
//    protected EnrolmentContext filterBySemester(EnrolmentContext enrolmentContext)
//    {
//        List curricularCoursesFromActualExecutionPeriod = new ArrayList();
//
//        Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
//        while (iterator.hasNext())
//        {
//            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
//            if (hasSemester(curricularCourse.getScopes(), enrolmentContext.getSemester()))
//            {
//                curricularCoursesFromActualExecutionPeriod.add(curricularCourse);
//            }
//        }
//
//        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(
//            curricularCoursesFromActualExecutionPeriod);
//        return enrolmentContext;
//    }
//
//    protected EnrolmentContext filterScopesOfCurricularCoursesToBeChosenForOptionalCurricularCourses(EnrolmentContext enrolmentContext)
//    {
//
//        try
//        {
//            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//            IPersistentPossibleCurricularCourseForOptionalCurricularCourse persistentPossibleCurricularCourseForOptionalCurricularCourse =
//                sp.getIPersistentChosenCurricularCourseForOptionalCurricularCourse();
//
//            List possibleCurricularCoursesForOptionalCurricularCourseList =
//                persistentPossibleCurricularCourseForOptionalCurricularCourse
//                    .readAllByDegreeCurricularPlan(
//                    enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan());
//            List possibleCurricularCourses =
//                (
//                    List) CollectionUtils
//                        .collect(
//                            possibleCurricularCoursesForOptionalCurricularCourseList,
//                            new Transformer()
//            {
//                public Object transform(Object obj)
//                {
//                    IPossibleCurricularCourseForOptionalCurricularCourse possibleCurricularCourseForOptionalCurricularCourse =
//                        (IPossibleCurricularCourseForOptionalCurricularCourse) obj;
//                    return possibleCurricularCourseForOptionalCurricularCourse
//                        .getPossibleCurricularCourse();
//                }
//            });
//
//            List curricularCoursesToRemove = new ArrayList();
//            Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
//            while (iterator.hasNext())
//            {
//                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
//                if (possibleCurricularCourses != null)
//                {
//                    if (possibleCurricularCourses.contains(curricularCourse))
//                    {
//                        curricularCoursesToRemove.add(curricularCourse);
//                    }
//                }
//            }
//
//            List finalSpan =
//                (List) CollectionUtils.subtract(
//                    enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled(),
//                    curricularCoursesToRemove);
//            enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(finalSpan);
//        }
//        catch (ExcepcaoPersistencia e)
//        {
//        }
//
//        return enrolmentContext;
//    }
//
//    /**
//	 * @param scopes
//	 * @param integer
//	 * @return
//	 */
//    public boolean hasSemester(List scopes, Integer integer)
//    {
//        boolean result = false;
//        Iterator iter = scopes.iterator();
//        while (iter.hasNext() && !result)
//        {
//            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
//            result = scope.getCurricularSemester().getSemester().equals(integer);
//        }
//        return result;
//    }

}