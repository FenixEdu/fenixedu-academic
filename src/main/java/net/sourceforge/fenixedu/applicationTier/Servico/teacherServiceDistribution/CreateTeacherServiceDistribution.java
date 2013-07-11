package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateTeacherServiceDistribution {
    protected TeacherServiceDistribution run(String tsdProcessPhaseId, String fatherTeacherServiceDistributionId, String name) {
        TSDProcessPhase tsdProcessPhase = FenixFramework.getDomainObject(tsdProcessPhaseId);
        TeacherServiceDistribution fatherTeacherServiceDistribution =
                FenixFramework.getDomainObject(fatherTeacherServiceDistributionId);

        TeacherServiceDistribution tsd =
                new TeacherServiceDistribution(tsdProcessPhase, name, fatherTeacherServiceDistribution,
                        new ArrayList<TSDTeacher>(), new ArrayList<TSDCourse>(), null, null, null, null);

        return tsd;
    }

    // Service Invokers migrated from Berserk

    private static final CreateTeacherServiceDistribution serviceInstance = new CreateTeacherServiceDistribution();

    @Atomic
    public static TeacherServiceDistribution runCreateTeacherServiceDistribution(String tsdProcessPhaseId,
            String fatherTeacherServiceDistributionId, String name) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(tsdProcessPhaseId, fatherTeacherServiceDistributionId, name);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(tsdProcessPhaseId, fatherTeacherServiceDistributionId, name);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(tsdProcessPhaseId, fatherTeacherServiceDistributionId, name);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}