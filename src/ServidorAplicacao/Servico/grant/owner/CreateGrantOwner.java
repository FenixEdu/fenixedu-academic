/*
 * Created on 29/10/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import DataBeans.InfoPerson;
import DataBeans.grant.owner.InfoGrantOwner;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.PersonRole;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class CreateGrantOwner
	extends ServidorAplicacao.Servico.person.base.CreatePersonBaseClass
	implements IServico {

	private static CreateGrantOwner service = new CreateGrantOwner();
	/**
	 * The singleton access method of this class.
	 */
	public static CreateGrantOwner getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateGrantOwner() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "CreateGrantOwner";
	}

	private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber)
		throws ExcepcaoPersistencia {
		String result = null;
		result = "B" + grantOwnerNumber.toString();
		return result;
	}

	private IPessoa checkIfPersonExists(InfoPerson person)
		throws FenixServiceException {
		ISuportePersistente persistentSupport = null;
		IPessoaPersistente persistentPerson = null;
		IPessoa personToCheck = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentPerson = persistentSupport.getIPessoaPersistente();
			personToCheck = persistentPerson.lerPessoaPorNumDocIdETipoDocId(
										person.getNumeroDocumentoIdentificacao(),
										person.getTipoDocumentoIdentificacao());
		} catch (ExcepcaoPersistencia persistentException) {
			throw new FenixServiceException(persistentException.getMessage());
		}
		
		return personToCheck;
	}

	private void checkIfGrantOwnerExists(Integer personIdInternal)
		throws FenixServiceException {
		ISuportePersistente persistentSupport = null;
		IPersistentGrantOwner persistentGrantOwner = null;
		IPessoaPersistente persistentPerson = null;
		IPessoa person = null;
		IGrantOwner grantOwner = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentPerson = persistentSupport.getIPessoaPersistente();
			persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

			grantOwner =
				persistentGrantOwner.readGrantOwnerByPerson(personIdInternal);

		} catch (ExcepcaoPersistencia persistentException) {
			throw new FenixServiceException(persistentException.getMessage());
		}
		if (grantOwner != null)
			throw new ExistingServiceException();
	}

	/**
	 * Executes the service.
	 */
	public boolean run(InfoGrantOwner infoGrantOwner)
		throws FenixServiceException {
		ISuportePersistente persistentSupport = null;
		IPersistentGrantOwner persistentGrantOwner = null;
		IPersistentPersonRole persistentPersonRole = null;

		IPessoa person = checkIfPersonExists(infoGrantOwner.getPersonInfo());

		if (person != null) 
			{
				System.out.println("PERSON EXISTS!!");
				checkIfGrantOwnerExists(person.getIdInternal());	
			}else System.out.println("PERSON DOES NOT EXIST!!");
			

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();
			persistentPersonRole = persistentSupport.getIPersistentPersonRole();

			IGrantOwner grantOwner = new GrantOwner();
			persistentGrantOwner.simpleLockWrite(grantOwner);
			person = createPersonBase(infoGrantOwner.getPersonInfo());

			//Generate the GrantOwner's number
			Integer maxNumber = persistentGrantOwner.readMaxGrantOwnerNumber();
			int aux = maxNumber.intValue() + 1;
			Integer nextNumber = new Integer(aux);
			grantOwner.setNumber(nextNumber);
			
			grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
			grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());

			//Generate the GrantOwner's Person Username
			if(person.getUsername() == null)
				//UNCOMMENT this line to run CreateGrantOwnerTest
				person.setUsername("17");
				//COMMENT this line to run CreateGrantOwnerTest
				//person.setUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));

			//Set the GRANT_OWNER Role to this new GrantOwner
			IPersonRole personRole = new PersonRole();
			persistentPersonRole.simpleLockWrite(personRole);
			personRole.setPerson(person);
			personRole.setRole(persistentSupport.getIPersistentRole().readByRoleType(RoleType.GRANT_OWNER));

			grantOwner.setPerson(person);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		return true;
	}
}