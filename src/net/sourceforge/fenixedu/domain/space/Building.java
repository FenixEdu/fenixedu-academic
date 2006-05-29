package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Building extends Building_Base {

	public static Comparator<Building> BUILDING_COMPARATOR_BY_NAME = new BeanComparator("spaceInformation.name", Collator.getInstance());

	public static abstract class BuildingFactory implements Serializable, FactoryExecutor {
		private String name;

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
