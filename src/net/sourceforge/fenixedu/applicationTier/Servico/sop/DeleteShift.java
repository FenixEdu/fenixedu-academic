/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteShift implements IService {

    public Object run(InfoShift infoShift) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;

        if (infoShift != null) {

            final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                    infoShift.getIdInternal());
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
                IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                List summariesShift = persistentSummary.readByShift(shift.getDisciplinaExecucao()
                        .getIdInternal(), shift.getIdInternal());
                if (summariesShift != null && summariesShift.size() > 0) {
                    throw new FenixServiceException("error.deleteShift.with.summaries");
                }
                IPersistentShiftProfessorship persistentShiftProfessorship = sp
                        .getIPersistentShiftProfessorship();
                List professorshipShift = persistentShiftProfessorship.readByShift(shift);
                for (int i = 0; i < professorshipShift.size(); i++) {
                    persistentShiftProfessorship.delete((IShiftProfessorship) professorshipShift.get(i));
                }
                for (int i = 0; i < shift.getAssociatedLessons().size(); i++) {
                    ILesson lesson = shift.getAssociatedLessons().get(i);

                    DeleteLessons.deleteLesson(sp, lesson.getIdInternal());
                }
                for (int i = 0; i < shift.getAssociatedClasses().size(); i++) {
                    ISchoolClass schoolClass = shift.getAssociatedClasses().get(i);
                    schoolClass.getAssociatedShifts().remove(shift);
                }

                shift.setDisciplinaExecucao(null);
                sp.getITurnoPersistente().deleteByOID(Shift.class, shift.getIdInternal());
                result = true;
            }

        }
        return new Boolean(result);
    }
}