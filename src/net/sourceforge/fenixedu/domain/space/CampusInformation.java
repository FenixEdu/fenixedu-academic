package net.sourceforge.fenixedu.domain.space;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactory;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactoryEditor;

public class CampusInformation extends CampusInformation_Base {

    protected CampusInformation(final Campus campus, final CampusFactory campusFactory) {
	super();	
	super.setSpace(campus);	
	setFirstTimeInterval(campusFactory.getBegin(), campusFactory.getEnd());
	setName(campusFactory.getName());	
    }
    
    public void editCampusCharacteristics(String name, YearMonthDay begin, YearMonthDay end) {
	editTimeInterval(begin, end);
	setName(name);
    }
    
    @Override
    public void setName(final String name) {
	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.campus.name.cannot.be.null");
	}
	super.setName(name);
    }

    @Override
    public void setSpace(final Space space) {
	throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Campus campus) {
	throw new DomainException("error.cannot.change.campus");
    }

    public CampusFactoryEditor getSpaceFactoryEditor() {
	final CampusFactoryEditor campusFactoryEditor = new CampusFactoryEditor();
	campusFactoryEditor.setSpace((Campus) getSpace());
	campusFactoryEditor.setName(getName());
	campusFactoryEditor.setBegin(getNextPossibleValidFromDate());
	return campusFactoryEditor;
    }

    @Override
    public String getPresentationName() {
	return getName();
    }

}
