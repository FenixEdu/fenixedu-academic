/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import Dominio.ITeacher;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.OldPublication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOldPublication;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationTeacherAuthorizationFilter extends AuthorizationByRoleFilter
{

    private static PublicationTeacherAuthorizationFilter instance =
        new PublicationTeacherAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    private PublicationTeacherAuthorizationFilter()
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

    public void preFiltragem(IUserView id, IServico service, Object[] arguments)
        throws NotAuthorizedException
    {
        try
        {
            boolean isNew = ((arguments[0] == null) || ((Integer) arguments[0]).equals(new Integer(0)));

            if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || ((!isNew) && (!oldPublicationBelongsToTeacher(id, (Integer) arguments[0]))))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean oldPublicationBelongsToTeacher(IUserView id, Integer oldPublicationId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IOldPublication oldPublication =
                (IOldPublication) persistentOldPublication.readByOID(
                    OldPublication.class,
                    oldPublicationId);

            if (!oldPublication.getTeacher().equals(teacher))
                return false;
            return true;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
