package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRoles extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoRole> result = new ArrayList<InfoRole>();

		for (Role role : rootDomainObject.getRoles()) {
            result.add(InfoRole.newInfoFromDomain(role));
        }
        
		return result;
	}
    
}
