package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDegreeCurricularPlan {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer idInternal) throws FenixServiceException {
        if (idInternal == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DegreeCurricularPlan dcpToDelete = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(idInternal);

        if (dcpToDelete == null) {
            throw new NonExistingServiceException();
        } else {
            dcpToDelete.delete();
        }
    }

}