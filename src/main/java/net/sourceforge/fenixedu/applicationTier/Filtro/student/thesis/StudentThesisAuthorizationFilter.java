package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StudentThesisAuthorizationFilter {

    public static final StudentThesisAuthorizationFilter instance = new StudentThesisAuthorizationFilter();

    public void execute(Thesis thesis) throws NotAuthorizedException {
        Student student = getStudent();

        if (student == null) {
            abort();
        }

        if (thesis.getStudent() != student) {
            abort();
        }
    }

    private void abort() throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    private Student getStudent() {
        User userView = Authenticate.getUser();
        return userView.getPerson().getStudent();
    }

}
