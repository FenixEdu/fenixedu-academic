package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.MasterDegreeThesisDataVersion;
import ServidorAplicacao.IServico;
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
public class ReadMasterDegreeThesisDataVersionByID implements IServico {

	private static ReadMasterDegreeThesisDataVersionByID servico = new ReadMasterDegreeThesisDataVersionByID();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadMasterDegreeThesisDataVersionByID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadMasterDegreeThesisDataVersionByID() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ReadMasterDegreeThesisDataVersionByID";
	}

	public Object run(Integer masterDegreeThesisDataVersionID) throws FenixServiceException {
		InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
		IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) sp.getIPersistentMasterDegreeThesisDataVersion().readByOID(MasterDegreeThesisDataVersion.class, masterDegreeThesisDataVersionID);

			if (masterDegreeThesisDataVersion == null)
				throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesisDataVersion"); 
					
			infoMasterDegreeThesisDataVersion = Cloner.copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeThesisDataVersion;
	}
}