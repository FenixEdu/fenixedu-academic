package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.DomainObject;

public class FindPersonService extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
        return InfoPerson.newInfoFromDomain((Person) object);
    }

    @Override
    protected List<Person> doSearch(HashMap searchParameters) {

        String request = (String) searchParameters.get("teacherId");
        Person person = Person.readPersonByUsername(request.trim());

        List<Person> returnList = new ArrayList<Person>();
        if (person != null) {
            returnList.add(person);
        }

        return returnList;
    }

}