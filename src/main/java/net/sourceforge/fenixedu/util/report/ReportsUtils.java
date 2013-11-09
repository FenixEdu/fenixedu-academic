package net.sourceforge.fenixedu.util.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import net.sf.jasperreports.engine.JRAbstractExporter;
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
import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;
import net.sourceforge.fenixedu.util.PrinterManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class ReportsUtils extends PropertiesManager {

    private static final Logger logger = LoggerFactory.getLogger(ReportsUtils.class);

    static final private Map<String, JasperReport> reportsMap = new ConcurrentHashMap<String, JasperReport>();

    static final private Properties properties = new Properties();

    static final private String reportsPropertiesFile = "/reports.properties";

    static {
        try {
            loadProperties(properties, reportsPropertiesFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties files.", e);
        }
    }

    static public Map<String, JasperReport> getReportsMap() {
        return reportsMap;
    }

    static public Properties getProperties() {
        return properties;
    }

    static public String getReportsPropertiesFile() {
        return reportsPropertiesFile;
    }

    static public boolean exportToPdfFile(String key, Map parameters, ResourceBundle bundle, Collection dataSource,
            String destination) {
        try {
            final JasperPrint jasperPrint = createJasperPrint(key, parameters, bundle, dataSource);
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

    static public byte[] exportToPdfFileAsByteArray(final String key, final Map parameters, final ResourceBundle bundle,
            final Collection dataSource) throws JRException {
        final JasperPrint jasperPrint = createJasperPrint(key, parameters, bundle, dataSource);

        if (jasperPrint != null) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            export(new JRPdfExporter(), Collections.singletonList(jasperPrint), baos, (PrintService) null,
                    (PrintRequestAttributeSet) null);
            return baos.toByteArray();
        }

        return null;
    }

    static public byte[] exportToProcessedPdfAsByteArray(final FenixReport report) throws JRException {
        return exportMultipleToProcessedPdfAsByteArray(report.getPreProcessor(), report);
    }

    static public byte[] exportMultipleToPdfAsByteArray(final FenixReport... reports) throws JRException {
        return exportMultipleToProcessedPdfAsByteArray((JasperPrintProcessor) null, reports);
    }

    static public byte[] exportMultipleToProcessedPdfAsByteArray(final JasperPrintProcessor processor,
            final FenixReport... reports) throws JRException {
        final List<JasperPrint> partials = new ArrayList<JasperPrint>();

        for (final FenixReport report : reports) {
            JasperPrint jasperPrint =
                    createJasperPrint(report.getReportTemplateKey(), report.getParameters(), report.getResourceBundle(),
                            report.getDataSource());

            if (jasperPrint == null) {
                throw new NullPointerException();
            } else {
                if (processor != null) {
                    jasperPrint = processor.process(jasperPrint);
                }
            }

            partials.add(jasperPrint);
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        export(new JRPdfExporter(), partials, baos, (PrintService) null, (PrintRequestAttributeSet) null);
        return baos.toByteArray();
    }

    static public byte[] stampPdfAt(byte[] originalPdf, byte[] toStampPdf, int positionX, int positionY) {
        try {
            PdfReader originalPdfReader = new PdfReader(originalPdf);
            PdfReader toStampPdfReader = new PdfReader(toStampPdf);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(originalPdfReader, stream);

            PdfImportedPage importedPage = stamper.getImportedPage(toStampPdfReader, 1);

            PdfContentByte overContent = stamper.getOverContent(1);

            Rectangle pageSizeWithRotation = originalPdfReader.getPageSizeWithRotation(1);
            Rectangle pageSizeWithRotationStamper = toStampPdfReader.getPageSizeWithRotation(1);

            System.out.println(String.format("[ %s, %s]", pageSizeWithRotation.getWidth(), pageSizeWithRotation.getHeight()));
            System.out.println(String.format("[ %s, %s]", pageSizeWithRotationStamper.getWidth(),
                    pageSizeWithRotationStamper.getHeight()));

            Image image = Image.getInstance(importedPage);

            overContent.addImage(image, image.getWidth(), 0f, 0f, image.getHeight(), positionX, positionY);

            stamper.close();

            originalPdfReader.close();
            toStampPdfReader.close();

            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static public boolean printReport(final String key, final Map parameters, final ResourceBundle bundle,
            final Collection dataSource, final String printerName) {
        try {
            final JasperPrint jasperPrint = createJasperPrint(key, parameters, bundle, dataSource);
            final PrintService printService = PrinterManager.getPrintServiceByName(printerName);
            if (jasperPrint != null && printService != null) {
                export(new JRPrintServiceExporter(), Collections.singletonList(jasperPrint), (ByteArrayOutputStream) null,
                        printService, createPrintRequestAttributeSet(210, 297));

                if (LogLevel.INFO) {
                    logger.info("Printer Job Sent");
                }

                return true;
            } else {
                if (jasperPrint == null) {
                    if (LogLevel.ERROR) {
                        logger.info("Couldn't find report " + key);
                    }
                }

                if (printService == null) {
                    if (LogLevel.ERROR) {
                        logger.info("Couldn't find print service " + printerName);
                    }
                }

                return false;
            }
        } catch (JRException e) {
            if (LogLevel.ERROR) {
                logger.info("Unable to print");
            }
            e.printStackTrace();
            return false;
        }

    }

    static private JasperPrint createJasperPrint(final String key, final Map parameters, final ResourceBundle bundle,
            Collection dataSource) throws JRException {
        JasperReport report = reportsMap.get(key);

        if (report == null) {
            final String reportFileName = properties.getProperty(key);
            if (reportFileName != null) {
                report = (JasperReport) JRLoader.loadObject(ReportsUtils.class.getResourceAsStream(reportFileName));
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
                dataSource = Collections.singletonList(StringUtils.EMPTY);
            }

            return JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(dataSource));
        }

        return null;
    }

    static private PrintRequestAttributeSet createPrintRequestAttributeSet(int width, int height) {
        final PrintRequestAttributeSet result = new HashPrintRequestAttributeSet();

        result.add(MediaSizeName.ISO_A4);
        result.add(OrientationRequested.PORTRAIT);
        result.add(new MediaPrintableArea(0, 0, width, height, MediaPrintableArea.MM));

        return result;
    }

    static private void export(final JRAbstractExporter exporter, final List<JasperPrint> prints,
            final ByteArrayOutputStream stream, final PrintService printService,
            final PrintRequestAttributeSet printRequestAttributeSet) throws JRException {
        exporter.setParameter(JRExporterParameter.FONT_MAP, createFontMap());

        if (prints.size() == 1) {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, prints.iterator().next());
        } else {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, prints);
        }

        if (stream != null) {
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
        }

        if (printService != null) {
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
        }

        if (printRequestAttributeSet != null) {
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        }

        exporter.exportReport();
        return;
    }

    static private Map<FontKey, PdfFont> createFontMap() {
        final Map<FontKey, PdfFont> result = new HashMap<FontKey, PdfFont>(4);

        addFont(result, "Quadraat-Regular", "QUAD____.ttf", BaseFont.CP1252);

        addFont(result, "Quadraat-Bold", "QUADBD__.ttf", BaseFont.CP1252);

        addFont(result, "Quadraat-Italic", "QUADI___.ttf", BaseFont.CP1252);

        addFont(result, "Quadraat-BoldItalic", "QUADBDI_.ttf", BaseFont.CP1252);

        addFont(result, "Arial", "Arial.ttf", BaseFont.CP1252);

        addFont(result, "Arial Black", "Arial_Black.ttf", BaseFont.CP1252);

        addFont(result, "Lucida Handwriting", "LucidaHandwriting.ttf", BaseFont.CP1252);

        addFont(result, "Garamond", "AdobeGaramondPro.ttf", BaseFont.CP1252);

        addFont(result, "Garamond Bold", "AdobeGaramondBold.ttf", BaseFont.CP1252);

        addFont(result, "Arial Unicode MS", "arialuni.ttf", BaseFont.IDENTITY_H);

        addFont(result, "DejaVu Serif Bold", "DejaVuSerif-Bold.ttf", BaseFont.IDENTITY_H);

        addFont(result, "Times New Roman", "tnr.ttf", BaseFont.CP1252);

        return result;
    }

    static private void addFont(final Map<FontKey, PdfFont> result, final String fontName, final String pdfFontName,
            final String baseFont) {
        final URL url = ReportsUtils.class.getClassLoader().getResource("fonts/" + pdfFontName);
        result.put(new FontKey(fontName, false, false), new PdfFont(url.toExternalForm(), baseFont, true));
    }

}
