package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
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
public class EditWorkLocation implements IService {

    private static EditWorkLocation service = new EditWorkLocation();

    /**
     * The singleton access method of this class.
     */
    public static EditWorkLocation getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private EditWorkLocation() {
    }

    public void run(Integer oldWorkLocationOID, String newWorkLocationName) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IWorkLocation storedWorkLocation = sp.getIPersistentWorkLocation().readByName(
                    newWorkLocationName);
            IWorkLocation oldWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(
                    WorkLocation.class, oldWorkLocationOID);

            if (oldWorkLocation == null) {
                throw new NonExistingServiceException(
                        "error.exception.commons.workLocation.workLocationNotFound");
            }

            if ((storedWorkLocation != null) && (!storedWorkLocation.equals(oldWorkLocation))) {
                throw new ExistingServiceException(
                        "error.exception.commons.workLocation.workLocationAlreadyExists");
            }

            oldWorkLocation.setName(newWorkLocationName);
            sp.getIPersistentWorkLocation().simpleLockWrite(oldWorkLocation);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}