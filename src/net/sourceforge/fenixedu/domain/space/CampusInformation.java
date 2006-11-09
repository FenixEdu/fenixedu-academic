package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus.CampusFactoryEditor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class CampusInformation extends CampusInformation_Base {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created campus information", parameters = {
	    "campus", "name", "begin", "end" })
    public CampusInformation(Campus campus, String name, YearMonthDay begin, YearMonthDay end) {
	super();
	super.setSpace(campus);
	setName(name);
	setFirstTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited campus information", parameters = {
	    "name", "begin", "end" })
    public void editCampusCharacteristics(String name, YearMonthDay begin, YearMonthDay end) {
	setName(name);
	editTimeInterval(begin, end);
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted campus information", parameters = {})
    public void delete() {
	super.delete();
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
