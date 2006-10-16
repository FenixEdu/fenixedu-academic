/**
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonByUsernameOrIstUsername extends Service {

    public Person run(String username) throws FenixServiceException {
	return Person.readPersonByUsername(username);
    }
}