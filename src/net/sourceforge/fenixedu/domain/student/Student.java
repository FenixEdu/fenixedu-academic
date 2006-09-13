package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.beanutils.BeanComparator;

public class Student extends Student_Base {

    public final static Comparator<Student> NUMBER_COMPARATOR = new BeanComparator("number");

    public Student(Person person, Integer number) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPerson(person);
	setNumber(number);
    }

    public Student(Person person) {
	this(person, Student.generateStudentNumber());
    }

    
    public Collection<Registration> getRegistrationsByDegreeType(DegreeType degreeType) {
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType) && registration.isActive()) {
		return registration;
	    }
	}
	return null;
    }

    public static Integer generateStudentNumber() {
	Integer nextNumber = 0;
	for (Student student : RootDomainObject.getInstance().getStudents()) {
	    if(student.getNumber() < 100000 && student.getNumber() > nextNumber){
		nextNumber = student.getNumber();
	    }
	}
	return nextNumber + 1;
    }

}
