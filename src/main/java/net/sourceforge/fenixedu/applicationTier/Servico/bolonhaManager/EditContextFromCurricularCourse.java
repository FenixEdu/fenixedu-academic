package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditContextFromCurricularCourse {

    @Atomic
    public static void run(CurricularCourse curricularCourse, Context context, CourseGroup courseGroup, Integer year,
            Integer semester, String beginExecutionPeriodID, String endExecutionPeriodID) {

        final CurricularPeriod degreeCurricularPeriod =
                context.getParentCourseGroup().getParentDegreeCurricularPlan().getDegreeStructure();

        // ********************************************************
        /*
         * TODO: Important - change this code (must be generic to support
         * several curricularPeriodInfoDTOs, instead of year and semester)
         */
        CurricularPeriod curricularPeriod = null;
        CurricularPeriodInfoDTO curricularPeriodInfoYear = null;
        if (courseGroup.getParentDegreeCurricularPlan().getDegree().getDegreeType().getYears() > 1) {
            curricularPeriodInfoYear = new CurricularPeriodInfoDTO(year, AcademicPeriod.YEAR);
        }
        final CurricularPeriodInfoDTO curricularPeriodInfoSemester =
                new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER);

        if (curricularPeriodInfoYear != null) {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod =
                        degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            }
        } else {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoSemester);
            }
        }

        // ********************************************************

        context.edit(courseGroup, curricularPeriod, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }

    private static ExecutionSemester getBeginExecutionPeriod(final String beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            return FenixFramework.getDomainObject(beginExecutionPeriodID);
        }
    }

    private static ExecutionSemester getEndExecutionPeriod(String endExecutionPeriodID) {
        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionSemester> getDomainObject(endExecutionPeriodID);
        return endExecutionPeriod;
    }
}
