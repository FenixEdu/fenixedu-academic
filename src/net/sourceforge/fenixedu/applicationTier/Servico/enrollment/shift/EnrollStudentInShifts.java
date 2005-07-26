/*
 * Created on 12/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jmota
 */
public class EnrollStudentInShifts implements IService {

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    /**
     * @param studentId
     * @param shiftIdsToEnroll
     * @return
     * @throws StudentNotFoundServiceException
     * @throws FenixServiceException
     */
    public ShiftEnrollmentErrorReport run(Integer studentId, List shiftIdsToEnroll)
            throws StudentNotFoundServiceException, FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

            if (shiftIdsToEnroll != null && !shiftIdsToEnroll.isEmpty()) {
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentId);
                if (student == null) {
                    throw new StudentNotFoundServiceException();
                }

                if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
                    throw new FenixServiceException(
                            "error.exception.notAuthorized.student.warningTuition");
                }

                IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
                List shiftEnrollments = persistentShiftStudent.readByStudentAndExecutionPeriod(student
                        .getIdInternal(), executionPeriod.getIdInternal());

                List shiftsToUnEnroll = new ArrayList();
                List filteredShiftsIdsToEnroll = new ArrayList();

                filteredShiftsIdsToEnroll = calculateShiftsToEnroll(shiftIdsToEnroll, shiftEnrollments);

                shiftsToUnEnroll = enrollStudentsInShiftsAndCalculateShiftsToUnEnroll(student,
                        filteredShiftsIdsToEnroll, errorReport, persistentShift, persistentShiftStudent);

                shiftsToUnEnroll = calculateShiftsToUnEnroll(shiftIdsToEnroll, shiftEnrollments,
                        shiftsToUnEnroll);

                unEnrollStudentsInShifts(shiftsToUnEnroll, errorReport, persistentShiftStudent);

            }

            return errorReport;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param shiftsToUnEnroll
     * @param errors
     * @param persistentShiftStudent
     */
    private void unEnrollStudentsInShifts(List shiftsToUnEnroll, ShiftEnrollmentErrorReport errorReport,
            ITurnoAlunoPersistente persistentShiftStudent) throws ExcepcaoPersistencia {
        Iterator iter = shiftsToUnEnroll.iterator();
        while (iter.hasNext()) {
            IShiftStudent shiftStudent = (IShiftStudent) iter.next();
            shiftStudent.getStudent().getShiftStudents().remove(shiftStudent);
            shiftStudent.getShift().getStudentShifts().remove(shiftStudent);
            shiftStudent.setShift(null);
            shiftStudent.setStudent(null);
            persistentShiftStudent.deleteByOID(ShiftStudent.class, shiftStudent.getIdInternal());
        }
    }

    /**
     * @param student
     * @param filteredShiftsIdsToEnroll
     * @param errors
     * @param persistentShift
     * @param persistentShiftStudent
     * @throws ExcepcaoPersistencia
     */
    private List enrollStudentsInShiftsAndCalculateShiftsToUnEnroll(IStudent student,
            List filteredShiftsIdsToEnroll, ShiftEnrollmentErrorReport errorReport,
            ITurnoPersistente persistentShift, ITurnoAlunoPersistente persistentShiftStudent)
            throws ExcepcaoPersistencia {
        List shiftsToUnEnroll = new ArrayList();
        Iterator iter = filteredShiftsIdsToEnroll.iterator();
        while (iter.hasNext()) {
            Integer shiftId = (Integer) iter.next();
            IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftId);
            if (shift == null) {
                errorReport.getUnExistingShifts().add(shiftId);
            } else {
                IShiftStudent shiftStudentToDelete = persistentShiftStudent
                        .readByStudentAndExecutionCourseAndLessonType(student.getIdInternal(), shift
                                .getDisciplinaExecucao().getIdInternal(), shift.getTipo());

                if (shift.getLotacao().intValue() > persistentShiftStudent
                        .readNumberOfStudentsByShift(shift.getIdInternal())) {
                    if (shiftStudentToDelete != null) {
                        shiftsToUnEnroll.add(shiftStudentToDelete);
                    }
                    IShiftStudent shiftStudentToWrite = new ShiftStudent();
                    persistentShiftStudent.simpleLockWrite(shiftStudentToWrite);
                    shiftStudentToWrite.setShift(shift);
                    shiftStudentToWrite.setStudent(student);
                } else {
                    errorReport.getUnAvailableShifts().add(InfoShift.newInfoFromDomain(shift));
                    // copyShift2InfoShift(shift));
                }

            }

        }
        return shiftsToUnEnroll;
    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     * @param filteredShiftsIdsToEnroll
     */
    private List calculateShiftsToEnroll(List shiftIdsToEnroll, List shiftEnrollments) {

        if (shiftEnrollments != null) {
            List shiftIdsToIgnore = new ArrayList();
            Iterator iter = shiftEnrollments.iterator();
            while (iter.hasNext()) {
                IShiftStudent shiftStudent = (IShiftStudent) iter.next();

                if (shiftIdsToEnroll.contains(shiftStudent.getShift().getIdInternal())) {
                    shiftIdsToIgnore.add(shiftStudent.getShift().getIdInternal());
                }
            }

            return (List) CollectionUtils.subtract(shiftIdsToEnroll, shiftIdsToIgnore);
        }

        return shiftIdsToEnroll;
    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     */
    private List calculateShiftsToUnEnroll(List shiftIdsToEnroll, List shiftEnrollments,
            List shiftsToUnEnroll) {
        return shiftsToUnEnroll;
    }

}
