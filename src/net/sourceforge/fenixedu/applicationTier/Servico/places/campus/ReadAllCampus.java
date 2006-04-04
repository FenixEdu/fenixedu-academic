package net.sourceforge.fenixedu.applicationTier.Servico.places.campus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllCampus extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
	    List<InfoCampus> result = new ArrayList<InfoCampus>();
        
	    for (Campus campus : rootDomainObject.getCampuss()) {
            result.add(InfoCampus.newInfoFromDomain(campus));   
        }
            
		return result;
	}
        
}
