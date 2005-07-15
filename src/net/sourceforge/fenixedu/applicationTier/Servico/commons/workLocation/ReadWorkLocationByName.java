package net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadWorkLocationByName implements IService {

    public InfoWorkLocation run(String workLocationName) throws FenixServiceException {
        InfoWorkLocation infoWorkLocation = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IWorkLocation workLocation = sp.getIPersistentWorkLocation().readByName(workLocationName);
       
        
            if(workLocation != null)
            {
                infoWorkLocation = new InfoWorkLocation();
                infoWorkLocation.copyFromDomain(workLocation);
            }
            
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoWorkLocation;
        
    }
}