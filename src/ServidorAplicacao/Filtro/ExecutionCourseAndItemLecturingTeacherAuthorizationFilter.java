/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoItem;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Item;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class ExecutionCourseAndItemLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndItemLecturingTeacherAuthorizationFilter() {

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

        if ((id == null)
                || (id.getRoles() == null)
                || !AuthorizationUtils.containsRole(id.getRoles(),
                        getRoleType())
                || !lecturesExecutionCourse(id, arguments)
                || !itemBelongsExecutionCourse(id, arguments)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean itemBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IItem item = null;
        InfoItem infoItem = null;

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
            IPersistentItem persistentItem = sp.getIPersistentItem();
            if (argumentos[1] instanceof InfoItem) {
                infoItem = (InfoItem) argumentos[1];

                item = (IItem) persistentItem.readByOID(Item.class, infoItem
                        .getIdInternal());
            } else {
                item = (IItem) persistentItem.readByOID(Item.class,
                        (Integer) argumentos[1]);

            }
        } catch (Exception e) {
            return false;
        }

        if (item == null) {
            return false;
        }

        return item.getSection().getSite().getExecutionCourse().equals(
                executionCourse);
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IProfessorship professorship = null;
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
            if (teacher != null && executionCourse != null) {
                IPersistentProfessorship persistentProfessorship = sp
                        .getIPersistentProfessorship();
                professorship = persistentProfessorship
                        .readByTeacherAndExecutionCoursePB(teacher,
                                executionCourse);
            }
        } catch (Exception e) {
            return false;
        }
        return professorship != null;
    }

}