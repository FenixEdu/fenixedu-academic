package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DissociateTSDTeacherWithTeacherServiceDistribution {
    protected void run(String tsdId, String tsdTeacherId) {
        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);
        TSDTeacher tsdTeacher = FenixFramework.getDomainObject(tsdTeacherId);

        tsd.removeTSDTeacherFromAllChilds(tsdTeacher);
    }

    // Service Invokers migrated from Berserk

    private static final DissociateTSDTeacherWithTeacherServiceDistribution serviceInstance =
            new DissociateTSDTeacherWithTeacherServiceDistribution();

    @Atomic
    public static void runDissociateTSDTeacherWithTeacherServiceDistribution(String tsdId, String tsdTeacherId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdId, tsdTeacherId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdId, tsdTeacherId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdId, tsdTeacherId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}