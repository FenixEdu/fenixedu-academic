package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllWorkLocations implements IService {

    public Object run() throws FenixServiceException, ExcepcaoPersistencia {
        List infoWorkLocations = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<IWorkLocation> workLocations = sp.getIPersistentWorkLocation().readAll();

        for (IWorkLocation workLocation : workLocations) {
            infoWorkLocations.add(InfoWorkLocation.newInfoFromDomain(workLocation));
        }

        return infoWorkLocations;
    }
}