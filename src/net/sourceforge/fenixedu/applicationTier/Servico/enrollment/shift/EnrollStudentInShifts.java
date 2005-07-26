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
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
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

    public ShiftEnrollmentErrorReport run(Integer studentId, List shiftIdsToEnroll)
            throws StudentNotFoundServiceException, FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
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
                
                persistentStudent.simpleLockWrite(student);
                
                IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

                List<IShift> shifts = student.getShifts();
                List shiftEnrollments = new ArrayList();
                for (IShift shift : shifts) {
                    if (shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal().equals(
                            executionPeriod.getIdInternal()))
                        ;
                    shiftEnrollments.add(shift);
                }
                
                List shiftsToUnEnroll = new ArrayList();
                List filteredShiftsIdsToEnroll = new ArrayList();

                filteredShiftsIdsToEnroll = calculateShiftsToEnroll(shiftIdsToEnroll, shiftEnrollments);

                shiftsToUnEnroll = enrollStudentsInShiftsAndCalculateShiftsToUnEnroll(student,
                        filteredShiftsIdsToEnroll, errorReport, persistentShift);

                unEnrollStudentsInShifts(shiftsToUnEnroll, student);

            }

            return errorReport;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }

    private void unEnrollStudentsInShifts(List shiftsToUnEnroll, IStudent student) throws ExcepcaoPersistencia {
        student.getShifts().removeAll(shiftsToUnEnroll);
    
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
            ITurnoPersistente persistentShift)
            throws ExcepcaoPersistencia {
        List shiftsToUnEnroll = new ArrayList();
        Iterator iter = filteredShiftsIdsToEnroll.iterator();
        while (iter.hasNext()) {
            Integer shiftId = (Integer) iter.next();
            IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftId);
            if (shift == null) {
                errorReport.getUnExistingShifts().add(shiftId);
            } else {
                if (shift.getLotacao().intValue() > shift.getStudents().size()) {
                    List<IShift> shifts = student.getShifts();
                    for (IShift shift2 : shifts) {
                        if (shift2.getDisciplinaExecucao().equals(shift.getDisciplinaExecucao())
                                && shift2.getTipo().equals(shift.getTipo())) {
                            shiftsToUnEnroll.add(shift2);
                            break;
                        }

                    }
                    persistentShift.simpleLockWrite(shift);
                    shift.getStudents().add(student);
                } else {
                    errorReport.getUnAvailableShifts().add(InfoShift.newInfoFromDomain(shift));
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
                IShift shift = (IShift) iter.next();

                if (shiftIdsToEnroll.contains(shift.getIdInternal())) {
					shiftIdsToIgnore.add(shift.getIdInternal());
                }
            }

            return (List) CollectionUtils.subtract(shiftIdsToEnroll, shiftIdsToIgnore);
        }

        return shiftIdsToEnroll;
    }

}