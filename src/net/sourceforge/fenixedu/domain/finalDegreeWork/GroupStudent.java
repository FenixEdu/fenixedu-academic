package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class GroupStudent extends GroupStudent_Base {

	public static final Comparator<GroupStudent> COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator("student.number");

    public GroupStudent() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeRootDomainObject();
        deleteDomainObject();
    }

}
