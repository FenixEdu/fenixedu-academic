package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class StudentThesisAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Student student = getStudent(request);
        
        if (student == null) {
            abort();
        }
        
        Thesis thesis = (Thesis) request.getServiceParameters().getParameter(0);
        if (thesis.getStudent() != student) {
            abort();
        }
    }

    private void abort() throws NotAuthorizedFilterException {
        throw new NotAuthorizedFilterException();
    }

    private Student getStudent(ServiceRequest request) {
        IUserView userView = getRemoteUser(request);
        return userView.getPerson().getStudent();
    }

}
