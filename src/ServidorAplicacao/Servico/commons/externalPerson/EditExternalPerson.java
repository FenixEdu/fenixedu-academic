package ServidorAplicacao.Servico.commons.externalPerson;

import DataBeans.InfoExternalPerson;
import Dominio.ExternalPerson;
import Dominio.IExternalPerson;
import Dominio.IPessoa;
import Dominio.IWorkLocation;
import Dominio.Pessoa;
import Dominio.WorkLocation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class EditExternalPerson implements IServico
{

	private static EditExternalPerson servico = new EditExternalPerson();

	/**
	 * The singleton access method of this class.
	 **/
	public static EditExternalPerson getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditExternalPerson()
	{
	}

	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "EditExternalPerson";
	}

	public void run(
		Integer externalPersonID,
		String name,
		String address,
		Integer workLocationID,
		String phone,
		String mobile,
		String fax,
		String homepage,
		String email)
		throws FenixServiceException
	{
		InfoExternalPerson infoExternalPerson = null;
		IExternalPerson storedExternalPerson = null;
		IExternalPerson storedExternalPerson2 = null;
		IPessoa person = null;
		IWorkLocation storedWorkLocation = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			storedExternalPerson =
				(IExternalPerson) sp.getIPersistentExternalPerson().readByOID(
					ExternalPerson.class,
					externalPersonID);

			if (storedExternalPerson == null)
				throw new NonExistingServiceException("error.exception.externalPerson.nonExistingExternalPsrson");

			storedExternalPerson2 =
				sp.getIPersistentExternalPerson().readByNameAndAddressAndWorkLocationID(
					name,
					address,
					workLocationID);

			// checks if existes another exernal person with the same name, address and name location
			if (storedExternalPerson2 != null)
			{
				if (!storedExternalPerson.getIdInternal().equals(storedExternalPerson2.getIdInternal()))
					throw new ExistingServiceException("error.exception.externalPerson.existingExternalPsrson");
			}

			person = new Pessoa();
			storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(WorkLocation.class, workLocationID);
			
			storedExternalPerson.getPerson().setNome(name);
			storedExternalPerson.getPerson().setMorada(address);
			storedExternalPerson.getPerson().setTelefone(phone);
			storedExternalPerson.getPerson().setTelemovel(mobile);
			storedExternalPerson.getPerson().setEnderecoWeb(homepage);
			storedExternalPerson.getPerson().setEmail(email);

			
			storedExternalPerson.setWorkLocation(storedWorkLocation);

			sp.getIPersistentExternalPerson().simpleLockWrite(storedExternalPerson);

		}
		catch (ExcepcaoPersistencia ex)
		{
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

	}
}