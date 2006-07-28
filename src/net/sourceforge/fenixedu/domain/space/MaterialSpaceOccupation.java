package net.sourceforge.fenixedu.domain.space;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.material.Material;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public abstract class MaterialSpaceOccupation extends MaterialSpaceOccupation_Base {
    
    public static final Comparator COMPARATOR_BY_CLASS_NAME = new BeanComparator("class.simpleName");
    
    public abstract Material getMaterial();
    
    public MaterialSpaceOccupation() {
        super();        
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
                .isBefore(currentDate)));
    }       
}
