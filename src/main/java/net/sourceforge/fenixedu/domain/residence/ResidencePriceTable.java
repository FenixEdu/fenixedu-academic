package net.sourceforge.fenixedu.domain.residence;

public class ResidencePriceTable extends ResidencePriceTable_Base {

    public ResidencePriceTable() {
        super();
    }

    public boolean isConfigured() {
        return getDoubleRoomValue() != null && getSingleRoomValue() != null && getPaymentLimitDay() != null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit> getUnits() {
        return getUnitsSet();
    }

    @Deprecated
    public boolean hasAnyUnits() {
        return !getUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDoubleRoomValue() {
        return getDoubleRoomValue() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSingleRoomValue() {
        return getSingleRoomValue() != null;
    }

    @Deprecated
    public boolean hasPaymentLimitDay() {
        return getPaymentLimitDay() != null;
    }

}
