/**
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonByUsernameOrIstUsername extends FenixService {

    @Service
    public static Person run(String username) throws FenixServiceException {
        return Person.readPersonByUsername(username);
    }
}