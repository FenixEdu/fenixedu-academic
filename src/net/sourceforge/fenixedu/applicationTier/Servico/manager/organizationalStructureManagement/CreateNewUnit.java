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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateNewUnit extends Service {

    public void run(Integer unitID, Integer parentUnitID, String unitName, String unitCostCenter,
            String acronym, Date beginDate, Date endDate, PartyTypeEnum type, Integer departmentID,
            Integer degreeID, AccountabilityType accountabilityType, String webAddress) throws ExcepcaoPersistencia,
            FenixServiceException, DomainException {

        Unit unit = null;
        if (unitID == null) {
            if(unitName == null) {
                throw new FenixServiceException("error.unit.empty.name");
            }
            unit = new Unit();
        } else {
            unit = (Unit) rootDomainObject.readPartyByOID(unitID);
            if (unit == null) {
                throw new FenixServiceException("error.noUnit");
            }
        }

        Unit parentUnit = setParentUnits(parentUnitID, unit);
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }

        unit.edit(unitName, costCenterCode, acronym, beginDate, endDate, type, parentUnit,
                accountabilityType, webAddress);

        setDepartment(departmentID, unit);
        setDegree(degreeID, unit);
    }

    private void setDegree(Integer degreeID, Unit unit) throws ExcepcaoPersistencia {

        Degree degree = null;
        if (degreeID != null && unit.getType() != null
                && (unit.getType().equals(PartyTypeEnum.DEGREE_UNIT))) {

            degree = rootDomainObject.readDegreeByOID(degreeID);
            unit.setDegree(degree);

        } else if (unit.getDegree() != null) {
            unit.removeDegree();
        }
    }

    private void setDepartment(Integer departmentID, Unit unit) throws ExcepcaoPersistencia {

        Department department = null;
        if (departmentID != null && unit.getType() != null
                && unit.getType().equals(PartyTypeEnum.DEPARTMENT)) {

            department = rootDomainObject.readDepartmentByOID(departmentID);
            unit.setDepartment(department);

        } else if (unit.getDepartment() != null) {
            unit.removeDepartment();
        }
    }

    private Unit setParentUnits(Integer parentUnitID, Unit unit) throws ExcepcaoPersistencia,
            FenixServiceException {

        Unit parentUnit = null;
        if (parentUnitID != null) {
            parentUnit = (Unit) rootDomainObject.readPartyByOID(parentUnitID);
            if (parentUnit.equals(unit)) {
                throw new FenixServiceException("error.same.unit");
            }
            if (unit.getParentUnits().contains(parentUnit)) {
                unit.removeParent(parentUnit);
                parentUnit = null;
            }
        }
        return parentUnit;
    }
}
