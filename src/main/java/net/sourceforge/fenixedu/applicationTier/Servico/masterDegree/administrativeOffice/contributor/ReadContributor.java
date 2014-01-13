package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Filtro.AcademicAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixframework.Atomic;

public class ReadContributor {

    protected InfoContributor run(Integer contributorNumber) throws FenixServiceException {
        final Party contributor = Party.readByContributorNumber(contributorNumber.toString());
        if (contributor == null) {
            throw new ExcepcaoInexistente();
        }

        return InfoContributor.newInfoFromDomain(contributor);
    }

    // Service Invokers migrated from Berserk

    private static final ReadContributor serviceInstance = new ReadContributor();

    @Atomic
    public static InfoContributor runReadContributor(Integer contributorNumber) throws FenixServiceException,
            NotAuthorizedException {
        try {
            AcademicAdministrativeOfficeAuthorizationFilter.instance.execute();
            return serviceInstance.run(contributorNumber);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(contributorNumber);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}