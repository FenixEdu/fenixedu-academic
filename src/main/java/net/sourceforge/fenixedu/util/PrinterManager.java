package net.sourceforge.fenixedu.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterManager {

    private static final Logger LOG = LoggerFactory.getLogger(PrinterManager.class);

    private static final Map<String, PrintService> printers = new HashMap<String, PrintService>();
    private static final PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
    private static final Map<String, String[]> printerNames = new HashMap<String, String[]>();
    private static final String PREFIX = "markSheet.printers.";

    static {
        LOG.info("Start loading printers configuration");
        loadPrinters();
        loadPrinterNames();
    }

    public static PrintService getPrintServiceByName(String name) {
        if (name == null) {
            System.out.println("returning default printer: " + defaultPrinter);
            return defaultPrinter;
        } else {
            System.out.println("returning non default printer: " + printers.get(name));
            return printers.get(name);
        }
    }

    public static String[] getFunctionPrinterNames(String key) {
        if (printerNames.get(key) != null) {
            return printerNames.get(key);
        }
        return new String[0];
    }

    private static void loadPrinterNames() {
        Properties properties = PropertiesManager.getProperties();
        for (Object key : properties.keySet()) {
            String function = (String) key;
            if (function.startsWith(PREFIX)) {
                String string = properties.getProperty(function);
                String[] names = string.split(",");
                printerNames.put(function, names);
                LOG.info("{}={}", function, string);
            }
        }
    }

    private static void loadPrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            printers.put(service.getName(), service);
            LOG.info("printer: {} service: {}", service.getName(), service);
        }
    }

}
