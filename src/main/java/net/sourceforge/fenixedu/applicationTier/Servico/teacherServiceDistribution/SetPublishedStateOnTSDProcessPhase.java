package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class SetPublishedStateOnTSDProcessPhase {
    protected void run(Integer tsdProcessPhaseId, Boolean publishedState) {
        TSDProcessPhase tsdProcessPhase = AbstractDomainObject.fromExternalId(tsdProcessPhaseId);
        tsdProcessPhase.setIsPublished(publishedState);
    }

    // Service Invokers migrated from Berserk

    private static final SetPublishedStateOnTSDProcessPhase serviceInstance = new SetPublishedStateOnTSDProcessPhase();

    @Service
    public static void runSetPublishedStateOnTSDProcessPhase(Integer tsdProcessPhaseId, Boolean publishedState)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            serviceInstance.run(tsdProcessPhaseId, publishedState);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                serviceInstance.run(tsdProcessPhaseId, publishedState);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    serviceInstance.run(tsdProcessPhaseId, publishedState);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}