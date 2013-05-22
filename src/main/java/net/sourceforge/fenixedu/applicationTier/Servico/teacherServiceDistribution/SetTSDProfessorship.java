package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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

public class SetTSDProfessorship extends FenixService {
    protected TSDProfessorship run(Integer tsdCourseId, Integer tsdTeacherId, Map<String, Object> tsdCourseParameters) {

        TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(tsdCourseId);
        TSDTeacher tsdTeacher = rootDomainObject.readTSDTeacherByOID(tsdTeacherId);
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
    public static TSDProfessorship runSetTSDProfessorship(Integer tsdCourseId, Integer tsdTeacherId,
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