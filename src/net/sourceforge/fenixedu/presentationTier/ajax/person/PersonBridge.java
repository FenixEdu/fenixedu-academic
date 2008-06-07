/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.ajax.person;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonBridge {

    public static String readNameByPersonUsername(String username) throws FenixFilterException,
            FenixServiceException {

        Object[] args = { username };
        InfoPerson person = (InfoPerson) ServiceUtils.executeService("ReadPersonByUsername", args);

        return person.getNome();
    }

}
