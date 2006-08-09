package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto extends Service {

    public void run(byte[] contents, ContentType contentType, String personUsername)
            throws ExcepcaoPersistencia, ExcepcaoInexistente {
        Person person = Person.readPersonByUsername(personUsername);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        storePersonalPhoto(contents, contentType, person);
    }

    public void run(byte[] contents, ContentType contentType, Integer personID)
            throws ExcepcaoPersistencia {
        Person person = (Person) rootDomainObject.readPartyByOID(personID);

        storePersonalPhoto(contents, contentType, person);
    }

    private void storePersonalPhoto(byte[] contents, ContentType contentType, Person person) {

        if (person.getPersonalPhoto() != null) {
            person.getPersonalPhoto().delete();
        }

        new FileEntry(contentType, new ByteArray(contents), person);

    }
}