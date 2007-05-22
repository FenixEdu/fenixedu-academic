package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkMovement;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class UnitExtraWorkAmountFactory implements Serializable, FactoryExecutor {
    private Integer year;

    private Integer costCenterCode;

    private Double amount;

    private DomainReference<Unit> unit;

    private DomainReference<UnitExtraWorkAmount> unitExtraWorkAmount;

    public UnitExtraWorkAmountFactory() {
        super();
        YearMonthDay date = new YearMonthDay();
        setYear(date.getYear());
    }

    public UnitExtraWorkAmountFactory(YearMonthDay date) {
        super();
        setYear(date.getYear());
    }

    public UnitExtraWorkAmountFactory(UnitExtraWorkAmount unitExtraWorkAmount) {
        setUnitExtraWorkAmount(unitExtraWorkAmount);
        setUnit(unitExtraWorkAmount.getUnit());
        setYear(unitExtraWorkAmount.getYear());
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(Integer costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public Unit getUnit() {
        return unit == null ? null : unit.getObject();
    }

    public void setUnit(Unit unit) {
        this.unit = unit != null ? new DomainReference<Unit>(unit) : null;
    }

    public UnitExtraWorkAmount getUnitExtraWorkAmount() {
        return unitExtraWorkAmount == null ? null : unitExtraWorkAmount.getObject();
    }

    public void setUnitExtraWorkAmount(UnitExtraWorkAmount unitExtraWorkAmount) {
        this.unitExtraWorkAmount = unitExtraWorkAmount != null ? new DomainReference<UnitExtraWorkAmount>(
                unitExtraWorkAmount)
                : null;
    }

    public List<UnitExtraWorkMovement> getOrderedMovements() {
        List<UnitExtraWorkMovement> unitExtraWorkMovements = new ArrayList<UnitExtraWorkMovement>();
        for (UnitExtraWorkMovement extraWorkMovement : getUnitExtraWorkAmount()
                .getUnitExtraWorkMovements()) {
            unitExtraWorkMovements.add(extraWorkMovement);
        }
        Collections.sort(unitExtraWorkMovements, new BeanComparator("date"));
        return unitExtraWorkMovements;
    }

    public Object execute() {
        if (getUnit() == null) {
            UnitExtraWorkAmount unitExtraWorkAmount = new UnitExtraWorkAmount(getYear(), Unit
                    .readByCostCenterCode(getCostCenterCode()));
            new UnitExtraWorkMovement(unitExtraWorkAmount, getAmount());
            return unitExtraWorkAmount;
        } else {
            new UnitExtraWorkMovement(getUnitExtraWorkAmount(), getAmount());
            return getUnitExtraWorkAmount();
        }
    }
}
