/*
 * Created on Dec 16, 2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedFilterException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeacherInformationCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.COORDINATOR;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *      pt.utl.ist.berserk.ServiceResponse)
	 */
    public void execute(ServiceRequest arg0, ServiceResponse arg1) throws FilterException, Exception
    {
        IUserView id = (IUserView) arg0.getRequester();
        Object[] arguments = arg0.getArguments();
        if (((id != null
            && id.getRoles() != null
            && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
            || (id == null)
            || (id.getRoles() == null)
            || !verifyCondition(id, (String) arguments[0]))
        {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
	 * @param id
	 * @param string
	 * @return
	 */
    protected boolean verifyCondition(IUserView id, String user)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            ITeacher coordinator = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            List executionDegrees = persistentExecutionDegree.readByTeacher(coordinator);

            List professorships = persistentProfessorship.readByExecutionDegrees(executionDegrees);
            Iterator iter = professorships.iterator();
            while (iter.hasNext())
            {
                IProfessorship professorship = (IProfessorship) iter.next();
                if (professorship.getTeacher().equals(teacher))
                    return true;
            }
            return false;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
