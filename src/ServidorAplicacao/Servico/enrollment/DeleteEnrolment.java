package ServidorAplicacao.Servico.enrollment;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudentGroupAttend;
import Dominio.precedences.IRestrictionByCurricularCourse;
import Dominio.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentRestrictionByCurricularCourse;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos Jan 26, 2004
 */
public class DeleteEnrolment implements IService {
    public DeleteEnrolment() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(Integer executionDegreeId, Integer studentCurricularPlanId, Integer enrolmentID)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistentSuport
                    .getIPersistentEnrolmentEvaluation();
            IPersistentRestrictionByCurricularCourse persistentRestriction = persistentSuport
                    .getIPersistentRestrictionByCurricularCourse();
            IEnrollment enrollment = (IEnrollment) enrolmentDAO.readByOID(Enrolment.class, enrolmentID);

            if (enrollment != null) {
                List restrictions = persistentRestriction.readByCurricularCourseAndRestrictionClass(
                        enrollment.getCurricularCourse(),

                        RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class);
                if (restrictions != null) {
                    Iterator iter = restrictions.iterator();
                    while (iter.hasNext()) {
                        final IRestrictionByCurricularCourse restriction = (RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) iter
                                .next();
                        if (enrollment.getStudentCurricularPlan().isCurricularCourseEnrolled(
                                restriction.getPrecedence().getCurricularCourse())) {
                            IEnrollment enrollment2Delete = (IEnrollment) CollectionUtils.find(
                                    enrollment.getStudentCurricularPlan()
                                            .getAllStudentEnrolledEnrollmentsInExecutionPeriod(
                                                    enrollment.getExecutionPeriod()), new Predicate() {

                                        public boolean evaluate(Object arg0) {
                                            IEnrollment enrollment = (IEnrollment) arg0;
                                            return enrollment.getCurricularCourse().equals(
                                                    restriction.getPrecedence().getCurricularCourse());
                                        }
                                    });
                            deleteEnrollment(enrolmentDAO, enrolmentEvaluationDAO, enrollment2Delete);
                        }
                    }
                }
                deleteEnrollment(enrolmentDAO, enrolmentEvaluationDAO, enrollment);
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

    /**
     * @param enrolmentDAO
     * @param enrolmentEvaluationDAO
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    protected void deleteEnrollment(IPersistentEnrollment enrolmentDAO,
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
    protected void deleteEnrollmentEvaluations(IPersistentEnrolmentEvaluation enrolmentEvaluationDAO,
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
    public static void deleteAttend(IEnrollment enrolment) throws ExcepcaoPersistencia {
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
                IFrequenta attend = attendDAO.readByAlunoAndDisciplinaExecucao(enrolment
                        .getStudentCurricularPlan().getStudent(), executionCourse);

                if (attend != null) {
                    List marks = markDAO.readBy(attend);
                    if (marks == null || marks.isEmpty()) {
                        IStudentGroupAttend studentGroupAttend = studentGroupAttendDAO.readBy(attend);
                        if (studentGroupAttend == null) {
                            List shiftsStudentIsIn = shiftStudentDAO.readByStudent(enrolment
                                    .getStudentCurricularPlan().getStudent());

                            if (shiftsStudentIsIn == null || shiftsStudentIsIn.isEmpty()) {

                                attendDAO.deleteByOID(Frequenta.class, attend.getIdInternal());
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