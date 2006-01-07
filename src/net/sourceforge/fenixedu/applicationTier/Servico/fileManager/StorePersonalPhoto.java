package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto implements IService {

    public void run(byte[] contents, ContentType contentType, String personUsername)
            throws ExcepcaoPersistencia, ExcepcaoInexistente {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(personUsername);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        storePersonalPhoto(contents, contentType, person);
    }

    public void run(byte[] contents, ContentType contentType, Integer personID)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person person = (Person) sp.getIPessoaPersistente().readByOID(Person.class, personID);

        storePersonalPhoto(contents, contentType, person);
    }

    private void storePersonalPhoto(byte[] contents, ContentType contentType, Person person) {

        if (person.getPersonalPhoto() != null) {
            person.getPersonalPhoto().delete();
        }

        DomainFactory.makeFileEntry(contentType, new ByteArray(contents), person);

    }
}