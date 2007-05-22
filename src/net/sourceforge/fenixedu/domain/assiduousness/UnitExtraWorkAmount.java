package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitExtraWorkAmount extends UnitExtraWorkAmount_Base {

    public UnitExtraWorkAmount(Integer year, Unit unit) {
        super();
        if (!isUniqueYearUnit(year,unit)) {
            throw new DomainException("error.extraWorkAmount.alreadyExistsUnitYear");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setYear(year);
        setUnit(unit);
        setSpent(0.0);
    }

    private boolean isUniqueYearUnit(Integer year, Unit unit) {
        for (UnitExtraWorkAmount unitExtraWorkAmount : RootDomainObject.getInstance()
                .getUnitsExtraWorkAmounts()) {
            if (unitExtraWorkAmount != this && unitExtraWorkAmount.getYear() == year
                    && unitExtraWorkAmount.getUnit() == unit) {
                return false;
            }
        }
        return true;
    }

    public Double getTotal() {
        Double total = 0.0;
        for (UnitExtraWorkMovement unitExtraWorkMovement : getUnitExtraWorkMovements()) {
            total += unitExtraWorkMovement.getAmount();
        }
        return total;
    }

    public Double getBalance() {
        return getTotal() - getSpent();
    }
}
