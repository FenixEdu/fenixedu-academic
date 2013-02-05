package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

public class ReadDomainTeacherByUsername extends FenixService {

    @Service
    public static Teacher run(final String username) {
        List<Role> roles = rootDomainObject.getRoles();
        Role teacherRole = (Role) CollectionUtils.find(roles, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Role role = (Role) object;
                return role.getRoleType().equals(RoleType.TEACHER);
            }
        });

        Person person = (Person) CollectionUtils.find(teacherRole.getAssociatedPersons(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                Person tempPerson = (Person) object;
                return tempPerson.hasUsername(username);
            }
        });
        return person.getTeacher();
    }

}