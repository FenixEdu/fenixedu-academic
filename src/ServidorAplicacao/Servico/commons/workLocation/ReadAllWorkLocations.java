package ServidorAplicacao.Servico.commons.workLocation;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllWorkLocations implements IService
{

    private static ReadAllWorkLocations service = new ReadAllWorkLocations();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadAllWorkLocations getService()
    {
        return service;
    }

    /**
	 * The actor of this class.
	 */
    private ReadAllWorkLocations()
    {
    }

    public Object run(String workLocationName) throws FenixServiceException
    {
        List infoWorkLocations = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List workLocations = sp.getIPersistentWorkLocation().readAll();
            infoWorkLocations = Cloner.copyListIWorkLocation2ListInfoWorkLocation(workLocations);
        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoWorkLocations;
    }
}