/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.BibliographicReference;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentBibliographicReference;
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
public class ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter extends
                                                                                        AuthorizationByRoleFilter
{

    public ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter()
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
        if ((id == null) || (id.getRoles() == null)
                        || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                        || !lecturesExecutionCourse(id, arguments)
                        || !bibliographicReferenceBelongsExecutionCourse(id, arguments))
        {
            throw new NotAuthorizedException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean bibliographicReferenceBelongsExecutionCourse(IUserView id, Object[] argumentos)
    {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IBibliographicReference bibReference = null;
        InfoBibliographicReference infoBibReference = null;

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
            IPersistentBibliographicReference persistentBibliographicReference = sp
                            .getIPersistentBibliographicReference();
            if (argumentos[1] instanceof InfoBibliographicReference)
            {
                infoBibReference = (InfoBibliographicReference) argumentos[1];
                bibReference = Cloner
                                .copyInfoBibliographicReference2IBibliographicReference(infoBibReference);
                bibReference = (IBibliographicReference) persistentBibliographicReference.readByOId(
                                bibReference, false);
            }
            else
            {
                bibReference = (IBibliographicReference) persistentBibliographicReference.readByOId(
                                new BibliographicReference((Integer) argumentos[1]), false);

            }
        }
        catch (Exception e)
        {
            return false;
        }
        return ((bibReference != null) && (bibReference.getExecutionCourse().equals(executionCourse)));
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
