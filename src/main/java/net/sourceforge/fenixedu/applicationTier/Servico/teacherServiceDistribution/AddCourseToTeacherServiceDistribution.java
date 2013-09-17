package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddCourseToTeacherServiceDistribution {

    protected void run(String tsdId, final String courseId) throws FenixServiceException {

        TeacherServiceDistribution rootTSD = FenixFramework.<TeacherServiceDistribution> getDomainObject(tsdId).getRootTSD();
        CompetenceCourse course = FenixFramework.getDomainObject(courseId);

        if (!rootTSD.getCompetenceCourses().contains(course)) {
            for (ExecutionSemester period : rootTSD.getTSDProcessPhase().getTSDProcess().getExecutionPeriods()) {
                if (course.getCurricularCoursesWithActiveScopesInExecutionPeriod(period).size() > 0) {
                    new TSDCompetenceCourse(rootTSD, course, period);
                }
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final AddCourseToTeacherServiceDistribution serviceInstance = new AddCourseToTeacherServiceDistribution();

    @Atomic
    public static void runAddCourseToTeacherServiceDistribution(String tsdId, String courseId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, courseId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, courseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, courseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}