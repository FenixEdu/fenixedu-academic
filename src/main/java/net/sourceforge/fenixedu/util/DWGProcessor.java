package net.sourceforge.fenixedu.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import javax.imageio.ImageIO;

import pt.utl.ist.fenix.tools.util.FileUtils;

import com.iver.cit.jdwglib.dwg.DwgFile;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.objects.DwgArc;
import com.iver.cit.jdwglib.dwg.objects.DwgAttdef;
import com.iver.cit.jdwglib.dwg.objects.DwgAttrib;
import com.iver.cit.jdwglib.dwg.objects.DwgBlock;
import com.iver.cit.jdwglib.dwg.objects.DwgBlockHeader;
import com.iver.cit.jdwglib.dwg.objects.DwgCircle;
import com.iver.cit.jdwglib.dwg.objects.DwgEllipse;
import com.iver.cit.jdwglib.dwg.objects.DwgEndblk;
import com.iver.cit.jdwglib.dwg.objects.DwgInsert;
import com.iver.cit.jdwglib.dwg.objects.DwgLayer;
import com.iver.cit.jdwglib.dwg.objects.DwgLine;
import com.iver.cit.jdwglib.dwg.objects.DwgLwPolyline;
import com.iver.cit.jdwglib.dwg.objects.DwgMText;
import com.iver.cit.jdwglib.dwg.objects.DwgPoint;
import com.iver.cit.jdwglib.dwg.objects.DwgPolyline2D;
import com.iver.cit.jdwglib.dwg.objects.DwgSeqend;
import com.iver.cit.jdwglib.dwg.objects.DwgSolid;
import com.iver.cit.jdwglib.dwg.objects.DwgText;
import com.iver.cit.jdwglib.dwg.objects.DwgVertex2D;

public class DWGProcessor {

    private static final String FONT_NAME = "Bitstream Vera Sans Mono";

    protected final int scaleRatio;

    protected final int fontSize;

    protected final int padding;

    protected final int xAxisOffset;

    protected final int yAxisOffset;

    protected final BigDecimal scalePercentage;

    protected BigDecimal HUNDRED_PERCENTAGE = BigDecimal.valueOf(100);

    public DWGProcessor() throws IOException {
        this(BigDecimal.valueOf(100));
    }

    public DWGProcessor(BigDecimal percentageOfScale) throws IOException {

        scalePercentage =
                (percentageOfScale == null || percentageOfScale.compareTo(HUNDRED_PERCENTAGE) == 1) ? HUNDRED_PERCENTAGE : percentageOfScale;

        String scaleRatioString = FenixConfigurationManager.getConfiguration().scaleRatio();
        String fontSizeString = FenixConfigurationManager.getConfiguration().fontSize();
        String paddingString = FenixConfigurationManager.getConfiguration().padding();
        String xAxisOffsetString = FenixConfigurationManager.getConfiguration().xAxisOffset();
        String yAxisOffsetString = FenixConfigurationManager.getConfiguration().yAxisOffset();

        Double scaleDouble = Double.valueOf(scaleRatioString);
        scaleRatio =
                (int) scalePercentage.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(scaleDouble)).doubleValue();

