package ServidorAplicacao.Servico.commons.workLocation;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IWorkLocation;
import Dominio.WorkLocation;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertWorkLocation implements IService
{

    private static InsertWorkLocation service = new InsertWorkLocation();

    /**
	 * The singleton access method of this class.
	 */
    public static InsertWorkLocation getService()
    {
        return service;
    }

    /**
	 * The actor of this class.
	 */
    private InsertWorkLocation()
    {
    }

    public void run(String workLocationName) throws FenixServiceException
    {
        try
        {
            IWorkLocation workLocation = new WorkLocation();
            workLocation.setName(workLocationName);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IWorkLocation storedWorkLocation =
                sp.getIPersistentWorkLocation().readByName(workLocationName);

            if (storedWorkLocation != null)
            {
                throw new ExistingServiceException("error.exception.commons.workLocation.workLocationAlreadyExists");
            }

            sp.getIPersistentWorkLocation().simpleLockWrite(workLocation);

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}