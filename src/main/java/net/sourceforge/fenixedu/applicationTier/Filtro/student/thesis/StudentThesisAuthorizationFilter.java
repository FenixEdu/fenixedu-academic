package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StudentThesisAuthorizationFilter extends Filtro {

    @Override
    public void execute(Object[] parameters) throws Exception {
        Student student = getStudent(parameters);

        if (student == null) {
            abort();
        }

        Thesis thesis = (Thesis) parameters[0];
        if (thesis.getStudent() != student) {
            abort();
        }
    }

    private void abort() throws NotAuthorizedFilterException {
        throw new NotAuthorizedFilterException();
    }

    private Student getStudent(Object[] parameters) {
        IUserView userView = AccessControl.getUserView();
        return userView.getPerson().getStudent();
    }

}
