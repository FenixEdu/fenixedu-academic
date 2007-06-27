package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class Building extends Building_Base {
    
    public Building(Space surroundingSpace, String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {	
	super();
	setSuroundingSpace(surroundingSpace);
	new BuildingInformation(this, name, begin, end, blueprintNumber);
    }

    @Override
    public void setSuroundingSpace(Space suroundingSpace) {
        if(suroundingSpace != null && !suroundingSpace.isCampus()) {
            throw new DomainException("error.Space.invalid.suroundingSpace");
        }
	super.setSuroundingSpace(suroundingSpace);
    }
    
    @Override
    public BuildingInformation getSpaceInformation() {
	return (BuildingInformation) super.getSpaceInformation();
    }

    @Override
    public BuildingInformation getSpaceInformation(final YearMonthDay when) {
	return (BuildingInformation) super.getSpaceInformation(when);
    }

    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted building", parameters = {})
    public void delete() {
	super.delete();
    }

    @Override
    public boolean isBuilding() {
	return true;
    }
    
    @Deprecated
    public String getName() {
	return getNameWithCampus();
    }
    
    public String getNameWithCampus() {
	Campus campus = getSpaceCampus();
	if(campus == null) {
	    return getSpaceInformation().getName();
	} else {
	    return getSpaceInformation().getName() + " - (" + getSpaceCampus().getSpaceInformation().getName() + ")";
	}
    }
         
    public static List<Building> getActiveBuildingsByNames(List<String> names){
	List<Building> result = new ArrayList<Building>();
	for (Resource space : RootDomainObject.getInstance().getResources()) {
	    if(space.isBuilding() && ((Building)space).isActive()) {
		Building building = (Building) space;
		for (String name : names) {
		    if (building.getName().equals(name)) {
			result.add((Building) space);
		    }
		}	 
	    }
	}
	return result;
    }

    public Floor readFloorByLevel(Integer floorNumber) {
	List<Space> containedSpaces = getContainedSpaces();
	for (Space space : containedSpaces) {
	    if(space.isFloor() && ((Floor)space).getSpaceInformation().getLevel().equals(floorNumber)) {
		return (Floor) space;
	    }
	}
	return null;
    }
      
    public static abstract class BuildingFactory implements Serializable, FactoryExecutor {
	private String name;

	private YearMonthDay begin;

	private YearMonthDay end;

	private String blueprintNumber;

	public YearMonthDay getBegin() {
	    return begin;
	}

	public void setBegin(YearMonthDay begin) {
	    this.begin = begin;
	}

	public YearMonthDay getEnd() {
	    return end;
	}

	public void setEnd(YearMonthDay end) {
	    this.end = end;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getBlueprintNumber() {
	    return blueprintNumber;
	}

	public void setBlueprintNumber(String blueprintNumber) {
	    this.blueprintNumber = blueprintNumber;
	}
    }

    public static class BuildingFactoryCreator extends BuildingFactory {

	private DomainReference<Space> surroundingSpaceReference;

	public Space getSurroundingSpace() {
	    return surroundingSpaceReference == null ? null : surroundingSpaceReference.getObject();
	}

	public void setSurroundingSpace(Space surroundingSpace) {
	    if (surroundingSpace != null) {
		this.surroundingSpaceReference = new DomainReference<Space>(surroundingSpace);
	    }
	}

	public Building execute() {
	    return new Building(getSurroundingSpace(), getName(), getBegin(), getEnd(),
		    getBlueprintNumber());
	}
    }

    public static class BuildingFactoryEditor extends BuildingFactory {

	private DomainReference<Building> buildingReference;

	public Building getSpace() {
	    return buildingReference == null ? null : buildingReference.getObject();
	}

	public void setSpace(Building building) {
	    if (building != null) {
		this.buildingReference = new DomainReference<Building>(building);
	    }
	}

	public BuildingInformation execute() {
	    return new BuildingInformation(getSpace(), getName(), getBegin(), getEnd(),
		    getBlueprintNumber());
	}
    }
}
