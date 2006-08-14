package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSites extends Service {

	public List<InfoSite> run() throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoSite> result = new ArrayList<InfoSite>();
        
		for (Site site : rootDomainObject.getSites()) {
            final InfoSite infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(site.getExecutionCourse()));
            result.add(infoSite);    
        }
		
		return result;
	}
    
}
