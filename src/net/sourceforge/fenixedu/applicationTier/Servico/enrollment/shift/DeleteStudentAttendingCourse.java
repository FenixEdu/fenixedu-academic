package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quit�rio 11/Fev/2004
 *  
 */
public class DeleteStudentAttendingCourse implements IService {

    public class AlreadyEnrolledInGroupServiceException extends FenixServiceException {
    }

    public class AlreadyEnrolledServiceException extends FenixServiceException {
    }

    public class AlreadyEnrolledInShiftServiceException extends FenixServiceException {
    }

    public Boolean run(InfoStudent infoStudent, Integer executionCourseID) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoStudent == null) {
            return Boolean.FALSE;
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IStudent student = readStudent(persistentSupport, infoStudent);
        final IExecutionCourse executionCourse = readExecutionCourse(persistentSupport,
                executionCourseID);

        deleteAttend(persistentSupport, student, executionCourse);

        return Boolean.TRUE;
    }

    private IExecutionCourse readExecutionCourse(final ISuportePersistente persistentSupport,
            final Integer executionCourseID) throws ExcepcaoPersistencia, FenixServiceException {

        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        return executionCourse;
    }

    private IStudent readStudent(final ISuportePersistente persistentSupport,
            final InfoStudent infoStudent) throws FenixServiceException, ExcepcaoPersistencia {

        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        final IStudent student = (IStudent) persistentStudent.readByOID(Student.class, infoStudent
                .getIdInternal());
        if (student == null) {
            throw new FenixServiceException("error.exception.noStudents");
        }
        return student;
    }

    private void deleteAttend(final ISuportePersistente persistentSupport, final IStudent student,
            final IExecutionCourse executionCourse) throws FenixServiceException, ExcepcaoPersistencia {

        final IFrequentaPersistente persistentAttends = persistentSupport.getIFrequentaPersistente();
        final IAttends attend = persistentAttends.readByAlunoAndDisciplinaExecucao(student
                .getIdInternal(), executionCourse.getIdInternal());

        if (attend != null) {
            checkIfHasStudentGroups(attend);
            checkIfIsEnrolled(attend);
            checkStudentShifts(student, executionCourse);

            persistentAttends.deleteByOID(Attends.class, attend.getIdInternal());
        }
    }

    private void checkStudentShifts(final IStudent student, final IExecutionCourse executionCourse)
            throws AlreadyEnrolledInShiftServiceException {
        for (final IShift shift : student.getShifts()) {
            if (shift.getDisciplinaExecucao() == executionCourse) {
                throw new AlreadyEnrolledInShiftServiceException();
            }
        }
    }

    private void checkIfIsEnrolled(final IAttends attend) throws AlreadyEnrolledInGroupServiceException {
        if (attend.getEnrolment() != null) {
            throw new AlreadyEnrolledInGroupServiceException();
        }
    }

    private void checkIfHasStudentGroups(final IAttends attend)
            throws AlreadyEnrolledInGroupServiceException {
        if (attend.getStudentGroupsCount() > 0) {
            throw new AlreadyEnrolledInGroupServiceException();
        }
    }
}