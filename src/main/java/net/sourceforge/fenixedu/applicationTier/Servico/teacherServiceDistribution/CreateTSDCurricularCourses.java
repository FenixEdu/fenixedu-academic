package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class CreateTSDCurricularCourses {
    protected void run(Integer tsdId, Integer competenceCourseId, Integer tsdProcessPhaseId, Integer executionPeriodId,
            Boolean activateCourses) {

        TeacherServiceDistribution tsd = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdId);
        CompetenceCourse competenceCourse = RootDomainObject.getInstance().readCompetenceCourseByOID(competenceCourseId);
        TSDProcessPhase tsdProcessPhase = RootDomainObject.getInstance().readTSDProcessPhaseByOID(tsdProcessPhaseId);
        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        List<CurricularCourse> curricularCourseList =
                competenceCourse.getCurricularCoursesWithActiveScopesInExecutionPeriod(executionSemester);

        Set<CurricularCourse> existingCurricularCourses = new HashSet<CurricularCourse>();
        for (TSDCurricularCourse tsdCourse : tsdProcessPhase.getRootTSD().getTSDCurricularCourses(competenceCourse,
                executionSemester)) {
            existingCurricularCourses.add(tsdCourse.getCurricularCourse());
        }

        for (CurricularCourse curricularCourse : curricularCourseList) {
            if (!existingCurricularCourses.contains(curricularCourse)) {
                TSDCurricularCourse tsdCurricularCourse = new TSDCurricularCourse(tsd, curricularCourse, executionSemester);
                tsdCurricularCourse.setIsActive(activateCourses);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateTSDCurricularCourses serviceInstance = new CreateTSDCurricularCourses();

    @Service
    public static void runCreateTSDCurricularCourses(Integer tsdId, Integer competenceCourseId, Integer tsdProcessPhaseId,
            Integer executionPeriodId, Boolean activateCourses) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, competenceCourseId, tsdProcessPhaseId, executionPeriodId, activateCourses);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, competenceCourseId, tsdProcessPhaseId, executionPeriodId, activateCourses);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, competenceCourseId, tsdProcessPhaseId, executionPeriodId, activateCourses);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}