package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IStudentCurricularPlan;
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
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan implements IServico {

	private static ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan servico = new ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan";
	}

	public InfoMasterDegreeThesisDataVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {
		InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
			IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = sp.getIPersistentMasterDegreeThesisDataVersion()
					.readActiveByStudentCurricularPlan(studentCurricularPlan);

			if (masterDegreeThesisDataVersion == null)
				throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesis");

			infoMasterDegreeThesisDataVersion = Cloner.copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(
					masterDegreeThesisDataVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeThesisDataVersion;
	}
}