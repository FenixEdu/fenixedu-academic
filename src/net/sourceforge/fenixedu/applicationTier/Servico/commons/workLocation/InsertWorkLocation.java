package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertWorkLocation implements IService {

    public IWorkLocation run(String workLocationName) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IWorkLocation storedWorkLocation = sp.getIPersistentWorkLocation().readByName(workLocationName);

        if (storedWorkLocation != null) {
            throw new ExistingServiceException(
                    "error.exception.commons.workLocation.workLocationAlreadyExists");
        }

        IWorkLocation workLocation = new WorkLocation();
        workLocation.setName(workLocationName);

        sp.getIPersistentWorkLocation().simpleLockWrite(workLocation);

        return workLocation;

    }
}