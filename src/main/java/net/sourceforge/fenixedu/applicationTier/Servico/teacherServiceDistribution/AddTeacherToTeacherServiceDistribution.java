package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDRealTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author jpmsit, amak
 */
public class AddTeacherToTeacherServiceDistribution {

    protected void run(String tsdId, final String teacherId) throws FenixServiceException {

        TeacherServiceDistribution rootTSD = FenixFramework.<TeacherServiceDistribution> getDomainObject(tsdId).getRootTSD();
        Teacher teacher = FenixFramework.getDomainObject(teacherId);

        if (rootTSD.getTSDTeacherByTeacher(teacher) == null) {
            rootTSD.addTSDTeachers(new TSDRealTeacher(teacher));
        }
    }

    // Service Invokers migrated from Berserk

    private static final AddTeacherToTeacherServiceDistribution serviceInstance = new AddTeacherToTeacherServiceDistribution();

    @Atomic
    public static void runAddTeacherToTeacherServiceDistribution(String tsdId, String teacherId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, teacherId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, teacherId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, teacherId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}