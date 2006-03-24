package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangePersonUsername extends Service {

    public void run(String newUsername, Integer personId) throws ExcepcaoPersistencia {
        Person person = (Person) persistentObject.readByOID(Person.class, personId);
        List<Person> persons = Party.readAllPersons();       
        person.changeUsername(newUsername, persons);
    }
}