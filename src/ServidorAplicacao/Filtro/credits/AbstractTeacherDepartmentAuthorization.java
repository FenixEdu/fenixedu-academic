/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AccessControlFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * Base class for authorization issues on credits information edition done by
 * department members.
 * 
 * @author jpvl
 */
public abstract class AbstractTeacherDepartmentAuthorization extends
        AccessControlFilter {

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest serviceRequest,
            ServiceResponse serviceResponse) throws Exception {
        IUserView requester = (IUserView) serviceRequest.getRequester();
        if ((requester == null)
                || !AuthorizationUtils.containsRole(requester.getRoles(),
                        RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();

        Integer teacherId = getTeacherId(serviceRequest.getArguments(), sp);
        if (teacherId != null) {

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPessoa requesterPerson = personDAO.lerPessoaPorUsername(requester
                    .getUtilizador());
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                    teacherId);

            IDepartment teacherDepartment = departmentDAO
                    .readByTeacher(teacher);

            List departmentsWithAccessGranted = requesterPerson
                    .getManageableDepartmentCredits();

            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }

    }

    /**
     * @param arguments
     * @return
     */
    protected abstract Integer getTeacherId(Object[] arguments,
            ISuportePersistente sp) throws FenixServiceException;
}