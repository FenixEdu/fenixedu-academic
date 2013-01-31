package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPersonByUsername extends FenixService {

	@Service
	public static InfoPerson run(String username) throws ExcepcaoInexistente {
		final Person person = Person.readPersonByUsername(username);
		if (person == null) {
			throw new ExcepcaoInexistente("error.readPersonByUsername.noPerson");
		}
		return InfoPerson.newInfoFromDomain(person);
	}
}