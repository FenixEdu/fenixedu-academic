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

public class Unit extends Unit_Base {

    public List<IUnit> getTopUnits() {
        IUnit unit = this;
        List<IUnit> allTopUnits = new ArrayList<IUnit>();
        if (!unit.getParentUnits().isEmpty()) {
            for (IUnit parentUnit : this.getParentUnits()) {
                if (parentUnit.getParentUnits().isEmpty() && !allTopUnits.contains(parentUnit)) {
                    allTopUnits.add(parentUnit);
                } else if (!parentUnit.getParentUnits().isEmpty()) {
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
        if(endDate != null && endDate.before(beginDate)){
            throw new DomainException("error.endDateBeforeBeginDate");
        }
    }

    public boolean isActive(Date currentDate) {
        if (this.getEndDate() == null
                || (this.getEndDate().after(currentDate) || this.getEndDate().equals(currentDate))) {
            return true;
        }
        return false;
    }

    public void delete() {
        if (this.getSubUnits().isEmpty()
                && (this.getParentUnits().isEmpty() || this.getParentUnits().size() == 1)
                && this.getFunctions().isEmpty() && this.getWorkingContracts().isEmpty()
                && this.getMailingContracts().isEmpty() && this.getSalaryContracts().isEmpty()) {

            if (!this.getParentUnits().isEmpty()) {
                this.removeParentUnits(this.getParentUnits().get(0));
            }
            this.removeDepartment();
            super.deleteDomainObject();

        } else {
            throw new DomainException("error.delete.unit");
        }
    }

    public List<IContract> getWorkingContracts(Date begin, Date end) {
        List<IContract> contracts = new ArrayList<IContract>();
        for (IContract contract : this.getWorkingContracts()) {
            if ((contract.getBeginDate().after(begin) || contract.getBeginDate().equals(begin))
                    && (contract.getEndDate() == null || (contract.getEndDate().before(end) || contract
                            .getEndDate().equals(end)))) {
                contracts.add(contract);
            }
        }
        return contracts;
    }
    
    public List<IUnit> getScientificAreas() {
        List<IUnit> result = new ArrayList<IUnit>();        
        for (IUnit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(UnitType.SCIENTIFIC_AREA)) {
                result.add(unit);                
            }
        }        
        return result;
    }
    
    public List<IUnit> getCompetenceCourseGroups() {
        List<IUnit> result = new ArrayList<IUnit>();        
        for (IUnit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(UnitType.COMPETENCE_COURSE_GROUP)) {
                result.add(unit);                
            }
        }
        return result;
    }
}
