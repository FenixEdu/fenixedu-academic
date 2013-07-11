package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class SetTSDProfessorship {
    protected TSDProfessorship run(String tsdCourseId, String tsdTeacherId, Map<String, Object> tsdCourseParameters) {

        TSDCourse tsdCourse = FenixFramework.getDomainObject(tsdCourseId);
        TSDTeacher tsdTeacher = FenixFramework.getDomainObject(tsdTeacherId);
        ShiftType type = ShiftType.valueOf((String) tsdCourseParameters.get("shiftType"));

        TSDProfessorship tsdProfessorship = tsdCourse.getTSDProfessorshipByTSDTeacherAndShiftType(tsdTeacher, type);

        if (tsdProfessorship == null) {
            tsdProfessorship = new TSDProfessorship(tsdCourse, tsdTeacher, type);
        }

        tsdProfessorship.setHoursManual((Double) tsdCourseParameters.get("hoursManual"));
        tsdProfessorship.setHoursType(TSDValueType.valueOf((String) tsdCourseParameters.get("hoursType")));

        return tsdProfessorship;
    }

    // Service Invokers migrated from Berserk

    private static final SetTSDProfessorship serviceInstance = new SetTSDProfessorship();

    @Service
    public static TSDProfessorship runSetTSDProfessorship(String tsdCourseId, String tsdTeacherId,
            Map<String, Object> tsdCourseParameters) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(tsdCourseId, tsdTeacherId, tsdCourseParameters);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(tsdCourseId, tsdTeacherId, tsdCourseParameters);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(tsdCourseId, tsdTeacherId, tsdCourseParameters);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}