/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear {

    @Atomic
    public static List<InfoShift> run(AcademicInterval academicInterval, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final CurricularYear curricularYear = FenixFramework.getDomainObject(infoCurricularYear.getExternalId());
        final List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        final List<ExecutionCourse> executionCourses =
                ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(academicInterval,
                        degreeCurricularPlan, curricularYear, "%");
        for (final ExecutionCourse executionCourse : executionCourses) {
            for (final Shift shift : executionCourse.getAssociatedShifts()) {
                final InfoShift infoShift = new InfoShift(shift);
                infoShifts.add(infoShift);
            }
        }

        return infoShifts;
    }
}