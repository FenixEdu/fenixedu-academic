package ServidorAplicacao.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
public class ReadExternalPersonsByWorkLocation implements IServico {

	private static ReadExternalPersonsByWorkLocation servico = new ReadExternalPersonsByWorkLocation();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExternalPersonsByWorkLocation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExternalPersonsByWorkLocation() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ReadExternalPersonsByWorkLocation";
	}

	public List run(Integer workLocationID) throws FenixServiceException {
		List infoExternalPersons = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			List externalPersons = sp.getIPersistentExternalPerson().readByWorkLocation(workLocationID);
			infoExternalPersons = Cloner.copyListIExternalPerson2ListInfoExternalPerson(externalPersons);


		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoExternalPersons;
	}
}