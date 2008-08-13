package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadBranch extends Service {

    public InfoBranch run(Integer idInternal) throws FenixServiceException{
		Branch branch = rootDomainObject.readBranchByOID(idInternal);
        if (branch == null) {
            throw new NonExistingServiceException();
        }
        
        return InfoBranch.newInfoFromDomain(branch);
    }
    
}
