/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformationAuthorizationFilter extends AuthorizationByRoleFilter
{

    public EditTeacherInformationAuthorizationFilter()
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
    /* (non-Javadoc)
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
                    Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);        
        try
        {
            if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(
                            id.getRoles(), getRoleType())))
            || (id == null)
            || (id.getRoles() == null)
            || (!argumentsBelongToTeacher(
                            id, (InfoServiceProviderRegime) arguments[0],
                            (InfoWeeklyOcupation) arguments[1])))
            {
                throw new NotAuthorizedException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean argumentsBelongToTeacher(IUserView id,
                    InfoServiceProviderRegime infoServiceProviderRegime,
                    InfoWeeklyOcupation infoWeeklyOcupation)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            Integer teacherId = teacher.getIdInternal();

            if (!infoServiceProviderRegime.getInfoTeacher().getIdInternal().equals(teacherId))
                            return false;

            if (!infoWeeklyOcupation.getInfoTeacher().getIdInternal().equals(teacherId)) return false;
            return true;
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        }
        catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }
}
