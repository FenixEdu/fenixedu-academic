package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class GrantOrientationTeacher extends GrantOrientationTeacher_Base {

    final static Comparator<GrantOrientationTeacher> BEGIN_DATE_COMPARATOR = new BeanComparator("beginDate");
    
	public GrantOrientationTeacher() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
