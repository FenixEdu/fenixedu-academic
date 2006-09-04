package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadShiftsByExecutionCourseID extends Service {

    public InfoExecutionCourseOccupancy run(Integer executionCourseID) throws ExcepcaoPersistencia {
        final InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        final List<Shift> shifts = executionCourse.getAssociatedShifts();

        infoExecutionCourseOccupancy.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));

        for (final Shift shift : shifts) {
            Integer capacity = Integer.valueOf(1);
            if (shift.getLotacao() != null && shift.getLotacao().intValue() != 0) {
                capacity = shift.getLotacao();
            }

            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            infoExecutionCourseOccupancy.getInfoShifts().add(infoShift);
        }

        return infoExecutionCourseOccupancy;
    }

}
