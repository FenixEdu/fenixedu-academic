package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDegree {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer externalId) throws FenixServiceException {
        if (externalId == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToDelete = RootDomainObject.getInstance().readDegreeByOID(externalId);

        if (degreeToDelete == null) {
            throw new NonExistingServiceException();
        } else {
            degreeToDelete.delete();
        }
    }

}