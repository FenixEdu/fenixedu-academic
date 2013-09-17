package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhaseStatus;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteTSDProcessPhase {
    protected void run(String tsdProcessPhaseId) {
        TSDProcessPhase tsdProcessPhase = FenixFramework.getDomainObject(tsdProcessPhaseId);

        if (tsdProcessPhase.getStatus() == TSDProcessPhaseStatus.CLOSED) {
            if (tsdProcessPhase.getPreviousTSDProcessPhase() != null) {
                tsdProcessPhase.getPreviousTSDProcessPhase().setNextTSDProcessPhase(tsdProcessPhase.getNextTSDProcessPhase());
            }

            if (tsdProcessPhase.getNextTSDProcessPhase() != null) {
                tsdProcessPhase.getNextTSDProcessPhase().setPreviousTSDProcessPhase(tsdProcessPhase.getPreviousTSDProcessPhase());
            }

            tsdProcessPhase.deleteDataAndPhase();
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTSDProcessPhase serviceInstance = new DeleteTSDProcessPhase();

    @Atomic
    public static void runDeleteTSDProcessPhase(String tsdProcessPhaseId) throws NotAuthorizedException {
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