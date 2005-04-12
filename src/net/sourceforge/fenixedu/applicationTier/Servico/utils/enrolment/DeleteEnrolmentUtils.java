/*
 * Created on Jan 12, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author nmgo
 */
public abstract class DeleteEnrolmentUtils {

    /**
     * @param enrolmentDAO
     * @param enrolmentEvaluationDAO
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    public static void deleteEnrollment(IPersistentEnrollment enrolmentDAO,
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO, IEnrolment enrolment)
            throws ExcepcaoPersistencia {

        deleteEnrollmentEvaluations(enrolmentEvaluationDAO, enrolment);

        deleteAttend(enrolment);

        enrolmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());

    }

    /**
     * @param enrolmentEvaluationDAO
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    protected static void deleteEnrollmentEvaluations(IPersistentEnrolmentEvaluation enrolmentEvaluationDAO,
            IEnrolment enrolment) throws ExcepcaoPersistencia {
        if (enrolment.getEvaluations() != null) {
            Iterator iterator = enrolment.getEvaluations().iterator();
            while (iterator.hasNext()) {
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();

                enrolmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation
                        .getIdInternal());
            }
        }
    }

    /**
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    protected static void deleteAttend(IEnrolment enrolment) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse executionCourseDAO = persistentSuport.getIPersistentExecutionCourse();
        IFrequentaPersistente attendDAO = persistentSuport.getIFrequentaPersistente();
        IPersistentMark markDAO = persistentSuport.getIPersistentMark();
        IPersistentStudentGroupAttend studentGroupAttendDAO = persistentSuport
                .getIPersistentStudentGroupAttend();
        ITurnoAlunoPersistente shiftStudentDAO = persistentSuport.getITurnoAlunoPersistente();

        List executionCourses = executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(enrolment
                .getCurricularCourse(), enrolment.getExecutionPeriod());

        Iterator iterator = executionCourses.iterator();
        while (iterator.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterator.next();

            if (executionCourse != null) {
                IAttends attend = attendDAO.readByAlunoAndDisciplinaExecucao(enrolment
                        .getStudentCurricularPlan().getStudent(), executionCourse);

                if (attend != null) {
                    List marks = markDAO.readBy(attend);
                    if (marks == null || marks.isEmpty()) {
                        IStudentGroupAttend studentGroupAttend = studentGroupAttendDAO.readBy(attend);
                        if (studentGroupAttend == null) {
                            List shiftsStudentIsIn = shiftStudentDAO.readByStudent(enrolment
                                    .getStudentCurricularPlan().getStudent());

                            if (shiftsStudentIsIn == null || shiftsStudentIsIn.isEmpty()) {

                                attendDAO.deleteByOID(Attends.class, attend.getIdInternal());
                            } else {
                                attendDAO.simpleLockWrite(attend);
                                attend.setEnrolment(null);
                            }
                        } else {
                            attendDAO.simpleLockWrite(attend);
                            attend.setEnrolment(null);
                        }
                    } else {
                        attendDAO.simpleLockWrite(attend);
                        attend.setEnrolment(null);
                    }
                }
            }
        }
    }
}
