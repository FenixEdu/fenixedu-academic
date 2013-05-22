package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteTSDCurricularCourseGroup extends FenixService {
    protected void run(Integer tsdCurricularCourseGroupId) {
        TSDCurricularCourseGroup tsdCurricularCourseGroup =
                (TSDCurricularCourseGroup) rootDomainObject.readTSDCourseByOID(tsdCurricularCourseGroupId);

        for (TSDCurricularCourse tsdCurricularCourse : tsdCurricularCourseGroup.getTSDCurricularCourses()) {
            tsdCurricularCourse.setTSDCurricularCourseGroup(null);
        }

        tsdCurricularCourseGroup.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTSDCurricularCourseGroup serviceInstance = new DeleteTSDCurricularCourseGroup();

    @Service
    public static void runDeleteTSDCurricularCourseGroup(Integer tsdCurricularCourseGroupId) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdCurricularCourseGroupId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdCurricularCourseGroupId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdCurricularCourseGroupId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}