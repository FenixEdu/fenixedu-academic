package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;

/**
 * @author Fernanda Quitério 10/07/2003
 *  
 */
public class ReadStudentsFinalEvaluationForConfirmation implements IServico {

    private static ReadStudentsFinalEvaluationForConfirmation servico = new ReadStudentsFinalEvaluationForConfirmation();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentsFinalEvaluationForConfirmation getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentsFinalEvaluationForConfirmation() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadStudentsFinalEvaluationForConfirmation";
    }

    public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode,
            String yearString) throws FenixServiceException {

        List infoEnrolmentEvaluations = new ArrayList();
        InfoTeacher infoTeacher = new InfoTeacher();
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            IPersistentEnrollment persistentEnrolment = sp
                    .getIPersistentEnrolment();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);

            List enrolments = null;
            if (yearString != null) {
                enrolments = persistentEnrolment.readByCurricularCourseAndYear(
                        curricularCourse, yearString);
            } else {
                enrolments = persistentEnrolment
                        .readByCurricularCourse(curricularCourse);
            }

            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext()) {
                IEnrollment enrolment = (IEnrollment) iterEnrolment.next();
                List allEnrolmentEvaluations = persistentEnrolmentEvaluation
                        .readEnrolmentEvaluationByEnrolment(enrolment);
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) allEnrolmentEvaluations
                        .get(allEnrolmentEvaluations.size() - 1);
                enrolmentEvaluations.add(enrolmentEvaluation);
            }

            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {

                List temporaryEnrolmentEvaluations = null;
                //				try
                //				{
                temporaryEnrolmentEvaluations = checkForInvalidSituations(enrolmentEvaluations);
                //
                //				}
                //				catch (ExistingServiceException e)
                //				{
                //					throw new ExistingServiceException();
                //				}
                //				catch (InvalidSituationServiceException e)
                //				{
                //					throw new InvalidSituationServiceException();
                //				}

                //				get teacher responsible for final evaluation - he is
                // responsible for all evaluations
                // for this
                //				curricularCourseScope
                IPessoa person = ((IEnrolmentEvaluation) temporaryEnrolmentEvaluations
                        .get(0)).getPersonResponsibleForGrade();
                ITeacher teacher = persistentTeacher
                        .readTeacherByUsername(person.getUsername());
                infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

                //				transform evaluations in databeans
                ListIterator iter = temporaryEnrolmentEvaluations
                        .listIterator();
                while (iter.hasNext()) {
                    IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter
                            .next();
                    InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner
                            .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

                    InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod.newInfoFromDomain(elem.getEnrolment());
                    infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
                    infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                }
            }
            if (infoEnrolmentEvaluations.size() == 0) {
                throw new NonExistingServiceException();
            }
            IExecutionPeriod executionPeriod = persistentExecutionPeriod
                    .readActualExecutionPeriod();
            infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                    .get(executionPeriod);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation
                .setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
        Date evaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations
                .get(0)).getGradeAvailableDate();
        infoSiteEnrolmentEvaluation.setLastEvaluationDate(evaluationDate);
        infoSiteEnrolmentEvaluation.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoSiteEnrolmentEvaluation;
    }

    private List checkForInvalidSituations(List enrolmentEvaluations)
            throws ExistingServiceException, InvalidSituationServiceException {
        //			evaluations can only be confirmated if they are not already
        // confirmated
        List temporaryEnrolmentEvaluations = (List) CollectionUtils.select(
                enrolmentEvaluations, new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if (enrolmentEvaluation.getEnrolmentEvaluationState()
                                .equals(EnrolmentEvaluationState.TEMPORARY_OBJ))
                            return true;
                        return false;
                    }
                });

        if (temporaryEnrolmentEvaluations == null
                || temporaryEnrolmentEvaluations.size() == 0) {
            throw new ExistingServiceException();
        }

        List enrolmentEvaluationsWithoutGrade = (List) CollectionUtils.select(
                temporaryEnrolmentEvaluations, new Predicate() {
                    public boolean evaluate(Object input) {
                        //						see if there are evaluations without grade
                        IEnrolmentEvaluation enrolmentEvaluationInput = (IEnrolmentEvaluation) input;
                        if (enrolmentEvaluationInput.getGrade() == null
                                || enrolmentEvaluationInput.getGrade().length() == 0)
                            return true;
                        return false;
                    }
                });
        if (enrolmentEvaluationsWithoutGrade != null) {
            if (enrolmentEvaluationsWithoutGrade.size() == temporaryEnrolmentEvaluations
                    .size()) {
                throw new InvalidSituationServiceException();
            }
            temporaryEnrolmentEvaluations = (List) CollectionUtils.subtract(
                    temporaryEnrolmentEvaluations,
                    enrolmentEvaluationsWithoutGrade);
        }

        return temporaryEnrolmentEvaluations;
    }
}