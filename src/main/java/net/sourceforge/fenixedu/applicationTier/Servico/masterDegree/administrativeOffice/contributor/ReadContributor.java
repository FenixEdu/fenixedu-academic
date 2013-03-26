package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class ReadContributor extends FenixService {

    public InfoContributor run(Integer contributorNumber) throws FenixServiceException {
        final Party contributor = Party.readByContributorNumber(contributorNumber.toString());
        if (contributor == null) {
            throw new ExcepcaoInexistente();
        }

        return InfoContributor.newInfoFromDomain(contributor);
    }

}
