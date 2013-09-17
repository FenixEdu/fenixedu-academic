package net.sourceforge.fenixedu.domain.util;

import net.sourceforge.fenixedu.util.PrinterManager;

public class FunctionalityPrinters extends FunctionalityPrinters_Base {

    public FunctionalityPrinters() {
        super();
    }

    public String[] getPrinterNames() {
        return PrinterManager.getFunctionPrinterNames(getKeyPrinters());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Unit> getUnits() {
        return getUnitsSet();
    }

    @Deprecated
    public boolean hasAnyUnits() {
        return !getUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasKeyPrinters() {
        return getKeyPrinters() != null;
    }

    @Deprecated
    public boolean hasFunctionality() {
        return getFunctionality() != null;
    }

}
