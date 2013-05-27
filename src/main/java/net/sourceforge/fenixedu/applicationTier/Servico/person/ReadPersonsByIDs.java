package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPersonsByIDs {

    @Service
    public static List<InfoPerson> run(final List<Integer> personIds) {

        final List<InfoPerson> result = new ArrayList<InfoPerson>(personIds.size());
        for (final Integer personId : personIds) {
            result.add(InfoPerson.newInfoFromDomain((Person) RootDomainObject.getInstance().readPartyByOID(personId)));
        }
        return result;
    }

}