/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

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

import org.apache.log4j.Logger;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear {

    private static final Logger logger = Logger.getLogger(ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear.class);

    private static String logExternalId(DomainObject obj) {
        return obj == null ? "null" : obj.getExternalId();
    }

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List<InfoShift> run(AcademicInterval academicInterval, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) {

        final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(infoExecutionDegree.getExternalId());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final CurricularYear curricularYear = AbstractDomainObject.fromExternalId(infoCurricularYear.getExternalId());
        logger.warn(String.format("executionDegree %s degreeCurricularPlan %s curricularYear %s", logExternalId(executionDegree),
                logExternalId(degreeCurricularPlan), logExternalId(curricularYear)));
        final List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        final List<ExecutionCourse> executionCourses =
                ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(academicInterval,
                        degreeCurricularPlan, curricularYear, "%");
        logger.warn(String.format("filtering execution courses size : %s", executionCourses.size()));
        for (final ExecutionCourse executionCourse : executionCourses) {
            for (final Shift shift : executionCourse.getAssociatedShifts()) {
                final InfoShift infoShift = new InfoShift(shift);
                infoShifts.add(infoShift);
            }
        }

        logger.warn(String.format("infoShits size : %s", infoShifts.size()));
        return infoShifts;
    }
}