        fontSize = (int) (scaleRatio * Double.valueOf(fontSizeString));
        padding = (int) (scaleRatio * Double.valueOf(paddingString));
        xAxisOffset = (int) (scaleRatio * Double.valueOf(xAxisOffsetString));
        yAxisOffset = (int) (scaleRatio * Double.valueOf(yAxisOffsetString));
    }

    protected static String constructOutputFilename(final File inputFile, final String outputDirname) {
        final String simplename = inputFile.getName();
        return outputDirname + "/" + simplename.substring(0, simplename.length() - 3) + "jpg";
    }

    public void generateJPEGImage(final InputStream inputStream, final OutputStream outputStream) throws IOException {

        final File file = FileUtils.copyToTemporaryFile(inputStream);
        generateJPEGImage(file.getAbsolutePath(), outputStream);
    }

    public void generateJPEGImage(final String filename, final OutputStream outputStream) throws IOException {

        final BufferedImage bufferedImage = process(filename, outputStream);
        ImageIO.write(bufferedImage, "jpg", outputStream);
        outputStream.close();
    }

    protected BufferedImage process(final String filename, final OutputStream outputStream) throws IOException {

        final DwgFile dwgFile = readDwgFile(filename);
        final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
        final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects, scaleRatio);
        final BufferedImage bufferedImage =
                new BufferedImage((int) referenceConverter.convX(referenceConverter.maxX),
                        (int) referenceConverter.convY(referenceConverter.minY), BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        graphics2D.setBackground(Color.WHITE);
        graphics2D.setColor(Color.BLACK);
        graphics2D.clearRect(0, 0, (int) referenceConverter.convX(referenceConverter.maxX),
                (int) referenceConverter.convY(referenceConverter.minY));

        for (final DwgObject dwgObject : dwgObjects) {
            drawObject(referenceConverter, graphics2D, dwgObject);
        }

        graphics2D.dispose();
        return bufferedImage;
    }

    private void drawObject(final ReferenceConverter referenceConverter, final Graphics2D graphics2D, final DwgObject dwgObject) {

        if (dwgObject instanceof DwgLine) {
            final DwgLine dwgLine = (DwgLine) dwgObject;
            drawLine(referenceConverter, graphics2D, dwgLine);

        } else if (dwgObject instanceof DwgArc) {
            final DwgArc dwgArc = (DwgArc) dwgObject;
            drawArc(referenceConverter, graphics2D, dwgArc);

        } else if (dwgObject instanceof DwgText) {
            final DwgText dwgText = (DwgText) dwgObject;
            drawText(referenceConverter, graphics2D, dwgText);

        } else if (dwgObject instanceof DwgMText) {
            final DwgMText dwgMText = (DwgMText) dwgObject;
            drawText(referenceConverter, graphics2D, dwgMText);

        } else if (dwgObject instanceof DwgLwPolyline) {
            final DwgLwPolyline dwgLwPolyline = (DwgLwPolyline) dwgObject;
            drawPolygonLine(referenceConverter, graphics2D, dwgLwPolyline);

        } else if (dwgObject instanceof DwgEllipse) {
            final DwgEllipse dwgEllipse = (DwgEllipse) dwgObject;
            drawEllipse(referenceConverter, graphics2D, dwgEllipse);

        } else if (dwgObject instanceof DwgCircle) {
            final DwgCircle dwgCircle = (DwgCircle) dwgObject;
            drawCircle(referenceConverter, graphics2D, dwgCircle);
        }
    }

    protected void drawCircle(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgCircle dwgCircle) {

        final double radius = dwgCircle.getRadius();
        final double xc = dwgCircle.getCenter()[0];
        final double yc = dwgCircle.getCenter()[1];
        final int startAngle = 0;
        final int endAngle = 360;

        graphics2DDrawArc(referenceConverter, graphics2D, radius, xc, yc, startAngle, endAngle);
    }

    protected void drawEllipse(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgEllipse dwgEllipse) {

        final double width = ReferenceConverter.getEllipseWidth(dwgEllipse);
        final double heigth = ReferenceConverter.getEllipseHeigth(dwgEllipse);

        final double xc = dwgEllipse.getCenter()[0];
        final double yc = dwgEllipse.getCenter()[1];
        final double ti = dwgEllipse.getInitAngle();
        final double tf = dwgEllipse.getEndAngle();

        final int startAngle;
        final int endAngle;
        if (tf > ti) {
            startAngle = calcDegreeAngle(ti);
            endAngle = calcDegreeAngle(Math.abs(Math.abs(tf) - Math.abs(ti)));
        } else {
            startAngle = calcDegreeAngle(tf);
            endAngle = -1 * calcDegreeAngle(Math.abs(Math.abs(ti) - Math.abs(tf + 2 * Math.PI)));
        }

        final int xmax = convXCoord(xc - (width / 2), referenceConverter);
        final int ymax = convYCoord(yc + (heigth / 2), referenceConverter);
        graphics2D.drawArc(xmax, ymax, (int) width, (int) heigth, startAngle, endAngle);
    }

    protected void drawPolygonLine(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgLwPolyline dwgLwPolyline) {

        Point2D[] vertices = dwgLwPolyline.getVertices();
        if (vertices != null && vertices.length > 1) {
            for (int i = 0; i < vertices.length; i++) {
                Point2D point2D = vertices[i];
                if (i < (vertices.length - 1)) {
                    Point2D nextPoint2D = vertices[i + 1];
                    drawLine(referenceConverter, graphics2D, point2D, nextPoint2D);
                }
            }
        }
    }

    protected void drawLine(ReferenceConverter referenceConverter, Graphics2D graphics2D, Point2D point2DToDraw,
            Point2D nextPoint2D) {
        graphics2DDrawLine(referenceConverter, graphics2D, point2DToDraw.getX(), point2DToDraw.getY(), nextPoint2D.getX(),
                nextPoint2D.getY());
    }

    protected void drawLine(final ReferenceConverter referenceConverter, final Graphics2D graphics2D, final DwgLine dwgLine) {
        graphics2DDrawLine(referenceConverter, graphics2D, dwgLine.getP1()[0], dwgLine.getP1()[1], dwgLine.getP2()[0],
                dwgLine.getP2()[1]);
    }

    protected void drawArc(final ReferenceConverter referenceConverter, final Graphics2D graphics2D, final DwgArc dwgArc) {

        final double radius = dwgArc.getRadius();
        final double xc = dwgArc.getCenter()[0];
        final double yc = dwgArc.getCenter()[1];
        final double ti = dwgArc.getInitAngle();
        final double tf = dwgArc.getEndAngle();

        final int startAngle;
        final int endAngle;
        if (tf > ti) {
            startAngle = calcDegreeAngle(ti);
            endAngle = calcDegreeAngle(Math.abs(Math.abs(tf) - Math.abs(ti)));
        } else {
            startAngle = calcDegreeAngle(tf);
            endAngle = -1 * calcDegreeAngle(Math.abs(Math.abs(ti) - Math.abs(tf + 2 * Math.PI)));
        }

        graphics2DDrawArc(referenceConverter, graphics2D, radius, xc, yc, startAngle, endAngle);
    }

    protected void drawText(final ReferenceConverter referenceConverter, final Graphics2D graphics2D, final DwgText dwgText) {
        final Point2D point2D = dwgText.getInsertionPoint();
        graphics2D.drawString(dwgText.getText(), convXCoord(point2D.getX(), referenceConverter),
                convYCoord(point2D.getY(), referenceConverter));
    }

    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgMText dwgMText) {
        graphics2D.drawString(getText(dwgMText), convXCoord(dwgMText.getInsertionPoint()[0], referenceConverter),
                convYCoord(dwgMText.getInsertionPoint()[1], referenceConverter));
    }

    protected static String getText(DwgMText dwgText) {
        String text = dwgText.getText();
        String[] strings = text.split(";");
        if (strings.length > 1) {
            strings = strings[strings.length - 1].split("}");
            text = strings[0];
        }
        return text;
    }

    private void graphics2DDrawArc(ReferenceConverter referenceConverter, Graphics2D graphics2D, final double radius,
            final double xc, final double yc, final int startAngle, final int endAngle) {

        final int xmax = convXCoord(xc - radius, referenceConverter);
        final int ymax = convYCoord(yc + radius, referenceConverter);

        final int xmin = convXCoord(xc + radius, referenceConverter);
        final int ymin = convYCoord(yc - radius, referenceConverter);

        graphics2D.drawArc(xmax, ymax, Math.abs(xmax - xmin), Math.abs(ymax - ymin), startAngle, endAngle);
    }

    private void graphics2DDrawLine(final ReferenceConverter referenceConverter, final Graphics2D graphics2D, final double x1,
            final double y1, final double x2, final double y2) {

        int x1_ = convXCoord(x1, referenceConverter);
        int y1_ = convYCoord(y1, referenceConverter);
        int x2_ = convXCoord(x2, referenceConverter);
        int y2_ = convYCoord(y2, referenceConverter);

        graphics2D.drawLine(x1_, y1_, x2_, y2_);
    }

    protected DwgFile readDwgFile(final String filename) throws IOException {
        final DwgFile dwgFile = new DwgFile(filename);

        dwgFile.read();
        initializeDwgFile(dwgFile);

        return dwgFile;
    }

    private void initializeDwgFile(final DwgFile dwgFile) {
        dwgFile.initializeLayerTable();
        dwgFile.applyExtrusions();
        dwgFile.blockManagement();
        dwgFile.calculateCadModelDwgPolylines();
        dwgFile.calculateGisModelDwgPolylines();
    }

    protected int calcDegreeAngle(final double radians) {
        return (int) Math.round((radians * 180) / Math.PI);
    }

    protected int convXCoord(final double coordinate, final ReferenceConverter referenceConverter) {
        return (int) referenceConverter.convX(coordinate);
    }

    protected int convYCoord(final double coordinate, final ReferenceConverter referenceConverter) {
        return (int) referenceConverter.convY(coordinate);
    }

    public static class ReferenceConverter {
        double minX = Double.MAX_VALUE;

        double maxX = Double.MAX_VALUE * -1.0;

        double minY = Double.MAX_VALUE;

        double maxY = Double.MAX_VALUE * -1.0;

        int scaleRatio = 0;

        public ReferenceConverter(final Vector<DwgObject> dwgObjects, int scaleRatio) {
            for (final DwgObject dwgObject : dwgObjects) {

                if (dwgObject instanceof DwgText) {
                    final DwgText dwgText = (DwgText) dwgObject;

                    minX = Math.min(minX, dwgText.getInsertionPoint().getX());
                    minY = Math.min(minY, dwgText.getInsertionPoint().getY());

                    maxX = Math.max(maxX, dwgText.getInsertionPoint().getX());
                    maxY = Math.max(maxY, dwgText.getInsertionPoint().getY());

                } else if (dwgObject instanceof DwgArc) {
                    final DwgArc dwgArc = (DwgArc) dwgObject;
                    final double ti = dwgArc.getInitAngle();
                    final double tf = dwgArc.getEndAngle();

                    if (ti != tf) {

                        final double r = dwgArc.getRadius();
                        final double xc = dwgArc.getCenter()[0];
                        final double yc = dwgArc.getCenter()[1];

                        final double xi = r * Math.cos(ti) + xc;
                        final double yi = r * Math.sin(ti) + yc;

                        final double xf = r * Math.cos(tf) + xc;
                        final double yf = r * Math.sin(tf) + yc;

                        minX = Math.min(minX, xi);
                        minX = Math.min(minX, xf);
                        minY = Math.min(minY, yi);
                        minY = Math.min(minY, yf);

                        maxX = Math.max(maxX, xi);
                        maxX = Math.max(maxX, xf);
                        maxY = Math.max(maxY, yi);
                        maxY = Math.max(maxY, yf);
                    }

                } else if (dwgObject instanceof DwgLine) {
                    final DwgLine dwgLine = (DwgLine) dwgObject;

                    minX = Math.min(minX, dwgLine.getP1()[0]);
                    minY = Math.min(minY, dwgLine.getP1()[1]);
                    maxX = Math.max(maxX, dwgLine.getP1()[0]);
                    maxY = Math.max(maxY, dwgLine.getP1()[1]);

                    minX = Math.min(minX, dwgLine.getP2()[0]);
                    minY = Math.min(minY, dwgLine.getP2()[1]);
                    maxX = Math.max(maxX, dwgLine.getP2()[0]);
                    maxY = Math.max(maxY, dwgLine.getP2()[1]);

                } else if (dwgObject instanceof DwgLwPolyline) {
                    final DwgLwPolyline dwgLwPolyline = (DwgLwPolyline) dwgObject;

                    Point2D[] vertices = dwgLwPolyline.getVertices();
                    if (vertices != null) {
                        for (Point2D point2D : vertices) {
                            minX = Math.min(minX, point2D.getX());
                            minY = Math.min(minY, point2D.getY());
                            maxX = Math.max(maxX, point2D.getX());
                            maxY = Math.max(maxY, point2D.getY());
                        }
                    }

                } else if (dwgObject instanceof DwgEllipse) {
                    final DwgEllipse dwgEllipse = (DwgEllipse) dwgObject;
                    final double ti = dwgEllipse.getInitAngle();
                    final double tf = dwgEllipse.getEndAngle();

                    if (ti != tf) {

                        final double width = getEllipseWidth(dwgEllipse);
                        final double heigth = getEllipseHeigth(dwgEllipse);

                        final double xc = dwgEllipse.getCenter()[0];
                        final double yc = dwgEllipse.getCenter()[1];

                        final double xi = (width / 2) * Math.cos(ti) + xc;
                        final double yi = (heigth / 2) * Math.sin(ti) + yc;

                        final double xf = (width / 2) * Math.cos(tf) + xc;
                        final double yf = (heigth / 2) * Math.sin(tf) + yc;

                        minX = Math.min(minX, xi);
                        minX = Math.min(minX, xf);
                        minY = Math.min(minY, yi);
                        minY = Math.min(minY, yf);

                        maxX = Math.max(maxX, xi);
                        maxX = Math.max(maxX, xf);
                        maxY = Math.max(maxY, yi);
                        maxY = Math.max(maxY, yf);
                    }

                } else if (dwgObject instanceof DwgCircle) {
                    final DwgCircle dwgCircle = (DwgCircle) dwgObject;

                    final int ti = 0;
                    final int tf = 360;

                    final double r = dwgCircle.getRadius();
                    final double xc = dwgCircle.getCenter()[0];
                    final double yc = dwgCircle.getCenter()[1];

                    final double xi = r * Math.cos(ti) + xc;
                    final double yi = r * Math.sin(ti) + yc;

                    final double xf = r * Math.cos(tf) + xc;
                    final double yf = r * Math.sin(tf) + yc;

                    minX = Math.min(minX, xi);
                    minX = Math.min(minX, xf);
                    minY = Math.min(minY, yi);
                    minY = Math.min(minY, yf);

                    maxX = Math.max(maxX, xi);
                    maxX = Math.max(maxX, xf);
                    maxY = Math.max(maxY, yi);
                    maxY = Math.max(maxY, yf);

                } else if (dwgObject instanceof DwgMText) {
                    final DwgMText dwgMText = (DwgMText) dwgObject;

                    minX = Math.min(minX, dwgMText.getInsertionPoint()[0]);
                    minY = Math.min(minY, dwgMText.getInsertionPoint()[1]);

                    maxX = Math.max(maxX, dwgMText.getInsertionPoint()[0]);
                    maxY = Math.max(maxY, dwgMText.getInsertionPoint()[1]);

                } else if (dwgObject instanceof DwgPoint) {
                    // final DwgPoint dwgPoint = (DwgPoint) dwgObject;

                } else if (dwgObject instanceof DwgBlockHeader) {
                    // final DwgBlockHeader dwgBlockHeader = (DwgBlockHeader) dwgObject;

                } else if (dwgObject instanceof DwgLayer) {
                    // final DwgLayer dwgLayer = (DwgLayer) dwgObject;

                } else if (dwgObject instanceof DwgSolid) {
                    // final DwgSolid dwgSolid = (DwgSolid) dwgObject;

                } else if (dwgObject instanceof DwgBlock) {
                    // final DwgBlock dwgBlock = (DwgBlock) dwgObject;

                } else if (dwgObject instanceof DwgEndblk) {
                    // final DwgEndblk dwgEndblk = (DwgEndblk) dwgObject;		   			 

                } else if (dwgObject instanceof DwgInsert) {
                    // final DwgInsert dwgInsert = (DwgInsert) dwgObject;

                } else if (dwgObject instanceof DwgAttdef) {
                    // final DwgAttdef dwgAttdef = (DwgAttdef) dwgObject;

                } else if (dwgObject instanceof DwgAttrib) {
                    // final DwgAttrib dwgAttrib = (DwgAttrib) dwgObject;

                } else if (dwgObject instanceof DwgSeqend) {
                    // final DwgSeqend dwgSeqend = (DwgSeqend) dwgObject;

                } else if (dwgObject instanceof DwgPolyline2D) {
                    //final DwgPolyline2D dwgPolyline2D = (DwgPolyline2D) dwgObject;

                } else if (dwgObject instanceof DwgVertex2D) {
                    //final DwgVertex2D dwgPolyline2D = (DwgVertex2D) dwgObject;

                } else {
                    // System.out.println(" ********* DWG Processor -> Unknown DwgObject: " + dwgObject.getClass().getName());
                }
            }

            this.scaleRatio = (int) Math.round(scaleRatio / (1 - (minX / maxX)));
        }

        private double maxAbsValue(double maxX, double minX, double maxY, double minY) {
            return Math.max(Math.max(Math.abs(maxX), Math.abs(minX)), Math.max(Math.abs(maxY), Math.abs(minY)));
        }

        public static double getEllipseWidth(final DwgEllipse dwgEllipse) {
            final double[] vector = dwgEllipse.getMajorAxisVector();
            return vector[0] == 0 ? vector[1] * 2 * dwgEllipse.getAxisRatio() : vector[0] * 2;
        }

        public static double getEllipseHeigth(final DwgEllipse dwgEllipse) {
            final double[] vector = dwgEllipse.getMajorAxisVector();
            return vector[1] == 0 ? vector[0] * 2 * dwgEllipse.getAxisRatio() : vector[1] * 2;
        }

        public double convX(final double x) {
            return (x - minX) * scaleRatio / maxX;
        }

        public double convY(final double y) {
            return (maxY - y) * scaleRatio / maxX;
        }
    }

    public static void main(String[] args) {
        try {
            final File inputDir = new File(args[0]);
            final String outputDirname = args[1];
            final DWGProcessor processor = new DWGProcessor();
            for (final File file : inputDir.listFiles()) {
                if (file.isFile()) {
                    final String inputFilename = file.getAbsolutePath();
                    if (inputFilename.endsWith(".dwg") || inputFilename.endsWith(".DWG")) {
                        final String outputFilename = constructOutputFilename(file, outputDirname);
                        final OutputStream outputStream = new FileOutputStream(outputFilename);
                        try {
                            processor.generateJPEGImage(inputFilename, outputStream);
                        } catch (Error error) {
                            error.printStackTrace();
                        } finally {
                            outputStream.close();
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
