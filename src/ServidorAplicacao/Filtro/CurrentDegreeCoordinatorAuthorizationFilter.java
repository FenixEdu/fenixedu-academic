/*
 * Created on 5/Nov/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class CurrentDegreeCoordinatorAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public CurrentDegreeCoordinatorAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response)
            throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        try {
            if ((id == null)
                    || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(),
                            getRoleType())
                    || !isCoordinatorOfCurrentExecutionDegree(id, argumentos)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isCoordinatorOfCurrentExecutionDegree(IUserView id,
            Object[] argumentos) {

        ISuportePersistente sp;
        boolean result = false;
        if (argumentos == null) {
            return result;
        }
        if (argumentos[0] == null) {
            return result;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id
                    .getUtilizador());
            IPersistentCoordinator persistentCoordinator = sp
                    .getIPersistentCoordinator();
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, (Integer) argumentos[0]);
            IExecutionYear executionYear = executionDegree.getExecutionYear();

            ICoordinator coordinator = persistentCoordinator
                    .readCoordinatorByTeacherAndExecutionDegree(teacher,
                            executionDegree);

            result = (coordinator != null)
                    && executionYear.getState().equals(PeriodState.CURRENT);

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}