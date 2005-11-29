/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateNewUnit implements IService {

    public void run(Integer unitID, Integer parentUnitID, String unitName, String unitCostCenter, Date beginDate, Date endDate,
            UnitType type) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        IUnit unit = null;
        if(unitID == null){
            unit = DomainFactory.makeUnit();
        }
        else{
           unit = (IUnit) suportePersistente.getIPersistentObject().readByOID(Unit.class, unitID);
           if(unit == null){
               throw new FenixServiceException("error.noUnit");
           }
        }                
        
        IUnit parentUnit = null;
        if(parentUnitID != null){
            parentUnit = (IUnit) suportePersistente.getIPersistentObject().readByOID(Unit.class, parentUnitID);
            if(unit.getParentUnits().contains(parentUnit)){
                unit.removeParentUnits(parentUnit);
                parentUnit = null;
            }
        }
        
        unit.edit(unitName, unitCostCenter, beginDate, endDate, type, parentUnit);
    }
}
