package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPersonsByIDs extends FenixService {

	@Service
	public static List<InfoPerson> run(final List<Integer> personIds) {

		final List<InfoPerson> result = new ArrayList<InfoPerson>(personIds.size());
		for (final Integer personId : personIds) {
			result.add(InfoPerson.newInfoFromDomain((Person) rootDomainObject.readPartyByOID(personId)));
		}
		return result;
	}

}