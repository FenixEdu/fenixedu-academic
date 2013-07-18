package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import pt.ist.fenixWebFramework.services.Service;

public class SetCurrentTSDProcessPhase {
    protected void run(Integer tsdProcessPhaseId) {
        TSDProcessPhase tsdProcessPhase = RootDomainObject.getInstance().readTSDProcessPhaseByOID(tsdProcessPhaseId);

        tsdProcessPhase.setCurrent();
    }

    // Service Invokers migrated from Berserk

    private static final SetCurrentTSDProcessPhase serviceInstance = new SetCurrentTSDProcessPhase();

    @Service
    public static void runSetCurrentTSDProcessPhase(Integer tsdProcessPhaseId) throws NotAuthorizedException {
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