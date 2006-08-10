/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter() {
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
                || !lecturesExecutionCourse(id, arguments)
                || !sectionBelongsExecutionCourse(id, arguments)) {
            throw new NotAuthorizedFilterException();
        }

    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean sectionBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        Section section = null;
        InfoSection infoSection = null;

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
            if (argumentos[1] == null) {
                return true;
            }
            if (argumentos[1] instanceof InfoSection) {
                infoSection = (InfoSection) argumentos[1];

                section = rootDomainObject.readSectionByOID(infoSection.getIdInternal());
            } else {
                section = rootDomainObject.readSectionByOID((Integer) argumentos[1]);

            }
        } catch (Exception e) {
            return false;
        }

        if (section == null) {
            return false;
        }

        return section.getSite().getExecutionCourse().equals(executionCourse);
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
