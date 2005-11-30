/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateNewUnit implements IService {

    public void run(Integer unitID, Integer parentUnitID, String unitName, String unitCostCenter, Date beginDate, Date endDate,
            UnitType type, Integer departmentID, Integer degreeID) throws ExcepcaoPersistencia, FenixServiceException {

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
        
        IUnit parentUnit = setParentUnits(parentUnitID, suportePersistente, unit);
                                  
        unit.edit(unitName, unitCostCenter, beginDate, endDate, type, parentUnit);
        
        setDepartment(departmentID, suportePersistente, unit);
        
        //setDegree(degreeID, suportePersistente);
    }


//    private void setDegree(Integer degreeID, ISuportePersistente suportePersistente, IUnit unit) throws ExcepcaoPersistencia {
//        IDegree degree = null;
//        if(degreeID != null && unit.getType() != null && unit.getType().equals(UnitType.DEGREE)){
//            degree = (IDegree) suportePersistente.getIPersistentObject().readByOID(Degree.class, degreeID);
//            unit.setDegree(degree);
//        }
//    }

    private void setDepartment(Integer departmentID, ISuportePersistente suportePersistente, IUnit unit) throws ExcepcaoPersistencia {
        IDepartment department = null;
        if(departmentID != null && unit.getType() != null && unit.getType().equals(UnitType.DEPARTMENT)){
            department = (IDepartment) suportePersistente.getIPersistentObject().readByOID(Department.class, departmentID);
            unit.setDepartment(department);
        }
//        else if(departmentID == null && unit.getDepartment() != null){
//            unit.removeDepartment();
//        }            
    }

 
    private IUnit setParentUnits(Integer parentUnitID, ISuportePersistente suportePersistente, IUnit unit) throws ExcepcaoPersistencia {
        IUnit parentUnit = null;
        if(parentUnitID != null){
            parentUnit = (IUnit) suportePersistente.getIPersistentObject().readByOID(Unit.class, parentUnitID);
            if(unit.getParentUnits().contains(parentUnit)){
                unit.removeParentUnits(parentUnit);
                parentUnit = null;
            }
        }
        return parentUnit;
    }
}
