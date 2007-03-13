package net.sourceforge.fenixedu.domain.util;

import net.sourceforge.fenixedu.util.PrinterManager;

public class FunctionalityPrinters extends FunctionalityPrinters_Base {
    
    public  FunctionalityPrinters() {
        super();
    }

    public String[] getPrinterNames() {
	return PrinterManager.getFunctionPrinterNames(getKeyPrinters());
    }
    
}
