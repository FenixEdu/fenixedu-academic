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

    public IUnit getTopUnit() {
        IUnit unit = this;
        if (unit.getParentUnit() != null) {
            while (unit.getParentUnit() != null) {
                unit = unit.getParentUnit();
            }
        }
        return unit;
    }

    public void edit(String unitName, String unitCostCenter, Date beginDate, Date endDate,
            UnitType type, IUnit parentUnit) {
        this.setName(unitName);
        if(unitCostCenter != null && !unitCostCenter.equals("")){
            this.setCostCenterCode(Integer.valueOf(unitCostCenter));
        }
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setType(type);
        this.setParentUnit(parentUnit);
    }

    public boolean isActive(Date currentDate) {
        if (this.getEndDate() == null
                || (this.getEndDate() != null && (this.getEndDate().after(currentDate) || this
                        .getEndDate().equals(currentDate)))) {
            return true;
        }
        return false;
    }

    public void delete() {
        if (this.getAssociatedUnits().isEmpty() && this.getFunctions().isEmpty()
                && this.getWorkingContracts().isEmpty() && this.getMailingContracts().isEmpty()
                && this.getSalaryContracts().isEmpty()) {

            this.setParentUnit(null);
            this.setDepartment(null);
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
}
