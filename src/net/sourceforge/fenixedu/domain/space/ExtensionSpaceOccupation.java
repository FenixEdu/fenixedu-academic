package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class ExtensionSpaceOccupation extends ExtensionSpaceOccupation_Base {

    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    @FenixDomainObjectActionLogAnnotation(
         actionName="Created extension occupation", 
	 parameters={"space","extension","begin","end"}
    )
    public ExtensionSpaceOccupation(Space space, Extension extension, YearMonthDay begin,
	    YearMonthDay end) {
	super();	
	setSpace(space);
	setExtension(extension);
	checkExtensionSpaceOccupationIntersection(begin, end, extension);
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    @Checked("SpacePredicates.checkPermissionsToManageOccupations")
    @FenixDomainObjectActionLogAnnotation(
         actionName="Edited extension occupation time interval", 
	 parameters={"begin","end"}
    )
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkExtensionSpaceOccupationIntersection(begin, end, getExtension());
	super.setBegin(begin);
	super.setEnd(end);
    }
      
    public void delete() {
	super.setExtension(null);
	super.delete();
    }
           
    @Override    
    public void setExtension(Extension extension) {
	if (extension == null) {
	    throw new DomainException("error.extensionSpaceOccupation.no.extension");
	}
	super.setExtension(extension);
    }

    @Override    
    public void setBegin(YearMonthDay begin) {
	checkExtensionSpaceOccupationIntersection(begin, getEnd(), getExtension());
	super.setBegin(begin);
    }

    @Override    
    public void setEnd(YearMonthDay end) {
	checkExtensionSpaceOccupationIntersection(getBegin(), end, getExtension());
	super.setEnd(end);
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
	
	checkBeginDateAndEndDate(begin, end);
	for (ExtensionSpaceOccupation extensionSpaceOccupation : extension
		.getExtensionSpaceOccupations()) {
	    if (!extensionSpaceOccupation.equals(this)
		    && extensionSpaceOccupation.occupationsIntersection(begin, end)
		    && extensionSpaceOccupation.getSpace().equals(getSpace())) {
		throw new DomainException("error.extensionSpaceOccupation.intersection");
	    }
	}
    }
    
    private boolean occupationsIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBegin().isAfter(end)) && (this.getEnd() == null || !this
		.getEnd().isBefore(begin)));
    }
    
    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.personSpaceOccupation.no.beginDate");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}
    }    
}
