/**
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonByUsernameOrIstUsername {

    @Atomic
    public static Person run(String username) throws FenixServiceException {
        return Person.readPersonByUsername(username);
    }
}