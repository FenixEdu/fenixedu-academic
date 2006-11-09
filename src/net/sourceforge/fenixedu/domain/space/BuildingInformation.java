package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class BuildingInformation extends BuildingInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created building information", parameters = {
	    "building", "name", "begin", "end" })
    public BuildingInformation(Building building, String name, YearMonthDay begin, YearMonthDay end) {
	super();
	super.setSpace(building);
	setName(name);
	setFirstTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited building information", parameters = {
	    "name", "begin", "end" })
    public void editBuildingCharacteristics(String name, YearMonthDay begin, YearMonthDay end) {
	setName(name);
	editTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted building information", parameters = {})
    public void delete() {
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

}
