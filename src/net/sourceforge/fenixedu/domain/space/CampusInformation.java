package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactory;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactoryEditor;

public class CampusInformation extends CampusInformation_Base {

    protected CampusInformation(final Campus campus, final CampusFactory campusFactory) {
	super();	
	super.setSpace(campus);
	setName(campusFactory.getName());
	setTimeInterval(campusFactory.getBegin(), campusFactory.getEnd());
    }

    @Override
    public void setName(final String name) {
	if (name == null) {
	    throw new NullPointerException("error.campus.name.cannot.be.null");
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
