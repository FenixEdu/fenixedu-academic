package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class Student extends Student_Base {

    public Student(Person person, Integer number) {
        super();
        setPerson(person);
        setNumber(number);
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

    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType){
        for (Registration registration : getRegistrations()) {
            if (registration.getDegreeType().equals(degreeType) && registration.isActive()) {
                return registration;
            }
        }
        return null;
    }
    
}
