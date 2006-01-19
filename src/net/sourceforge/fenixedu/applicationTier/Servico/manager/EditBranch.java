/*
 * Created on 18/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class EditBranch extends Service {

    public void run(InfoBranch infoBranch) throws FenixServiceException, ExcepcaoPersistencia {
        try {
			ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
			IPersistentBranch persistentbranch = persistentSuport.getIPersistentBranch();

			Branch branch = (Branch) persistentbranch.readByOID(Branch.class, infoBranch.getIdInternal());

            if (branch == null) {
                throw new NonExistingServiceException();
            }
			
			branch.edit(infoBranch.getName(),infoBranch.getNameEn(),infoBranch.getCode());
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException();
        }
    }
}