package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeProofVersion;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
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
public class ChangeMasterDegreeProof implements IServico {

	private static ChangeMasterDegreeProof servico = new ChangeMasterDegreeProof();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeMasterDegreeProof getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeMasterDegreeProof() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ChangeMasterDegreeProof";
	}

	public InfoMasterDegreeProofVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {
		InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
			IMasterDegreeProofVersion masterDegreeProofVersion = sp.getIPersistentMasterDegreeProofVersion()
					.readActiveByStudentCurricularPlan(studentCurricularPlan);

			if (masterDegreeProofVersion == null)
				throw new ExcepcaoInexistente("Master Degree Proof not found.");

			infoMasterDegreeProofVersion = Cloner.copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeProofVersion;
	}
}