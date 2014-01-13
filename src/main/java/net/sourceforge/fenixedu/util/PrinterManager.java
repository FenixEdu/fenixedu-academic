package net.sourceforge.fenixedu.util;

import java.util.HashMap;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterManager {
    private static final Logger logger = LoggerFactory.getLogger(PrinterManager.class);

    private Map<String, PrintService> printers = new HashMap<>();
    private Map<String, String[]> printerNames = new HashMap<>();
    private PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();

    PrinterManager(Map<String, String> printerNames) {
        for (String key : printerNames.keySet()) {
            String[] names = printerNames.get(key).trim().split("\\s*,\\s*");
            this.printerNames.put(key, names);
        }
        loadPrinters();
    }

    public PrintService getPrintServiceByName(String name) {
        if (name == null) {
            logger.info("returning default printer: " + defaultPrinter);
            return defaultPrinter;
        }
        logger.info("returning non default printer: " + printers.get(name));
        return printers.get(name);
    }

    public String[] getFunctionPrinterNames(String key) {
        if (printerNames.get(key) != null) {
            return printerNames.get(key);
        }
        return new String[0];
    }

    private void loadPrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            printers.put(service.getName(), service);
            logger.info("printer: {} service: {}", service.getName(), service);
        }
    }

}
