package net.sourceforge.fenixedu.applicationTier.Servico.groups;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePersonalGroup implements IService {
    
    public void run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        PersonalGroup personalGroup = (PersonalGroup) persistentSupport.getIPersistentObject().readByOID(PersonalGroup.class, groupId);
        
        if (personalGroup == null) {
            throw new FenixServiceException("error.noPersonalGroup");
        }
        
        personalGroup.delete();
    }

}
