package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.material.Material;

import org.joda.time.YearMonthDay;

public abstract class MaterialSpaceOccupation extends MaterialSpaceOccupation_Base {
     
    public abstract Material getAssociatedMaterial();
    
    public MaterialSpaceOccupation() {
        super();        
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
                .isBefore(currentDate)));
    }       
}
