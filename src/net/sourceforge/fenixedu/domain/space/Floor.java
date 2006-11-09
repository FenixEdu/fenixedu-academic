package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Floor extends Floor_Base {

    public final static Comparator<Floor> FLOOR_COMPARATOR_BY_LEVEL = new ComparatorChain();
    static {
	((ComparatorChain) FLOOR_COMPARATOR_BY_LEVEL).addComparator(new ReverseComparator(
		new BeanComparator("spaceInformation.level")));
	((ComparatorChain) FLOOR_COMPARATOR_BY_LEVEL).addComparator(new BeanComparator("idInternal"));
    }

    public static abstract class FloorFactory implements Serializable, FactoryExecutor {
	private Integer level;

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

	public Integer getLevel() {
	    return level;
	}

	public void setLevel(Integer level) {
	    this.level = level;
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
	    return new Floor(getSurroundingSpace(), getLevel(), getBegin(), getEnd());
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
	    return new FloorInformation(getSpace(), getLevel(), getBegin(), getEnd());
	}

    }

    protected Floor() {
	super();
    }

    public Floor(Space suroundingSpace, Integer level, YearMonthDay begin, YearMonthDay end) {
	this();

	if (suroundingSpace == null) {
	    throw new NullPointerException("error.surrounding.space");
	}
	setSuroundingSpace(suroundingSpace);	
	new FloorInformation(this, level, begin, end);
    }    

    @Override
    public FloorInformation getSpaceInformation() {
	return (FloorInformation) super.getSpaceInformation();
    }

    @Override
    public FloorInformation getSpaceInformation(final YearMonthDay when) {
	return (FloorInformation) super.getSpaceInformation(when);
    }

}
