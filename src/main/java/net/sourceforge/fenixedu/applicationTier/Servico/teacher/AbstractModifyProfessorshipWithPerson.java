package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public abstract class AbstractModifyProfessorshipWithPerson {
    @Service
    public static Boolean run(Person person) throws NotAuthorizedException {
        /* start chain */
        Person requester = AccessControl.getPerson();
        if ((requester == null) || !requester.hasRole(RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }
        if (person.getTeacher() != null) {
            final Person requesterPerson = requester;
            Department teacherDepartment = person.getTeacher().getCurrentWorkingDepartment();
            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();
            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }
        /* end chain */
        return true;
    }

}
