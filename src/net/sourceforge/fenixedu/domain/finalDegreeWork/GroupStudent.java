package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

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

    @Override
    @Deprecated
    public Registration getStudent() {
	return getRegistration();
    }

    @Override
    @Deprecated    
    public boolean hasStudent() {
	return hasRegistration();
    }

    @Override
    @Deprecated
    public void removeStudent() {
	removeRegistration();
    }

    @Override
    @Deprecated
    public void setStudent(Registration registration) {
	setRegistration(registration);
    }

    public Registration getRegistration() {
	return super.getStudent();
    }

    public boolean hasRegistration() {
	return super.hasStudent();
    }

    public void removeRegistration() {
	super.removeStudent();
    }

    public void setRegistration(Registration registration) {
	super.setStudent(registration);
    }

}
