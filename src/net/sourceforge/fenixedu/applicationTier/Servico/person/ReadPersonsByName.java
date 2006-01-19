package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadPersonsByName extends Service {

    public List<InfoPerson> run(String stringtoSearch) throws ExcepcaoPersistencia {

        IPessoaPersistente persistentPerson = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPessoaPersistente();

        String names[] = stringtoSearch.split(" ");
        StringBuilder authorName = new StringBuilder("%");

        for (int i = 0; i <= names.length - 1; i++) {
            authorName.append(names[i]);
            authorName.append("%");
        }
        List<Person> persons = persistentPerson.readPersonsBySubName(authorName.toString());

       List<InfoPerson> infoPersons = new ArrayList<InfoPerson>(persons.size());

        for (Person individualPerson : persons) {
            infoPersons.add(InfoPerson.newInfoFromDomain(individualPerson));
        }
        
        return infoPersons;

    }
}