/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.ICoordinator;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
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

    public DegreeCoordinatorAuthorizationFilter()
    {
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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        try
        {
            if ((id == null) || (id.getRoles() == null)
                            || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                            || !isCoordinatorOfExecutionDegree(id, argumentos))
            {
                throw new NotAuthorizedFilterException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedFilterException();
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
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();

            ICoordinator coordinator = persistentCoordinator
                            .readCoordinatorByTeacherAndExecutionDegreeId(
                                            teacher, (Integer) argumentos[0]);

            result = coordinator != null;

        }
        catch (Exception e)
        {
            return false;
        }

        return result;
    }

}
