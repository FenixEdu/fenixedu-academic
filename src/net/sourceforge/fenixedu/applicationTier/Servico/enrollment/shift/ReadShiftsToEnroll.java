/**
 * Aug 4, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoNewShiftEnrollment;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadShiftsToEnroll implements IService {

    public List run(Integer studentNumber) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        Student student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                DegreeType.DEGREE);
        if (student == null) {
            student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                    DegreeType.MASTER_DEGREE);
        }
        if (student == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
        	if(student.getInterruptedStudies().equals(Boolean.FALSE))
        		throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        List attendList = frequentaPersistente
                .readByStudentNumberInCurrentExecutionPeriod(studentNumber);

        List infoNewShiftEnrollments = new ArrayList();
        for (Iterator iter = attendList.iterator(); iter.hasNext();) {
            InfoNewShiftEnrollment infoNewShiftEnrollment = new InfoNewShiftEnrollment();
            Attends attends = (Attends) iter.next();
            setShifts(infoNewShiftEnrollment, attends.getDisciplinaExecucao(), student);
            infoNewShiftEnrollment.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(attends
                    .getDisciplinaExecucao()));
            if (attends.getEnrolment() != null) {
                infoNewShiftEnrollment.setEnrolled(true);
            }
            infoNewShiftEnrollments.add(infoNewShiftEnrollment);
        }
        return infoNewShiftEnrollments;
    }

    /**
     * @param infoNewShiftEnrollment
     * @param student
     * @param disciplinaExecucao
     * @param student
     */
    private void setShifts(InfoNewShiftEnrollment infoNewShiftEnrollment,
            ExecutionCourse executionCourse, Student student) {
        for (Iterator iter = executionCourse.getAssociatedShifts().iterator(); iter.hasNext();) {
            Shift shift = (Shift) iter.next();
            if (shift.getTipo().equals(ShiftType.TEORICA)) {
                infoNewShiftEnrollment.setTheoricType(ShiftType.TEORICA);
            } else if (shift.getTipo().equals(ShiftType.PRATICA)) {
                infoNewShiftEnrollment.setPraticType(ShiftType.PRATICA);
            } else if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
                infoNewShiftEnrollment.setLaboratoryType(ShiftType.LABORATORIAL);
            } else if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
                infoNewShiftEnrollment.setTheoricoPraticType(ShiftType.TEORICO_PRATICA);
            }
        }
        for (Iterator iter = student.getShifts().iterator(); iter.hasNext();) {
            Shift shift = (Shift) iter.next();
            if (shift.getDisciplinaExecucao().equals(executionCourse)
                    && shift.getTipo().equals(ShiftType.TEORICA)) {
                infoNewShiftEnrollment.setTheoricShift(InfoShift.newInfoFromDomain(shift));
            } else if (shift.getDisciplinaExecucao().equals(executionCourse)
                    && shift.getTipo().equals(ShiftType.PRATICA)) {
                infoNewShiftEnrollment.setPraticShift(InfoShift.newInfoFromDomain(shift));
            } else if (shift.getDisciplinaExecucao().equals(executionCourse)
                    && shift.getTipo().equals(ShiftType.LABORATORIAL)) {
                infoNewShiftEnrollment.setLaboratoryShift(InfoShift.newInfoFromDomain(shift));
            } else if (shift.getDisciplinaExecucao().equals(executionCourse)
                    && shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
                infoNewShiftEnrollment.setTheoricoPraticShift(InfoShift.newInfoFromDomain(shift));
            }
        }

    }

}
