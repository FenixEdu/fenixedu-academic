package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactory;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactoryEditor;

public class BuildingInformation extends BuildingInformation_Base {

    protected BuildingInformation(final Building building, final BuildingFactory buildingFactory) {
	super();	
	Space.checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getUserView().getPerson(), building);
	super.setSpace(building);
	setName(buildingFactory.getName());
	setTimeInterval(buildingFactory.getBegin(), buildingFactory.getEnd());
    }

    @Override
    public void setName(final String name) {
	if (name == null) {
	    throw new NullPointerException("error.building.name.cannot.be.null");
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
