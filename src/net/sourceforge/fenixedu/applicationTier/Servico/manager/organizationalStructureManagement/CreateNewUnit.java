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
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateNewUnit extends Service {

    public void run(Integer unitID, Integer parentUnitID, String unitName, String unitCostCenter,
            String acronym, Date beginDate, Date endDate, PartyTypeEnum type, Integer departmentID,
            Integer degreeID) throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        Unit unit = null;
        if (unitID == null) {
            unit = DomainFactory.makeUnit();
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

        unit.edit(unitName, costCenterCode, acronym, beginDate, endDate, type, parentUnit);

        setDepartment(departmentID, unit);

        setDegree(degreeID, unit);
    }

    private void setDegree(Integer degreeID, Unit unit) throws ExcepcaoPersistencia {

        Degree degree = null;
        if (degreeID != null
                && unit.getType() != null
                && (unit.getType().equals(PartyTypeEnum.DEGREE) || unit.getType().equals(
                        PartyTypeEnum.MASTER_DEGREE))) {

            degree = rootDomainObject.readDegreeByOID(degreeID);

            if ((degree.getTipoCurso().equals(DegreeType.DEGREE) && unit.getType().equals(
                    PartyTypeEnum.DEGREE))
                    || (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE) && unit.getType().equals(
                            PartyTypeEnum.MASTER_DEGREE))) {
                unit.setDegree(degree);
            } else if (unit.getDegree() != null) {
                unit.removeDegree();
            }

        } else if (((degreeID != null && unit.getType() != null) || degreeID == null)
                && unit.getDegree() != null) {
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
