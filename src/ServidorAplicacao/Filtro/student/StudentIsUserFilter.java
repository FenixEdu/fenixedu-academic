/*
 * Created on 13/Fev/2004
 *  
 */
package ServidorAplicacao.Filtro.student;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class StudentIsUserFilter extends Filtro {

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest serviceRequest, ServiceResponse serviceResponse)
            throws FilterException, Exception {
        Integer studentId = (Integer) serviceRequest.getArguments()[0];
        IUserView userView = getRemoteUser(serviceRequest);

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

        IStudent student = (IStudent) persistentSupport.getIPersistentStudent().readByOID(Student.class,
                studentId);
        if (student == null || userView == null
                || !userView.getUtilizador().equals(student.getPerson().getUsername())) {
            throw new NotAuthorizedFilterException();
        }

    }

}