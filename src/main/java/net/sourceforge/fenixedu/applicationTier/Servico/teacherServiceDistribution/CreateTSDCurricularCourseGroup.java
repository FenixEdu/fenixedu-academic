package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateTSDCurricularCourseGroup {
    protected TSDCurricularCourseGroup run(String tsdId, String[] tsdCurricularCourseToGroupArray) {

        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        List<TSDCurricularCourse> tsdCurricularCourseList = new ArrayList<TSDCurricularCourse>();
        TSDCurricularCourseGroup tsdCurricularCourseGroup = null;

        for (String tsdCurricularCourseId : tsdCurricularCourseToGroupArray) {
            TSDCurricularCourse tsdCurricularCourse =
                    (TSDCurricularCourse) FenixFramework.getDomainObject(tsdCurricularCourseId);

            if (tsdCurricularCourse != null) {
                tsdCurricularCourseList.add(tsdCurricularCourse);
            }
        }

        if (!tsdCurricularCourseList.isEmpty()) {
            tsdCurricularCourseGroup = new TSDCurricularCourseGroup(tsd, tsdCurricularCourseList);
            tsdCurricularCourseGroup.setIsActive(true);
        }

        return tsdCurricularCourseGroup;
    }

    // Service Invokers migrated from Berserk

    private static final CreateTSDCurricularCourseGroup serviceInstance = new CreateTSDCurricularCourseGroup();

    @Atomic
    public static TSDCurricularCourseGroup runCreateTSDCurricularCourseGroup(String tsdId,
            String[] tsdCurricularCourseToGroupArray) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(tsdId, tsdCurricularCourseToGroupArray);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(tsdId, tsdCurricularCourseToGroupArray);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(tsdId, tsdCurricularCourseToGroupArray);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}