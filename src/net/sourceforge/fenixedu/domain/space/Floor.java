package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Floor extends Floor_Base {

    public final static Comparator<Floor> FLOOR_COMPARATOR_BY_LEVEL = new ComparatorChain();
    static {
	((ComparatorChain) FLOOR_COMPARATOR_BY_LEVEL).addComparator(new ReverseComparator(
		new BeanComparator("spaceInformation.level")));
	((ComparatorChain) FLOOR_COMPARATOR_BY_LEVEL).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public static abstract class FloorFactory implements Serializable, FactoryExecutor {
	private Integer level;

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

	public Integer getLevel() {
	    return level;
	}

	public void setLevel(Integer level) {
	    this.level = level;
	}

	public String getBlueprintNumber() {
	    return blueprintNumber;
	}

	public void setBlueprintNumber(String blueprintNumber) {
	    this.blueprintNumber = blueprintNumber;
	}
    }

    public static class FloorFactoryCreator extends FloorFactory {

	private DomainReference<Space> surroundingSpaceReference;

	public Space getSurroundingSpace() {
	    return surroundingSpaceReference == null ? null : surroundingSpaceReference.getObject();
	}

	public void setSurroundingSpace(Space surroundingSpace) {
	    if (surroundingSpace != null) {
		this.surroundingSpaceReference = new DomainReference<Space>(surroundingSpace);
	    }
	}

	public Floor execute() {
	    return new Floor(getSurroundingSpace(), getLevel(), getBegin(), getEnd(),
		    getBlueprintNumber());
	}
    }

    public static class FloorFactoryEditor extends FloorFactory {

	private DomainReference<Floor> floorReference;

	public Floor getSpace() {
	    return floorReference == null ? null : floorReference.getObject();
	}

	public void setSpace(Floor floor) {
	    if (floor != null) {
		this.floorReference = new DomainReference<Floor>(floor);
	    }
	}

	public FloorInformation execute() {
	    return new FloorInformation(getSpace(), getLevel(), getBegin(), getEnd(),
		    getBlueprintNumber());
	}

    }

    protected Floor() {
	super();
    }

    public Floor(Space suroundingSpace, Integer level, YearMonthDay begin, YearMonthDay end,
	    String blueprintNumber) {
	this();

	if (suroundingSpace == null) {
	    throw new NullPointerException("error.surrounding.space");
	}
	setSuroundingSpace(suroundingSpace);
	new FloorInformation(this, level, begin, end, blueprintNumber);
    }

    @Override
    public FloorInformation getSpaceInformation() {
	return (FloorInformation) super.getSpaceInformation();
    }

    @Override
    public FloorInformation getSpaceInformation(final YearMonthDay when) {
	return (FloorInformation) super.getSpaceInformation(when);
    }

    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted floor", parameters = {})
    public void delete() {
	super.delete();
    }

    @Override
    public boolean isFloor() {
	return true;
    }

}
