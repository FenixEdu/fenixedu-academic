package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePersonUsername implements IService {

    public void run(String newUsername, Integer personId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();;
        IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
        IPerson person = (IPerson) pessoaPersistente.readByOID(Person.class, personId);
        List<IPerson> persons = (List<IPerson>) pessoaPersistente.readAll(Person.class);
        
        person.changeUsername(newUsername, persons);
    }
}