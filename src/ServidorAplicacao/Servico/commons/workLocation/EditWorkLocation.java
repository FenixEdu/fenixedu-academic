package ServidorAplicacao.Servico.commons.workLocation;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IWorkLocation;
import Dominio.WorkLocation;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditWorkLocation implements IService
{

    private static EditWorkLocation service = new EditWorkLocation();

    /**
	 * The singleton access method of this class.
	 */
    public static EditWorkLocation getService()
    {
        return service;
    }

    /**
	 * The actor of this class.
	 */
    private EditWorkLocation()
    {
    }

    public void run(Integer oldWorkLocationOID, String newWorkLocationName) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IWorkLocation storedWorkLocation =
                sp.getIPersistentWorkLocation().readByName(newWorkLocationName);
            IWorkLocation oldWorkLocation =
                (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(
                    WorkLocation.class,
                    oldWorkLocationOID);

            if (oldWorkLocation == null)
            {
                throw new NonExistingServiceException("error.exception.commons.workLocation.workLocationNotFound");
            }

            if ((storedWorkLocation != null) && (!storedWorkLocation.equals(oldWorkLocation)))
            {
                throw new ExistingServiceException("error.exception.commons.workLocation.workLocationAlreadyExists");
            }

            oldWorkLocation.setName(newWorkLocationName);
            sp.getIPersistentWorkLocation().simpleLockWrite(oldWorkLocation);
        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}