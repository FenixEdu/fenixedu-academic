package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadContributorList extends Service {

    public List<InfoContributor> run(final Integer contributorNumber) throws FenixServiceException, ExcepcaoPersistencia {
        final Contributor contributor = Contributor.readByContributorNumber(contributorNumber);

        final List<InfoContributor> result = new ArrayList<InfoContributor>();
        if (contributor != null) {
            final InfoContributor infoContributor = InfoContributor.newInfoFromDomain(contributor);
            result.add(infoContributor);
        }

        return result;
    }
    
}
