package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SetTSDCourseType {
    protected void run(String competenceCourseId, String tsdId, String executionPeriodId, String courseTSDProcessPhaseTypeString) {
        CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseId);
        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);

        TSDCourseType tsdCourseType = TSDCourseType.valueOf(courseTSDProcessPhaseTypeString);

        tsd.setTSDCourseType(competenceCourse, executionSemester, tsdCourseType);
    }

    // Service Invokers migrated from Berserk

    private static final SetTSDCourseType serviceInstance = new SetTSDCourseType();

    @Atomic
    public static void runSetTSDCourseType(String competenceCourseId, String tsdId, String executionPeriodId,
            String courseTSDProcessPhaseTypeString) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(competenceCourseId, tsdId, executionPeriodId, courseTSDProcessPhaseTypeString);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(competenceCourseId, tsdId, executionPeriodId, courseTSDProcessPhaseTypeString);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(competenceCourseId, tsdId, executionPeriodId, courseTSDProcessPhaseTypeString);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}