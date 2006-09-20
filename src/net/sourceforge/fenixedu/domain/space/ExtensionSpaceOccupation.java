package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.Material;

import org.joda.time.YearMonthDay;

public class ExtensionSpaceOccupation extends ExtensionSpaceOccupation_Base {
            
    public ExtensionSpaceOccupation(Space space, Extension extension, YearMonthDay begin, YearMonthDay end) {
        super();        
        checkParameters(extension, begin, end, space);        
        checkExtensionSpaceOccupationIntersection(begin, end, extension);
        setSpace(space);
        checkPermissionsToMakeOperations();
        setExtension(extension);        
        super.setBegin(begin);
        super.setEnd(end);
    }

    private void checkParameters(Extension extension, YearMonthDay begin, YearMonthDay end, Space space) {
        if(extension == null) {
            throw new DomainException("error.extensionSpaceOccupation.no.extension");
        }  
        if(space == null) {
            throw new DomainException("error.extensionSpaceOccupation.no.space");
        }  
        if(begin == null) {
            throw new DomainException("error.extensionSpaceOccupation.no.beginDate");
        }                    
    }
    
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {
	checkPermissionsToMakeOperations();	
        checkExtensionSpaceOccupationIntersection(begin, end, getExtension());
        super.setBegin(begin);
        super.setEnd(end);
    }
   
    @Override
    public void setBegin(YearMonthDay begin) {	
	checkPermissionsToMakeOperations();
        checkExtensionSpaceOccupationIntersection(begin, getEnd(), getExtension());
        super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {	
	checkPermissionsToMakeOperations();
        checkExtensionSpaceOccupationIntersection(getBegin(), end, getExtension());
        super.setEnd(end);
    }      
           
    public void delete() {
	checkPermissionsToMakeOperations();
        removeExtension();
        super.delete();
    }

    @Override
    public Material getMaterial() {        
        return (Material) getExtension();
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getExtensionOccupationsAccessGroup();	
    }
    
    private void checkExtensionSpaceOccupationIntersection(YearMonthDay begin, YearMonthDay end, Extension extension) {
        checkEndDate(begin, end);
	for (ExtensionSpaceOccupation extensionSpaceOccupation : extension.getExtensionSpaceOccupations()) {
            if (!extensionSpaceOccupation.equals(this)
                    && extensionSpaceOccupation.checkIntersections(begin, end)) {
                throw new DomainException("error.extensionSpaceOccupation.intersection");
            }
        }
    } 
    
    private void checkPermissionsToMakeOperations() {
	if (getAccessGroup() == null
		|| !getAccessGroup().isMember(AccessControl.getUserView().getPerson())) {
	    throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	}
    }
    
    private void checkEndDate(final YearMonthDay begin, final YearMonthDay end) {
	if(end != null && !end.isAfter(begin)) {
            throw new DomainException("error.begin.after.end");
        }
    }
    
    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !this.getBegin().isAfter(end))
                && (this.getEnd() == null || !this.getEnd().isBefore(begin)));
    }
}
