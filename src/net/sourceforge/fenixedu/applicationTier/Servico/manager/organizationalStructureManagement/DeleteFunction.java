/*
 * Created on Nov 23, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteFunction extends Service {

 public void run(Integer functionID) throws ExcepcaoPersistencia, FenixServiceException{
        
        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        Function function = (Function) suportePersistente.getIPersistentObject().readByOID(Function.class, functionID);
        if(function == null){
            throw new FenixServiceException("error.noFunction");
        }
        
        function.delete();        
    }
}
