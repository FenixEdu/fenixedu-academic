/*
 * Created on 21/Nov/2003
 */
package ServidorAplicacao.Filtro.person;

import Dominio.IPessoa;
import Dominio.IQualification;
import Dominio.Qualification;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualificationAuthorizationFilter extends Filtro
{

    public final static ReadQualificationAuthorizationFilter instance =
        new ReadQualificationAuthorizationFilter();

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
            boolean isNew = ((arguments[0] == null) || ((Integer) arguments[0]).equals(new Integer(0)));

            //Verify if needed fields are null
            if ((id == null) || (id.getRoles() == null))
            {
                throw new NotAuthorizedException();
            }

            Integer objectId = (Integer) arguments[0];

            //Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            if (!isNew)
            {
                boolean valid = false;

                if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
                    && isGrantOwner(objectId))
                {
                    valid = true;
                }

                if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())
                    && isOwnQualification(id.getUtilizador(), objectId))
                {
                    valid = true;
                }

                if (!valid)
                    throw new NotAuthorizedException();
            } else
            {
                if (!AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())
                    && !AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher()))
                    throw new NotAuthorizedException();
            }
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
    /**
	 * Verifies if the qualification user is a grant owner
	 * 
	 * @param arguments
	 * @return true or false
	 */
    private boolean isGrantOwner(Integer objectId)
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentQualification persistentQualification =
                persistentSuport.getIPersistentQualification();
            IQualification qualification =
                (IQualification) persistentQualification.readByOID(Qualification.class, objectId);

            IPersistentGrantOwner persistentGrantOwner = persistentSuport.getIPersistentGrantOwner();
            //Try to read the grant owner from the database
            IGrantOwner grantOwner =
                persistentGrantOwner.readGrantOwnerByPerson(qualification.getPerson().getIdInternal());

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

    /**
	 * Verifies if the qualification to be changed is owned by the user that is running the service
	 * 
	 * @param arguments
	 * @return true or false
	 */
    private boolean isOwnQualification(String username, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(username);

            IPersistentQualification persistentQualification = sp.getIPersistentQualification();
            IQualification qualification =
                (IQualification) persistentQualification.readByOID(Qualification.class, objectId);

            return qualification.getPerson().equals(person);
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