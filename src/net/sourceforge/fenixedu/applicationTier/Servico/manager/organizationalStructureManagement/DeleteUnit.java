/*
 * Created on Nov 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DeleteUnit implements IService {
    
    public void run(Integer unitID) throws ExcepcaoPersistencia, FenixServiceException{
        
        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        Unit unit = (Unit) suportePersistente.getIPersistentObject().readByOID(Unit.class, unitID);
        if(unit == null){
            throw new FenixServiceException("error.noUnit");
        }
        
        unit.delete();        
    }
}
