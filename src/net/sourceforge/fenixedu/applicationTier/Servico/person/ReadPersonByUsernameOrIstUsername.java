/**
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonByUsernameOrIstUsername extends FenixService {

    public Person run(String username) throws FenixServiceException {
	return Person.readPersonByUsername(username);
    }
}