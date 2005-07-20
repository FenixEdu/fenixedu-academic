/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteBranches implements IService {

    // delete a set of branches
    public List run(List internalIds, Boolean forceDelete) throws FenixServiceException {

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();
            Iterator iter = internalIds.iterator();

            List undeletedCodes = new ArrayList();
            Integer internalId;
            IBranch branch;

            while (iter.hasNext()) {
                internalId = (Integer) iter.next();
                branch = (IBranch) persistentBranch.readByOID(Branch.class, internalId);
                if (branch != null) {
					try {
	                    if (branch.getStudentCurricularPlans().isEmpty()) {
							branch.delete();
	                    } else {
	                        if (forceDelete.booleanValue() == true) {
								branch.delete();
	                        } else {
	                            undeletedCodes.add(branch.getCode());
	                        }
	                    }
					} catch (DomainException e) {
						undeletedCodes.add(branch.getCode());
					}
                }
            }

            return undeletedCodes;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}