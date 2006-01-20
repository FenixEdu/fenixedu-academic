/*
 * Created on Jan 5, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePersonFunction extends Service {

    public void run(Integer personFunctionID) throws ExcepcaoPersistencia {
        PersonFunction person_Function = (PersonFunction) persistentSupport.getIPersistentObject()
                .readByOID(PersonFunction.class, personFunctionID);

        person_Function.delete();
    }
}
