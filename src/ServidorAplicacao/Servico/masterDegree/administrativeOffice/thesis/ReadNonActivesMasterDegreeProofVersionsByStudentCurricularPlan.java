package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.util.List;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
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
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan implements IServico
{

	private static ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan servico =
		new ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan getService()
	{
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan()
	{
	}

	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan";
	}

	public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException
	{
		List infoMasterDegreeProofVersions = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlan studentCurricularPlan =
				Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

			List masterDegreeProofVersions =
				sp.getIPersistentMasterDegreeProofVersion().readNotActiveByStudentCurricularPlan(
					studentCurricularPlan);

			if (masterDegreeProofVersions.isEmpty())
				throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeProofVersion");

			infoMasterDegreeProofVersions =
				Cloner.copyListIMasterDegreeProofVersion2ListInfoMasterDegreeProofVersion(
					masterDegreeProofVersions);

		} catch (ExcepcaoPersistencia ex)
		{
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeProofVersions;
	}
}