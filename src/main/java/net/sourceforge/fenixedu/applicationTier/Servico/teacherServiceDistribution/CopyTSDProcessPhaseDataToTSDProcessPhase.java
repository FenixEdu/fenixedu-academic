package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CopyTSDProcessPhaseDataToTSDProcessPhase {

    protected void run(String oldTSDProcessPhaseId, String newTSDProcessPhaseId) {
        TSDProcessPhase oldTSDProcessPhase = AbstractDomainObject.fromExternalId(oldTSDProcessPhaseId);
        TSDProcessPhase newTSDProcessPhase = AbstractDomainObject.fromExternalId(newTSDProcessPhaseId);

        CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

        service.copyDataFromTSDProcessPhase(newTSDProcessPhase, oldTSDProcessPhase);
    }

    // Service Invokers migrated from Berserk

    private static final CopyTSDProcessPhaseDataToTSDProcessPhase serviceInstance =
            new CopyTSDProcessPhaseDataToTSDProcessPhase();

    @Service
    public static void runCopyTSDProcessPhaseDataToTSDProcessPhase(String oldTSDProcessPhaseId, String newTSDProcessPhaseId)
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