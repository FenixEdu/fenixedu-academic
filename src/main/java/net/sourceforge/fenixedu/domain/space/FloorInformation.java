package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.joda.time.YearMonthDay;

public class FloorInformation extends FloorInformation_Base {

    @FenixDomainObjectActionLogAnnotation(actionName = "Created floor information", parameters = { "floor", "level", "begin",
            "end", "blueprintNumber" })
    public FloorInformation(Floor floor, Integer level, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
        super();
        super.setSpace(floor);
        setLevel(level);
        setBlueprintNumber(blueprintNumber);
        setFirstTimeInterval(begin, end);
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Edited floor information", parameters = { "level", "begin", "end",
            "blueprintNumber" })
    public void editFloorCharacteristics(Integer level, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToEditSpaceInformation);
        setLevel(level);
        setBlueprintNumber(blueprintNumber);
        editTimeInterval(begin, end);
    }

    @Override
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted floor information", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation);
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

    @Override
    public FloorFactoryEditor getSpaceFactoryEditor() {
        final FloorFactoryEditor floorFactoryEditor = new FloorFactoryEditor();
        floorFactoryEditor.setSpace(getSpace());
        floorFactoryEditor.setLevel(getLevel());
        floorFactoryEditor.setBegin(getNextPossibleValidFromDate());
        return floorFactoryEditor;
    }

    @Override
    public String getPresentationName() {
        Space suroundingSpace = getSpace().getSuroundingSpace();
        if (suroundingSpace != null && suroundingSpace.isFloor()) {
            FloorInformation suroundingSpaceInformation = ((Floor) suroundingSpace).getSpaceInformation();
            if (getLevel().intValue() == 0) {
                return suroundingSpaceInformation.getPresentationName();
            } else if (getLevel().intValue() == 1 && suroundingSpaceInformation.getPresentationName().contains("i")) {
                return suroundingSpaceInformation.getPresentationName().replace("i", "s");
            } else if (getLevel().intValue() == 1 && !suroundingSpaceInformation.getPresentationName().contains("i")) {
                return suroundingSpaceInformation.getPresentationName().concat("i");
            }
        }
        return String.valueOf(getLevel());
    }

    @Override
    public RoomClassification getRoomClassification() {
        // Necessary for Renderers
        return null;
    }

    @Deprecated
    public boolean hasLevel() {
        return getLevel() != null;
    }

}
