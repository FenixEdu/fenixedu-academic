/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndItemLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseAndItemLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        if ((id == null) || (id.getRoleTypes() == null)
                || !id.hasRoleType(getRoleType())
                || !lecturesExecutionCourse(id, arguments) || !itemBelongsExecutionCourse(id, arguments)) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean itemBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        Item item = null;
        InfoItem infoItem = null;

        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = rootDomainObject.readExecutionCourseByOID(
                        infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = rootDomainObject.readExecutionCourseByOID(
                        (Integer) argumentos[0]);
            }
            if (argumentos[1] instanceof InfoItem) {
                infoItem = (InfoItem) argumentos[1];

                item = rootDomainObject.readItemByOID(infoItem.getIdInternal());
            } else {
                item = rootDomainObject.readItemByOID((Integer) argumentos[1]);

            }
        } catch (Exception e) {
            return false;
        }

        if (item == null) {
            return false;
        }

        return item.getSection().getSite().getExecutionCourse().equals(executionCourse);
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {

        Integer executionCourseID = null;

        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourseID = infoExecutionCourse.getIdInternal();
            } else {
                executionCourseID = (Integer) argumentos[0];
            }

            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            Professorship professorship = null;
            if (teacher != null) {
                ExecutionCourse executionCourse = rootDomainObject
                        .readExecutionCourseByOID(executionCourseID);
                professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }

}
