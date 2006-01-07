package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.NumberUtils;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadShiftsByExecutionCourseID implements IService {

    public InfoExecutionCourseOccupancy run(Integer executionCourseID) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        final ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, executionCourseID);
        final List<Shift> shifts = executionCourse.getAssociatedShifts();

        infoExecutionCourseOccupancy.setInfoExecutionCourse(InfoExecutionCourse
                .newInfoFromDomain(executionCourse));

        for (final Shift shift : shifts) {
            Integer capacity = Integer.valueOf(1);
            if (shift.getLotacao() != null && shift.getLotacao().intValue() != 0) {
                capacity = shift.getLotacao();
            }

            final InfoShift infoShift = InfoShiftWithInfoLessons.newInfoFromDomain(shift);
            infoShift.setOcupation(new Integer(shift.getStudentsCount()));
            infoShift.setPercentage(NumberUtils.formatNumber(new Double(infoShift.getOcupation()
                    .floatValue()
                    * 100 / capacity.floatValue()), 1));

            infoExecutionCourseOccupancy.getInfoShifts().add(infoShift);
        }
        infoExecutionCourseOccupancy.getInfoExecutionCourse().setNumberOfAttendingStudents(
                executionCourse.getAttendsCount());

        return infoExecutionCourseOccupancy;
    }

}