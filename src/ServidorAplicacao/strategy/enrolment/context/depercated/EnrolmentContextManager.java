/*
 * Created on 10/Abr/2003 by jpvl
 *  
 */
package ServidorAplicacao.strategy.enrolment.context.depercated;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;

/**
 * @author jpvl
 */
public abstract class EnrolmentContextManager
{

    public static EnrolmentContext initialEnrolmentContext(IStudent student)
        throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod
    {

        EnrolmentContext enrolmentContext = new EnrolmentContext();

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

        IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
            persistentSupport.getIStudentCurricularPlanPersistente();
        IPersistentEnrolmentPeriod enrolmentPeriodDAO =
            persistentSupport.getIPersistentEnrolmentPeriod();
        IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

        final IStudentCurricularPlan studentActiveCurricularPlan =
            persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
                student.getNumber(),
                student.getDegreeType());

        IEnrolmentPeriod enrolmentPeriod =
            getEnrolmentPeriod(enrolmentPeriodDAO, studentActiveCurricularPlan);
        List degreeCurricularPlanCurricularCourses =
            studentActiveCurricularPlan.getDegreeCurricularPlan().getCurricularCourses();

        final List studentEnrolments =
            persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

        final List studentEnrolmentsWithStateApproved =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
            }
        });

        final List studentDoneCurricularCourses =
            (List) CollectionUtils.collect(studentEnrolmentsWithStateApproved, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getCurricularCourse();
            }
        });

        final List studentEnrolmentsWithStateEnroled =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED);
            }
        });

        final List studentEnroledCurricularCourses =
            (List) CollectionUtils.collect(studentEnrolmentsWithStateEnroled, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getCurricularCourse();
            }
        });

        List studentEnroledAndDoneCurricularCourses = new ArrayList();
        studentEnroledAndDoneCurricularCourses.addAll(studentDoneCurricularCourses);
        studentEnroledAndDoneCurricularCourses.addAll(studentEnroledCurricularCourses);

      

        List enrolmentsWithStateNotApproved =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
                return !studentDoneCurricularCourses.contains(curricularCourse)
                    && enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED);
            }
        });

        List curricularCoursesEnrolled =
            (List) CollectionUtils.collect(enrolmentsWithStateNotApproved, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return (
                    enrolment.getCurricularCourse().getCode()
                        + enrolment.getCurricularCourse().getName());
            }
        });

        List studentCurricularPlanCurricularCourses =
            computeStudentCurricularPlanCurricularCourses(
                degreeCurricularPlanCurricularCourses,
                studentActiveCurricularPlan);

        IExecutionPeriod enrolmentExecutionPeriod = enrolmentPeriod.getExecutionPeriod();

        enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(
            studentCurricularPlanCurricularCourses);
        enrolmentContext.setStudent(student);
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(
            computeCurricularCoursesNotYetDoneByStudent(
                degreeCurricularPlanCurricularCourses,
                studentEnroledAndDoneCurricularCourses));
        enrolmentContext.setEnrolmentsAprovedByStudent(studentEnrolmentsWithStateApproved);
        enrolmentContext.setAcumulatedEnrolments(
            CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
        enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
        enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
        enrolmentContext.setCurricularCoursesAutomaticalyEnroled(studentEnroledCurricularCourses);
        enrolmentContext.setExecutionPeriod(enrolmentExecutionPeriod);

        enrolmentContext.setActualEnrolments(new ArrayList());
        enrolmentContext.setDegreesForOptionalCurricularCourses(new ArrayList());
        enrolmentContext.setOptionalCurricularCoursesEnrolments(new ArrayList());
        enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(new ArrayList());

        return enrolmentContext;
    }

    private static IEnrolmentPeriod getEnrolmentPeriod(
        IPersistentEnrolmentPeriod enrolmentPeriodDAO,
        final IStudentCurricularPlan studentActiveCurricularPlan)
        throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod
    {
        IEnrolmentPeriod enrolmentPeriod =
            enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(
                studentActiveCurricularPlan.getDegreeCurricularPlan());
        if (enrolmentPeriod == null)
        {
            IEnrolmentPeriod nextEnrolmentPeriod =
                enrolmentPeriodDAO.readNextEnrolmentPeriodForDegreeCurricularPlan(
                    studentActiveCurricularPlan.getDegreeCurricularPlan());
            Date startDate = null;
            Date endDate = null;
            if (nextEnrolmentPeriod != null)
            {
                startDate = nextEnrolmentPeriod.getStartDate();
                endDate = nextEnrolmentPeriod.getEndDate();
            }
            throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
        }
        return enrolmentPeriod;
    }

    public static List computeStudentCurricularPlanCurricularCourses(
        List degreeCurricularPlanCurricularCourses,
        IStudentCurricularPlan studentActiveCurricularPlan)
    {

        List scopesOfCurricularCourses =
            computeScopesOfCurricularCourses(degreeCurricularPlanCurricularCourses);
        Iterator iteratorScopesOfCurricularCourses = scopesOfCurricularCourses.iterator();
        List aux = new ArrayList();
        while (iteratorScopesOfCurricularCourses.hasNext())
        {
            ICurricularCourseScope curricularCourseScope =
                (ICurricularCourseScope) iteratorScopesOfCurricularCourses.next();
            if ((curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()))
                || (curricularCourseScope.getBranch().getName().equals("")))
            {
                if (!aux.contains(curricularCourseScope.getCurricularCourse()))
                {
                    aux.add(curricularCourseScope.getCurricularCourse());
                }
            }
        }
        return aux;
    }

    private static List computeCurricularCoursesNotYetDoneByStudent(
        List curricularCoursesFromStudentDegreeCurricularPlan,
        List aprovedCurricularCoursesFromStudent)
    /* throws ExcepcaoPersistencia */
    {

        List coursesNotDone =
            (List) CollectionUtils.subtract(
                curricularCoursesFromStudentDegreeCurricularPlan,
                aprovedCurricularCoursesFromStudent);

        return coursesNotDone;
    }

    public static List computeScopesOfCurricularCourses(List curricularCourses)
    {

        List scopes = new ArrayList();

        Iterator iteratorCourses = curricularCourses.iterator();
        while (iteratorCourses.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iteratorCourses.next();
            List scopesTemp = curricularCourse.getScopes();
            Iterator iteratorScopes = scopesTemp.iterator();
            while (iteratorScopes.hasNext())
            {
                ICurricularCourseScope curricularCourseScope =
                    (ICurricularCourseScope) iteratorScopes.next();
                scopes.add(curricularCourseScope);
            }
        }

        return scopes;
    }

    public static EnrolmentContext getEnrolmentContext(InfoEnrolmentContext infoEnrolmentContext)
    {

        EnrolmentContext enrolmentContext = new EnrolmentContext();

        IStudent student = Cloner.copyInfoStudent2IStudent(infoEnrolmentContext.getInfoStudent());

        IStudentCurricularPlan studentActiveCurricularPlan =
            Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(
                infoEnrolmentContext.getInfoStudentActiveCurricularPlan());

        // Transform final span of info curricular course scopes:
        List curricularCourseList = new ArrayList();
        List infoCurricularCourseList = null;
        infoCurricularCourseList = infoEnrolmentContext.getInfoFinalCurricularCoursesSpanToBeEnrolled();
        if (infoCurricularCourseList != null && !infoCurricularCourseList.isEmpty())
        {
            Iterator iterator = infoCurricularCourseList.iterator();
            while (iterator.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
                ICurricularCourse curricularCourse =
                    Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
                curricularCourseList.add(curricularCourse);
            }
        }

        // Transform list of info curricular course scopes being enroled:
        List curricularCourseList2 = new ArrayList();
        List infoCurricularCourseList2 = infoEnrolmentContext.getActualEnrolment();
        if (infoCurricularCourseList2 != null && !infoCurricularCourseList2.isEmpty())
        {
            Iterator iterator = infoCurricularCourseList2.iterator();
            while (iterator.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
                ICurricularCourse curricularCourse =
                    Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
                curricularCourseList2.add(curricularCourse);
            }
        }

        // Transform list of info curricular course scopes actually enroled:
        List infoEnroledCurricularCourseList =
            infoEnrolmentContext.getInfoCurricularCoursesAutomaticalyEnroled();
        List enroledCurricularCourseList = new ArrayList();
        if (infoEnroledCurricularCourseList != null && !infoEnroledCurricularCourseList.isEmpty())
        {
            Iterator iterator = infoEnroledCurricularCourseList.iterator();
            while (iterator.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
                ICurricularCourse curricularCourse =
                    Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
                enroledCurricularCourseList.add(curricularCourse);
            }
        }

        // Transform list of info degrees to choose from, for optional courses
        // selection:
        List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
        List optionalDegreeList = new ArrayList();
        if (infoDegreeList != null && !infoDegreeList.isEmpty())
        {
            Iterator iterator = infoDegreeList.iterator();
            while (iterator.hasNext())
            {
                InfoDegree infoDegree = (InfoDegree) iterator.next();
                ICurso degree = getDegree(infoDegree);
                optionalDegreeList.add(degree);
            }
        }

        // Transform list of optional info curricular courses to choose from:
        List infoOptionalCurricularCourseList =
            infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree();
        List optionalCurricularCourseList = new ArrayList();
        if (infoOptionalCurricularCourseList != null && !infoOptionalCurricularCourseList.isEmpty())
        {
            Iterator iterator = infoOptionalCurricularCourseList.iterator();
            while (iterator.hasNext())
            {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
                ICurricularCourse curricularCourse =
                    Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
                optionalCurricularCourseList.add(curricularCourse);
            }
        }

        // Transform list of optional info curricular courses enrolments:
        List infoOptionalCurricularCoursesEnrolmentsList =
            infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
        List optionalCurricularCoursesEnrolmentsList = new ArrayList();
        if (infoOptionalCurricularCoursesEnrolmentsList != null
            && !infoOptionalCurricularCoursesEnrolmentsList.isEmpty())
        {
            Iterator iterator = infoOptionalCurricularCoursesEnrolmentsList.iterator();
            while (iterator.hasNext())
            {
                InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse =
                    (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
                IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse =
                    (IEnrolmentInOptionalCurricularCourse) Cloner.copyInfoEnrolment2IEnrolment(
                        infoEnrolmentInOptionalCurricularCourse);
                optionalCurricularCoursesEnrolmentsList.add(enrolmentInOptionalCurricularCourse);
            }
        }

        ICurso degree = getDegree(infoEnrolmentContext.getChosenOptionalInfoDegree());

        IExecutionPeriod executionPeriod =
            Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
                infoEnrolmentContext.getInfoExecutionPeriod());

        ICurricularCourse chosenCurricularCourse = null;
        if (infoEnrolmentContext.getInfoChosenOptionalCurricularCourse() != null)
        {
            chosenCurricularCourse =
                Cloner.copyInfoCurricularCourse2CurricularCourse(
                    infoEnrolmentContext.getInfoChosenOptionalCurricularCourse());
        }

        // Transform list of info curricular courses done by the student:
        List infoEnrolmentsAprovedByStudentList =
            infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent();
        List enrolmentsAprovedByStudentList = new ArrayList();
        if (infoEnrolmentsAprovedByStudentList != null && !infoEnrolmentsAprovedByStudentList.isEmpty())
        {
            Iterator iterator = infoEnrolmentsAprovedByStudentList.iterator();
            while (iterator.hasNext())
            {
                InfoEnrolment infoEnrolment = (InfoEnrolment) iterator.next();
                IEnrolment enrolment = Cloner.copyInfoEnrolment2IEnrolment(infoEnrolment);
                enrolmentsAprovedByStudentList.add(enrolment);
            }
        }

        try
        {
            BeanUtils.copyProperties(enrolmentContext, infoEnrolmentContext);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(curricularCourseList);
        enrolmentContext.setStudent(student);
        enrolmentContext.setActualEnrolments(curricularCourseList2);
        enrolmentContext.setCurricularCoursesAutomaticalyEnroled(enroledCurricularCourseList);
        enrolmentContext.setChosenOptionalDegree(degree);
        enrolmentContext.setDegreesForOptionalCurricularCourses(optionalDegreeList);
        enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(optionalCurricularCourseList);
        enrolmentContext.setEnrolmentsAprovedByStudent(enrolmentsAprovedByStudentList);
        enrolmentContext.setOptionalCurricularCoursesEnrolments(optionalCurricularCoursesEnrolmentsList);
        enrolmentContext.setChosenOptionalCurricularCourse(chosenCurricularCourse);
        enrolmentContext.setExecutionPeriod(executionPeriod);

        return enrolmentContext;
    }

    public static InfoEnrolmentContext getInfoEnrolmentContext(EnrolmentContext enrolmentContext)
    {

        InfoEnrolmentContext infoEnrolmentContext = new InfoEnrolmentContext();

        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(enrolmentContext.getStudent());

        InfoStudentCurricularPlan infoStudentActiveCurricularPlan =
            Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(
                enrolmentContext.getStudentActiveCurricularPlan());

        // Transform final span of curricular course scopes to respective info:
        List infoCurricularCourseList = new ArrayList();
        List curricularCourseList = null;
        curricularCourseList = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled();
        if (curricularCourseList != null && !curricularCourseList.isEmpty())
        {
            Iterator iterator = curricularCourseList.iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourseList.add(infoCurricularCourse);
            }
        }

        // Transform list of curricular course scopes being enroled to
        // respective info:
        List infoCurricularCourseList2 = new ArrayList();
        List curricularCourseList2 = enrolmentContext.getActualEnrolments();
        if (curricularCourseList2 != null && !curricularCourseList2.isEmpty())
        {
            Iterator iterator = curricularCourseList2.iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourseList2.add(infoCurricularCourse);
            }
        }

        // Transform list of curricular course scopes actually enroled to
        // respective info:
        List enroledCurricularCourseList = enrolmentContext.getCurricularCoursesAutomaticalyEnroled();
        List infoEnroledCurricularCourseList = new ArrayList();
        if (enroledCurricularCourseList != null && !enroledCurricularCourseList.isEmpty())
        {
            Iterator iterator = enroledCurricularCourseList.iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoEnroledCurricularCourseList.add(infoCurricularCourse);
            }
        }

        // Transform list of degrees to choose from, for optional courses
        // selection to respective info:
        List optionalDegreeList = enrolmentContext.getDegreesForOptionalCurricularCourses();
        List infoDegreeList = new ArrayList();
        if (optionalDegreeList != null && !optionalDegreeList.isEmpty())
        {
            Iterator iterator = optionalDegreeList.iterator();
            while (iterator.hasNext())
            {
                ICurso degree = (ICurso) iterator.next();
                InfoDegree infoDegree = getInfoDegree(degree);
                infoDegreeList.add(infoDegree);
            }
        }

        // Transform list of optional curricular courses to choose from to
        // respective info:
        List optionalCurricularCourseList =
            enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
        List infoOptionalCurricularCourseList = new ArrayList();
        if (optionalCurricularCourseList != null && !optionalCurricularCourseList.isEmpty())
        {
            Iterator iterator = optionalCurricularCourseList.iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoOptionalCurricularCourseList.add(infoCurricularCourse);
            }
        }

        // Transform list of optional curricular courses enrolments to
        // respective info:
        List optionalCurricularCourseEnrolmentsList =
            enrolmentContext.getOptionalCurricularCoursesEnrolments();
        List infoOptionalCurricularCoursesEnrolmentsList = new ArrayList();
        if (optionalCurricularCourseEnrolmentsList != null
            && !optionalCurricularCourseEnrolmentsList.isEmpty())
        {
            Iterator iterator = optionalCurricularCourseEnrolmentsList.iterator();
            while (iterator.hasNext())
            {
                IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse =
                    (IEnrolmentInOptionalCurricularCourse) iterator.next();
                InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse =
                    (InfoEnrolmentInOptionalCurricularCourse) Cloner.copyIEnrolment2InfoEnrolment(
                        enrolmentInOptionalCurricularCourse);
                infoOptionalCurricularCoursesEnrolmentsList.add(infoEnrolmentInOptionalCurricularCourse);
            }
        }

        InfoDegree infoDegree = getInfoDegree(enrolmentContext.getChosenOptionalDegree());

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) Cloner.get(enrolmentContext.getExecutionPeriod());

        InfoCurricularCourse infoChosenCurricularCourse = null;
        if (enrolmentContext.getChosenOptionalCurricularCourse() != null)
        {
            infoChosenCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(
                    enrolmentContext.getChosenOptionalCurricularCourse());
        }

        // Transform list of info curricular courses done by the student:
        List enrolmentsAprovedByStudentList = enrolmentContext.getEnrolmentsAprovedByStudent();
        List infoEnrolmentsAprovedByStudentList = new ArrayList();
        if (enrolmentsAprovedByStudentList != null && !enrolmentsAprovedByStudentList.isEmpty())
        {
            Iterator iterator = enrolmentsAprovedByStudentList.iterator();
            while (iterator.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterator.next();
                InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
                infoEnrolmentsAprovedByStudentList.add(infoEnrolment);
            }
        }

        try
        {
            BeanUtils.copyProperties(infoEnrolmentContext, enrolmentContext);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        infoEnrolmentContext.setInfoStudentActiveCurricularPlan(infoStudentActiveCurricularPlan);
        infoEnrolmentContext.setInfoFinalCurricularCoursesSpanToBeEnrolled(infoCurricularCourseList);
        infoEnrolmentContext.setInfoStudent(infoStudent);
        infoEnrolmentContext.setActualEnrolment(infoCurricularCourseList2);
        infoEnrolmentContext.setInfoCurricularCoursesAutomaticalyEnroled(
            infoEnroledCurricularCourseList);
        infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);
        infoEnrolmentContext.setInfoDegreesForOptionalCurricularCourses(infoDegreeList);
        infoEnrolmentContext.setOptionalInfoCurricularCoursesToChooseFromDegree(
            infoOptionalCurricularCourseList);
        infoEnrolmentContext.setInfoEnrolmentsAprovedByStudent(infoEnrolmentsAprovedByStudentList);
        infoEnrolmentContext.setInfoOptionalCurricularCoursesEnrolments(
            infoOptionalCurricularCoursesEnrolmentsList);
        infoEnrolmentContext.setInfoChosenOptionalCurricularCourse(infoChosenCurricularCourse);
        infoEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoEnrolmentContext;
    }

    private static InfoDegree getInfoDegree(ICurso degree)
    {
        InfoDegree infoDegree = null;
        if (degree != null)
        {
            infoDegree = Cloner.copyIDegree2InfoDegree(degree);
            List degreeCurricularPlanList = degree.getDegreeCurricularPlans();
            List infoDegreeCurricularPlanList = new ArrayList();
            if (degreeCurricularPlanList != null && !degreeCurricularPlanList.isEmpty())
            {
                Iterator iterator = degreeCurricularPlanList.iterator();
                while (iterator.hasNext())
                {
                    IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
                    InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                        Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan);
                    infoDegreeCurricularPlanList.add(infoDegreeCurricularPlan);
                }
            }
            infoDegree.setInfoDegreeCurricularPlans(infoDegreeCurricularPlanList);
        }
        return infoDegree;
    }

    private static ICurso getDegree(InfoDegree infoDegree)
    {

        ICurso degree = null;
        if (infoDegree != null)
        {
            degree = Cloner.copyInfoDegree2IDegree(infoDegree);
            List infoDegreeCurricularPlanList = infoDegree.getInfoDegreeCurricularPlans();
            List degreeCurricularPlanList = new ArrayList();
            if (infoDegreeCurricularPlanList != null && !infoDegreeCurricularPlanList.isEmpty())
            {
                Iterator iterator = infoDegreeCurricularPlanList.iterator();
                while (iterator.hasNext())
                {
                    InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                        (InfoDegreeCurricularPlan) iterator.next();
                    IDegreeCurricularPlan degreeCurricularPlan =
                        Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
                            infoDegreeCurricularPlan);
                    degreeCurricularPlanList.add(degreeCurricularPlan);
                }
            }
            degree.setDegreeCurricularPlans(degreeCurricularPlanList);
        }
        return degree;
    }

    private static List subtract(List fromList, List toRemoveList)
    {
        List result = new ArrayList();
        if ((fromList != null) && (toRemoveList != null) && (!fromList.isEmpty()))
        {
            result.addAll(fromList);
            Iterator iterator = toRemoveList.iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
                if (fromList.contains(curricularCourse))
                {
                    result.remove(curricularCourse);
                }

            }
        }
        return result;
    }

    //	private static List filterByExecutionCourses(List
    // possibleCurricularCoursesScopesToChoose, IExecutionPeriod
    // executionPeriod) {
    //
    //		List curricularCoursesToRemove = new ArrayList();
    //		final IExecutionPeriod executionPeriod2 = executionPeriod;
    //		
    //		Iterator iterator = possibleCurricularCoursesScopesToChoose.iterator();
    //		while (iterator.hasNext()) {
    //			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope)
    // iterator.next();
    //			List executionCourseList =
    // curricularCourseScope.getCurricularCourse().getAssociatedExecutionCourses();
    //				
    //			List executionCourseInExecutionPeriod = (List)
    // CollectionUtils.select(executionCourseList, new Predicate() {
    //				public boolean evaluate(Object obj) {
    //					IDisciplinaExecucao disciplinaExecucao = (IDisciplinaExecucao) obj;
    //					return disciplinaExecucao.getExecutionPeriod().equals(executionPeriod2);
    //				}
    //			});
    //				
    //			if(executionCourseInExecutionPeriod.isEmpty()){
    //				curricularCoursesToRemove.add(curricularCourseScope);
    //			} else {
    //				executionCourseInExecutionPeriod.clear();
    //			}
    //		}
    //
    //		possibleCurricularCoursesScopesToChoose.removeAll(curricularCoursesToRemove);
    //		
    //		return possibleCurricularCoursesScopesToChoose;
    //	}

    public static EnrolmentContext initialOptionalEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(
        IStudent student,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        EnrolmentContext enrolmentContext = new EnrolmentContext();

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
            persistentSupport.getIStudentCurricularPlanPersistente();
        IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

        IStudentCurricularPlan studentActiveCurricularPlan =
            persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
                student.getNumber(),
                student.getDegreeType());
        IDegreeCurricularPlan degreeCurricularPlan =
            studentActiveCurricularPlan.getDegreeCurricularPlan();
        List curricularCoursesFromDegreeCurricularPlan = degreeCurricularPlan.getCurricularCourses();

        final IBranch studentBranch = studentActiveCurricularPlan.getBranch();
        List optionalCurricularCoursesFromStudentCurricularPlan =
            (List) CollectionUtils.select(curricularCoursesFromDegreeCurricularPlan, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) obj;
                List scopes = curricularCourse.getScopes();
                Iterator iter = scopes.iterator();
                boolean result = false;
                while (iter.hasNext() && !result)
                {
                    ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                    if (scope
                        .getCurricularCourse()
                        .getType()
                        .equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)
                        && (scope.getBranch().equals(studentBranch)
                            || (scope.getBranch().getName().equals("")
                                && scope.getBranch().getCode().equals(""))))
                    {
                        result = true;
                    }
                }
                return result;
            }
        });

        List studentEnrolments =
            persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

        List studentAprovedOptionalEnrolments =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                boolean result = false;
                List scopes = enrolment.getCurricularCourse().getScopes();
                Iterator iter = scopes.iterator();
                while (iter.hasNext() && !result)
                {
                    ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                    if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)
                        && enrolment.getCurricularCourse().getType().equals(
                            CurricularCourseType.OPTIONAL_COURSE_OBJ)
                        && (scope.getBranch().equals(studentBranch)
                            || (scope.getBranch().getName().equals("")
                                && scope.getBranch().getCode().equals(""))))
                    {
                        result = true;
                    }
                }
                return result;
            }
        });

        List studentEnroledAndTemporarilyEnroledOptionalEnrolments =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                List scopes = enrolment.getCurricularCourse().getScopes();
                boolean result = false;
                Iterator iter = scopes.iterator();
                while (iter.hasNext() && !result)
                {
                    ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                    if ((enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
                        || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED))
                        && enrolment.getCurricularCourse().getType().equals(
                            CurricularCourseType.OPTIONAL_COURSE_OBJ)
                        && (scope.getBranch().equals(studentBranch)
                            || (scope.getBranch().getName().equals("")
                                && scope.getBranch().getCode().equals(""))))
                    {
                        result = true;
                    }
                }
                return result;
            }
        });

        List curricularCoursesFromStudentAprovedOptionalEnrolments =
            (List) CollectionUtils.collect(studentAprovedOptionalEnrolments, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getCurricularCourse();
            }
        });

        List possibleCurricularCoursesToChoose =
            (List) CollectionUtils.subtract(
                optionalCurricularCoursesFromStudentCurricularPlan,
                curricularCoursesFromStudentAprovedOptionalEnrolments);

        enrolmentContext.setAcumulatedEnrolments(null);
        enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(new ArrayList());
        enrolmentContext.setActualEnrolments(new ArrayList());
        enrolmentContext.setDegreesForOptionalCurricularCourses(new ArrayList());
        enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(new ArrayList());
        enrolmentContext.setCurricularCoursesAutomaticalyEnroled(new ArrayList());

        enrolmentContext.setStudent(student);
        enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(
            possibleCurricularCoursesToChoose);
        enrolmentContext.setExecutionPeriod(executionPeriod);
        enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
        enrolmentContext.setOptionalCurricularCoursesEnrolments(
            studentEnroledAndTemporarilyEnroledOptionalEnrolments);
        enrolmentContext.setEnrolmentsAprovedByStudent(studentAprovedOptionalEnrolments);

        return enrolmentContext;
    }

    public static EnrolmentContext initialEnrolmentWithoutRulesContextForDegreeAdministrativeOffice(
        IStudent student,
        IExecutionPeriod executionPeriod,
        ICursoExecucao executionDegree,
        final List listOfChosenCurricularSemesters,
        final List listOfChosenCurricularYears)
        throws ExcepcaoPersistencia
    {
        EnrolmentContext enrolmentContext = new EnrolmentContext();

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
            persistentSupport.getIStudentCurricularPlanPersistente();
        IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
            persistentSupport.getIPersistentDegreeCurricularPlan();

        IDegreeCurricularPlan degreeCurricularPlanTemp = new DegreeCurricularPlan();
        degreeCurricularPlanTemp.setIdInternal(executionDegree.getCurricularPlan().getIdInternal());
        final IDegreeCurricularPlan degreeCurricularPlan =
            (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
                degreeCurricularPlanTemp,
                false);

        IStudentCurricularPlan studentActiveCurricularPlan =
            persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
                student.getNumber(),
                student.getDegreeType());

        // filters a list of curricular courses by all of its scopes that
        // matters in relation to the selected semester and the selected year.
        List curricularCoursesFromDegreeCurricularPlan =
            (List) CollectionUtils.select(degreeCurricularPlan.getCurricularCourses(), new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                boolean result = false;
                if (arg0 instanceof ICurricularCourse
                    && !((ICurricularCourse) arg0).getType().equals(
                        CurricularCourseType.OPTIONAL_COURSE_OBJ))
                {
                    ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                    List scopes = curricularCourse.getScopes();
                    Iterator iter = scopes.iterator();
                    while (iter.hasNext() && !result)
                    {
                        ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                        if (listOfChosenCurricularSemesters
                            .contains(scope.getCurricularSemester().getSemester())
                            && listOfChosenCurricularYears.contains(
                                scope.getCurricularSemester().getCurricularYear().getYear()))
                        {
                            result = true;
                        }
                    }
                }
                return result;
            }

        });

        final IExecutionPeriod executionPeriod2 = executionPeriod;

        List studentEnrolments =
            persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

        // Enrolments that have state APROVED are to be subtracted from list of
        // possible choices.
        List aprovedStudentEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)
                    && enrolment.getExecutionPeriod().equals(executionPeriod2);
            }
        });

        List aprovedStudentCurricularCourses =
            (List) CollectionUtils.collect(aprovedStudentEnrolments, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return enrolment.getCurricularCourse();
            }
        });

        List studentEnrolmentsWithStateEnrolled =
            (List) CollectionUtils.select(studentEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return (
                    enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED)
                        || enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED))
                    && enrolment.getExecutionPeriod().equals(executionPeriod2)
                    && !enrolment.getCurricularCourse().getType().equals(
                        CurricularCourseType.OPTIONAL_COURSE_OBJ);
            }
        });

        List studentEnrolmentsWithStateAprovedAndEnrolled = new ArrayList();
        studentEnrolmentsWithStateAprovedAndEnrolled.addAll(aprovedStudentEnrolments);
        //		studentEnrolmentsWithStateAprovedAndEnrolled.addAll(studentEnrolmentsWithStateEnrolled);

        final List listOfChosenCurricularSemesters2 = listOfChosenCurricularSemesters;
        final List listOfChosenCurricularYears2 = listOfChosenCurricularYears;
        List studentEnrolmentsWithStateAprovedAndEnrolledForSelectedDegree =
            (List) CollectionUtils.select(studentEnrolmentsWithStateAprovedAndEnrolled, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                boolean result = false;
                if (enrolment
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .equals(degreeCurricularPlan))
                {
                    List scopes = enrolment.getCurricularCourse().getScopes();
                    Iterator iter = scopes.iterator();
                    while (iter.hasNext() && !result)
                    {
                        ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                        if (listOfChosenCurricularSemesters2
                            .contains(scope.getCurricularSemester().getSemester())
                            && listOfChosenCurricularYears2.contains(
                                scope.getCurricularSemester().getCurricularYear().getYear()))
                        {
                            result = true;
                        }
                    }
                }

                return result;

            }
        });

        List studentEnrolmentsWithStateEnroledForSelectedDegree =
            (List) CollectionUtils.select(studentEnrolmentsWithStateEnrolled, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                boolean result = false;
                if (enrolment
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .equals(degreeCurricularPlan))
                {
                    List scopes = enrolment.getCurricularCourse().getScopes();
                    Iterator iter = scopes.iterator();
                    while (iter.hasNext() && !result)
                    {
                        ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                        if (listOfChosenCurricularSemesters2
                            .contains(scope.getCurricularSemester().getSemester())
                            && listOfChosenCurricularYears2.contains(
                                scope.getCurricularSemester().getCurricularYear().getYear()))
                        {
                            result = true;
                        }
                    }
                }

                return result;
            }
        });

        List studentCurricularCoursesFromEnrolmentsWithStateEnroled =
            (
                List) CollectionUtils
                    .collect(studentEnrolmentsWithStateEnroledForSelectedDegree, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;

                return enrolment.getCurricularCourse();
            }
        });

        List possibleCurricularCoursesToChoose =
            EnrolmentContextManager.subtract(
                curricularCoursesFromDegreeCurricularPlan,
                aprovedStudentCurricularCourses);

        enrolmentContext.setAcumulatedEnrolments(null);
        enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(new ArrayList());
        enrolmentContext.setDegreesForOptionalCurricularCourses(new ArrayList());
        enrolmentContext.setOptionalCurricularCoursesEnrolments(new ArrayList());
        enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(new ArrayList());
        enrolmentContext.setCurricularCoursesAutomaticalyEnroled(new ArrayList());

        enrolmentContext.setStudent(student); // To keep the actor
        enrolmentContext.setChosenOptionalDegree(degreeCurricularPlan.getDegree());
        // To keep the chosen degree
        enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
        enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(possibleCurricularCoursesToChoose);
        enrolmentContext.setExecutionPeriod(executionPeriod);
        enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
        // To keep the information about all the aproved curricular courses
        // from the chosen degree.
        enrolmentContext.setEnrolmentsAprovedByStudent(
            studentEnrolmentsWithStateAprovedAndEnrolledForSelectedDegree);
        // To keep the information about witch curricular courses from the
        // chosen degree
        // the student is still enrolled (temporarily or not).
        enrolmentContext.setActualEnrolments(studentCurricularCoursesFromEnrolmentsWithStateEnroled);

        return enrolmentContext;
    }

}