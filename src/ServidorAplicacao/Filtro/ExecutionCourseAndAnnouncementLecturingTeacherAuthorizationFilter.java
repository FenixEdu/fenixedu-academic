/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.Announcement;
import Dominio.ExecutionCourse;
import Dominio.IAnnouncement;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *  
 */
public class ExecutionCourseAndAnnouncementLecturingTeacherAuthorizationFilter extends
                                                                              AuthorizationByRoleFilter
{

    public ExecutionCourseAndAnnouncementLecturingTeacherAuthorizationFilter()
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
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try
        {
            if ((id == null) || (id.getRoles() == null)
                            || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                            || !lecturesExecutionCourse(id, arguments)
                            || !announcementBelongsExecutionCourse(id, arguments))
            {
                throw new NotAuthorizedFilterException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean announcementBelongsExecutionCourse(IUserView id, Object[] argumentos)
    {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IAnnouncement announcement = null;
        InfoAnnouncement infoAnnouncement = null;

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
            IPersistentAnnouncement persistentAnnouncement = sp.getIPersistentAnnouncement();
            if (argumentos[1] instanceof InfoAnnouncement)
            {
                infoAnnouncement = (InfoAnnouncement) argumentos[1];
                announcement = Cloner.copyInfoAnnouncement2IAnnouncement(infoAnnouncement);
                announcement = (IAnnouncement) persistentAnnouncement.readByOId(announcement, false);
            }
            else
            {
                announcement = (IAnnouncement) persistentAnnouncement.readByOId(new Announcement(
                                (Integer) argumentos[1]), false);

            }
        }
        catch (Exception e)
        {
            return false;
        }
        return announcement.getSite().getExecutionCourse().equals(executionCourse);
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

}
