package net.sourceforge.fenixedu.domain.material;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public abstract class Material extends Material_Base {
    
    public static final Comparator COMPARATOR_BY_CLASS_NAME = new BeanComparator("class.simpleName");
    
    public abstract String getMaterialSpaceOccupationSlotName();
    
    public abstract Class getMaterialSpaceOccupationSubClass();
    
    public abstract String getPresentationDetails();
    
    protected Material() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }   
    
}
