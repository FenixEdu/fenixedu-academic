package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import pt.ist.fenixWebFramework.services.Service;

public class CopyTSDProcessPhaseDataToTSDProcessPhase extends FenixService {

    protected void run(Integer oldTSDProcessPhaseId, Integer newTSDProcessPhaseId) {
        TSDProcessPhase oldTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(oldTSDProcessPhaseId);
        TSDProcessPhase newTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(newTSDProcessPhaseId);

        CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

        service.copyDataFromTSDProcessPhase(newTSDProcessPhase, oldTSDProcessPhase);
    }

    // Service Invokers migrated from Berserk

    private static final CopyTSDProcessPhaseDataToTSDProcessPhase serviceInstance =
            new CopyTSDProcessPhaseDataToTSDProcessPhase();

    @Service
    public static void runCopyTSDProcessPhaseDataToTSDProcessPhase(Integer oldTSDProcessPhaseId, Integer newTSDProcessPhaseId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(oldTSDProcessPhaseId, newTSDProcessPhaseId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(oldTSDProcessPhaseId, newTSDProcessPhaseId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(oldTSDProcessPhaseId, newTSDProcessPhaseId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}