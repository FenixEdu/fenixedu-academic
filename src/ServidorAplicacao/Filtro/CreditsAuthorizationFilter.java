/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro;

import java.util.Collection;
import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoTeacher;
import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class CreditsAuthorizationFilter extends Filtro
{

    // the singleton of this class
    public CreditsAuthorizationFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView requester = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        Collection roles = requester.getRoles();
        boolean authorizedRequester = false;
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        // ATTENTION: ifs order matters...
        if (AuthorizationUtils.containsRole(roles, RoleType.CREDITS_MANAGER))
        {
            authorizedRequester = true;
        }
        else if (AuthorizationUtils.containsRole(roles, RoleType.DEPARTMENT_CREDITS_MANAGER))
        {
            ITeacher teacherToEdit = readTeacher(arguments[0], sp);

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPessoa requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();
            IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
            IDepartment department = departmentDAO.readByTeacher(teacherToEdit);
            if (department == null)
            {
                System.out.println("----------->" + teacherToEdit.getTeacherNumber()
                                + " doesn't have department!");
            }

            authorizedRequester = departmentsWithAccessGranted.contains(department);

        }
        else if (AuthorizationUtils.containsRole(roles, RoleType.TEACHER))
        {
            ITeacher teacherToEdit = readTeacher(arguments[0], sp);
            authorizedRequester = teacherToEdit.getPerson().getUsername().equals(
                            requester.getUtilizador());

        }

        if (!authorizedRequester)
        {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                            + "ACCESS NOT GRANTED!");
        }

    }

    /**
     * @param object
     * @return
     */
    private ITeacher readTeacher(Object object, ISuportePersistente sp)
    {
        Integer teacherOID = null;
        if (object instanceof InfoTeacher)
        {
            teacherOID = ((InfoTeacher) object).getIdInternal();
        }
        else if (object instanceof Integer)
        {
            teacherOID = (Integer) object;
        }
        return (ITeacher) sp.getIPersistentTeacher().readByOId(new Teacher(teacherOID), false);
    }
}
