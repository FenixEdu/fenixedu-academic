package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Building extends Building_Base {

    public final static Comparator<Building> BUILDING_COMPARATOR_BY_PRESENTATION_NAME = new ComparatorChain();
    static {
	((ComparatorChain) BUILDING_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator("spaceInformation.presentationName", Collator.getInstance()));
	((ComparatorChain) BUILDING_COMPARATOR_BY_PRESENTATION_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
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

    protected Building() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    public Building(Space surroundingSpace, String name, YearMonthDay begin, YearMonthDay end,
	    String blueprintNumber) {
	this();
	setSuroundingSpace(surroundingSpace);
	new BuildingInformation(this, name, begin, end, blueprintNumber);
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

}
