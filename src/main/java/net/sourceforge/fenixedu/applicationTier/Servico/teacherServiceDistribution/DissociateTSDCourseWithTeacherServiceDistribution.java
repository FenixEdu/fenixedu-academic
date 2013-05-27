package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class DissociateTSDCourseWithTeacherServiceDistribution {
    protected void run(Integer tsdId, Integer tsdCourseId) {
        TeacherServiceDistribution tsd = RootDomainObject.getInstance().readTeacherServiceDistributionByOID(tsdId);
        TSDCourse tsdCourse = RootDomainObject.getInstance().readTSDCourseByOID(tsdCourseId);

        for (TSDCourse course : tsd.getTSDCoursesByCompetenceCourse(tsdCourse.getCompetenceCourse())) {
            tsd.removeTSDCourseFromAllChilds(course);
        }

        tsd.removeTSDCourseFromAllChilds(tsdCourse);
    }

    // Service Invokers migrated from Berserk

    private static final DissociateTSDCourseWithTeacherServiceDistribution serviceInstance =
            new DissociateTSDCourseWithTeacherServiceDistribution();

    @Service
    public static void runDissociateTSDCourseWithTeacherServiceDistribution(Integer tsdId, Integer tsdCourseId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, tsdCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, tsdCourseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, tsdCourseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}