package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExam;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Pessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.TipoCurso;

/**
 * @author Fernanda Quitério
 * 01/07/2003
 * 
 */
public class ReadStudentsAndMarksByCurricularCourse implements IServico
{

    private static ReadStudentsAndMarksByCurricularCourse servico =
        new ReadStudentsAndMarksByCurricularCourse();

    /**
     * The singleton access method of this class.
     **/
    public static ReadStudentsAndMarksByCurricularCourse getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private ReadStudentsAndMarksByCurricularCourse()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "ReadStudentsAndMarksByCurricularCourse";
    }

    public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString)
        throws FenixServiceException
    {

        List infoEnrolmentEvaluations = new ArrayList();
        InfoTeacher infoTeacher = new InfoTeacher();
        Date lastEvaluationDate = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation =
                sp.getIPersistentEnrolmentEvaluation();
            IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ICurricularCourse curricularCourse = new CurricularCourse();
            curricularCourse.setIdInternal(curricularCourseCode);
            curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

            List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse, yearString);
            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterEnrolment.next();
                if (enrolment
                    .getStudentCurricularPlan()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .getTipoCurso()
                    .equals(TipoCurso.MESTRADO_OBJ))
                {
                    List allEnrolmentEvaluations =
                        persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);
                    IEnrolmentEvaluation enrolmentEvaluation =
                        (IEnrolmentEvaluation) allEnrolmentEvaluations.get(
                            allEnrolmentEvaluations.size() - 1);
                    enrolmentEvaluations.add(enrolmentEvaluation);

                }
            }

            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
            {
                //			in case we have evaluations they can be submitted only if they are temporary
                List temporaryEnrolmentEvaluations =
                    (List) CollectionUtils.select(enrolmentEvaluations, new Predicate()
                {
                    public boolean evaluate(Object arg0)
                    {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if (enrolmentEvaluation
                            .getEnrolmentEvaluationState()
                            .equals(EnrolmentEvaluationState.TEMPORARY_OBJ))
                            return true;
                        return false;
                    }
                });

                if (temporaryEnrolmentEvaluations == null || temporaryEnrolmentEvaluations.size() == 0)
                {
                    throw new ExistingServiceException();
                }

                //				if (((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
                //					.getEnrolmentEvaluationState()
                //					.equals(EnrolmentEvaluationState.FINAL_OBJ)) {
                //					throw new ExistingServiceException();
                //				}

                //				get teacher responsible for final evaluation - he is responsible for all evaluations for this
                //				curricularCourse
                List enrolmentEvaluationsWithResponsiblePerson =
                    (List) CollectionUtils.select(enrolmentEvaluations, new Predicate()
                {
                    public boolean evaluate(Object arg0)
                    {
                        IEnrolmentEvaluation enrolEval = (IEnrolmentEvaluation) arg0;
                        if (enrolEval.getPersonResponsibleForGrade() != null)
                        {
                            return true;
                        }
                        return false;
                    }
                });
                if (enrolmentEvaluationsWithResponsiblePerson.size() > 0)
                {
                    IPessoa person =
                        ((IEnrolmentEvaluation) enrolmentEvaluationsWithResponsiblePerson.get(0))
                            .getPersonResponsibleForGrade();
                    ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
                    infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                }

                //				transform evaluations in databeans
                ListIterator iter = temporaryEnrolmentEvaluations.listIterator();
                while (iter.hasNext())
                {
                    IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter.next();
                    InfoEnrolmentEvaluation infoEnrolmentEvaluation =
                        Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

                    infoEnrolmentEvaluation.setInfoEnrolment(
                        Cloner.copyIEnrolment2InfoEnrolment(elem.getEnrolment()));
                    infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                }
            }

            if (infoEnrolmentEvaluations.size() == 0)
            {
                throw new NonExistingServiceException();
            }

            //				get last evaluation date to show in interface
            if (((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getExamDate() == null)
            {
                lastEvaluationDate = getLastEvaluationDate(yearString, curricularCourse);
            } else
            {
                lastEvaluationDate =
                    ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getExamDate();
            }

        } catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
        infoSiteEnrolmentEvaluation.setLastEvaluationDate(lastEvaluationDate);
        return infoSiteEnrolmentEvaluation;
    }

    private Date getLastEvaluationDate(String yearString, ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia, NonExistingServiceException
    {

        Date lastEvaluationDate = null;
        Iterator iterator = curricularCourse.getAssociatedExecutionCourses().listIterator();
        while (iterator.hasNext())
        {
            IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iterator.next();
            if (executionCourse.getExecutionPeriod().getExecutionYear().getYear().equals(yearString))
            {

                if (executionCourse.getAssociatedEvaluations() != null
                    && executionCourse.getAssociatedEvaluations().size() > 0)
                {
                    List evaluationsWithoutFinal =
                        (
                            List) CollectionUtils
                                .select(executionCourse.getAssociatedEvaluations(), new Predicate()
                    {
                        public boolean evaluate(Object input)
                        {
                                //for now returns only exams
    if (input instanceof IExam)
                                return true;
                            return false;
                        }
                    });

                    ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(new BeanComparator("day.time"));
                    comparatorChain.addComparator(new BeanComparator("beginning.time"));
                    Collections.sort(evaluationsWithoutFinal, comparatorChain);

                    if (evaluationsWithoutFinal.get(evaluationsWithoutFinal.size() - 1)
                        instanceof IExam)
                    {
                        IExam lastEvaluation =
                            (IExam) (evaluationsWithoutFinal.get(evaluationsWithoutFinal.size() - 1));
                        if (lastEvaluationDate != null)
                        {
                            if (lastEvaluationDate.before(lastEvaluation.getDay().getTime()))
                            {
                                lastEvaluationDate = lastEvaluation.getDay().getTime();
                            }
                        } else
                        {
                            lastEvaluationDate = lastEvaluation.getDay().getTime();
                        }
                    }
                }
            }
        }

        if (lastEvaluationDate == null)
        {
            Calendar calendar = Calendar.getInstance();
            lastEvaluationDate = calendar.getTime();
        }
        return lastEvaluationDate;
    }

    public List run(Integer curricularCourseID, Integer studentNumber, String executionYear)
        throws FenixServiceException
    {

        List enrolmentEvaluations = null;
        InfoTeacher infoTeacher = null;
        List infoSiteEnrolmentEvaluations = new ArrayList();
        ICurricularCourse curricularCourse = null;
        IEnrolment enrolment = new Enrolment();
        IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation =
                sp.getIPersistentEnrolmentEvaluation();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            //read curricularCourse by ID
            ICurricularCourse curricularCourseTemp = new CurricularCourse();
            curricularCourseTemp.setIdInternal(curricularCourseID);
            curricularCourse =
                (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(
                    curricularCourseTemp,
                    false);

            //			get student curricular Plan
            IStudentCurricularPlan studentCurricularPlan =
                sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
                    studentNumber,
                    new TipoCurso(TipoCurso.MESTRADO));
            if (studentCurricularPlan == null)
            {
                throw new ExistingServiceException();
            }

            enrolment =
                sp.getIPersistentEnrolment().readEnrolmentByStudentCurricularPlanAndCurricularCourse(
                    studentCurricularPlan,
                    curricularCourse,
                    executionYear);

            if (enrolment != null)
            {

                //						ListIterator iter1 = enrolments.listIterator();
                //						while (iter1.hasNext()) {
                //							enrolment = (IEnrolment) iter1.next();

                EnrolmentEvaluationState enrolmentEvaluationState =
                    new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL);
                enrolmentEvaluations =
                    persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationState(
                        enrolment,
                        enrolmentEvaluationState);
                //				enrolmentEvaluations = enrolment.getEvaluations();

                List infoTeachers = new ArrayList();
                if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
                {
                    IPessoa person =
                        ((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
                            .getPersonResponsibleForGrade();
                    ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
                    infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                    infoTeachers.add(infoTeacher);
                }

                List infoEnrolmentEvaluations = new ArrayList();
                if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
                {
                    ListIterator iter = enrolmentEvaluations.listIterator();
                    while (iter.hasNext())
                    {
                        enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
                        InfoEnrolmentEvaluation infoEnrolmentEvaluation =
                            Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
                        infoEnrolmentEvaluation.setInfoEnrolment(
                            Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment()));

                        if (enrolmentEvaluation != null)
                        {
                            if (enrolmentEvaluation.getEmployee() != null)
                            {
                                IPessoa person = new Pessoa();
                                person.setIdInternal(enrolmentEvaluation.getEmployee().getKeyPerson());
                                IPessoa person2 =
                                    (IPessoa) sp.getIPessoaPersistente().readByOId(person, false);
                                infoEnrolmentEvaluation.setInfoEmployee(
                                    Cloner.copyIPerson2InfoPerson(person2));
                            }

                        }
                        infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                    }

                }
                InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation =
                    new InfoSiteEnrolmentEvaluation();
                infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
                infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
                infoSiteEnrolmentEvaluations.add(infoSiteEnrolmentEvaluation);

            }
        } catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoSiteEnrolmentEvaluations;
    }

}
