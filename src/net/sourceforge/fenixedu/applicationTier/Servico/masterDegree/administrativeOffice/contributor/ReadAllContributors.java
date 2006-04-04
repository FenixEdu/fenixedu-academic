package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllContributors extends Service {

	public List<InfoContributor> run() throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoContributor> result = new ArrayList<InfoContributor>();
        
        for (Contributor contributor : rootDomainObject.getContributors()) {
            result.add(InfoContributor.newInfoFromDomain(contributor));
        }

		return result;
	}

}
