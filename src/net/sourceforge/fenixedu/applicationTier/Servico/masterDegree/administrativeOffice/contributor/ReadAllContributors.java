package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class ReadAllContributors extends Service {

	public List<InfoContributor> run() {
        List<InfoContributor> result = new ArrayList<InfoContributor>();
        
        for (final Party contributor : Party.readContributors()) {
            result.add(InfoContributor.newInfoFromDomain(contributor));
        }

		return result;
	}

}
