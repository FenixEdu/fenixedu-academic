package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

public class ChangePersonUsername extends Service {

    public void run(String newUsername, Integer personId) throws ExcepcaoPersistencia {
        IPessoaPersistente pessoaPersistente = persistentSupport.getIPessoaPersistente();
        Person person = (Person) pessoaPersistente.readByOID(Person.class, personId);
        List<Person> persons = (List<Person>) pessoaPersistente.readAll(Person.class);
        
        person.changeUsername(newUsername, persons);
    }
}