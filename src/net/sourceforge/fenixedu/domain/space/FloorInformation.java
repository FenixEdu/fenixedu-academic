package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class FloorInformation extends FloorInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created room information", parameters = {
	    "floor", "level", "begin", "end" })
    public FloorInformation(Floor floor, Integer level, YearMonthDay begin, YearMonthDay end) {
	super();
	super.setSpace(floor);
	setLevel(level);
	setFirstTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited room information", parameters = {
	    "level", "begin", "end" })
    public void editFloorCharacteristics(Integer level, YearMonthDay begin, YearMonthDay end) {
	setLevel(level);
	editTimeInterval(begin, end);
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
