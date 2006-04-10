/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileItem;
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
        FileItem fileItem = FileItem.readByDspaceBitstreamIdentification(dspaceBitstreamIdentification);

        if (person != null && fileItem != null) {
            return fileItem.isPersonAllowedToAccess(person);
        }

        return false;
    }
}
