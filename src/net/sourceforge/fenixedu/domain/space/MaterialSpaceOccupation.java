package net.sourceforge.fenixedu.domain.space;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public abstract class MaterialSpaceOccupation extends MaterialSpaceOccupation_Base {
    
    public final static Comparator<MaterialSpaceOccupation> COMPARATOR_BY_CLASS_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("class.simpleName"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("idInternal"));
    }
    
    public abstract Material getMaterial();
    
    public MaterialSpaceOccupation() {
        super();        
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBegin().isAfter(currentDate) && (this.getEnd() == null || !this.getEnd()
                .isBefore(currentDate)));
    }       
    
    @Override
    public void setBegin(YearMonthDay beginDate) {
	if (beginDate == null) {
	    throw new DomainException("error.materialSpaceOccupation.inexistent.beginDate");
	}
	super.setBegin(beginDate);
    }

    @Override
    public void setEnd(YearMonthDay endDate) {
	if (getBegin() == null || (endDate != null && endDate.isBefore(getBegin()))) {
	    throw new DomainException("error.materialSpaceOccupation.endDate.before.beginDate");
	}
	super.setEnd(endDate);
    }
}
