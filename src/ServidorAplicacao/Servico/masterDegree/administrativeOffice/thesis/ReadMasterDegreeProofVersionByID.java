package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeProofVersion;
import Dominio.MasterDegreeProofVersion;
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
public class ReadMasterDegreeProofVersionByID implements IServico {

	private static ReadMasterDegreeProofVersionByID servico = new ReadMasterDegreeProofVersionByID();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadMasterDegreeProofVersionByID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadMasterDegreeProofVersionByID() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ReadMasterDegreeProofVersionByID";
	}

	public Object run(Integer masterDegreeProofVersionID) throws FenixServiceException {
		InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
		IMasterDegreeProofVersion masterDegreeProofVersion = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			masterDegreeProofVersion = (IMasterDegreeProofVersion) sp.getIPersistentMasterDegreeProofVersion().readByOID(MasterDegreeProofVersion.class, masterDegreeProofVersionID);

			if (masterDegreeProofVersion == null)
				throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeProofVersion"); 
					
			infoMasterDegreeProofVersion = Cloner.copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeProofVersion;
	}
}