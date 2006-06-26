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

    public Boolean run(String uniqueUsername, String dspaceBitstreamIdentification)
            throws FenixServiceException {
        Person person = Person.readPersonByIstUsername(uniqueUsername);
        File file = File.readByExternalStorageIdentification(dspaceBitstreamIdentification);

        if (person != null && file != null) {
            return file.isPersonAllowedToAccess(person);
        }

        return false;
    }

}
