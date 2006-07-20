package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadContributorList extends Service {

    public List<InfoContributor> run(final Integer contributorNumber) throws FenixServiceException, ExcepcaoPersistencia {
        final List<InfoContributor> result = new ArrayList<InfoContributor>();
        
        final String contributorNumberString = contributorNumber.toString();
        for (final Party contributor : Party.readContributors()) {
            if (contributor.getSocialSecurityNumber().equals(contributorNumberString)) {
                final InfoContributor infoContributor = InfoContributor.newInfoFromDomain(contributor);
                result.add(infoContributor);
            }
        }

        return result;
    }
    
}
