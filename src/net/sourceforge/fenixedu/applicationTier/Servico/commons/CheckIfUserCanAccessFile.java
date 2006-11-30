/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;

/**
 * 
 * @author naat
 * 
 */
public class CheckIfUserCanAccessFile extends Service {

    public Boolean run(String uniqueUsername, String externalStorageIdentification)
            throws FenixServiceException {
        Person person = Person.readPersonByUsername(uniqueUsername);
        File file = File.readByExternalStorageIdentification(externalStorageIdentification);

        if (person != null && file != null) {
            return file.isPersonAllowedToAccess(person);
        }

        return false;
    }

}
