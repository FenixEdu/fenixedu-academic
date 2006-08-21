/*
 * Created on 13/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Luis Cruz
 */
public class StudentIsUserFilter extends Filtro {

    public void execute(final ServiceRequest serviceRequest, final ServiceResponse serviceResponse)
            throws FilterException, Exception {
        final Integer studentId = (Integer) serviceRequest.getServiceParameters().getParameter(0);
        final Registration registration = rootDomainObject.readRegistrationByOID(studentId);

        final IUserView userView = getRemoteUser(serviceRequest);

        if (registration == null || userView == null || userView.getPerson() != registration.getPerson()) {
            throw new NotAuthorizedFilterException();
        }

    }

}