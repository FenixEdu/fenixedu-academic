package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertWorkLocation implements IService {

    private static InsertWorkLocation service = new InsertWorkLocation();

    /**
     * The singleton access method of this class.
     */
    public static InsertWorkLocation getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private InsertWorkLocation() {
    }

    public void run(String workLocationName) throws FenixServiceException {
        try {
            IWorkLocation workLocation = new WorkLocation();
            workLocation.setName(workLocationName);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IWorkLocation storedWorkLocation = sp.getIPersistentWorkLocation().readByName(
                    workLocationName);

            if (storedWorkLocation != null) {
                throw new ExistingServiceException(
                        "error.exception.commons.workLocation.workLocationAlreadyExists");
            }

            sp.getIPersistentWorkLocation().simpleLockWrite(workLocation);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}