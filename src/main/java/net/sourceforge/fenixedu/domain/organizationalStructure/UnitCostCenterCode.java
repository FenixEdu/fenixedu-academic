package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class UnitCostCenterCode extends UnitCostCenterCode_Base {

    public UnitCostCenterCode(final Unit unit, final Integer costCenterCode) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setUnit(unit);
        setCostCenterCode(costCenterCode);
    }

    public void delete() {
        setUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static UnitCostCenterCode find(final Integer costCenterCode) {
        if (costCenterCode != null) {
            for (final UnitCostCenterCode unitCostCenterCode : RootDomainObject.getInstance().getUnitCostCenterCodesSet()) {
                if (unitCostCenterCode.getCostCenterCode().equals(costCenterCode)) {
                    return unitCostCenterCode;
                }
            }
        }
        return null;
    }

    @Deprecated
    public boolean hasCostCenterCode() {
        return getCostCenterCode() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
