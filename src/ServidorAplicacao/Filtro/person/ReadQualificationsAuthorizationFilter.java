/*
 * Created on 10/Nov/2003
 */

package ServidorAplicacao.Filtro.person;

import Dominio.IPessoa;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class ReadQualificationsAuthorizationFilter extends Filtro
{

    public final static ReadQualificationsAuthorizationFilter instance =
        new ReadQualificationsAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    //Role Type of teacher
    protected RoleType getRoleTypeTeacher()
    {
        return RoleType.TEACHER;
    }

    //Role Type of Grant Owner Manager
    protected RoleType getRoleTypeGrantOwnerManager()
    {
        return RoleType.GRANT_OWNER_MANAGER;
    }

    /**
	 * Runs the filter
	 * 
	 * @param id
	 * @param service
	 * @param arguments
	 */
    public void preFiltragem(IUserView id, IServico service, Object[] arguments)
        throws NotAuthorizedException
    {
        try
        {
            //Verify if needed fields are null
            if ((id == null) || (id.getRoles() == null))
            {
                throw new NotAuthorizedException();
            }

            //Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            boolean valid = false;

            if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
                && isGrantOwner((String) arguments[0]))
            {
                valid = true;
            }

            if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher()))
            {
                valid = true;
            }

            if (!valid)
                throw new NotAuthorizedException();
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException();
        }
    }

    /**
	 * Verifies if the qualification user ir a grant owner
	 * 
	 * @param arguments
	 * @return true or false
	 */
    private boolean isGrantOwner(String user)
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();
            IPersistentGrantOwner persistentGrantOwner = persistentSuport.getIPersistentGrantOwner();

            IPessoa person = persistentPerson.lerPessoaPorUsername(user);
            //Try to read the grant owner from de database
            IGrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(person.getIdInternal());

            return grantOwner != null;
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