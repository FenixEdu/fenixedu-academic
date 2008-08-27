package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto extends Service {
    public void run(byte[] contents, byte[] compressed, ContentType contentType, String personUsername)
	    throws ExcepcaoInexistente {
	Person person = Person.readPersonByUsername(personUsername);

	if (person == null) {
	    throw new ExcepcaoInexistente("Unknown Person !!");
	}

	storePersonalPhoto(contents, compressed, contentType, person);
    }

    public void run(byte[] contents, byte[] compressed, ContentType contentType, Integer personID) throws ExcepcaoPersistencia {
	Person person = (Person) rootDomainObject.readPartyByOID(personID);

	storePersonalPhoto(contents, compressed, contentType, person);
    }

    private void storePersonalPhoto(byte[] contents, byte[] compressed, ContentType contentType, Person person) {
	person.setPersonalPhoto(new Photograph(contentType, new ByteArray(contents), new ByteArray(compressed),
		PhotoType.INSTITUTIONAL));
    }
}