/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadBranch implements IService {

    /**
     * Executes the service. Returns the current infoBranch.
     */
    public InfoBranch run(Integer idInternal) throws FenixServiceException {
        ISuportePersistente sp;
        IBranch branch = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            branch = (IBranch) sp.getIPersistentBranch().readByOID(Branch.class, idInternal);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (branch == null) {
            throw new NonExistingServiceException();
        }

        InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(branch);
        return infoBranch;
    }
}