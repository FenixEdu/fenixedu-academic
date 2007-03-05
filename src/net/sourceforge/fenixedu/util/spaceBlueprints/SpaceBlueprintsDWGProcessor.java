package net.sourceforge.fenixedu.util.spaceBlueprints;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import com.iver.cit.jdwglib.dwg.objects.DwgMText;
import com.iver.cit.jdwglib.dwg.objects.DwgText;

public class SpaceBlueprintsDWGProcessor extends DWGProcessor {

    private Space parentSpace;

    private Boolean viewSpaceIdentifications;

    private Boolean viewBlueprintNumbers;

    private Boolean viewDoorNumbers;

    private Boolean suroundingSpaceBlueprint;

    private Boolean viewOriginalSpaceBlueprint;

    private Space thisSpace;

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Boolean viewBlueprintNumbers_,
	    Boolean viewSpaceIdentifications_, Boolean viewDoorNumbers_) throws IOException {

	super();
	this.suroundingSpaceBlueprint = false;
	this.viewOriginalSpaceBlueprint = false;

	this.viewDoorNumbers = viewDoorNumbers_;
	this.viewSpaceIdentifications = viewSpaceIdentifications_;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;

	this.parentSpace = parentSpace_;
    }

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Space thisSpace_,
	    Boolean viewBlueprintNumbers_, Boolean viewSpaceIdentifications_, Boolean viewDoorNumbers_)
	    throws IOException {

	super();
	this.viewOriginalSpaceBlueprint = false;
	this.suroundingSpaceBlueprint = true;

	this.viewDoorNumbers = viewDoorNumbers_;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.viewSpaceIdentifications = viewSpaceIdentifications_;

	this.thisSpace = thisSpace_;
	this.parentSpace = parentSpace_;
    }

    public SpaceBlueprintsDWGProcessor() throws IOException {
	super();	
	this.viewOriginalSpaceBlueprint = true;
    }
    
    @Override
    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgMText dwgMText) {
	
        String text = getText(dwgMText);        
	if (isToViewOriginalSpaceBlueprint() != null && isToViewOriginalSpaceBlueprint()) {
	    super.drawText(referenceConverter, graphics2D, dwgMText);

	} else {	    
	    int x = convXCoord(dwgMText.getInsertionPoint()[0], referenceConverter);
	    int y = convYCoord(dwgMText.getInsertionPoint()[1], referenceConverter);

	    Space discoveredSpace = getParentSpace().readSubSpaceByBlueprintNumber(text.trim());
	    String textToInsert = getTextToInsert(text, discoveredSpace, isToViewBlueprintNumbers(), isToViewSpaceIdentifications(), isToViewDoorNumbers());
	    drawTextAndArc(graphics2D, x, y, discoveredSpace, textToInsert);
	}		                    
    }
       
    @Override
    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgText dwgText) {

	if (isToViewOriginalSpaceBlueprint() != null && isToViewOriginalSpaceBlueprint()) {
	    super.drawText(referenceConverter, graphics2D, dwgText);

	} else {
	    final Point2D point2D = dwgText.getInsertionPoint();
	    int x = convXCoord(point2D.getX(), referenceConverter);
	    int y = convYCoord(point2D.getY(), referenceConverter);

	    Space discoveredSpace = getParentSpace().readSubSpaceByBlueprintNumber(dwgText.getText().trim());
	    String textToInsert = getTextToInsert(dwgText.getText(), discoveredSpace, isToViewBlueprintNumbers(), isToViewSpaceIdentifications(), isToViewDoorNumbers());
	    drawTextAndArc(graphics2D, x, y, discoveredSpace, textToInsert);
	}
    }

    public static BlueprintTextRectangles getBlueprintTextRectangles(final InputStream inputStream,
	    Space parentSpace, Boolean viewBlueprintNumbers, Boolean viewOriginalSpaceBlueprint,
	    Boolean viewSpaceIdentifications, Boolean viewDoorNumbers) throws IOException {

	BlueprintTextRectangles map = new BlueprintTextRectangles();
	if (viewOriginalSpaceBlueprint != null && viewOriginalSpaceBlueprint) {
	    return map;
	}

	File file = FileUtils.copyToTemporaryFile(inputStream);
	final SpaceBlueprintsDWGProcessor processor = new SpaceBlueprintsDWGProcessor();
	final DwgFile dwgFile = processor.readDwgFile(file.getAbsolutePath());	
	final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();		
	final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects, processor.scaleRatio);

	for (final DwgObject dwgObject : dwgObjects) {
	    
	    if (dwgObject instanceof DwgText) {
		DwgText dwgText = ((DwgText) dwgObject);
		final Point2D point2D = dwgText.getInsertionPoint();		
		Space discoveredSpace = parentSpace.readSubSpaceByBlueprintNumber(dwgText.getText().trim());
		String textToInsert = getTextToInsert(dwgText.getText(), discoveredSpace, viewBlueprintNumbers, viewSpaceIdentifications, viewDoorNumbers);
		putLinksCoordinatesToMap(map, processor, referenceConverter, point2D.getX(), point2D.getY(), textToInsert, discoveredSpace);
	    
	    } else if(dwgObject instanceof DwgMText) {
		DwgMText dwgMText = (DwgMText) dwgObject;		
		String text = getText(dwgMText);		
		Space discoveredSpace = parentSpace.readSubSpaceByBlueprintNumber(text.trim());		
		String textToInsert = getTextToInsert(text, discoveredSpace, viewBlueprintNumbers, viewSpaceIdentifications, viewDoorNumbers);
		putLinksCoordinatesToMap(map, processor, referenceConverter, dwgMText.getInsertionPoint()[0], dwgMText.getInsertionPoint()[1],
			textToInsert, discoveredSpace);
	    }
	}
	return map;
    }

    private static void putLinksCoordinatesToMap(BlueprintTextRectangles map, final SpaceBlueprintsDWGProcessor processor, final ReferenceConverter referenceConverter,
	    double x, double y, String textToInsert, Space space) {

	if (textToInsert != null) {

	    List<BlueprintTextRectangle> blueprintTextRectangules = map.get(space);
	    if (blueprintTextRectangules == null) {
		blueprintTextRectangules = new ArrayList<BlueprintTextRectangle>();
	    }

	    blueprintTextRectangules.add(new BlueprintTextRectangle(textToInsert, processor.convXCoord(x, referenceConverter), processor.convYCoord(y, referenceConverter), processor.fontSize));
	    map.put(space, blueprintTextRectangules);
	}
    }

    private void drawArcAroundText(Graphics2D graphics2D, int x, int y, String textToInsert) {

	double numberOfCharacters = textToInsert.length();
	double characterWidth = (fontSize / 1.6);
	double textSize = numberOfCharacters * characterWidth;

	int x1 = (int) (x - characterWidth);
	int y1 = (int) (y - (2 * fontSize));
	int width = (int) (Math.round(textSize) + (2 * characterWidth));
	int height = (int) (4 * fontSize);
	int startAngle = 0;
	int arcAngle = 360;

	graphics2D.setColor(Color.YELLOW);
	graphics2D.fillArc(x1, y1, width, height, startAngle, arcAngle);
	graphics2D.setColor(Color.BLACK);
    }

    private void drawTextAndArc(Graphics2D graphics2D, int x, int y, Space discoveredSpace, String textToInsert) {
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

    private static String getTextToInsert(String textToInsert, Space space,
	    Boolean isToViewBlueprintNumbers, Boolean isToViewSpaceIdentifications, Boolean isToViewDoorNumbers) {
	
	if (space != null) {
	    if (isToViewSpaceIdentifications != null && isToViewSpaceIdentifications) {
	
		SpaceInformation spaceInformation = space.getSpaceInformation();
		if (spaceInformation instanceof RoomInformation) {
		    textToInsert = ((RoomInformation) spaceInformation).getIdentification();

		} else if (spaceInformation instanceof FloorInformation) {
		    textToInsert = ((FloorInformation) spaceInformation).getLevel().toString();

		} else if (spaceInformation instanceof CampusInformation) {
		    textToInsert = ((CampusInformation) spaceInformation).getName();

		} else if (spaceInformation instanceof BuildingInformation) {
		    textToInsert = ((BuildingInformation) spaceInformation).getName();
		}

		return textToInsert;

	    } else if (isToViewDoorNumbers != null && isToViewDoorNumbers) {
			
		SpaceInformation spaceInformation = space.getSpaceInformation();
		if (spaceInformation instanceof RoomInformation) {
		    textToInsert = ((RoomInformation) spaceInformation).getDoorNumber();
		}
		return textToInsert;
		
	    } else if (isToViewBlueprintNumbers != null && isToViewBlueprintNumbers) {		
		return textToInsert;
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

    public Boolean isToViewSpaceIdentifications() {
	return viewSpaceIdentifications;
    }

    public Boolean isToViewDoorNumbers() {
	return viewDoorNumbers;
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
