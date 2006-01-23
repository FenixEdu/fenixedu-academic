package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPersonByID extends Service {

	public InfoPerson run(Integer idInternal) throws ExcepcaoPersistencia {
		Person person = (Person) persistentObject.readByOID(Person.class, idInternal);
		
		InfoPerson infoPerson = null;
		
		if (person != null) {
			infoPerson = InfoPerson.newInfoFromDomain(person);
		}
		
		return infoPerson;
	}
        
}
