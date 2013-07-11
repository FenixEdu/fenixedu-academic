package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyLastYearRealDataService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CopyLastYearRealDataToTSDProcessPhase {
    protected void run(String tsdProcessPhaseId) {
        TSDProcessPhase tsdProcessPhase = FenixFramework.getDomainObject(tsdProcessPhaseId);

        CopyLastYearRealDataService service = CopyLastYearRealDataService.getInstance();

        service.copyLastYearRealDataToTSDProcessPhase(tsdProcessPhase);
    }

    // Service Invokers migrated from Berserk

    private static final CopyLastYearRealDataToTSDProcessPhase serviceInstance = new CopyLastYearRealDataToTSDProcessPhase();

    @Atomic
    public static void runCopyLastYearRealDataToTSDProcessPhase(String tsdProcessPhaseId) throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdProcessPhaseId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdProcessPhaseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdProcessPhaseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}