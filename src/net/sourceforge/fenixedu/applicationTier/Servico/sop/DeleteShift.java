/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class DeleteShift implements IServico {

    private static DeleteShift _servico = new DeleteShift();

    /**
     * The singleton access method of this class.
     */
    public static DeleteShift getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DeleteShift() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DeleteShift";
    }

    public Object run(InfoShift infoShift) throws FenixServiceException {

        boolean result = false;

        if (infoShift != null) {

            try {
                final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

                IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                        infoShift.getIdInternal());
                if (shift != null) {
                    //if the shift has students associated it can't be deleted
                    ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
                    List studentShifts = persistentShiftStudent.readByShift(shift);
                    if (studentShifts != null && studentShifts.size() > 0) {
                        throw new FenixServiceException("error.deleteShift.with.students");
                    }

                    //if the shift has student groups associated it can't be
                    // deleted
                    IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
                    List studentGroupShift = persistentStudentGroup.readAllStudentGroupByShift(shift);
                    for (int i = 0; i < studentGroupShift.size(); i++) {
                        IPersistentStudentGroupAttend persistentStudentGroupAttend = sp
                                .getIPersistentStudentGroupAttend();

                        List studentGroupAttendShift = persistentStudentGroupAttend
                                .readAllByStudentGroup((IStudentGroup) studentGroupShift.get(i));
                        if (studentGroupAttendShift != null && studentGroupAttendShift.size() > 0) {
                            throw new FenixServiceException("error.deleteShift.with.studentGroups");
                        }
                    }

                    //if the shift has summaries it can't be deleted
                    IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                    List summariesShift = persistentSummary.readByShift(shift.getDisciplinaExecucao(),
                            shift);
                    if (summariesShift != null && summariesShift.size() > 0) {
                        throw new FenixServiceException("error.deleteShift.with.summaries");
                    }

                    IPersistentShiftProfessorship persistentShiftProfessorship = sp
                            .getIPersistentShiftProfessorship();
                    List professorshipShift = persistentShiftProfessorship.readByShift(shift);
                    for (int i = 0; i < professorshipShift.size(); i++) {
                        persistentShiftProfessorship.delete((IShiftProfessorship) professorshipShift
                                .get(i));
                    }

                    for (int i = 0; i < shift.getAssociatedLessons().size(); i++) {
                        ILesson lesson = (ILesson) shift.getAssociatedLessons().get(i);
                        sp.getIPersistentRoomOccupation().delete(lesson.getRoomOccupation());
                        sp.getIAulaPersistente().delete(lesson);
                    }

                    for (int i = 0; i < shift.getAssociatedClasses().size(); i++) {
                        ISchoolClass schoolClass = (ISchoolClass) shift.getAssociatedClasses().get(i);
                        sp.getITurmaPersistente().simpleLockWrite(schoolClass);
                        schoolClass.getAssociatedShifts().remove(shift);
                    }

                    sp.getITurnoPersistente().delete(shift);

                    result = true;
                }
            } catch (ExcepcaoPersistencia ex) {
                throw new FenixServiceException("Error deleting shift");
            }
        }

        return new Boolean(result);

    }

}