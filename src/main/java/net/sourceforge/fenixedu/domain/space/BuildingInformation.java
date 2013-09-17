package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class BuildingInformation extends BuildingInformation_Base {

    @FenixDomainObjectActionLogAnnotation(actionName = "Created building information", parameters = { "building", "name",
            "begin", "end", "blueprintNumber" })
    public BuildingInformation(Building building, String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber,
            String emails) {
        super();
        super.setSpace(building);
        setName(name);
        setBlueprintNumber(blueprintNumber);
        setFirstTimeInterval(begin, end);
        setEmails(emails);
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Edited building information", parameters = { "name", "begin", "end",
            "blueprintNumber" })
    public void editBuildingCharacteristics(String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber,
            String emails) {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToEditSpaceInformation);
        setName(name);
        setBlueprintNumber(blueprintNumber);
        editTimeInterval(begin, end);
        setEmails(emails);
    }

    @Override
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted building information", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation);
        super.delete();
    }

    @Override
    public void setName(final String name) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.building.name.cannot.be.null");
        }
        super.setName(name);
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Building building) {
        throw new DomainException("error.cannot.change.building");
    }

    @Override
    public BuildingFactoryEditor getSpaceFactoryEditor() {
        final BuildingFactoryEditor buildingFactoryEditor = new BuildingFactoryEditor();
        buildingFactoryEditor.setSpace((Building) getSpace());
        buildingFactoryEditor.setName(getName());
        buildingFactoryEditor.setBegin(getNextPossibleValidFromDate());
        return buildingFactoryEditor;
    }

    @Override
    public String getPresentationName() {
        return getName();
    }

    @Override
    public RoomClassification getRoomClassification() {
        // Necessary for Renderers
        return null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

}
