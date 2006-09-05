package net.sourceforge.fenixedu.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission.PrintMarkSheet;
import pt.utl.ist.fenix.tools.util.PropertiesManager;

public class ReportsUtils extends PropertiesManager {

    private static final Map<String, JasperReport> reportsMap = new HashMap<String, JasperReport>();

    private static final Properties properties = new Properties();

    static {
        try {
            loadProperties(properties, "/reports.properties");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties files.", e);
        }
    }

    public static boolean printReport(String key, Map parameters, ResourceBundle bundle,
            Collection dataSource, String printerName) {
        try {
            JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
            PrintService printService = PrinterManager.getPrintServiceByName(printerName);
            if (jasperPrint != null && printService != null) {
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                printRequestAttributeSet.add(MediaSizeName.ISO_A4);
                printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
                printRequestAttributeSet.add(new MediaPrintableArea(0, 0, 210, 297,
                        MediaPrintableArea.MM));

                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
                        printRequestAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
                exporter.exportReport();
                return true;
            } else {
                return false;
            }
        } catch (JRException e) {
            System.out.println("Unable to print");
            e.printStackTrace();
            return false;
        }

    }

    public static boolean exportToPdf(String key, Map parameters, ResourceBundle bundle,
            Collection dataSource, String destination) {
        try {
            JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
            if (jasperPrint != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, destination);
                return true;
            } else {
                return false;
            }
        } catch (JRException e) {
            return false;
        }

    }

    private static JasperPrint getReport(String key, Map parameters, ResourceBundle bundle,
            Collection dataSource) throws JRException {
        JasperReport report = reportsMap.get(key);
        if (report == null) {
            String reportFile = properties.getProperty(key);
            if (reportFile != null) {
                report = (JasperReport) JRLoader.loadObject(PrintMarkSheet.class
                        .getResourceAsStream(reportFile));
                reportsMap.put(key, report);
            }
        }
        if (report != null) {
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters,
                    new JRBeanCollectionDataSource(dataSource));
            return jasperPrint;
        }
        return null;
    }

    public static byte[] exportToPdf(String key, Map parameters, ResourceBundle bundle,
            Collection dataSource) throws JRException {
        try {
            JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
            if (jasperPrint != null) {
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        } catch (JRException e) {
            throw e;
        }
        return null;
    }

}
