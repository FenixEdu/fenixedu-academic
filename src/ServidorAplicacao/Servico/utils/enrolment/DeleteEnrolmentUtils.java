/*
 * Created on Jan 12, 2005
 */
package ServidorAplicacao.Servico.utils.enrolment;

import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Attends;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IStudentGroupAttend;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO, IEnrollment enrolment)
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
            IEnrollment enrolment) throws ExcepcaoPersistencia {
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
    protected static void deleteAttend(IEnrollment enrolment) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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
