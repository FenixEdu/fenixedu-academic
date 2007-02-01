package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class VigilantBound extends VigilantBound_Base {
    
	public static final Comparator<VigilantBound> VIGILANT_NAME_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			VigilantBound b1 = (VigilantBound) o1;
			VigilantBound b2 = (VigilantBound) o2;
			Comparator comparator = Vigilant.NAME_COMPARATOR;
			return comparator.compare(b1.getVigilant(), b2.getVigilant());
		}
		
	};
	public static final Comparator<VigilantBound> VIGILANT_GROUP_COMPARATOR = new BeanComparator("vigilantGroup.name");
	
	public static final Comparator<VigilantBound> VIGILANT_USERNAME_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			VigilantBound b1 = (VigilantBound) o1;
			VigilantBound b2 = (VigilantBound) o2;
			Comparator comparator = Vigilant.USERNAME_COMPARATOR;
			return comparator.compare(b1.getVigilant(), b2.getVigilant());
		}
		
	};
 
	
	public static final Comparator<Vigilant> VIGILANT_CATEGORY_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
				VigilantBound b1 = (VigilantBound) o1;
				VigilantBound b2 = (VigilantBound) o2;
				
				Comparator comparator = Vigilant.CATEGORY_COMPARATOR;
				return comparator.compare(b1.getVigilant(), b2.getVigilant());
			}
		
	};
	
    public  VigilantBound() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

	public VigilantBound(Vigilant vigilant, VigilantGroup group) {
		this();
		this.setVigilant(vigilant);
		this.setVigilantGroup(group);
		this.setConvokable(true);
	}
	
	public void delete() {
		removeVigilant();
		removeVigilantGroup();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

    
}
