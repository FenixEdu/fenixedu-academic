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

    public SpaceBlueprintsDWGProcessor(Space parentSpace_, Boolean viewBlueprintNumbers_)
	    throws IOException {
	super();
	this.viewBlueprintNumbers = viewBlueprintNumbers_;
	this.parentSpace = parentSpace_;
    }

    @Override
    protected void drawText(ReferenceConverter referenceConverter, Graphics2D graphics2D, DwgText dwgText) {	
	if (isViewBlueprintNumbers() != null && !isViewBlueprintNumbers()) {	    
	    final Point2D point2D = dwgText.getInsertionPoint();
	    String text = dwgText.getText();
	    if (StringUtils.isNumeric(text)) {
		Integer blueprintNumber = Integer.valueOf(text);
		Room room = getParentSpace().readRoomByBlueprintNumber(blueprintNumber);
		if (room != null) {
		    String identification = ((RoomInformation) room.getSpaceInformation())
			    .getIdentification();
		    if (!StringUtils.isEmpty(identification)) {
			int x = convXCoord(point2D.getX(), referenceConverter);
			int y = convYCoord(point2D.getY(), referenceConverter);
			graphics2D.drawString(identification, x, y);
		    }
		}
	    }
	} else {
	    super.drawText(referenceConverter, graphics2D, dwgText);
	}
    }

    public static BlueprintTextRectangles getBlueprintTextReactangles(final InputStream inputStream,
	    Space parentSpace, Boolean viewBlueprintNumbers) throws IOException {

	File file = FileUtils.copyToTemporaryFile(inputStream);
	final SpaceBlueprintsDWGProcessor processor = new SpaceBlueprintsDWGProcessor(parentSpace,
		viewBlueprintNumbers);
	final DwgFile dwgFile = processor.readDwgFile(file.getAbsolutePath());
	final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
	final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects,
		processor.scaleRatio);

	BlueprintTextRectangles map = new BlueprintTextRectangles();
	for (final DwgObject dwgObject : dwgObjects) {
	    if (dwgObject instanceof DwgText) {
		DwgText dwgText = ((DwgText) dwgObject);
		if (StringUtils.isNumeric(dwgText.getText())) {
		    Integer blueprintNumber = Integer.valueOf(dwgText.getText());
		    Room room = parentSpace.readRoomByBlueprintNumber(blueprintNumber);
		    if (room != null) {
			final Point2D point2D = dwgText.getInsertionPoint();
			map.put(room, new BlueprintTextRectangle(dwgText.getText(), processor
				.convXCoord(point2D.getX(), referenceConverter), processor.convYCoord(
				point2D.getY(), referenceConverter), processor.fontSize));
		    }
		}
	    }
	}
	return map;
    }

    public Space getParentSpace() {
	return parentSpace;
    }

    public Boolean isViewBlueprintNumbers() {
	return viewBlueprintNumbers;
    }
}
