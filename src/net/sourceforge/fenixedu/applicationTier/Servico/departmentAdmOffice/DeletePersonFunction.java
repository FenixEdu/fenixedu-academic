/*
 * Created on Jan 5, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

public class DeletePersonFunction extends FenixService {

    public void run(Integer personFunctionID) throws FenixServiceException {
        PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(personFunctionID);
        if (personFunction == null) {
            throw new FenixServiceException("error.delete.personFunction.no.personFunction");
        }
        personFunction.delete();
    }
}
