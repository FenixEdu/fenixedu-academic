/*
 * Created on Nov 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AccessControlFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author André Fernandes / João Brito
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StudentTutorAuthorizationFilter extends AccessControlFilter {

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException;

        if (id == null || id.getRoleTypes() == null || !id.hasRoleType(getRoleType())) {
            throw new NotAuthorizedFilterException();
        }
        messageException = studentTutor(id, request.getServiceParameters().parametersArray());
        if (messageException != null)
            throw new NotAuthorizedFilterException(messageException);

    }

    // devolve null se tudo OK
    // noAuthorization se algum prob
    private String studentTutor(IUserView id, Object[] arguments) {
        String username = (String) arguments[0];

        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        Registration registration = Registration.readByUsername(username);
        if (registration == null) {
            return "noAuthorization";
        }

        List allStudents = registration.getPerson().getStudents();
        if (allStudents == null || allStudents.isEmpty()) {
            return "noAuthorization";
        }

        if (teacher == null) {
            return "noAuthorization";
        }

        if (!verifyStudentTutor(teacher, allStudents)) {
            return "error.enrollment.notStudentTutor+" + registration.getNumber().toString();
        }
        return null;
    }

    private boolean verifyStudentTutor(Teacher teacher, List<Registration> students) {
        for (Registration registration : students) {
            if (registration.getAssociatedTutor() != null
                    && registration.getAssociatedTutor().getTeacher() == teacher) {
                return true;
            }
        }

        return false;
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

}
