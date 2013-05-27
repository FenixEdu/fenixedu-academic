package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveCourseFromTeacherServiceDistribution {

    protected void run(Integer tsdId, Integer courseId) throws FenixServiceException {

        TeacherServiceDistribution tsd = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdId);
        TSDCourse course = RootDomainObject.getInstance().readTSDCourseByOID(courseId);
        CompetenceCourse competenceCourse = course.getCompetenceCourse();

        if (course instanceof TSDVirtualCourseGroup) {
            course.delete();
        } else {
            for (TSDCourse tsdCourse : tsd.getTSDCoursesByCompetenceCourse(competenceCourse)) {
                tsdCourse.delete();
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final RemoveCourseFromTeacherServiceDistribution serviceInstance =
            new RemoveCourseFromTeacherServiceDistribution();

    @Service
    public static void runRemoveCourseFromTeacherServiceDistribution(Integer tsdId, Integer courseId)
            throws FenixServiceException, NotAuthorizedException {
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