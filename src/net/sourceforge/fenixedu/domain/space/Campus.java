package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

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

    public static class CampusFactoryCreator extends CampusFactory {
	public Campus execute() {
	    return new Campus(getName(), getBegin(), getEnd(), getBlueprintNumber());
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
	    return new CampusInformation(getSpace(), getName(), getBegin(), getEnd(), getBlueprintNumber());
	}
    }

    protected Campus() {
	super();
    }

    public Campus(String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
	this();	
	new CampusInformation(this, name, begin, end, blueprintNumber);
    }
    
    @Override
    public CampusInformation getSpaceInformation() {
	return (CampusInformation) super.getSpaceInformation();
    }

    @Override
    public CampusInformation getSpaceInformation(final YearMonthDay when) {
	return (CampusInformation) super.getSpaceInformation(when);
    }
    
    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted campus", parameters = {})
    public void delete() {
	super.delete();
    }
    
    // TODO : fix this when the new spaces structure is introduced
    // and the location of each campus is known.
    public String getLocation() {
	if (getSpaceInformation().getName().equals("Alameda")) {
	    return "Lisboa";
	} else if (getSpaceInformation().getName().equals("TagusPark")) {
	    return "Oeiras";
	}
	
	return null;
    }

    public String getName() {
	return getSpaceInformation().getName();
    }

}
