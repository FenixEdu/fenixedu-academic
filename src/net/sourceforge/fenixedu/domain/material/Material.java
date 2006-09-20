package net.sourceforge.fenixedu.domain.material;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public abstract class Material extends Material_Base {
    
    public final static Comparator COMPARATOR_BY_CLASS_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("class.simpleName"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("acquisition"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("idInternal"));
    }
    
    public abstract String getMaterialSpaceOccupationSlotName();
    
    public abstract Class getMaterialSpaceOccupationSubClass();
    
    public abstract String getPresentationDetails();
    
    protected Material() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }   
    
}
