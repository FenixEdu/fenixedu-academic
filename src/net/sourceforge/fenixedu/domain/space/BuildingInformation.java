package net.sourceforge.fenixedu.domain.space;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactory;
import net.sourceforge.fenixedu.domain.space.Building.BuildingFactoryEditor;

public class BuildingInformation extends BuildingInformation_Base {

    protected BuildingInformation(final Building building, final BuildingFactory buildingFactory) {
	super();
	super.setSpace(building);
	setFirstTimeInterval(buildingFactory.getBegin(), buildingFactory.getEnd());
	setName(buildingFactory.getName());	
    }
    
    public void editBuildingCharacteristics(String name, YearMonthDay begin, YearMonthDay end) {
	editTimeInterval(begin, end);
	setName(name);
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
