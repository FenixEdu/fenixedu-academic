package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPersonsByName extends FenixService {

	@Service
	public static List<InfoPerson> run(String stringtoSearch) {

		String names[] = stringtoSearch.split(" ");
		StringBuilder authorName = new StringBuilder("%");

		for (int i = 0; i <= names.length - 1; i++) {
			authorName.append(names[i]);
			authorName.append("%");
		}

		final List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();
		for (final Person individualPerson : Person.readPersonsByName(authorName.toString())) {
			infoPersons.add(InfoPerson.newInfoFromDomain(individualPerson));
		}
		return infoPersons;

	}
}