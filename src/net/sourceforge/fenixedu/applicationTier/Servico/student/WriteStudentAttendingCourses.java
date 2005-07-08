package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * Describe class <code>WriteStudentAttendingCourses</code> here.
 * 
 * @author <a href="mailto:tdi-dev@tagus.ist.utl.pt">tdi-dev:Edgar Gon�alves
 *         </a>
 * @author Fernanda Quit�rio
 * @version 1.0
 */
public class WriteStudentAttendingCourses implements IService {
    public class AlreadyEnroledInGroupServiceException extends FenixServiceException {
    }

    public class AlreadyEnroledServiceException extends FenixServiceException {
    }

    public WriteStudentAttendingCourses() {
    }

    /**
     * Describe <code>run</code> method here.
     * 
     * @param infoStudent
     *            an <code>InfoStudent</code>,
     * @param infoExecutionCourses
     *            a <code>List</code> with the wanted
     *            executionCourse.idInternal's of the ATTEND table.
     * @return a <code>Boolean</code> to indicate if all went fine.
     * @exception FenixServiceException
     *                if an error occurs.
     * @exception AlreadyEnroledInGroupServiceException
     *                if student is enrolled in any of the executionCourses
     *                groups.
     * @exception AlreadyEnroledServiceException
     *                if student is enrolled in any of the executionCourses
     */
    public Boolean run(InfoStudent infoStudent, List infoExecutionCourseIds)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            if (infoStudent == null) {
                return new Boolean(false);
            }
            if (infoExecutionCourseIds == null) {
                infoExecutionCourseIds = new ArrayList();
            }

            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(infoStudent
                    .getNumber(), infoStudent.getDegreeType());

            // Read every course the student attends to:
            List attends = persistentAttend.readByStudentNumber(student.getNumber(), student
                    .getDegreeType());

            List attendingCourses = getExecutionCoursesFromAttends(attends);

            // Gets the database objects for the wanted courses
            List wantedAttendingCourses = new ArrayList();
            Iterator i = infoExecutionCourseIds.iterator();
            while (i.hasNext()) {

                IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, (Integer) i.next());

                if (executionCourse == null) {

                    throw new FenixServiceException("nullExecutionCourseId");
                }
                wantedAttendingCourses.add(executionCourse);

            }

            // Delete all courses the student is currently attending to that
            // he/she doesn't want to:
            // attendings to remove :
            List attendsToRemove = (List) CollectionUtils.subtract(attendingCourses,
                    wantedAttendingCourses);
            List attendingCoursesToAdd = (List) CollectionUtils.subtract(wantedAttendingCourses,
                    attendingCourses);
            if (attendsToRemove != null && !attendsToRemove.isEmpty()) {
                deleteAttends(attendsToRemove, student, sp, persistentAttend);
            }

            // Add new courses (without duplicates)
            i = attendingCoursesToAdd.iterator();
            while (i.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) i.next();

                IAttends attendsEntry = persistentAttend.readByAlunoAndDisciplinaExecucao(student,
                        executionCourse);
                if (attendsEntry == null) {
                    attendsEntry = new Attends();
                    persistentAttend.simpleLockWrite(attendsEntry);
                    attendsEntry.setAluno(student);
                    attendsEntry.setDisciplinaExecucao(executionCourse);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }

    /**
     * @param attends
     * @return
     */
    private List getExecutionCoursesFromAttends(List attends) {
        List executionCourses = new ArrayList();
        Iterator iter = attends.iterator();
        while (iter.hasNext()) {
            IAttends attend = (IAttends) iter.next();
            executionCourses.add(attend.getDisciplinaExecucao());
        }
        return executionCourses;
    }

    private void deleteAttends(List attendingCoursesToRemove, IStudent student, ISuportePersistente sp,
            IFrequentaPersistente persistentAttends) throws FenixServiceException {
        try {
            ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();

            Iterator iterator = attendingCoursesToRemove.iterator();
            while (iterator.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iterator.next();
                IAttends attend = persistentAttends.readByAlunoAndDisciplinaExecucao(student,
                        executionCourse);
                List<IStudentGroupAttend> studentGroupAttends = attend.getStudentGroupAttends();
                IStudentGroupAttend studentGroupAttend = (studentGroupAttends.isEmpty()) ? null
                        : studentGroupAttends.get(0);

                if (studentGroupAttend != null) {
                    throw new AlreadyEnroledInGroupServiceException();
                }

                if (attend != null) {
                    if (attend.getEnrolment() != null) {
                        throw new AlreadyEnroledServiceException();
                    }

                    // NOTE: attends that are linked to enrollments are not
                    // deleted
                    List shiftAttendsToDelete = persistentShiftStudent.readByStudentAndExecutionCourse(
                            student.getIdInternal(), executionCourse.getIdInternal());
                    if (shiftAttendsToDelete != null) {
                        Iterator iter = shiftAttendsToDelete.iterator();
                        while (iter.hasNext()) {
                            // persistentShiftStudent.delete((IShiftStudent)
                            // iter.next());
                            IShiftStudent shiftStudent = (IShiftStudent) iter.next();
                            shiftStudent.getStudent().getShiftStudents().remove(shiftStudent);
                            shiftStudent.getShift().getStudentShifts().remove(shiftStudent);
                            shiftStudent.setShift(null);
                            shiftStudent.setStudent(null);
                            persistentShiftStudent.deleteByOID(ShiftStudent.class, shiftStudent
                                    .getIdInternal());
                        }
                    }
                    persistentAttends.delete(attend);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}