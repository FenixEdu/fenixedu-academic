/*
 * Created on 19/Mai/2003
 */
package ServidorAplicacao.Filtro;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseCoordinatorAuthorizationFilter() {

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
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !hasExecutionCourseInCurricularCourseList(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasExecutionCourseInCurricularCourseList(IUserView id, Object[] argumentos) {
        boolean result = false;
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;

        if (argumentos == null) {
            return result;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            } else {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            if (teacher != null && executionCourse != null) {
                List executionDegrees = persistentCoordinator.readExecutionDegreesByTeacher(teacher);
                if (executionDegrees != null && !executionDegrees.isEmpty()) {
                    Iterator iter = executionDegrees.iterator();
                    while (iter.hasNext() && !result) {
                        IExecutionDegree executionDegree = (IExecutionDegree) iter.next();
                        if (executionDegree.getExecutionYear().equals(
                                executionCourse.getExecutionPeriod().getExecutionYear())) {
                            if (CollectionUtils.containsAny(executionDegree.getCurricularPlan()
                                    .getCurricularCourses(), executionCourse
                                    .getAssociatedCurricularCourses())) {
                                result = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

}