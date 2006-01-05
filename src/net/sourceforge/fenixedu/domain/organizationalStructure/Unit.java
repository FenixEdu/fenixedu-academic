/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IContract;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Unit extends Unit_Base {

    public List<IUnit> getTopUnits() {
        IUnit unit = this;
        List<IUnit> allTopUnits = new ArrayList<IUnit>();
        if (unit.hasAnyParentUnits()) {
            for (IUnit parentUnit : this.getParentUnits()) {
                if (!parentUnit.hasAnyParentUnits() && !allTopUnits.contains(parentUnit)) {
                    allTopUnits.add(parentUnit);
                } else if (parentUnit.hasAnyParentUnits()) {
                    for (IUnit parentUnit2 : parentUnit.getTopUnits()) {
                        if (!allTopUnits.contains(parentUnit2)) {
                            allTopUnits.add(parentUnit2);
                        }
                    }
                }
            }
        }
        return allTopUnits;
    }

    public List<IUnit> getInactiveSubUnits(Date currentDate) {
        List<IUnit> allInactiveSubUnits = new ArrayList<IUnit>();
        for (IUnit subUnit : this.getSubUnits()) {
            if (!subUnit.isActive(currentDate)) {
                allInactiveSubUnits.add(subUnit);
            }
        }
        return allInactiveSubUnits;
    }

    public List<IUnit> getActiveSubUnits(Date currentDate) {
        List<IUnit> allActiveSubUnits = new ArrayList<IUnit>();
        for (IUnit subUnit : this.getSubUnits()) {
            if (subUnit.isActive(currentDate)) {
                allActiveSubUnits.add(subUnit);
            }
        }
        return allActiveSubUnits;
    }

    public void edit(String unitName, Integer unitCostCenter, Date beginDate, Date endDate,
            UnitType type, IUnit parentUnit) {

        this.setName(unitName);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setType(type);
        this.setCostCenterCode(unitCostCenter);
        if (parentUnit != null) {
            this.addParentUnits(parentUnit);
        }
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
    }

    public boolean isActive(Date currentDate) {
        if (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate))) {
            return true;
        }
        return false;
    }

    public void delete() {
        if (!hasAnySubUnits()
                && (!hasAnyParentUnits() || this.getParentUnits().size() == 1)
                && !hasAnyFunctions() && !hasAnyWorkingContracts()
                && !hasAnyMailingContracts() && !hasAnySalaryContracts()) {

            if (hasAnyParentUnits()) {
                this.removeParentUnits(this.getParentUnits().get(0));
            }
            removeDepartment();
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.unit");
        }
    }

    public List<IContract> getWorkingContracts(Date begin, Date end) {
        List<IContract> contracts = new ArrayList<IContract>();
        for (IContract contract : this.getWorkingContracts()) {
            if (contract.belongsToPeriod(begin, end)) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    public List<IUnit> getScientificAreaUnits() {
        List<IUnit> result = new ArrayList<IUnit>();
        for (IUnit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(UnitType.SCIENTIFIC_AREA)) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<IUnit> getCompetenceCourseGroupUnits() {
        List<IUnit> result = new ArrayList<IUnit>();
        for (IUnit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(UnitType.COMPETENCE_COURSE_GROUP)) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<IUnit> getDegreeUnits() {
        List<IUnit> result = new ArrayList<IUnit>();
        for (IUnit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(UnitType.DEGREE)) {
                result.add(unit);
            }
        }

        return result;
    }
}
