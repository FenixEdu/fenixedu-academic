package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteFunction extends Service {

 public void run(Integer functionID) throws ExcepcaoPersistencia, FenixServiceException{
        Function function = rootDomainObject.readFunctionByOID(functionID);
        if(function == null){
            throw new FenixServiceException("error.noFunction");
        }
        
        function.delete();        
    }
 
}
