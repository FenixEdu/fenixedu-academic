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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class InsertExternalPerson implements IServico
{

	private static InsertExternalPerson servico = new InsertExternalPerson();

	/**
	 * The singleton access method of this class.
	 **/
	public static InsertExternalPerson getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private InsertExternalPerson()
	{
	}

	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "InsertExternalPerson";
	}

	public void run(
		String name,
		String address,
		Integer workLocationID,
		String phone,
		String mobile,
		String homepage,
		String email)
		throws FenixServiceException
	{
		InfoExternalPerson infoExternalPerson = null;
		IExternalPerson externalPerson = null;
		IExternalPerson storedExternalPerson = null;
		IPessoa person = null;
		IWorkLocation storedWorkLocation = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			storedExternalPerson =
				sp.getIPersistentExternalPerson().readByNameAndAddressAndWorkLocationID(
					name,
					address,
					workLocationID);

			if (storedExternalPerson != null)
				throw new ExistingServiceException("error.exception.commons.externalPerson.existingExternalPerson");

			storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(WorkLocation.class, workLocationID);
			
			//generate new identification number
			String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
			int nextID = Integer.parseInt(lastDocumentIdNumber) + 1;
			lastDocumentIdNumber  = "" + nextID; 
			
			externalPerson = new ExternalPerson();
			person = new Pessoa();
			
			person.setNome(name);
			person.setMorada(address);
			person.setTelefone(phone);
			person.setTelemovel(mobile);
			person.setEnderecoWeb(homepage);
			person.setEmail(email);
			person.setNumeroDocumentoIdentificacao(lastDocumentIdNumber);
			person.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));
			person.setUsername("e" + lastDocumentIdNumber);
			
			sp.getIPessoaPersistente().simpleLockWrite(person);
			
			externalPerson.setPerson(person);
			externalPerson.setWorkLocation(storedWorkLocation);

			
			sp.getIPersistentExternalPerson().simpleLockWrite(externalPerson);

		}
		catch (ExcepcaoPersistencia ex)
		{
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

	}
}