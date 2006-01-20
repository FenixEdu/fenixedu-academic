/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateNewUnit extends Service {

    public void run(Integer unitID, Integer parentUnitID, String unitName, String unitCostCenter,
            Date beginDate, Date endDate, UnitType type, Integer departmentID, Integer degreeID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        Unit unit = null;
        if (unitID == null) {
            unit = DomainFactory.makeUnit();
        } else {
            unit = (Unit) persistentObject.readByOID(Unit.class, unitID);
            if (unit == null) {
                throw new FenixServiceException("error.noUnit");
            }
        }

        Unit parentUnit = setParentUnits(parentUnitID, unit);
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }
        
        unit.edit(unitName, costCenterCode, beginDate, endDate, type, parentUnit);

        setDepartment(departmentID, unit);

        setDegree(degreeID, unit);
    }

    private void setDegree(Integer degreeID, Unit unit)
            throws ExcepcaoPersistencia {

        Degree degree = null;
        if (degreeID != null
                && unit.getType() != null
                && (unit.getType().equals(UnitType.DEGREE) || unit.getType().equals(
                        UnitType.MASTER_DEGREE))) {

            degree = (Degree) persistentObject.readByOID(Degree.class,
                    degreeID);

            if ((degree.getTipoCurso().equals(DegreeType.DEGREE) && unit.getType().equals(
                    UnitType.DEGREE))
                    || (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE) && unit.getType().equals(
                            UnitType.MASTER_DEGREE))){
                unit.setDegree(degree);   
            }                
            else if(unit.getDegree() != null){
                unit.removeDegree();
            }

        } else if (((degreeID != null && unit.getType() != null) || degreeID == null)
                && unit.getDegree() != null) {
            unit.removeDegree();
        }
    }

    private void setDepartment(Integer departmentID, Unit unit)
            throws ExcepcaoPersistencia {

        Department department = null;
        if (departmentID != null && unit.getType() != null && unit.getType().equals(UnitType.DEPARTMENT)) {
            department = (Department) persistentObject.readByOID(
                    Department.class, departmentID);
            unit.setDepartment(department);

        } else if (unit.getDepartment() != null) {
            unit.removeDepartment();        
        }
    }

    private Unit setParentUnits(Integer parentUnitID, Unit unit)
            throws ExcepcaoPersistencia {
        Unit parentUnit = null;
        if (parentUnitID != null) {
            parentUnit = (Unit) persistentObject.readByOID(Unit.class,
                    parentUnitID);
            if (unit.getParentUnits().contains(parentUnit)) {
                unit.removeParentUnits(parentUnit);
                parentUnit = null;
            }
        }
        return parentUnit;
    }
}
