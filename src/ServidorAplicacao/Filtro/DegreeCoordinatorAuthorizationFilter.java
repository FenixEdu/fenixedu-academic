/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import Dominio.ICoordinator;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class DegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter
{

    public final static DegreeCoordinatorAuthorizationFilter instance =
        new DegreeCoordinatorAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.COORDINATOR;
    }

    public void preFiltragem(IUserView id, Object[] argumentos)
        throws NotAuthorizedException
    {
        try
        {
            if ((id == null)
                || (id.getRoles() == null)
                || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                || !isCoordinatorOfExecutionDegree(id, argumentos))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException();
        }
    }

    /**
	 * @param id
	 * @param argumentos
	 * @return
	 */
    private boolean isCoordinatorOfExecutionDegree(IUserView id, Object[] argumentos)
    {

        ISuportePersistente sp;
        boolean result = false;
        if (argumentos == null)
        {
            return result;
        }
        if (argumentos[0] == null)
        {
            return result;
        }
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();

            ICoordinator coordinator =
                persistentCoordinator.readCoordinatorByTeacherAndExecutionDegreeId(
                    teacher,
                    (Integer) argumentos[0]);

            result = coordinator != null;

        } catch (Exception e)
        {
            return false;
        }

        return result;
    }

}
