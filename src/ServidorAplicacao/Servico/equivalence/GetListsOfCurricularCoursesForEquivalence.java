package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos 9/Jul/2003
 */

public class GetListsOfCurricularCoursesForEquivalence implements IService
{

    public GetListsOfCurricularCoursesForEquivalence()
    {
    }

    public InfoEquivalenceContext run(
        InfoStudent infoStudent,
        IUserView responsible,
        InfoExecutionPeriod infoExecutionPeriod)
        throws FenixServiceException
    {

        InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

        List studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences =
            new ArrayList();
        //  List curricularCoursesFromActiveDegreeCurricularPlanForStudent = new
        // ArrayList();

        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
                persistentSupport.getIStudentCurricularPlanPersistente();
            IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

            IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);

            List studentCurricularPlansList =
                persistentStudentCurricularPlan.readAllFromStudent(student.getNumber().intValue());

            Iterator iterator = studentCurricularPlansList.iterator();
            while (iterator.hasNext())
            {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

                if (!studentCurricularPlan
                    .getCurrentState()
                    .equals(StudentCurricularPlanState.ACTIVE_OBJ))
                {
                    List studentEnrolments =
                        persistentEnrolment.readAllByStudentCurricularPlan(studentCurricularPlan);
                    //					printEnrolments(studentEnrolments);

                    List studentAprovedEnrolments =
                        (List) CollectionUtils.select(studentEnrolments, new Predicate()
                    {
                        public boolean evaluate(Object obj)
                        {
                            IEnrolment enrolment = (IEnrolment) obj;
                            return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
                        }
                    });
                    studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences
                        .addAll(
                        GetListsOfCurricularCoursesForEquivalence.getEnrolmentsWithNoEquivalences(
                            studentAprovedEnrolments,
                            persistentSupport));
                }
            }

            IStudentCurricularPlan studentActiveCurricularPlan =
                persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
                    student.getNumber(),
                    student.getDegreeType());
            final IDegreeCurricularPlan activeDegreeCurricularPlanForStudent =
                studentActiveCurricularPlan.getDegreeCurricularPlan();
            List curricularCoursesFromActiveDegreeCurricularPlanForStudent =
                activeDegreeCurricularPlanForStudent.getCurricularCourses();

            List studentEnrolments =
                persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

            List studentAprovedEnrolments =
                (List) CollectionUtils.select(studentEnrolments, new Predicate()
            {
                public boolean evaluate(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
                }
            });

            List curricularCoursesFromStudentAprovedEnrolments =
                (List) CollectionUtils.collect(studentAprovedEnrolments, new Transformer()
            {
                public Object transform(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return enrolment.getCurricularCourse();
                }
            });

            List studentEnroledEnrolments =
                (List) CollectionUtils.select(studentEnrolments, new Predicate()
            {
                public boolean evaluate(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
                        || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED);
                }
            });

            List curricularCoursesFromStudentEnroledEnrolments =
                (List) CollectionUtils.collect(studentEnroledEnrolments, new Transformer()
            {
                public Object transform(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return enrolment.getCurricularCourse();
                }
            });

            Iterator iterator1 = curricularCoursesFromActiveDegreeCurricularPlanForStudent.iterator();
            List aux1 = new ArrayList();
            while (iterator1.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
                if ((hasBranch(curricularCourse.getScopes(), studentActiveCurricularPlan.getBranch())
                    || hasNullBranch(curricularCourse.getScopes())
                    || (studentActiveCurricularPlan.getBranch().getName().equals("")
                        && studentActiveCurricularPlan.getBranch().getCode().equals("")))
                    && !curricularCoursesFromStudentAprovedEnrolments.contains(curricularCourse)
                    && !curricularCoursesFromStudentEnroledEnrolments.contains(curricularCourse))
                {
                    aux1.add(curricularCourse);
                }

            }

            List aux2 =
                (List) CollectionUtils.union(
                    curricularCoursesFromStudentAprovedEnrolments,
                    curricularCoursesFromStudentEnroledEnrolments);
            curricularCoursesFromActiveDegreeCurricularPlanForStudent.addAll(this.subtract(aux1, aux2));

            if (studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences != null
                && curricularCoursesFromActiveDegreeCurricularPlanForStudent != null)
            {
                infoEquivalenceContext.setInfoEnrolmentsToGiveEquivalence(
                    this.cloneEnrolmentsToInfoEnrolments(
                        studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences));
                infoEquivalenceContext.setInfoCurricularCoursesToGetEquivalence(
                    this.cloneCurricularCoursesToInfoCurricularCourses(
                        curricularCoursesFromActiveDegreeCurricularPlanForStudent));
            }
            else
            {
                infoEquivalenceContext.setInfoEnrolmentsToGiveEquivalence(new ArrayList());
                infoEquivalenceContext.setInfoCurricularCoursesToGetEquivalence(new ArrayList());
            }

            infoEquivalenceContext.setCurrentInfoExecutionPeriod(infoExecutionPeriod);
            infoEquivalenceContext.setInfoStudentCurricularPlan(
                Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(
                    studentActiveCurricularPlan));
            infoEquivalenceContext.setResponsible(responsible);

            return infoEquivalenceContext;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    private List cloneCurricularCoursesToInfoCurricularCourses(List curricularCourses)
    {
        List infoCurricularCourses = new ArrayList();
        Iterator iterator = curricularCourses.iterator();
        while (iterator.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    private List cloneEnrolmentsToInfoEnrolments(List enrolments)
    {
        List infoEnrolments = new ArrayList();
        Iterator iterator = enrolments.iterator();
        while (iterator.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iterator.next();
            InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
            infoEnrolments.add(infoEnrolment);
        }
        return infoEnrolments;
    }

    public static List getEnrolmentsWithNoEquivalences(
        List enrolments,
        ISuportePersistente persistentSupport)
        throws ExcepcaoPersistencia
    {
        List enrolmentsWithNoEquivalences = new ArrayList();
        Iterator iterator = enrolments.iterator();
        while (iterator.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iterator.next();
            IPersistentEquivalentEnrolmentForEnrolmentEquivalence persistentEnrolmentEquivalenceRestriction =
                persistentSupport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();
            List result = persistentEnrolmentEquivalenceRestriction.readByEquivalentEnrolment(enrolment);
            if (result.isEmpty())
            {
                enrolmentsWithNoEquivalences.add(enrolment);
            }
        }
        return enrolmentsWithNoEquivalences;
    }

    private List subtract(List finalCurricularCoursesList, final List curricularCoursesToRemove)
    {

        List finalList = (List) CollectionUtils.select(finalCurricularCoursesList, new Predicate()
        {
            public boolean evaluate(Object arg0)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                return !curricularCoursesToRemove.contains(curricularCourse);
            }
        });

        return finalList;
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