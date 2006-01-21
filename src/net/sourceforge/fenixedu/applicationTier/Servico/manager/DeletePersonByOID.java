/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class DeletePersonByOID extends Service {
    
    public boolean run(Integer personID) throws ExcepcaoPersistencia{
        Person person = (Person) persistentObject.readByOID(Person.class, personID);        
        return person.delete();
        
    }

}
