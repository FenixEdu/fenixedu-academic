/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class DeletePersonByOID implements IService {
    
    public boolean run(Integer personID) throws ExcepcaoPersistencia{
        
        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();        
        Person person = (Person) ps.getIPersistentObject().readByOID(Person.class, personID);
        
        return person.delete();
        
    }

}
