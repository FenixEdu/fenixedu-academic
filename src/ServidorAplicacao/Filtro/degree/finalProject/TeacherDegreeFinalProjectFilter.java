/*
 * Created on Nov 12, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.degree.finalProject;

import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IDepartment;
import Dominio.IPerson;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectFilter extends AuthorizationByRoleFilter {

    public TeacherDegreeFinalProjectFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.DEPARTMENT_CREDITS_MANAGER;
    }

    /**
     * @param integer
     */
    private void verifyTeacherPermission(IUserView requester, Integer teacherNumber)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException("Teacher doesn't exists");
            }

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());
            if (requesterPerson == null) {
                throw new NotAuthorizedException("No person with that userView");
            }

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();
            IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();

            IDepartment department = departmentDAO.readByTeacher(teacher);

            if (department == null) {
                throw new NotAuthorizedException("Teacher number " + teacher.getTeacherNumber()
                        + " doesn't have department!");
            }

            if (!departmentsWithAccessGranted.contains(department)) {
                throw new NotAuthorizedException("Not authorized to run the service!");
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems with database!", e);

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        super.execute(request, response);
        verifyTeacherPermission(getRemoteUser(request), (Integer) getServiceCallArguments(request)[0]);
    }

}