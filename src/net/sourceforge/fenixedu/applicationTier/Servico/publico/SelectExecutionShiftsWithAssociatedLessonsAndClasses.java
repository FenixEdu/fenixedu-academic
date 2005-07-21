package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 */
public class SelectExecutionShiftsWithAssociatedLessonsAndClasses implements IService {

    public Object run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final List<IShift> shifts = sp.getITurnoPersistente().readByExecutionCourse(
                infoExecutionCourse.getIdInternal());
        if (shifts == null || shifts.isEmpty()) {
            return null;
        }

        List<InfoShiftWithAssociatedInfoClassesAndInfoLessons> shiftsWithAssociatedClassesAndLessons = new ArrayList<InfoShiftWithAssociatedInfoClassesAndInfoLessons>();
        for (IShift shift : shifts) {
            InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons = new InfoShiftWithAssociatedInfoClassesAndInfoLessons();

            // InfoShift
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            shiftWithAssociatedClassesAndLessons.setInfoShift(infoShift);

            // InfoClasses
            List<ISchoolClassShift> classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(
                    shift.getIdInternal());
            List<InfoClass> infoClasses = new ArrayList<InfoClass>();
            for (ISchoolClassShift schoolClassShift : classesShifts) {
                infoClasses.add(InfoClass.newInfoFromDomain(schoolClassShift.getTurma()));
            }
            shiftWithAssociatedClassesAndLessons.setInfoClasses(infoClasses);

            // InfoLessons
            List<ILesson> lessons = shift.getAssociatedLessons();
            List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
            for (ILesson lesson : lessons) {
                InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod
                        .newInfoFromDomain(lesson);
                infoLesson.setInfoShift(infoShift);
                infoLessons.add(infoLesson);
            }
            shiftWithAssociatedClassesAndLessons.setInfoLessons(infoLessons);

            shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
        }

        return shiftsWithAssociatedClassesAndLessons;
    }

}
