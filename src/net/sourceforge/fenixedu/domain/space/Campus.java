package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Campus extends Campus_Base {

    public final static Comparator<Campus> CAMPUS_COMPARATOR_BY_PRESENTATION_NAME = new ComparatorChain();
    static {
	((ComparatorChain) CAMPUS_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator(
		"spaceInformation.presentationName", Collator.getInstance()));
	((ComparatorChain) CAMPUS_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator(
		"idInternal"));
    }

    public static abstract class CampusFactory implements Serializable, FactoryExecutor {
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

    public static class CampusFactoryCreator extends CampusFactory {
	public Campus execute() {
	    return new Campus(this);
	}
    }

    public static class CampusFactoryEditor extends CampusFactory {
	private DomainReference<Campus> campusReference;

	public Campus getSpace() {
	    return campusReference == null ? null : campusReference.getObject();
	}

	public void setSpace(Campus campus) {
	    if (campus != null) {
		this.campusReference = new DomainReference<Campus>(campus);
	    }
	}

	public CampusInformation execute() {
	    return new CampusInformation(getSpace(), this);
	}
    }

    protected Campus() {
	super();
    }

    public Campus(CampusFactory campusFactory) {
	this();
	new CampusInformation(this, campusFactory);
    }

    @Override
    public CampusInformation getSpaceInformation() {
	return (CampusInformation) super.getSpaceInformation();
    }

    @Override
    public CampusInformation getSpaceInformation(final YearMonthDay when) {
	return (CampusInformation) super.getSpaceInformation(when);
    }

}
