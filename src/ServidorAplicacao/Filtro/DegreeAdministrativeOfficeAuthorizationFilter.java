package ServidorAplicacao.Filtro;

import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import Util.RoleType;

/**
 * @author David Santos
 *
 */

public class DegreeAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter
{

    public final static DegreeAdministrativeOfficeAuthorizationFilter instance =
        new DegreeAdministrativeOfficeAuthorizationFilter();

    /**
     * The singleton access method of this class.
     *
     * @return Returns the instance of this class responsible for the
     * authorization access to services.
     **/
    public static Filtro getInstance()
    {
        return instance;
    }

    protected RoleType getRoleType()
    {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Filtro.Filtro#preFiltragem(ServidorAplicacao.IUserView, ServidorAplicacao.IServico, java.lang.Object[])
     */
    public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception
    {

        if (!AuthorizationUtils
            .containsRole(id.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))
        {
            super.preFiltragem(id, servico, argumentos);
        }
    }

}