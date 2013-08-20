package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AssociateTSDCourseWithTeacherServiceDistribution {
    protected void run(String tsdId, String tsdcourseId) {
        TeacherServiceDistribution tsd = AbstractDomainObject.fromExternalId(tsdId);

        if (tsdcourseId == null) {
            tsd.getTSDCoursesSet().addAll(tsd.getParent().getTSDCourses());
        } else {
            TSDCourse course = AbstractDomainObject.fromExternalId(tsdcourseId);
            course.addTeacherServiceDistributions(tsd);
            tsd.getTSDCoursesSet().addAll(tsd.getParent().getTSDCoursesByCompetenceCourse(course.getCompetenceCourse()));
        }
    }

    // Service Invokers migrated from Berserk

    private static final AssociateTSDCourseWithTeacherServiceDistribution serviceInstance =
            new AssociateTSDCourseWithTeacherServiceDistribution();

    @Service
    public static void runAssociateTSDCourseWithTeacherServiceDistribution(String tsdId, String tsdcourseId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, tsdcourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, tsdcourseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, tsdcourseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}