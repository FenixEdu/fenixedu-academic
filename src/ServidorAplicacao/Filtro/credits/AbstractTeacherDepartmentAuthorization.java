/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import java.util.List;

import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * Base class for authorization issues on credits information edition done by department members.
 * 
 * @author jpvl
 */
public abstract class AbstractTeacherDepartmentAuthorization extends Filtro
{

    public void preFiltragem(IUserView requester, Object[] arguments) throws Exception
    {

        if ((requester == null)
                || !AuthorizationUtils.containsRole(requester.getRoles(),
                        RoleType.DEPARTMENT_CREDITS_MANAGER))
        {
            throw new NotAuthorizedException();
        }

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();

        Integer teacherId = getTeacherId(arguments, sp);
        if (teacherId != null) {
        
        IPessoaPersistente personDAO = sp.getIPessoaPersistente();
        IPessoa requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(teacherId), false);

        IDepartment teacherDepartment = departmentDAO.readByTeacher(teacher);

        List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();

        if (!departmentsWithAccessGranted.contains(teacherDepartment))
        {
            throw new NotAuthorizedException();
        }
        }

    }
    /**
	 * @param arguments
	 * @return
	 */
    protected abstract Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException;

}