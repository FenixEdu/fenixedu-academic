/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

public class ReadStudentCurriculum implements IService
{

    /**
     * The actor of this class.
     */
    public ReadStudentCurriculum()
    {
    }

    public List run(Integer executionDegreeCode, Integer studentCurricularPlanID)
            throws ExcepcaoInexistente, FenixServiceException
    {
        ISuportePersistente sp = null;

        IStudentCurricularPlan studentCurricularPlan = null;
        
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            // The student Curricular plan

            IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
            studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);

            studentCurricularPlan = (IStudentCurricularPlan) sp
                    .getIStudentCurricularPlanPersistente().readByOId(
                            studentCurricularPlanTemp, false);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error", ex);
            throw newEx;
        }

        if (studentCurricularPlan == null) { throw new NonExistingServiceException(); }

        Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
        List result = new ArrayList();

        while (iterator.hasNext())
        {
            IEnrolment enrolmentTemp = (IEnrolment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;

            List enrollmentEvaluations = new ArrayList();
            enrollmentEvaluations.addAll(enrolmentTemp
                    .getEvaluations());
            if (enrollmentEvaluations != null
                    && enrollmentEvaluations.size() > 0)
            {
                if (enrollmentEvaluations.size() == 1)
                {
                    IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) enrollmentEvaluations
                            .get(0);
                    infoEnrolmentEvaluation = Cloner
                            .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);
                }
                else
                {
                    BeanComparator dateComparator = new BeanComparator("when");

                    Collections.sort(enrollmentEvaluations, dateComparator);
                    Collections.reverse(enrollmentEvaluations);

                    IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) enrollmentEvaluations
                            .get(0);

                    if (enrolmentTemp.getEnrolmentState().equals(
                            EnrolmentState.NOT_APROVED))
                    {
                        infoEnrolmentEvaluation = Cloner
                                .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);
                    }
                    if (enrollmentEvaluation.getObservation() != null)
                    {
                        if (enrollmentEvaluation.getObservation().equals(
                                GetEnrolmentGrade.RECTIFIED))
                        {
                            Iterator iterator1 = enrollmentEvaluations
                                    .iterator();
                            while (iterator.hasNext())
                            {
                                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator1
                                        .next();
                                if (enrolmentEvaluation
                                        .getObservation()
                                        .equals(GetEnrolmentGrade.RECTIFICATION))
                                {
                                    enrollmentEvaluation = enrolmentEvaluation;
                                    break;
                                }
                            }
                        }
                    }
                    if (!enrollmentEvaluation.getEnrolmentEvaluationType()
                            .equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ))
                    {

                        infoEnrolmentEvaluation = Cloner
                                .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);

                    }
                    else
                    {
                        IEnrolmentEvaluation previousEvaluation = null;
                        for (int i = 1; i < enrollmentEvaluations.size(); i++)
                        {
                            previousEvaluation = (IEnrolmentEvaluation) enrollmentEvaluations
                                    .get(i);
                            if (!previousEvaluation
                                    .getEnrolmentEvaluationType()
                                    .equals(
                                            EnrolmentEvaluationType.IMPROVEMENT_OBJ))
                            {
                                break;
                            }
                        }

                        Integer latestMark = new Integer(0);
                        try
                        {
                            latestMark = Integer.valueOf(enrollmentEvaluation
                                    .getGrade());
                        }
                        catch (NumberFormatException e)
                        {
                            // If there's an Exception , the the student wasn't
                            // able to
                            // improve
                            //exception is to be ignored
                        }

                        // if there is no exception we must check which is the
                        // higher
                        // grade

                        Integer previousMark = new Integer(0);
                        try
                        {
                            previousMark = Integer.valueOf(previousEvaluation
                                    .getGrade());
                        }
                        catch (NumberFormatException e)
                        {
                            //              latestEvaluation.setGrade((new
                            // Integer(latestEvaluation.getGrade())).toString());
                            infoEnrolmentEvaluation = Cloner
                                    .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);

                        }

                        if (previousMark.intValue() >= latestMark.intValue())
                        {
                            try
                            {
                                previousEvaluation.setGrade((new Integer(
                                        previousEvaluation.getGrade()))
                                        .toString());
                                infoEnrolmentEvaluation = Cloner
                                        .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);

                            }
                            finally
                            {
                            }

                        }
                        else
                        {

                            infoEnrolmentEvaluation = Cloner
                                    .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrollmentEvaluation);

                        }

                    }
                }
            }
            enrolmentTemp.getExecutionPeriod();
            InfoEnrolment infoEnrolment = Cloner
                    .copyIEnrolment2InfoEnrolment(enrolmentTemp);
            
            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

            result.add(infoEnrolment);
        }
        
        return result;
    }
}