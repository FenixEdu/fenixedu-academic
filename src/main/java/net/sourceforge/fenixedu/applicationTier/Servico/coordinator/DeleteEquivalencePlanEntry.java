package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.AcademicAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteEquivalencePlanEntry extends FenixService {

    protected void run(final EquivalencePlanEntry EquivalencePlanEntry) {
        if (EquivalencePlanEntry != null) {
            EquivalencePlanEntry.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteEquivalencePlanEntry serviceInstance = new DeleteEquivalencePlanEntry();

    @Service
    public static void runDeleteEquivalencePlanEntry(EquivalencePlanEntry EquivalencePlanEntry) throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(EquivalencePlanEntry);
        } catch (NotAuthorizedException ex1) {
            try {
                AcademicAdministrativeOfficeAuthorizationFilter.instance.execute();
                serviceInstance.run(EquivalencePlanEntry);
            } catch (NotAuthorizedException ex2) {
                try {
                    ManagerAuthorizationFilter.instance.execute();
                    serviceInstance.run(EquivalencePlanEntry);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}