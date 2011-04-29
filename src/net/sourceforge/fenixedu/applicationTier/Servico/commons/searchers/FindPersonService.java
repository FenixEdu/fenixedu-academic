package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

public class FindPersonService extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
	return InfoPerson.newInfoFromDomain((Person) object);
    }

    @Override
    protected List doSearch(HashMap searchParameters) {

	String request = (String) searchParameters.get("teacherId");
	Person person;
	if (request.substring(0, 3).equals("ist")) {
	    person = Person.readPersonByIstUsername(request);
	} else {
	    Teacher teacher = Teacher.readByIstId(request);
	    if (teacher == null) {
		person = null;
	    } else {
		person = teacher.getPerson();
	    }

	}

	List<Person> returnList = new ArrayList<Person>();
	if (person != null) {
	    returnList.add(person);
	}
	return returnList;
    }

}
