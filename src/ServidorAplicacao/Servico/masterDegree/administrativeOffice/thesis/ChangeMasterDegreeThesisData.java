package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeProofVersion;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
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
public class ChangeMasterDegreeThesisData implements IServico {

	private static ChangeMasterDegreeThesisData servico = new ChangeMasterDegreeThesisData();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeMasterDegreeThesisData getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeMasterDegreeThesisData() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ChangeMasterDegreeThesisData";
	}

	public void run(
		IUserView userView,
		InfoStudentCurricularPlan infoStudentCurricularPlan,
		String dissertationTitle,
		ArrayList infoTeacherGuiders,
		ArrayList infoTeacherAssistentGuiders,
		ArrayList infoExternalPersonExternalAssistentGuiders)
		throws FenixServiceException {
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
			IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
				sp.getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(studentCurricularPlan);

			//if (masterDegreeThesisDataVersion == null)
			//	throw new ExcepcaoInexistente("Master Degree Proof not found.");

			//infoMasterDegreeProofVersion = Cloner.copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

	}
}