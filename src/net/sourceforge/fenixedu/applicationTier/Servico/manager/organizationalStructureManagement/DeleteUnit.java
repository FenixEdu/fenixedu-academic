/*
 * Created on Nov 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteUnit extends Service {
    
    public void run(Integer unitID) throws FenixServiceException{
        Unit unit = (Unit) rootDomainObject.readPartyByOID(unitID);
        if(unit == null){
            throw new FenixServiceException("error.noUnit");
        }        
        unit.delete();        
    }
    
}
