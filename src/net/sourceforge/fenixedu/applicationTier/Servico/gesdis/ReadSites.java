package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSites extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
        Collection<Site> allSites = persistentObject.readAll(Site.class);
		if (allSites == null || allSites.isEmpty())
			throw new InvalidArgumentsServiceException();

		List<InfoSite> result = new ArrayList<InfoSite>(allSites.size());
		for (Site site : allSites) {
            result.add(InfoSiteWithInfoExecutionCourse.newInfoFromDomain(site));    
        }
		
		return result;
	}
    
}
