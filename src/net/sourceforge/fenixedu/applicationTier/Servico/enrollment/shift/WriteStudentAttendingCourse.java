package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quitério 11/Fev/2004
 *  
 */
public class WriteStudentAttendingCourse implements IService {
    public class ReachedAttendsLimitServiceException extends FenixServiceException {

    }

    public WriteStudentAttendingCourse() {
    }

    public Boolean run(InfoStudent infoStudent, Integer executionCourseId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            IPersistentStudent studentDAO = sp.getIPersistentStudent();
            IStudent student = (IStudent) studentDAO.readByOID(Student.class, infoStudent
                    .getIdInternal());
            infoStudent.setNumber(student.getNumber());

            if (infoStudent == null) {
                return new Boolean(false);
            }

            if (executionCourseId != null) {
                IStudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(infoStudent,
                        persistentStudentCurricularPlan);

                IExecutionCourse executionCourse = findExecutionCourse(executionCourseId,
                        persistentExecutionCourse);

                //Read every course the student attends to:
                List attends = persistentAttend
                        .readByStudentNumberInCurrentExecutionPeriod(studentCurricularPlan.getStudent()
                                .getNumber());

                if (attends != null && attends.size() >= 8) {
                    throw new ReachedAttendsLimitServiceException();
                }

                IAttends attendsEntry = persistentAttend.readByAlunoAndDisciplinaExecucao(
                        studentCurricularPlan.getStudent(), executionCourse);

                if (attendsEntry == null) {
                    attendsEntry = new Attends();
                    persistentAttend.simpleLockWrite(attendsEntry);
                    attendsEntry.setAluno(studentCurricularPlan.getStudent());
                    attendsEntry.setDisciplinaExecucao(executionCourse);

                    findEnrollmentForAttend(persistentEnrolment, studentCurricularPlan, executionCourse,
                            attendsEntry);

                }
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }

    private void findEnrollmentForAttend(IPersistentEnrollment persistentEnrolment,
            IStudentCurricularPlan studentCurricularPlan, IExecutionCourse executionCourse,
            IAttends attendsEntry) throws ExcepcaoPersistencia {
        // checks if there is an enrollment for this attend
        Iterator iterCurricularCourses = executionCourse.getAssociatedCurricularCourses().iterator();
        while (iterCurricularCourses.hasNext()) {
            ICurricularCourse curricularCourseElem = (ICurricularCourse) iterCurricularCourses.next();

            IEnrollment enrollment = persistentEnrolment
                    .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                            studentCurricularPlan, curricularCourseElem, executionCourse
                                    .getExecutionPeriod());
            if (enrollment != null) {
                attendsEntry.setEnrolment(enrollment);
                break;
            }
        }
    }

    private IExecutionCourse findExecutionCourse(Integer executionCourseId,
            IPersistentExecutionCourse persistentExecutionCourse) throws FenixServiceException,
            ExcepcaoPersistencia {
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);

        if (executionCourse == null) {
            throw new FenixServiceException("noExecutionCourse");
        }
        return executionCourse;
    }

    private IStudentCurricularPlan findStudentCurricularPlan(InfoStudent infoStudent,
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan)
            throws FenixServiceException {
        try {

            IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(),
                            TipoCurso.LICENCIATURA_OBJ);

            if (studentCurricularPlan == null) {

                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(),
                                TipoCurso.MESTRADO_OBJ);

            }
            if (studentCurricularPlan == null) {

                throw new FenixServiceException("noStudent");

            }

            return studentCurricularPlan;
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException();
        }
    }
}