package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.ist.fenixWebFramework.services.Service;

public class CreateTeacherServiceDistribution extends FenixService {
    protected TeacherServiceDistribution run(Integer tsdProcessPhaseId, Integer fatherTeacherServiceDistributionId, String name) {
        TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
        TeacherServiceDistribution fatherTeacherServiceDistribution =
                rootDomainObject.readTeacherServiceDistributionByOID(fatherTeacherServiceDistributionId);

        TeacherServiceDistribution tsd =
                new TeacherServiceDistribution(tsdProcessPhase, name, fatherTeacherServiceDistribution,
                        new ArrayList<TSDTeacher>(), new ArrayList<TSDCourse>(), null, null, null, null);

        return tsd;
    }

    // Service Invokers migrated from Berserk

    private static final CreateTeacherServiceDistribution serviceInstance = new CreateTeacherServiceDistribution();

    @Service
    public static TeacherServiceDistribution runCreateTeacherServiceDistribution(Integer tsdProcessPhaseId,
            Integer fatherTeacherServiceDistributionId, String name) throws NotAuthorizedException {
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