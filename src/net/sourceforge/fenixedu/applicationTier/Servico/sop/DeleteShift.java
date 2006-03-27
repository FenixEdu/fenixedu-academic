/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteShift extends Service {

    public Object run(InfoShift infoShift) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;

        if (infoShift != null) {
            deleteShift(infoShift.getIdInternal());
            result = true;
        }

        return Boolean.valueOf(result);
    }

    public static void deleteShift(final Integer shiftID) throws ExcepcaoPersistencia, FenixServiceException {
        Shift shift = (Shift) persistentObject.readByOID(Shift.class, shiftID);
        if (shift != null) {
            List studentShifts = shift.getStudents();
            if (studentShifts != null && studentShifts.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.students");
            }

            // if the shift has student groups associated it can't be
            // deleted
            List studentGroupShift = shift.getAssociatedStudentGroups();

            if (studentGroupShift.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.studentGroups");
            }

            // if the shift has summaries it can't be deleted            
            List summariesShift = shift.getAssociatedSummaries();
            if (summariesShift != null && summariesShift.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.summaries");
            }

            for (final List<ShiftProfessorship> shiftProfessorship = shift.getAssociatedShiftProfessorship();
                    !shiftProfessorship.isEmpty();
                    shiftProfessorship.get(0).delete());

            for (final List<Lesson> lessons = shift.getAssociatedLessons();
                    !lessons.isEmpty();
                    DeleteLessons.deleteLesson(persistentSupport, lessons.get(0)));

            for (final List<SchoolClass> schoolClasses = shift.getAssociatedClasses();
                    !schoolClasses.isEmpty(); shift.removeAssociatedClasses(schoolClasses.get(0)));

            shift.setDisciplinaExecucao(null);
            persistentObject.deleteByOID(Shift.class, shift.getIdInternal());
        }
    }

}