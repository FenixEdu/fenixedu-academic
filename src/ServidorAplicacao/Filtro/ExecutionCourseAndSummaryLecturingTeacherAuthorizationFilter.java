/*
 * Created on 19/Mai/2003
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSummary;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 */
public class ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter
        extends AuthorizationByRoleFilter {

    public ExecutionCourseAndSummaryLecturingTeacherAuthorizationFilter() {

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

        try {
            if ((id == null)
                    || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(),
                            getRoleType())
                    || !lecturesExecutionCourse(id, arguments)
                    || !SummaryBelongsExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException ex) {
            throw new NotAuthorizedFilterException(ex.getMessage());
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean SummaryBelongsExecutionCourse(IUserView id,
            Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        ISummary summary = null;
        InfoSummary infoSummary = null;

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
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            if (argumentos[1] instanceof InfoSummary) {
                infoSummary = (InfoSummary) argumentos[1];

                summary = (ISummary) persistentSummary.readByOID(Summary.class,
                        infoSummary.getIdInternal());
                if (summary == null) {
                    return false;
                }
            } else {
                summary = (ISummary) persistentSummary.readByOID(Summary.class,
                        (Integer) argumentos[1]);
                if (summary == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        if (summary.getShift() != null
                && summary.getShift().getDisciplinaExecucao() != null) {
            return summary.getShift().getDisciplinaExecucao().equals(
                    executionCourse);
        } else if (summary.getExecutionCourse() != null) {
            return summary.getExecutionCourse().equals(executionCourse);
        } else {
            return false;
        }
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