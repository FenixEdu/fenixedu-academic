/*
 * Created on 29/10/2003
 *  
 */
package ServidorAplicacao.Servico.grant.owner;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.owner.InfoGrantOwner;
import Dominio.IDomainObject;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.PersonRole;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.person.base.CreatePersonBaseClass;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class EditGrantOwner extends CreatePersonBaseClass implements IService
{

	public EditGrantOwner()
    {
    }

    private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber)
    {
        String result = null;
        result = "b" + grantOwnerNumber.toString();
        return result;
    }

    private IGrantOwner checkIfGrantOwnerExists(
        Integer grantOwnerNumber,
        IPersistentGrantOwner persistentGrantOwner)
        throws FenixServiceException
    {
        IGrantOwner grantOwner = null;
        try
        {
            //grantOwner = persistentGrantOwner.readGrantOwnerByPerson(personIdInternal);
        	grantOwner = persistentGrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return grantOwner;
    }

    private IGrantOwner prepareGrantOwner(
        IGrantOwner grantOwner,
        IPessoa person,
        InfoGrantOwner infoGrantOwner,
        IPersistentGrantOwner pGrantOwner)
        throws ExcepcaoPersistencia
    {
        grantOwner.setPerson(person);
        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());
        pGrantOwner.simpleLockWrite(grantOwner);

        if (infoGrantOwner.getGrantOwnerNumber() == null)
        {
            //Generate the GrantOwner's number
            Integer maxNumber = pGrantOwner.readMaxGrantOwnerNumber();
            int aux = maxNumber.intValue() + 1;
            Integer nextNumber = new Integer(aux);
            grantOwner.setNumber(nextNumber);
        } else
            grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());

        return grantOwner;
    }

    protected boolean isNew(IDomainObject domainObject)
    {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(new Integer(0)));
    }

    /**
	 * Executes the service.
	 */
    public Integer run(InfoGrantOwner infoGrantOwner) throws FenixServiceException
    {
        ISuportePersistente sp = null;
        IPessoaPersistente pPerson = null;
        IPersistentGrantOwner pGrantOwner = null;
        IPersistentPersonRole pPersonRole = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw new FenixServiceException("Unable to dao factory!", e);
        }
        pGrantOwner = sp.getIPersistentGrantOwner();
        pPersonRole = sp.getIPersistentPersonRole();
        pPerson = sp.getIPessoaPersistente();

        try
        {
            IPessoa person = null;
            IGrantOwner grantOwner = null;

            //create or edit person information
            person =
                CreatePersonBaseClass.createPersonBase(
                    person,
                    infoGrantOwner.getPersonInfo(),
                    sp,
                    pPerson,
                    pPersonRole);

            //verify if person is new
            if (person.getUsername() != null)
                grantOwner = checkIfGrantOwnerExists(infoGrantOwner.getGrantOwnerNumber(), pGrantOwner);

            //create or edit grantOwner information
            IPersonRole personRole = null;
            if (grantOwner == null)
            {
                grantOwner = new GrantOwner();

                //Set the GRANT_OWNER Role to this new GrantOwner
                personRole = new PersonRole();
                personRole.setPerson(person);
                pPersonRole.simpleLockWrite(personRole);
                personRole.setRole(sp.getIPersistentRole().readByRoleType(RoleType.GRANT_OWNER));
            }
            grantOwner = prepareGrantOwner(grantOwner, person, infoGrantOwner, pGrantOwner);

            //Generate the GrantOwner's Person Username
            if (person.getUsername() == null)
                person.setUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));
            
            return grantOwner.getIdInternal();
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
    }
}