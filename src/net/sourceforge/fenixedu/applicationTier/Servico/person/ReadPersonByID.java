package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;

public class ReadPersonByID extends FenixService {

    public InfoPerson run(Integer idInternal) {
	return InfoPerson.newInfoFromDomain((Person) rootDomainObject.readPartyByOID(idInternal));
    }
}
