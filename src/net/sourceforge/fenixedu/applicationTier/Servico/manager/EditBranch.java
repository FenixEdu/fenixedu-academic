/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class EditBranch implements IService {

    /**
     * The constructor of this class.
     */
    public EditBranch() {
    }

    /**
     * Executes the service.
     */

    public void run(InfoBranch infoBranch) throws FenixServiceException {

        ISuportePersistente persistentSuport = null;
        IPersistentBranch persistentbranch = null;
        IBranch branch = null;
        String code = infoBranch.getCode();

        try {

            persistentSuport = SuportePersistenteOJB.getInstance();
            persistentbranch = persistentSuport.getIPersistentBranch();

            branch = (IBranch) persistentbranch.readByOID(Branch.class, infoBranch.getIdInternal());

            if (branch == null) {
                throw new NonExistingServiceException();
            }
            persistentbranch.simpleLockWrite(branch);
            branch.setName(infoBranch.getName());
            branch.setCode(code);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}