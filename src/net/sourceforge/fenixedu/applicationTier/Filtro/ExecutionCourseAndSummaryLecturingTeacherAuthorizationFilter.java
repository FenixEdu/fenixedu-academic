/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 */
public class ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        Summary summary = getSummary(arguments);
        Teacher teacherLogged = Teacher.readTeacherByUsername(id.getUtilizador());

        try {
            if (isTeacherResponsible(arguments, request)) {
                List teachers = getExecutionCourseTeachers(arguments, request);
                if (isTeacherExecutionCourseMember(teachers, arguments, id)) {
                    throw new NotAuthorizedFilterException();
                }
            } else if ((summary.getTeacher() != null) && (summary.getTeacher().equals(teacherLogged))) {

            } else if ((id == null) || (id.getRoleTypes() == null)
                    || !id.hasRoleType(getRoleType())
                    || !lecturesExecutionCourse(id, arguments)
                    || !SummaryBelongsExecutionCourse(id, arguments)
                    || !summaryBelongsToTeacher(id, arguments) || !validTeacher(id, arguments)) {
                throw new NotAuthorizedFilterException("error.summary.not.authorized");
            }
        } catch (RuntimeException ex) {
            throw new NotAuthorizedFilterException(ex.getMessage());
        }
    }

    /**
     * @param arguments
     * @param request
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getExecutionCourseTeachers(Object[] arguments, ServiceRequest request)
            throws ExcepcaoPersistencia {

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(((Integer) arguments[0]));

        List professorShips = executionCourse.getProfessorships();

        return professorShips;
    }

    private boolean isTeacherExecutionCourseMember(List teachers, Object[] arguments, IUserView userView)
            throws ExcepcaoPersistencia {

        Summary summary = getSummary(arguments);

        Teacher teacherLogged = Teacher.readTeacherByUsername(userView.getUtilizador());
        Integer executionCourseID = (Integer) arguments[0];

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

        Professorship professorshipLogged = teacherLogged
                .getProfessorshipByExecutionCourse(executionCourse);

        if (summary.getProfessorship() != null
                && !summary.getProfessorship().equals(professorshipLogged)) {
            return true;
        }

        if (summary.getTeacher() != null) {
            Professorship professorship = summary.getTeacher().getProfessorshipByExecutionCourse(
                    executionCourse);
            if (teachers.contains(professorship) && !summary.getTeacher().equals(teacherLogged)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param arguments
     * @throws NotAuthorizedFilterException
     * @throws ExcepcaoPersistencia
     */
    private boolean isTeacherResponsible(Object[] arguments, ServiceRequest request)
            throws NotAuthorizedFilterException {

        IUserView userView = getRemoteUser(request);
        List responsibleTeachers = getResponsibleTeachers((Integer) arguments[0]);
        boolean loggedIsResponsible = false;

        for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
            Teacher teacher = (Teacher) iter.next();
            if (teacher.getPerson().getUsername().equals(userView.getUtilizador()))
                loggedIsResponsible = true;
            break;
        }

        return loggedIsResponsible;
    }

    /**
     * @param executionCourse
     * @return
     * @throws NotAuthorizedFilterException
     */
    private List getResponsibleTeachers(Integer executionCourseId) throws NotAuthorizedFilterException {
        try {
            List result = null;

            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

            result = executionCourse.responsibleFors();

            List<Teacher> infoResult = new ArrayList<Teacher>();
            if (result != null) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    Professorship responsibleFor = (Professorship) iter.next();
                    Teacher teacher = responsibleFor.getTeacher();
                    infoResult.add(teacher);
                }
                return infoResult;
            }
            return result;

        } catch (Exception e) {
            throw new NotAuthorizedFilterException(e);
        }
    }

    /**
     * @param id
     * @param arguments
     * @return
     * @throws ExcepcaoPersistencia
     */
    private boolean validTeacher(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        Teacher teacher = getTeacher(id);
        final InfoSummary infoSummary = getInfoSummary(arguments);
        if (infoSummary == null) {
            final Summary summary = getSummary(arguments);
            if (summary.getProfessorship() == null && summary.getTeacher() != null
                    && !summary.getTeacher().getTeacherNumber().equals(teacher.getTeacherNumber())) {

                List professorships = getProfessorships(arguments);
                Professorship professorship = (Professorship) CollectionUtils.find(professorships,
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                Professorship professorship = (Professorship) arg0;
                                return professorship.getTeacher().getTeacherNumber().equals(
                                        summary.getTeacher().getTeacherNumber());
                            }
                        });
                return professorship == null;
            }
        } else {

            if (infoSummary.getInfoProfessorship() == null
                    && infoSummary.getInfoTeacher() != null
                    && !infoSummary.getInfoTeacher().getTeacherNumber().equals(
                            teacher.getTeacherNumber())) {

                List professorships = getProfessorships(arguments);
                Professorship professorship = (Professorship) CollectionUtils.find(professorships,
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                Professorship professorship = (Professorship) arg0;
                                return professorship.getTeacher().getTeacherNumber().equals(
                                        infoSummary.getInfoTeacher().getTeacherNumber());
                            }
                        });
                return professorship == null;
            }
        }
        return true;
    }

    private List getProfessorships(Object[] arguments) throws ExcepcaoPersistencia {
        Summary summary = getSummary(arguments);

        if (summary.getShift() == null) {
            return summary.getExecutionCourse().getProfessorships();
        }
        return summary.getShift().getDisciplinaExecucao().getProfessorships();
    }

    /**
     * @param arguments
     * @return
     */
    private InfoSummary getInfoSummary(Object[] arguments) {
        InfoSummary infoSummary = null;
        if (arguments[1] instanceof InfoSummary) {
            infoSummary = (InfoSummary) arguments[1];
        }
        return infoSummary;
    }

    /**
     * @param id
     * @param arguments
     * @return
     * @throws ExcepcaoPersistencia
     */
    private boolean summaryBelongsToTeacher(IUserView id, Object[] arguments)
            throws ExcepcaoPersistencia {
        Teacher teacher = getTeacher(id);
        Summary summary = getSummary(arguments);
        if (summary.getProfessorship() == null) {
            return isResponsible(teacher, summary);
        }
        return isOwner(teacher, summary);

    }

    /**
     * @param teacher
     * @param summary
     * @return
     */
    private boolean isOwner(Teacher teacher, Summary summary) {
        if (summary.getTeacher() != null) {
            return teacher.getIdInternal().equals(summary.getTeacher().getIdInternal());
        }
        return teacher.getIdInternal().equals(summary.getProfessorship().getTeacher().getIdInternal());
    }

    /**
     * @param teacher
     * @param summary
     * @return
     * @throws ExcepcaoPersistencia
     */
    private boolean isResponsible(final Teacher teacher, Summary summary) throws ExcepcaoPersistencia {
        if (summary.getShift() == null) {

            List responsibleTeachers = summary.getExecutionCourse().responsibleFors();

            return CollectionUtils.find(responsibleTeachers, new Predicate() {

                public boolean evaluate(Object arg0) {
                    Professorship responsibleFor = (Professorship) arg0;
                    return responsibleFor.getTeacher().getIdInternal().equals(teacher.getIdInternal());
                }
            }) != null;
        }

        List responsibleTeachers = summary.getShift().getDisciplinaExecucao().responsibleFors();

        return CollectionUtils.find(responsibleTeachers, new Predicate() {

            public boolean evaluate(Object arg0) {
                Professorship responsibleFor = (Professorship) arg0;
                return responsibleFor.getTeacher().getIdInternal().equals(teacher.getIdInternal());
            }
        }) != null;

    }

    /**
     * @param arguments
     * @return
     * @throws ExcepcaoPersistencia
     */
    private Summary getSummary(Object[] arguments) throws ExcepcaoPersistencia {
        Summary summary = null;
        InfoSummary infoSummary = null;

        if (arguments[1] instanceof InfoSummary) {
            infoSummary = (InfoSummary) arguments[1];
            summary = rootDomainObject.readSummaryByOID(infoSummary.getIdInternal());
        } else {
            summary = rootDomainObject.readSummaryByOID((Integer) arguments[1]);
        }
        return summary;
    }

    /**
     * @param id
     * @return
     * @throws ExcepcaoPersistencia
     */
    private Teacher getTeacher(IUserView id) throws ExcepcaoPersistencia {
        return Teacher.readTeacherByUsername(id.getUtilizador());
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean SummaryBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        Summary summary = null;
        InfoSummary infoSummary = null;

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
            if (argumentos[1] instanceof InfoSummary) {
                infoSummary = (InfoSummary) argumentos[1];

                summary = rootDomainObject.readSummaryByOID(infoSummary
                        .getIdInternal());
                if (summary == null) {
                    return false;
                }
            } else {
                summary = rootDomainObject.readSummaryByOID((Integer) argumentos[1]);
                if (summary == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        if (summary.getShift() != null && summary.getShift().getDisciplinaExecucao() != null) {
            return summary.getShift().getDisciplinaExecucao().equals(executionCourse);
        } else if (summary.getExecutionCourse() != null) {
            return summary.getExecutionCourse().equals(executionCourse);
        } else {
            return false;
        }
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
