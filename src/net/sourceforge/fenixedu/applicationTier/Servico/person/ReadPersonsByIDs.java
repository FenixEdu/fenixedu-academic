package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPersonsByIDs extends Service {

    public List<InfoPerson> run(List<Integer> personsInternalIds) throws ExcepcaoPersistencia {
        List<InfoPerson> persons = new ArrayList<InfoPerson>();

        for (Integer personId : personsInternalIds) {
            Person person = (Person) rootDomainObject.readPartyByOID(personId);
            persons.add(InfoPerson.newInfoFromDomain(person));
        }

        return persons;
    }

}