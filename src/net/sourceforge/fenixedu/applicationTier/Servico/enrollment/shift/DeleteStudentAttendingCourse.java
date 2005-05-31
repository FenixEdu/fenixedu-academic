package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quitério 11/Fev/2004
 *  
 */
public class DeleteStudentAttendingCourse implements IService {
    public class AlreadyEnrolledInGroupServiceException extends FenixServiceException {

    }

    public class AlreadyEnrolledServiceException extends FenixServiceException {

    }

    public class AlreadyEnrolledInShiftServiceException extends FenixServiceException {

    }

    public DeleteStudentAttendingCourse() {
    }

    public Boolean run(InfoStudent infoStudent, Integer executionCourseId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            if (infoStudent == null) {
                return new Boolean(false);
            }
            if (executionCourseId != null) {
                IStudent student = findStudent(infoStudent, persistentStudent);

                IExecutionCourse executionCourse = findExecutionCourse(executionCourseId,
                        persistentExecutionCourse);

                deleteAttend(executionCourse, student, sp);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
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

    private IStudent findStudent(InfoStudent infoStudent, IPersistentStudent persistentStudent)
            throws FenixServiceException, ExcepcaoPersistencia {
        IStudent student = (IStudent) persistentStudent.readByOID(Student.class, infoStudent
                .getIdInternal());
        if (student == null) {
            throw new FenixServiceException("noStudent");
        }
        return student;
    }

    private void deleteAttend(IExecutionCourse executionCourse, IStudent student, ISuportePersistente sp)
            throws FenixServiceException {
        try {
            IFrequentaPersistente persistentAttends = sp.getIFrequentaPersistente();
            ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
            IPersistentStudentGroupAttend studentGroupAttendDAO = sp.getIPersistentStudentGroupAttend();

            IAttends attend = persistentAttends.readByAlunoAndDisciplinaExecucao(student,
                    executionCourse);
            IStudentGroupAttend studentGroupAttend = studentGroupAttendDAO.readBy(attend);
            if (studentGroupAttend != null) {
                throw new AlreadyEnrolledInGroupServiceException();
            }

            if (attend != null) {
                if (attend.getEnrolment() != null) {
                    throw new AlreadyEnrolledServiceException();
                }

                List shiftAttendsToDelete = persistentShiftStudent.readByStudentAndExecutionCourse(
                        student.getIdInternal(), executionCourse.getIdInternal());

                if (shiftAttendsToDelete != null && shiftAttendsToDelete.size() > 0) {
                    throw new AlreadyEnrolledInShiftServiceException();
                }
                persistentAttends.delete(attend);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }
}