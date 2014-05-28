/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
