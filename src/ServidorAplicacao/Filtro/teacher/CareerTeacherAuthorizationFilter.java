/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ICareer;
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
import ServidorPersistente.teacher.IPersistentCareer;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerTeacherAuthorizationFilter extends AuthorizationByRoleFilter
{

    private static CareerTeacherAuthorizationFilter instance = new CareerTeacherAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    private CareerTeacherAuthorizationFilter()
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
            if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || ((arguments[0] instanceof Integer)
                    && (arguments[0] != null)
                    && !careerBelongsToTeacher(id, (Integer) arguments[0])))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean careerBelongsToTeacher(IUserView id, Integer careerId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            ICareer career = (ICareer) persistentCareer.readByOID(Career.class, careerId);
            
            if (!career.getTeacher().equals(teacher))
                return false;
            return true;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }
}
