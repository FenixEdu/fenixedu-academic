package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 *  
 */
public class SelectExecutionShiftsWithAssociatedLessonsAndClasses implements IServico {

    private static SelectExecutionShiftsWithAssociatedLessonsAndClasses _servico = new SelectExecutionShiftsWithAssociatedLessonsAndClasses();

    /**
     * The actor of this class.
     */

    private SelectExecutionShiftsWithAssociatedLessonsAndClasses() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SelectExecutionShiftsWithAssociatedLessonsAndClasses";
    }

    /**
     * Returns the _servico.
     * 
     * @return SelectShifts
     */
    public static SelectExecutionShiftsWithAssociatedLessonsAndClasses getService() {
        return _servico;
    }

    public Object run(InfoExecutionCourse infoExecutionCourse) {

        List shiftsWithAssociatedClassesAndLessons = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse disciplinaExecucao = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            List shifts = sp.getITurnoPersistente().readByExecutionCourse(disciplinaExecucao);

            if (shifts == null || shifts.isEmpty()) {

            } else {

                for (int i = 0; i < shifts.size(); i++) {
                    IShift shift = (IShift) shifts.get(i);
                    InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons = new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
							InfoShift.newInfoFromDomain(shift), null, null);

                    //List lessons =
                    // sp.getITurnoAulaPersistente().readByShift(shift);
                    List lessons = shift.getAssociatedLessons();
                    List infoLessons = new ArrayList();
                    List classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
                    List infoClasses = new ArrayList();

                    for (int j = 0; j < lessons.size(); j++) {
                        InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                        InfoLesson infoLesson = Cloner.copyILesson2InfoLesson((ILesson) lessons.get(j));
                        infoLesson.setInfoShift(infoShift);
                        infoLessons.add(infoLesson);
                    }
                    shiftWithAssociatedClassesAndLessons.setInfoLessons(infoLessons);

                    for (int j = 0; j < classesShifts.size(); j++)
                        infoClasses.add(Cloner.copyClass2InfoClass(((ISchoolClassShift) classesShifts.get(j))
                                .getTurma()));

                    shiftWithAssociatedClassesAndLessons.setInfoClasses(infoClasses);

                    shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
                }
            }

        } catch (ExcepcaoPersistencia e) {
        }
        return shiftsWithAssociatedClassesAndLessons;
    }

}