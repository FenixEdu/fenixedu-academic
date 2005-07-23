package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPersonByID implements IService {

	public InfoPerson run(Integer idInternal) throws ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
		
		IPerson person = (IPerson) persistentPerson.readByOID(Person.class, idInternal);
		
		InfoPerson infoPerson = null;
		
		if (person != null) {
			infoPerson = InfoPerson.newInfoFromDomain(person);
		}
		
		return infoPerson;
	}
        
}
