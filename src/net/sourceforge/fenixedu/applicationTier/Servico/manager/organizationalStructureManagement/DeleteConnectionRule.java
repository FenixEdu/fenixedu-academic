/*
 * Created on Mar 3, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteConnectionRule extends Service {
    
    public void run(Integer connectionRuleID) throws ExcepcaoPersistencia, FenixServiceException{
        ConnectionRule connectionRule = (ConnectionRule) persistentObject.readByOID(ConnectionRule.class, connectionRuleID);
        if(connectionRule == null){
            throw new FenixServiceException("error.no.connectionRule");
        }        
        connectionRule.delete();        
    }
}
