/*
 * Created on Nov 12, 2003
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.ExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class ExecutionCourseAndExamLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter
{

    public ExecutionCourseAndExamLecturingTeacherAuthorizationFilter()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType()
    {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
                    Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try
        {
            if ((id == null) || (id.getRoles() == null)
                            || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                            || !lecturesExecutionCourse(id, arguments)
                            || !examBelongsExecutionCourse(id, arguments))
            {
                throw new NotAuthorizedException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedException();
        }

    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos)
    {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IProfessorship professorship = null;
        if (argumentos == null)
        {
            return false;
        }
        try
        {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse)
            {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            }
            else
            {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                                new ExecutionCourse((Integer) argumentos[0]), false);
            }

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
            if (teacher != null && executionCourse != null)
            {
                IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
                professorship = persistentProfessorship.readByTeacherAndExecutionCoursePB(
                                teacher, executionCourse);
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return professorship != null;
    }

    private boolean examBelongsExecutionCourse(IUserView id, Object[] argumentos)
    {

        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        InfoExam infoExam = null;
        IExam exam = null;

        if (argumentos == null)
        {
            return false;
        }
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentExam persistentExam = sp.getIPersistentExam();

            if (argumentos[0] instanceof InfoExecutionCourse)
            {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            }
            else
            {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                                new ExecutionCourse((Integer) argumentos[0]), false);
            }

            if (argumentos[1] instanceof InfoExam)
            {
                infoExam = (InfoExam) argumentos[1];
                exam = Cloner.copyInfoExam2IExam(infoExam);
            }
            else
            {
                exam = (IExam) persistentExam.readByOId(new Exam((Integer) argumentos[1]), false);
            }

            if (executionCourse != null && exam != null) return executionCourse.getAssociatedExams()
                            .contains(exam);
            else
                return false;

        }
        catch (Exception e)
        {
            return false;
        }
    }

}
