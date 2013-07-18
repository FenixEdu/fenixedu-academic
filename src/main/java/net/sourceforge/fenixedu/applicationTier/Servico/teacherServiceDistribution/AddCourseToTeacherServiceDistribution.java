package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class AddCourseToTeacherServiceDistribution {

    protected void run(Integer tsdId, final Integer courseId) throws FenixServiceException {

        TeacherServiceDistribution rootTSD = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdId).getRootTSD();
        CompetenceCourse course = RootDomainObject.getInstance().readCompetenceCourseByOID(courseId);

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

    @Service
    public static void runAddCourseToTeacherServiceDistribution(Integer tsdId, Integer courseId) throws FenixServiceException,
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