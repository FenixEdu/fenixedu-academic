package net.sourceforge.fenixedu.domain.accessControl;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

@SuppressWarnings("serial")
public class AlumniDegreeGroup extends DegreeGroup {

    public AlumniDegreeGroup(Degree degree) {
        super(degree);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> persons = super.buildSet();
        for (Person alumni : Role.getRoleByRoleType(RoleType.ALUMNI).getAssociatedPersons()) {
            if (isMember(alumni)) {
                persons.add(alumni);
            }
        }
        return super.freezeSet(persons);
    }

    @Override
    public boolean isMember(Person person) {
        if (person == null) {
            return false;
        }
        final Student student = person.getStudent();
        if (student != null) {
            final List<Registration> registrations = student.getRegistrationsFor(getDegree());
            for (Registration registration : registrations) {
                if (new RegistrationConclusionBean(registration).isConcluded()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegree().getPresentationName() };
    }

    public static class Builder extends DegreeGroup.DegreeGroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new AlumniDegreeGroup(getDegree(arguments));
        }

    }

    @Override
    public PersistentAlumniGroup convert() {
        return PersistentAlumniGroup.getInstance(getDegree());
    }
}
