/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment.DeleteEnrolmentUtils;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 */
public class ImprovmentUnEnrollService implements IService {

    public Object run(Integer studentNumber, List enrolmentsIds) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                .getIPersistentEnrolmentEvaluation();

        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                DegreeType.DEGREE);
        if (student == null) {
            throw new InvalidArgumentsServiceException();
        }

        Iterator iterator = enrolmentsIds.iterator();
        while (iterator.hasNext()) {
            Integer enrolmentId = (Integer) iterator.next();
            IEnrolment enrollment = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class,
                    enrolmentId);
            if (enrollment == null) {
                throw new InvalidArgumentsServiceException();
            }
            IEnrolmentEvaluation improvmentEnrolmentEvaluation = (IEnrolmentEvaluation) CollectionUtils
                    .find(enrollment.getEvaluations(), new Predicate() {

                        public boolean evaluate(Object arg0) {
                            IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                                    EnrolmentEvaluationType.IMPROVEMENT)
                                    && enrolmentEvaluation.getEnrolmentEvaluationState().equals(
                                            EnrolmentEvaluationState.TEMPORARY_OBJ))
                                return true;
                            return false;
                        }

                    });

            if (improvmentEnrolmentEvaluation != null) {
				IPersistentEnrolmentEvaluation persistentEvaluation = sp.getIPersistentEnrolmentEvaluation();
				DeleteEnrolmentUtils.deleteEnrolmentEvaluation(improvmentEnrolmentEvaluation, persistentEvaluation);
            }

            setAttend(sp, enrollment, student);
        }

        return new Boolean(true);

    }

    /**
     * @param sp
     * @param enrollment
     * @param student
     */
    private void setAttend(ISuportePersistente sp, IEnrolment enrollment, final IStudent student)
            throws ExcepcaoPersistencia {

        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        final IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod
                .readActualExecutionPeriod();

        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();

        List executionCourses = enrollment.getCurricularCourse().getAssociatedExecutionCourses();
        IExecutionCourse currentExecutionCourse = (IExecutionCourse) CollectionUtils.find(
                executionCourses, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                        if (executionCourse.getExecutionPeriod().equals(currentExecutionPeriod))
                            return true;
                        return false;
                    }

                });

        if (currentExecutionCourse != null) {
            List attends = currentExecutionCourse.getAttends();
            IAttends attend = (IAttends) CollectionUtils.find(attends, new Predicate() {

                public boolean evaluate(Object arg0) {
                    IAttends frequenta = (IAttends) arg0;
                    if (frequenta.getAluno().equals(student))
                        return true;
                    return false;
                }

            });
            if (attend != null) {
                frequentaPersistente.simpleLockWrite(attend);
                attend.setEnrolment(null);
            }
        }

    }

}
