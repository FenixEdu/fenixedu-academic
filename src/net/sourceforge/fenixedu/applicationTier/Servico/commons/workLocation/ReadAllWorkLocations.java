package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllWorkLocations implements IService {

    private static ReadAllWorkLocations service = new ReadAllWorkLocations();

    /**
     * The singleton access method of this class.
     */
    public static ReadAllWorkLocations getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadAllWorkLocations() {
    }

    public Object run() throws FenixServiceException {
        List infoWorkLocations = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List workLocations = sp.getIPersistentWorkLocation().readAll();
            infoWorkLocations = Cloner.copyListIWorkLocation2ListInfoWorkLocation(workLocations);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoWorkLocations;
    }
}