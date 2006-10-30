package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.Material;

import org.joda.time.YearMonthDay;

public class ExtensionSpaceOccupation extends ExtensionSpaceOccupation_Base {

    public ExtensionSpaceOccupation(Space space, Extension extension, YearMonthDay begin,
	    YearMonthDay end) {
	super();
	checkParameters(extension, begin, end, space);
	setSpace(space);
	checkPermissions();
	setExtension(extension);
	setOccupationInterval(begin, end);
    }

    private void checkParameters(Extension extension, YearMonthDay begin, YearMonthDay end, Space space) {
	if (extension == null) {
	    throw new DomainException("error.extensionSpaceOccupation.no.extension");
	}
	if (begin == null) {
	    throw new DomainException("error.extensionSpaceOccupation.no.beginDate");
	}
    }

    @Checked("SpaceOccupationsPredicates.permissionsToMakeOperations")
    private void checkPermissions() {
    }

    @Checked("SpaceOccupationsPredicates.permissionsToMakeOperations")
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkExtensionSpaceOccupationIntersection(begin, end, getExtension());
	super.setBegin(begin);
	super.setEnd(end);
    }

    @Override
    @Checked("SpaceOccupationsPredicates.permissionsToMakeOperations")
    public void setBegin(YearMonthDay begin) {
	checkExtensionSpaceOccupationIntersection(begin, getEnd(), getExtension());
	super.setBegin(begin);
    }

    @Override
    @Checked("SpaceOccupationsPredicates.permissionsToMakeOperations")
    public void setEnd(YearMonthDay end) {
	checkExtensionSpaceOccupationIntersection(getBegin(), end, getExtension());
	super.setEnd(end);
    }

    @Checked("SpaceOccupationsPredicates.permissionsToMakeOperations")
    public void delete() {
	removeExtension();
	super.delete();
    }

    @Override
    public Material getMaterial() {
	return (Material) getExtension();
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getExtensionOccupationsAccessGroupWithChainOfResponsibility();
    }

    private void checkExtensionSpaceOccupationIntersection(YearMonthDay begin, YearMonthDay end,
	    Extension extension) {
	checkEndDate(begin, end);
	for (ExtensionSpaceOccupation extensionSpaceOccupation : extension
		.getExtensionSpaceOccupations()) {
	    if (!extensionSpaceOccupation.equals(this)
		    && extensionSpaceOccupation.occupationsIntersection(begin, end)
		    && extensionSpaceOccupation.getSpace().equals(getSpace())) {
		throw new DomainException("error.extensionSpaceOccupation.intersection");
	    }
	}
    }

    private void checkEndDate(final YearMonthDay begin, final YearMonthDay end) {
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }

    private boolean occupationsIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }
}
