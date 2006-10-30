package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Building extends Building_Base {

    public final static Comparator<Building> BUILDING_COMPARATOR_BY_PRESENTATION_NAME = new ComparatorChain();
    static {
	((ComparatorChain) BUILDING_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator(
		"spaceInformation.presentationName", Collator.getInstance()));
	((ComparatorChain) BUILDING_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator("idInternal"));
    }

    public static abstract class BuildingFactory implements Serializable, FactoryExecutor {
	private String name;
	
	private YearMonthDay begin;
	
	private YearMonthDay end;

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
	    return new Building(this);
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
	    return new BuildingInformation(getSpace(), this);
	}	
    }

    protected Building() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    public Building(final BuildingFactoryCreator buildingFactoryCreator) {
	this();
	setSuroundingSpace(buildingFactoryCreator.getSurroundingSpace());
	checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getUserView().getPerson(), buildingFactoryCreator.getSurroundingSpace());
	new BuildingInformation(this, buildingFactoryCreator);
    }

    @Override
    public BuildingInformation getSpaceInformation() {
	return (BuildingInformation) super.getSpaceInformation();
    }

    @Override
    public BuildingInformation getSpaceInformation(final YearMonthDay when) {
	return (BuildingInformation) super.getSpaceInformation(when);
    }

}
