package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import ServidorAplicacao.Servico.enrolment.degree.ChangeEnrolmentStateFromTemporarilyToEnroled;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author David Santos 16/Jun/2003
 */

public class ConfirmActualEnrolmentWithoutRules implements IService
{

    public ConfirmActualEnrolmentWithoutRules()
    {
    }

    public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext)
        throws FenixServiceException
    {

        EnrolmentContext enrolmentContext =
            EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

        if (enrolmentContext.getEnrolmentValidationResult().isSucess())
        {
            try
            {
                writeTemporaryEnrolment(enrolmentContext);
                enrolmentContext.getEnrolmentValidationResult().setSucessMessage(
                    EnrolmentValidationResult.SUCCESS_ENROLMENT);
            }
            catch (ExcepcaoPersistencia e)
            {
                e.printStackTrace();
                throw new FenixServiceException(e);
            }
        }
        return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
    }

    private void writeTemporaryEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = null;
        IPersistentEnrolment persistentEnrolment = null;
        IPersistentCurricularCourse persistentCurricularCourse = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            persistentEnrolment = sp.getIPersistentEnrolment();

            persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            // FIXME [DAVID]: De futuro passar a ler todos os enrolments do
            // plano curricular e periodo de execução que vem no
            // enrolmentContext.
            List studentEnrolments =
                persistentEnrolment.readAllByStudentCurricularPlan(
                    enrolmentContext.getStudentActiveCurricularPlan());

            // List of all enrolments with state 'enrolled' and 'temporarily
            // enrolled'.
            List studentEnroledAndTemporarilyEnroledEnrolments =
                (List) CollectionUtils.select(studentEnrolments, new Predicate()
            {
                public boolean evaluate(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return (
                        enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
                            || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED))
                        && !enrolment.getCurricularCourse().getType().equals(
                            CurricularCourseType.OPTIONAL_COURSE_OBJ);
                }
            });

            List enrolmentsToDelete =
                this.subtract(
                    studentEnroledAndTemporarilyEnroledEnrolments,
                    enrolmentContext.getActualEnrolments(),
                    enrolmentContext.getChosenOptionalDegree(),
                    enrolmentContext.getExecutionPeriod());

            // Delete from data base the enrolments that don't mather.
            Iterator iterator = enrolmentsToDelete.iterator();
            while (iterator.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterator.next();
                ConfirmActualEnrolmentWithoutRules.deleteAttendAndEnrolmentEvaluations(enrolment);
                //				persistentEnrolment.delete(enrolment);
                persistentEnrolment.simpleLockWrite(enrolment);
                enrolment.setEnrolmentState(EnrolmentState.ANNULED);
            }

            iterator = enrolmentContext.getActualEnrolments().iterator();
            while (iterator.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                IEnrolment enrolment =
                    persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                        enrolmentContext.getStudentActiveCurricularPlan(),
                        curricularCourse,
						enrolmentContext.getExecutionPeriod());

                if (enrolment == null)
                {
                    ICurricularCourse curricularCourse2Write =
                        (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, true);

                    enrolment = new Enrolment();
                    persistentEnrolment.simpleLockWrite(enrolment);
                    enrolment.setCurricularCourse(curricularCourse2Write);
                    enrolment.setExecutionPeriod(enrolmentContext.getExecutionPeriod());
                    enrolment.setStudentCurricularPlan(
                        enrolmentContext.getStudentActiveCurricularPlan());
                    enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
                    enrolment.setEnrolmentState(EnrolmentState.ENROLED);

                    ChangeEnrolmentStateFromTemporarilyToEnroled.createAttend(enrolment);
                    ChangeEnrolmentStateFromTemporarilyToEnroled.createEnrolmentEvaluation(enrolment);
                }
                else
                {
                    persistentEnrolment.simpleLockWrite(enrolment);
                    enrolment.setEnrolmentState(EnrolmentState.ENROLED);
                }
            }
        }
        catch (ExistingPersistentException e1)
        {
            e1.printStackTrace();
            throw e1;
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    private List subtract(
        List fromList,
        List toRemoveList,
        ICurso chosenDegree,
        IExecutionPeriod executionPeriod)
    {
        List result = new ArrayList();
        if ((fromList != null) && (toRemoveList != null) && (!fromList.isEmpty()))
        {

            Iterator iterator = fromList.iterator();
            while (iterator.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterator.next();
                if (enrolment
                    .getCurricularCourse()
                    .getDegreeCurricularPlan()
                    .getDegree()
                    .equals(chosenDegree)
                    && enrolment.getExecutionPeriod().equals(executionPeriod))
                {
                    result.add(enrolment);
                }
            }

            iterator = fromList.iterator();
            while (iterator.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterator.next();

                if (toRemoveList.contains(enrolment.getCurricularCourse()))
                {
                    result.remove(enrolment);
                }
            }
        }
        return result;
    }

    public static void deleteAttendAndEnrolmentEvaluations(IEnrolment enrolment)
        throws ExcepcaoPersistencia
    {
        ISuportePersistente persistentSupport = null;
        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = null;
        IFrequentaPersistente persistentAttend = null;

        persistentSupport = SuportePersistenteOJB.getInstance();
        persistentAttend = persistentSupport.getIFrequentaPersistente();
        persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();

        IFrequenta attend = persistentAttend.readByEnrolment(enrolment);
        List enrolmentEvaluationsList =
            persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);

        if (attend != null)
        {
            persistentAttend.delete(attend);
        }

        if (enrolmentEvaluationsList != null)
        {
            Iterator iterator = enrolmentEvaluationsList.iterator();
            while (iterator.hasNext())
            {
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
                if (enrolmentEvaluation != null)
                {
                    //					persistentEnrolmentEvaluation.delete(enrolmentEvaluation);
                    persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluation);
                    enrolmentEvaluation.setEnrolmentEvaluationState(
                        EnrolmentEvaluationState.ANNULED_OBJ);
                }
            }
        }
    }

}