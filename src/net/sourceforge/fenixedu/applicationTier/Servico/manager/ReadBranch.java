/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadBranch extends Service {

    /**
     * Executes the service. Returns the current infoBranch.
     * @throws ExcepcaoPersistencia 
     */
    public InfoBranch run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
		Branch branch = (Branch) persistentSupport.getIPersistentBranch().readByOID(Branch.class, idInternal);

        if (branch == null) {
            throw new NonExistingServiceException();
        }
        return InfoBranch.newInfoFromDomain(branch);
    }
}