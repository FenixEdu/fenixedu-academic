/*
 * Created on Jan 5, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePersonFunction implements IService {

    public void run(Integer personFunctionID) throws ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        
        PersonFunction person_Function = (PersonFunction) suportePersistente.getIPersistentObject()
                .readByOID(PersonFunction.class, personFunctionID);

        person_Function.delete();
    }
}
