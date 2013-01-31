/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author naat
 * 
 */
public class CheckIfUserCanAccessFile extends FenixService {

	@Service
	public static Boolean run(final String username, final String externalStorageIdentification) {
		final Person person = Person.readPersonByUsername(username);
		final File file = File.readByExternalStorageIdentification(externalStorageIdentification);
		return person != null && file != null && file.isPersonAllowedToAccess(person);
	}

}