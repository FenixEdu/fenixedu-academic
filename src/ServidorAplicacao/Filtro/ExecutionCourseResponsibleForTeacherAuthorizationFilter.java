/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class ExecutionCourseResponsibleForTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseResponsibleForTeacherAuthorizationFilter() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
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
        Object[] arguments = getServiceCallArguments(request);

        if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(),
                        getRoleType()) && !isResponsibleForExecutionCourse(id,
                arguments)))
                || (id == null) || (id.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isResponsibleForExecutionCourse(IUserView id,
            Object[] argumentos) {

        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IResponsibleFor responsibleFor = null;
        if (argumentos == null) {
            return false;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner
                        .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            } else {
                executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class,
                                (Integer) argumentos[0]);
            }

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id
                    .getUtilizador());

            IPersistentResponsibleFor persistentResponsibleFor = sp
                    .getIPersistentResponsibleFor();
            responsibleFor = persistentResponsibleFor
                    .readByTeacherAndExecutionCoursePB(teacher, executionCourse);

        } catch (Exception e) {
            return false;
        }
        if (responsibleFor == null) {
            return false;
        } else {
            return true;
        }

    }

}