package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class FloorInformation extends FloorInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created floor information", parameters = {
	    "floor", "level", "begin", "end", "blueprintNumber" })
    public FloorInformation(Floor floor, Integer level, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
	super();
	super.setSpace(floor);
	setLevel(level);
	setBlueprintNumber(blueprintNumber);
	setFirstTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited floor information", parameters = {
	    "level", "begin", "end", "blueprintNumber" })
    public void editFloorCharacteristics(Integer level, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
	setLevel(level);
	setBlueprintNumber(blueprintNumber);
	editTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted floor information", parameters = {})
    public void delete() {
	super.delete();
    }

    @Override
    public Floor getSpace() {
	return (Floor) super.getSpace();
    }

    @Override
    public void setSpace(final Space space) {
	throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Floor floor) {
	throw new DomainException("error.cannot.change.floor");
    }

    @Override
    public void setLevel(Integer level) {
	if (level == null) {
	    throw new DomainException("error.floor.level.cannot.be.null");
	}
	super.setLevel(level);
    }

    public FloorFactoryEditor getSpaceFactoryEditor() {
	final FloorFactoryEditor floorFactoryEditor = new FloorFactoryEditor();
	floorFactoryEditor.setSpace(getSpace());
	floorFactoryEditor.setLevel(getLevel());
	floorFactoryEditor.setBegin(getNextPossibleValidFromDate());
	return floorFactoryEditor;
    }

    @Override
    public String getPresentationName() {
	return String.valueOf(getLevel());
    }
}
