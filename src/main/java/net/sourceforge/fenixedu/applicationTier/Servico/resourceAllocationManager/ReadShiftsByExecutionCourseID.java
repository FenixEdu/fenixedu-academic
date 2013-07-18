package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ReadShiftsByExecutionCourseIDAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

public class ReadShiftsByExecutionCourseID {

    protected InfoExecutionCourseOccupancy run(Integer executionCourseID) {

        final InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);
        final Set<Shift> shifts = executionCourse.getAssociatedShifts();

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

    // Service Invokers migrated from Berserk

    private static final ReadShiftsByExecutionCourseID serviceInstance = new ReadShiftsByExecutionCourseID();

    @Service
    public static InfoExecutionCourseOccupancy runReadShiftsByExecutionCourseID(Integer executionCourseID) throws NotAuthorizedException {
        ReadShiftsByExecutionCourseIDAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID);
    }

}