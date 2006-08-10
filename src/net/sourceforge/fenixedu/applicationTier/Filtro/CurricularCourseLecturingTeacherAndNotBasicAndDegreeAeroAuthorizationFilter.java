package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;


/**
 * This filter filter all currricular course that are of LEA(Engenharia
 * Aeroespacial) degree
 */
public class CurricularCourseLecturingTeacherAndNotBasicAndDegreeAeroAuthorizationFilter extends
        AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((id == null) || (id.getRoleTypes() == null)
                || !id.hasRoleType(getRoleType())
                || !lecturesExecutionCourse(id, argumentos)
                || !CurricularCourseBelongsExecutionCourse(id, argumentos)
                || !CurricularCourseNotBasic(argumentos) || !CurricularCourseAeroDegree(argumentos)) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean CurricularCourseBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        CurricularCourse curricularCourse = null;
        InfoCurricularCourse infoCurricularCourse = null;
        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = rootDomainObject.readExecutionCourseByOID((Integer) argumentos[0]);
            }
            if (argumentos[1] instanceof InfoCurricularCourse) {
                infoCurricularCourse = (InfoCurricularCourse) argumentos[1];
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourse.getIdInternal());
            } else {
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID((Integer) argumentos[1]);
            }

        } catch (Exception e) {
            return false;
        }
        return executionCourse.getAssociatedCurricularCourses().contains(curricularCourse);
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }
        try {
            Integer executionCourseID = null;

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

    /**
     * @param argumentos
     * @return
     */
    private boolean CurricularCourseAeroDegree(Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }

        InfoCurricularCourse infoCurricularCourse = null;
        CurricularCourse curricularCourse = null;
        Degree degree = null;

        try {
            if (argumentos[1] instanceof InfoCurricularCourse) {
                infoCurricularCourse = (InfoCurricularCourse) argumentos[1];
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourse.getIdInternal());
            } else {
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID((Integer) argumentos[1]);
            }

            degree = curricularCourse.getDegreeCurricularPlan().getDegree();
        } catch (Exception e) {
            return false;
        }
        return degree.getSigla().equals("LEA");// codigo
        // do
        // curso
        // de
        // Aeroespacial
    }

    /**
     * @param argumentos
     * @return
     */
    private boolean CurricularCourseNotBasic(Object[] argumentos) {
        if (argumentos == null) {
            return false;
        }

        InfoCurricularCourse infoCurricularCourse = null;
        CurricularCourse curricularCourse = null;

        try {
            if (argumentos[1] instanceof InfoCurricularCourse) {
                infoCurricularCourse = (InfoCurricularCourse) argumentos[1];
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourse.getIdInternal());
            } else {
                curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID((Integer) argumentos[1]);
            }
        } catch (Exception e) {
            return false;
        }
        return curricularCourse.getBasic().equals(Boolean.FALSE);
    }
    
}
