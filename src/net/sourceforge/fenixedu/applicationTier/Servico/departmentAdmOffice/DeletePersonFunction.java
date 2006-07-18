/*
 * Created on Jan 5, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePersonFunction extends Service {

    public void run(Integer personFunctionID) throws ExcepcaoPersistencia, FenixServiceException {
        PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(personFunctionID);
        if(personFunction == null) {
            throw new FenixServiceException("error.delete.personFunction.no.personFunction");
        }
        personFunction.delete();
    }
}
