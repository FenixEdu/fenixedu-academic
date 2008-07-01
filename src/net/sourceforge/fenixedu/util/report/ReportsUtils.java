package net.sourceforge.fenixedu.util.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;
import net.sourceforge.fenixedu.util.PrinterManager;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

import com.lowagie.text.pdf.BaseFont;

public class ReportsUtils extends PropertiesManager {

    private static final Map<String, JasperReport> reportsMap = new HashMap<String, JasperReport>();

    private static final Properties properties = new Properties();

    private static final String reportsPropertiesFile = "/reports.properties";

    static {
	try {
	    loadProperties(properties, reportsPropertiesFile);
	} catch (IOException e) {
	    throw new RuntimeException("Unable to load properties files.", e);
	}
    }

    public static Map<String, JasperReport> getReportsMap() {
	return reportsMap;
    }

    public static Properties getProperties() {
	return properties;
    }

    public static String getReportsPropertiesFile() {
	return reportsPropertiesFile;
    }

    public static boolean printReport(String key, Map parameters, ResourceBundle bundle, Collection dataSource, String printerName) {
	try {
	    JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
	    PrintService printService = PrinterManager.getPrintServiceByName(printerName);
	    if (jasperPrint != null && printService != null) {
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
		printRequestAttributeSet.add(new MediaPrintableArea(0, 0, 210, 297, MediaPrintableArea.MM));

		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
		exporter.exportReport();
		System.out.println("Printer Job Sent");
		return true;
	    } else {
		if (jasperPrint == null) {
		    System.out.println("Couldn't find report " + key);
		}
		if (printService == null) {
		    System.out.println("Couldn't find print service " + printerName);
		}

		return false;
	    }
	} catch (JRException e) {
	    System.out.println("Unable to print");
	    e.printStackTrace();
	    return false;
	}

    }

    public static boolean printReport(String key, Map parameters, ResourceBundle bundle, Collection dataSource, int width,
	    int height) {
	try {
	    JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
	    if (jasperPrint != null) {
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		printRequestAttributeSet.add(new MediaPrintableArea(0, 0, width, height, MediaPrintableArea.MM));
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		// exporter
		// .setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
		// Boolean.TRUE);
		exporter.exportReport();
		System.out.println("Printer Job Sent");
		return true;
	    } else {
		if (jasperPrint == null) {
		    System.out.println("Couldn't find report " + key);
		}
		return false;
	    }
	} catch (JRException e) {
	    System.out.println("Unable to print");
	    e.printStackTrace();
	    return false;
	}

    }

    private static JasperPrint getReport(String key, Map parameters, ResourceBundle bundle, Collection dataSource)
	    throws JRException {
	JasperReport report = reportsMap.get(key);
	if (report == null) {
	    String reportFileName = properties.getProperty(key);
	    if (reportFileName != null) {
		// Miracle solution for reloading jasper reports on the fly
		// while developing...
		File reportFile = new File("/home/alien/eclipseProjects/Fenix/build/WEB-INF/classes/" + reportFileName);
		report = reportFile.exists() ? (JasperReport) JRLoader.loadObject(reportFile) : (JasperReport) JRLoader
			.loadObject(ReportsUtils.class.getResourceAsStream(reportFileName));

		reportsMap.put(key, report);
	    }
	}
	if (report != null) {
	    if (parameters != null && bundle != null) {
		parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);
	    }

	    if (dataSource == null || dataSource.isEmpty()) {
		// dummy, engine seems to work not very well with empty data
		// sources
		dataSource = new ArrayList();
		dataSource.add(StringUtils.EMPTY);
	    }
	    JasperPrint jasperPrint = JasperFillManager
		    .fillReport(report, parameters, new JRBeanCollectionDataSource(dataSource));
	    return jasperPrint;
	}
	return null;
    }
    
    static public byte[] exportToPdf(final FenixReport report) throws JRException {
	return exportToPdfWithPreProcessing(report.getReportTemplateKey(), report.getParameters(), report.getResourceBundle(),
		report.getDataSource(), report.getPreProcessor());
    }

    static public byte[] exportMultipleToPdf(final FenixReport... reports) throws JRException {

	final List<JasperPrint> partials = new ArrayList<JasperPrint>();
	for (final FenixReport report : reports) {
	    final JasperPrint jasperPrint = getReport(report.getReportTemplateKey(), report.getParameters(), report
		    .getResourceBundle(), report.getDataSource());

	    if (jasperPrint == null) {
		throw new NullPointerException();
	    }

	    partials.add(jasperPrint);

	}

	final JRPdfExporter exporter = new JRPdfExporter();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, partials);

	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

	exporter.setParameter(JRExporterParameter.FONT_MAP, createFontMap());

	exporter.exportReport();

	return baos.toByteArray();

    }

    static public byte[] exportToPdf(String key, Map parameters, ResourceBundle bundle, Collection dataSource) throws JRException {
	return exportToPdfWithPreProcessing(key, parameters, bundle, dataSource, null);
    }

    private static byte[] exportToPdfWithPreProcessing(String key, Map parameters, ResourceBundle bundle, Collection dataSource,
	    JasperPrintProcessor processor) throws JRException {
	try {
	    JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
	    if (jasperPrint != null) {

		if (processor != null) {
		    jasperPrint = processor.process(jasperPrint);
		}

		final JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		exporter.setParameter(JRExporterParameter.FONT_MAP, createFontMap());

		exporter.exportReport();
		return baos.toByteArray();
	    }
	} catch (JRException e) {
	    throw e;
	}
	return null;
    }

    static private Map<FontKey, PdfFont> createFontMap() {
	final Map<FontKey, PdfFont> result = new HashMap<FontKey, PdfFont>(4);

	GenericPair<FontKey, PdfFont> aux = createFont("Quadraat-Regular", "/QUAD____.ttf");
	result.put(aux.getLeft(), aux.getRight());

	aux = createFont("Quadraat-Bold", "QUADBD__.ttf");
	result.put(aux.getLeft(), aux.getRight());

	aux = createFont("Quadraat-Italic", "QUADI___.ttf");
	result.put(aux.getLeft(), aux.getRight());

	aux = createFont("Quadraat-BoldItalic", "QUADBDI_.ttf");
	result.put(aux.getLeft(), aux.getRight());

	return result;
    }

    static private GenericPair<FontKey, PdfFont> createFont(final String fontName, final String pdfFontName) {
	return new GenericPair<FontKey, PdfFont>(new FontKey(fontName, false, false), new PdfFont(System.getenv("JAVA_HOME")
		+ "/jre/lib/fonts/" + pdfFontName, BaseFont.CP1252, true));
    }

    static public boolean exportToPdfFile(String key, Map parameters, ResourceBundle bundle, Collection dataSource,
	    String destination) {
	try {
	    final JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
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

    public static void exportToHtmlFile(String destFileName, String key, Map parameters, ResourceBundle bundle,
	    Collection dataSource) throws JRException {
	try {
	    JasperPrint jasperPrint = getReport(key, parameters, bundle, dataSource);
	    if (jasperPrint != null) {
		JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
	    }
	} catch (JRException e) {
	    throw e;
	}
    }

}
