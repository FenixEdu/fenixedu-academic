/*
 * Created on 29/10/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import DataBeans.InfoPerson;
import DataBeans.grant.owner.InfoGrantOwner;
import Dominio.IPessoa;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantOwner;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class CreateGrantOwner
	extends ServidorAplicacao.Servico.person.base.CreatePersonBaseClass {

	private ISuportePersistente persistentSupport = null;
	private IPersistentGrantOwner persistentGrantOwner = null;

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

	private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber) throws ExcepcaoPersistencia{
		String result = null;
		result = "B" + grantOwnerNumber.toString();
		return result;
	}

	private void checkIfGrantOwnerExists(InfoPerson infoPerson)
		throws FenixServiceException {
		IGrantOwner grantOwner = null;
		persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

		try {
			grantOwner =
				persistentGrantOwner.readGrantOwnerByPerson(infoPerson);

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

		IPessoa person = null;

		checkIfGrantOwnerExists(infoGrantOwner.getPersonInfo());

		try {
			IGrantOwner grantOwner = new GrantOwner();
			persistentGrantOwner.simpleLockWrite(grantOwner);
			person = createPersonBase(infoGrantOwner.getPersonInfo());
			
			//Generate the GrantOwner's number
			Integer maxNumber = persistentGrantOwner.readMaxGrantOwnerNumber();
			int aux = maxNumber.intValue() + 1;
			Integer nextNumber = new Integer(aux);
			grantOwner.setNumber(nextNumber);
			
			//Generate the GrantOwner's Person Username
			person.setUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));
			
			grantOwner.setPerson(person);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		return true;
	}
}