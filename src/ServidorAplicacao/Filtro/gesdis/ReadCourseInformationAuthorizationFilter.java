/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.gesdis;

import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformationAuthorizationFilter extends AuthorizationByRoleFilter
{

    private static ReadCourseInformationAuthorizationFilter instance =
        new ReadCourseInformationAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    private ReadCourseInformationAuthorizationFilter()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.TEACHER;
    }

    public void preFiltragem(IUserView id, IServico service, Object[] arguments)
        throws NotAuthorizedException
    {
        try
        {
            if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || (!isResponsibleFor(id, (Integer) arguments[0])))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean isResponsibleFor(IUserView id, Integer executioncourseId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(executioncourseId),
                    false);

            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            List responsiblesFor = persistentResponsibleFor.readByExecutionCourse(executionCourse);
            IResponsibleFor responsibleFor = new ResponsibleFor(teacher, executionCourse);

            if (!responsiblesFor.contains(responsibleFor))
                return false;
            return true;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }
}
