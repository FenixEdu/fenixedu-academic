package net.sourceforge.fenixedu.util.spaceBlueprints;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangle;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangles;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.image.DWGProcessor;
import pt.utl.ist.fenix.tools.util.FileUtils;

import com.iver.cit.jdwglib.dwg.DwgFile;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.objects.DwgText;

public class SpaceBlueprintsDWGProcessor extends DWGProcessor {

    private Space parentSpace;

    private Boolean viewBlueprintNumbers;

    private Boolean suroundingSpaceBlueprint;

    private Space thisSpace;

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Boolean viewBlueprintNumbers_,
	    Boolean suroundingSpaceBlueprint_) throws IOException {

	super();
	this.suroundingSpaceBlueprint = suroundingSpaceBlueprint_;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.parentSpace = parentSpace_;
    }

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Space thisSpace_,
	    Boolean viewBlueprintNumbers_, Boolean suroundingSpaceBlueprint_) throws IOException {

	super();
	this.thisSpace = thisSpace_;
	this.suroundingSpaceBlueprint = suroundingSpaceBlueprint_;
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.parentSpace = parentSpace_;
    }

    @Override
    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgText dwgText) {

	final Point2D point2D = dwgText.getInsertionPoint();
	int x = convXCoord(point2D.getX(), referenceConverter);
	int y = convYCoord(point2D.getY(), referenceConverter);

	Room room = (StringUtils.isNumeric(dwgText.getText())) ? getParentSpace()
		.readRoomByBlueprintNumber(Integer.valueOf(dwgText.getText())) : null;

	String textToInsert = getTextToInsert(dwgText, room, isToViewBlueprintNumbers());
	if (textToInsert != null) {
	    graphics2D.drawString(textToInsert, x, y);
	}

	if (textToInsert != null && isSuroundingSpaceBlueprint() != null && isSuroundingSpaceBlueprint()
		&& thisSpace != null && room.equals(thisSpace)) {

	    int characters = textToInsert.length();
	    double x_offset = characters * fontSize;
	    double y_offset = fontSize;

	    int x1 = x;
	    int y1 = (int) (y - y_offset);
	    int width = (int) (x_offset / 2);
	    int height = (int) (y_offset);
	    int startAngle = 0;
	    int arcAngle = 360;

	    graphics2D.drawArc(x1, y1, width, height, startAngle, arcAngle);
	}
    }

    public static BlueprintTextRectangles getBlueprintTextReactangles(final InputStream inputStream,
	    Space parentSpace, Boolean viewBlueprintNumbers, Boolean suroundingSpaceBlueprint)
	    throws IOException {

	File file = FileUtils.copyToTemporaryFile(inputStream);
	final SpaceBlueprintsDWGProcessor processor = new SpaceBlueprintsDWGProcessor(parentSpace,
		viewBlueprintNumbers, suroundingSpaceBlueprint);
	final DwgFile dwgFile = processor.readDwgFile(file.getAbsolutePath());
	final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
	final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects,
		processor.scaleRatio);
	BlueprintTextRectangles map = new BlueprintTextRectangles();

	for (final DwgObject dwgObject : dwgObjects) {
	    if (dwgObject instanceof DwgText) {
		DwgText dwgText = ((DwgText) dwgObject);
		final Point2D point2D = dwgText.getInsertionPoint();
		Room room = (StringUtils.isNumeric(dwgText.getText())) ? parentSpace
			.readRoomByBlueprintNumber(Integer.valueOf(dwgText.getText())) : null;
		String textToInsert = getTextToInsert(dwgText, room, viewBlueprintNumbers);
		if (textToInsert != null) {
		    map.put(room, new BlueprintTextRectangle(textToInsert, processor.convXCoord(point2D
			    .getX(), referenceConverter), processor.convYCoord(point2D.getY(),
			    referenceConverter), processor.fontSize));
		}
	    }
	}
	return map;
    }

    private static String getTextToInsert(DwgText dwgText, Room room, Boolean isToViewBlueprintNumbers) {
	if (room != null) {
	    if (isToViewBlueprintNumbers != null && !isToViewBlueprintNumbers) {
		String identification = ((RoomInformation) room.getSpaceInformation())
			.getIdentification();
		if (!StringUtils.isEmpty(identification)) {
		    return identification;
		}
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
}
