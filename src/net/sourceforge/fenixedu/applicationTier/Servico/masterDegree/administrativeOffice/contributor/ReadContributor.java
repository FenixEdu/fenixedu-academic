package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadContributor extends Service {

	public InfoContributor run(Integer contributorNumber) throws FenixServiceException, ExcepcaoPersistencia {
        final Party contributor = Party.readByContributorNumber(contributorNumber.toString());
		if (contributor == null) {
            throw new ExcepcaoInexistente();
        }

		return InfoContributor.newInfoFromDomain(contributor);
	}
    
}
