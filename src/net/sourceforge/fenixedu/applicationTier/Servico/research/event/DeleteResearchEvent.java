package net.sourceforge.fenixedu.applicationTier.Servico.research.event;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResearchEvent extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia, FenixServiceException {
        Event event = rootDomainObject.readEventByOID(oid);
        if(event == null){
            throw new FenixServiceException();
        }
        event.delete();        
    }
}
