package net.sourceforge.fenixedu.domain.space;

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
        if(end != null && end.isBefore(begin)) {
            throw new DomainException("error.extensionSpaceOccupation.endDateBeforeBeginDate");
        }              
    }
    
    public void setOccupationInterval(final YearMonthDay begin, final YearMonthDay end) {               
        checkExtensionSpaceOccupationIntersection(begin, end, getExtension());
        super.setBegin(begin);
        super.setEnd(end);
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
    
    private void checkExtensionSpaceOccupationIntersection(YearMonthDay begin, YearMonthDay end, Extension extension) {
        for (ExtensionSpaceOccupation extensionSpaceOccupation : extension.getExtensionSpaceOccupations()) {
            if (!extensionSpaceOccupation.equals(this)
                    && extensionSpaceOccupation.checkIntersections(begin, end)) {
                throw new DomainException("error.extensionSpaceOccupation.intersection");
            }
        }
    }      
    
    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !this.getBegin().isAfter(end))
                && (this.getEnd() == null || !this.getEnd().isBefore(begin)));
    }
    
    public void delete() {
        removeExtension();
        super.delete();
    }

    @Override
    public Material getMaterial() {        
        return (Material) getExtension();
    }    
}
