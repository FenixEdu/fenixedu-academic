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

	private IPessoa checkIfPersonExists(InfoPerson infoPerson,IPessoaPersistente persistentPerson)
		throws FenixServiceException {
			IPessoa person = null;

		try {
			person =
				persistentPerson.lerPessoaPorNumDocIdETipoDocId(
					infoPerson.getNumeroDocumentoIdentificacao(),
					infoPerson.getTipoDocumentoIdentificacao());
		} catch (ExcepcaoPersistencia persistentException) {
			throw new FenixServiceException(persistentException.getMessage());
		}

		return person;
	}

	private void checkIfGrantOwnerExists(
		Integer personIdInternal,
		IPessoaPersistente persistentPerson,
		IPersistentGrantOwner persistentGrantOwner)
		throws FenixServiceException {
		IPessoa person = null;
		IGrantOwner grantOwner = null;
		try {
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
		IPessoaPersistente persistentPerson = null;
		
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("Unable to dao factory!", e);
		}
		persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();
		persistentPersonRole = persistentSupport.getIPersistentPersonRole();
		persistentPerson = persistentSupport.getIPessoaPersistente();

		IPessoa person = null;

		person = checkIfPersonExists(
			infoGrantOwner.getPersonInfo(),
			persistentPerson);

		if (person != null)
			checkIfGrantOwnerExists(
				person.getIdInternal(),
				persistentPerson,
				persistentGrantOwner);
				
		try {
			//next 2lines are necessary due to a possible OJB lock problem
			persistentSupport.confirmarTransaccao();
			persistentSupport.iniciarTransaccao();
			
			person =
				createPersonBase(
					infoGrantOwner.getPersonInfo(),
					persistentSupport,
					persistentPerson,
					persistentPersonRole);
			IGrantOwner grantOwner = new GrantOwner();
			grantOwner.setPerson(person);
			grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
			grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());
			persistentGrantOwner.simpleLockWrite(grantOwner);

			//Generate the GrantOwner's number
			Integer maxNumber = persistentGrantOwner.readMaxGrantOwnerNumber();
			int aux = maxNumber.intValue() + 1;
			Integer nextNumber = new Integer(aux);
			grantOwner.setNumber(nextNumber);

			//Generate the GrantOwner's Person Username
			if (person.getUsername() == null)
				person.setUsername(
					generateGrantOwnerPersonUsername(grantOwner.getNumber()));

			//Set the GRANT_OWNER Role to this new GrantOwner
			IPersonRole personRole = new PersonRole();
			personRole.setPerson(person);
			persistentPersonRole.simpleLockWrite(personRole);
			personRole.setRole(
				persistentSupport.getIPersistentRole().readByRoleType(
					RoleType.GRANT_OWNER));
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return true;
	}
}