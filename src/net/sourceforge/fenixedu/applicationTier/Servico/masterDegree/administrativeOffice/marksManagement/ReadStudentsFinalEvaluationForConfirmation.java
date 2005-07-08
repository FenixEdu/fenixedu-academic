package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio 10/07/2003
 *  
 */
public class ReadStudentsFinalEvaluationForConfirmation implements IService {

    public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString)
            throws FenixServiceException {

        List infoEnrolmentEvaluations = new ArrayList();
        InfoTeacher infoTeacher = new InfoTeacher();
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);

            List enrolments = null;
            if (yearString != null) {
                enrolments = persistentEnrolment.readByCurricularCourseAndYear(curricularCourseCode,
                        yearString);
            } else {
                enrolments = curricularCourse.getEnrolments();
            }

            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext()) {
                IEnrolment enrolment = (IEnrolment) iterEnrolment.next();
                List allEnrolmentEvaluations = enrolment.getEvaluations();
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
                IPerson person = ((IEnrolmentEvaluation) temporaryEnrolmentEvaluations.get(0))
                        .getPersonResponsibleForGrade();
                ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
                infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

                //				transform evaluations in databeans
                ListIterator iter = temporaryEnrolmentEvaluations.listIterator();
                while (iter.hasNext()) {
                    IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter.next();
                    InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner
                            .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

                    InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                            .newInfoFromDomain(elem.getEnrolment());
                    infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
                    infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                }
            }
            if (infoEnrolmentEvaluations.size() == 0) {
                throw new NonExistingServiceException();
            }
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
        Date evaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0))
                .getGradeAvailableDate();
        infoSiteEnrolmentEvaluation.setLastEvaluationDate(evaluationDate);
        infoSiteEnrolmentEvaluation.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoSiteEnrolmentEvaluation;
    }

    private List checkForInvalidSituations(List enrolmentEvaluations) throws ExistingServiceException,
            InvalidSituationServiceException {
        //			evaluations can only be confirmated if they are not already
        // confirmated
        List temporaryEnrolmentEvaluations = (List) CollectionUtils.select(enrolmentEvaluations,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(
                                EnrolmentEvaluationState.TEMPORARY_OBJ))
                            return true;
                        return false;
                    }
                });

        if (temporaryEnrolmentEvaluations == null || temporaryEnrolmentEvaluations.size() == 0) {
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
            if (enrolmentEvaluationsWithoutGrade.size() == temporaryEnrolmentEvaluations.size()) {
                throw new InvalidSituationServiceException();
            }
            temporaryEnrolmentEvaluations = (List) CollectionUtils.subtract(
                    temporaryEnrolmentEvaluations, enrolmentEvaluationsWithoutGrade);
        }

        return temporaryEnrolmentEvaluations;
    }
}