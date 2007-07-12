package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class Campus extends Campus_Base {
    
    public Campus(String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
	super();	
	new CampusInformation(this, name, begin, end, blueprintNumber);
    }  
    
    @Override
    public void setSuroundingSpace(Space suroundingSpace) {
	throw new DomainException("error.Space.invalid.suroundingSpace");
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
    
    public String getLocation() {	
	return getSpaceInformation().hasLocality() ? getSpaceInformation().getLocality().getName() : null;	
    }

    public String getName() {
	return getSpaceInformation().getName();
    }
    
    public static Campus readActiveCampusByName(String campusName) {
	for (Resource space : RootDomainObject.getInstance().getResources()) {
	    if (space.isCampus() && ((Campus)space).isActive() 
		    && ((Campus)space).getSpaceInformation().getName().equals(campusName)) {
		return (Campus) space;
	    }
	}
	return null;
    }
      
    public static Campus readCampusByName(String name) {
	for (Campus campus : Space.getAllCampus()) {
	    if(campus.getName().equalsIgnoreCase(name)) {
		return campus;
	    }
	}
	return null;
    } 
      
    @Override
    public boolean isCampus() {
	return true;
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
}
