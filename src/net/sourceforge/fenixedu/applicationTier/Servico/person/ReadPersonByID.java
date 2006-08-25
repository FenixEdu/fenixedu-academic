package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;

public class ReadPersonByID extends Service {

    public InfoPerson run(Integer idInternal) {
	return InfoPerson.newInfoFromDomain((Person) rootDomainObject.readPartyByOID(idInternal));
    }
}
