/*
 * 
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import Dominio.ILesson;
import Dominio.IShiftProfessorship;
import Dominio.IStudentGroup;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.Shift;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteShifts implements IServico {

    private static DeleteShifts _servico = new DeleteShifts();

    /**
     * The singleton access method of this class.
     */
    public static DeleteShifts getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DeleteShifts() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DeleteShifts";
    }

    public Object run(List shiftOIDs) throws FenixServiceException {

        boolean result = false;

        for (int j = 0; j < shiftOIDs.size(); j++) {
            Integer shiftOID = (Integer) shiftOIDs.get(j);

            try {
                final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

                IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftOID);

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