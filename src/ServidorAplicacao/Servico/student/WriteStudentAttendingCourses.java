package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.ExecutionCourse;
import Dominio.Attends;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IStudent;
import Dominio.IStudentGroupAttend;
import Dominio.ITurnoAluno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * Describe class <code>WriteStudentAttendingCourses</code> here.
 * 
 * @author <a href="mailto:tdi-dev@tagus.ist.utl.pt">tdi-dev:Edgar Gonçalves
 *         </a>
 * @author Fernanda Quitério
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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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

            //Read every course the student attends to:
            List attends = persistentAttend.readByStudentNumber(student.getNumber(), student.getDegreeType());

            List attendingCourses = getExecutionCoursesFromAttends(attends);

            //Gets the database objects for the wanted courses
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

            //Delete all courses the student is currently attending to that
            // he/she doesn't want to:
            //attendings to remove :
            List attendsToRemove = (List) CollectionUtils.subtract(attendingCourses,
                    wantedAttendingCourses);
            List attendingCoursesToAdd = (List) CollectionUtils.subtract(wantedAttendingCourses,
                    attendingCourses);
            if (attendsToRemove != null && !attendsToRemove.isEmpty()) {
                deleteAttends(attendsToRemove, student, sp, persistentAttend);
            }

            //Add new courses (without duplicates)
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
            IPersistentStudentGroupAttend studentGroupAttendDAO = sp.getIPersistentStudentGroupAttend();

            Iterator iterator = attendingCoursesToRemove.iterator();
            while (iterator.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iterator.next();
                IAttends attend = persistentAttends.readByAlunoAndDisciplinaExecucao(student,
                        executionCourse);
                IStudentGroupAttend studentGroupAttend = studentGroupAttendDAO.readBy(attend);

                if (studentGroupAttend != null) {
                    throw new AlreadyEnroledInGroupServiceException();
                }

                if (attend != null) {
                    if (attend.getEnrolment() != null) {
                        throw new AlreadyEnroledServiceException();
                    }

                    //NOTE: attends that are linked to enrollments are not
                    // deleted
                    List shiftAttendsToDelete = persistentShiftStudent.readByStudentAndExecutionCourse(
                            student, executionCourse);
                    if (shiftAttendsToDelete != null) {
                        Iterator iter = shiftAttendsToDelete.iterator();
                        while (iter.hasNext()) {
                            persistentShiftStudent.delete((ITurnoAluno) iter.next());
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