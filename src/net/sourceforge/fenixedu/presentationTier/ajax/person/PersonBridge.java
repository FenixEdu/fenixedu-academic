/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.ajax.person;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonBridge {

    public static String readNameByPersonUsername(String username) throws FenixFilterException, FenixServiceException {

	InfoPerson person = (InfoPerson) ReadPersonByUsername.run(username);

	return person.getNome();
    }

}