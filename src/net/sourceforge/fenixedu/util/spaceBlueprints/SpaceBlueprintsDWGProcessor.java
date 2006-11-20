package net.sourceforge.fenixedu.util.spaceBlueprints;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.space.BuildingInformation;
import net.sourceforge.fenixedu.domain.space.CampusInformation;
import net.sourceforge.fenixedu.domain.space.FloorInformation;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangle;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangles;
import pt.utl.ist.fenix.tools.image.DWGProcessor;
import pt.utl.ist.fenix.tools.util.FileUtils;

import com.iver.cit.jdwglib.dwg.DwgFile;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.objects.DwgText;

public class SpaceBlueprintsDWGProcessor extends DWGProcessor {

    private Space parentSpace;

    private Boolean viewBlueprintNumbers;

    private Boolean suroundingSpaceBlueprint;

    private Boolean viewOriginalSpaceBlueprint;

    private Space thisSpace;

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Boolean viewBlueprintNumbers_)
	    throws IOException {

	super();
	this.suroundingSpaceBlueprint = false;
	this.viewOriginalSpaceBlueprint = false;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.parentSpace = parentSpace_;
    }

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Space thisSpace_,
	    Boolean viewBlueprintNumbers_) throws IOException {

	super();
	this.viewOriginalSpaceBlueprint = false;
	this.suroundingSpaceBlueprint = true;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.thisSpace = thisSpace_;
	this.parentSpace = parentSpace_;
    }

    public SpaceBlueprintsDWGProcessor() throws IOException {
	super();
	this.viewOriginalSpaceBlueprint = true;
    }

    @Override
    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgText dwgText) {

	if (isToViewOriginalSpaceBlueprint() != null && isToViewOriginalSpaceBlueprint()) {
	    super.drawText(referenceConverter, graphics2D, dwgText);

	} else {
	    final Point2D point2D = dwgText.getInsertionPoint();
	    int x = convXCoord(point2D.getX(), referenceConverter);
	    int y = convYCoord(point2D.getY(), referenceConverter);

	    Space discoveredSpace = getParentSpace().readSubSpaceByBlueprintNumber(
		    dwgText.getText().trim());
	    String textToInsert = getTextToInsert(dwgText, discoveredSpace, isToViewBlueprintNumbers());
	    drawTextAndArc(graphics2D, x, y, discoveredSpace, textToInsert);
	}
    }

    public static BlueprintTextRectangles getBlueprintTextRectangles(final InputStream inputStream,
	    Space parentSpace, Boolean viewBlueprintNumbers, Boolean viewOriginalSpaceBlueprint)
	    throws IOException {

	BlueprintTextRectangles map = new BlueprintTextRectangles();
	if (viewOriginalSpaceBlueprint != null && viewOriginalSpaceBlueprint) {
	    return map;
	}

	File file = FileUtils.copyToTemporaryFile(inputStream);
	final SpaceBlueprintsDWGProcessor processor = new SpaceBlueprintsDWGProcessor();
	final DwgFile dwgFile = processor.readDwgFile(file.getAbsolutePath());
	final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
	final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects,
		processor.scaleRatio);

	for (final DwgObject dwgObject : dwgObjects) {
	    if (dwgObject instanceof DwgText) {
		DwgText dwgText = ((DwgText) dwgObject);
		final Point2D point2D = dwgText.getInsertionPoint();
		Space discoveredSpace = parentSpace.readSubSpaceByBlueprintNumber(dwgText.getText()
			.trim());
		String textToInsert = getTextToInsert(dwgText, discoveredSpace, viewBlueprintNumbers);
		putLinksCoordinatesToMap(map, processor, referenceConverter, point2D, textToInsert,
			discoveredSpace);
	    }
	}
	return map;
    }

    private static void putLinksCoordinatesToMap(BlueprintTextRectangles map,
	    final SpaceBlueprintsDWGProcessor processor, final ReferenceConverter referenceConverter,
	    final Point2D point2D, String textToInsert, Space space) {

	if (textToInsert != null) {
	    map.put(space, new BlueprintTextRectangle(textToInsert, processor.convXCoord(point2D.getX(),
		    referenceConverter), processor.convYCoord(point2D.getY(), referenceConverter),
		    processor.fontSize));
	}
    }

    private void drawArcAroundText(Graphics2D graphics2D, int x, int y, String textToInsert) {

	double numberOfCharacters = textToInsert.length();
	double characterWidth = (fontSize / 1.6);
	double textSize = numberOfCharacters * characterWidth;
	
	int x1 = (int)(x - characterWidth);
	int y1 = (int) (y - (2 * fontSize));
	int width = (int) (Math.round(textSize) + (2 * characterWidth));
	int height = (int) (4 * fontSize);
	int startAngle = 0;
	int arcAngle = 360;

	graphics2D.setColor(Color.YELLOW);
	graphics2D.fillArc(x1, y1, width, height, startAngle, arcAngle);
	graphics2D.setColor(Color.BLACK);
    }  

    private void drawTextAndArc(Graphics2D graphics2D, int x, int y, Space discoveredSpace,
	    String textToInsert) {

	if (textToInsert != null) {
	    if (isSuroundingSpaceBlueprint() != null && isSuroundingSpaceBlueprint()
		    && getThisSpace() != null && discoveredSpace.equals(getThisSpace())) {

		drawArcAroundText(graphics2D, x, y, textToInsert);
		graphics2D.drawString(textToInsert, x, y);

	    } else {
		graphics2D.drawString(textToInsert, x, y);
	    }
	}
    }

    private static String getTextToInsert(DwgText dwgText, Space space, Boolean isToViewBlueprintNumbers) {
	if (space != null) {
	    if (isToViewBlueprintNumbers != null && !isToViewBlueprintNumbers) {
		String textToInsert = dwgText.getText();
		SpaceInformation spaceInformation = space.getSpaceInformation();

		if (spaceInformation instanceof RoomInformation) {
		    textToInsert = ((RoomInformation) space.getSpaceInformation()).getIdentification();

		} else if (spaceInformation instanceof FloorInformation) {
		    textToInsert = ((FloorInformation) space.getSpaceInformation()).getLevel()
			    .toString();

		} else if (spaceInformation instanceof CampusInformation) {
		    textToInsert = ((CampusInformation) space.getSpaceInformation()).getName();

		} else if (spaceInformation instanceof BuildingInformation) {
		    textToInsert = ((BuildingInformation) space.getSpaceInformation()).getName();
		}

		return textToInsert;
	    } else {
		return dwgText.getText();
	    }
	}
	return null;
    }

    public Space getParentSpace() {
	return parentSpace;
    }

    public Boolean isToViewBlueprintNumbers() {
	return viewBlueprintNumbers;
    }

    public Boolean isSuroundingSpaceBlueprint() {
	return suroundingSpaceBlueprint;
    }

    public Boolean getViewBlueprintNumbers() {
	return viewBlueprintNumbers;
    }

    public Space getThisSpace() {
	return thisSpace;
    }

    public Boolean isToViewOriginalSpaceBlueprint() {
	return viewOriginalSpaceBlueprint;
    }
}
