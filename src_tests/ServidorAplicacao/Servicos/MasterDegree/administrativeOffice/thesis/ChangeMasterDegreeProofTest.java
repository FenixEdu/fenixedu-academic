package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.GregorianCalendar;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MasterDegreeClassification;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProofTest extends ServiceTestCase {

	private GestorServicos serviceManager = GestorServicos.manager();
	private IUserView userView = null;
	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public ChangeMasterDegreeProofTest(String testName) {
		super(testName);
		this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testChangeMasterDegreeProofDataSet.xml";
	}

	protected void setUp() {
		super.setUp();
		this.userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
	}

	/**
	 * @param strings
	 * @return
	 */
	private IUserView authenticateUser(String[] args) {
		SuportePersistenteOJB.resetInstance();

		try {
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;
		}
	}

	protected String getNameOfServiceToBeTested() {
		return "ChangeMasterDegreeProof";
	}

	protected String getDataSetFilePath() {
		return this.dataSetFilePath;
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "f3667", "pass", getApplication()};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.INTRANET;
	}

	public void testSuccessChangeExistingMasterDegreeProof() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
				(InfoMasterDegreeProofVersion) serviceManager.executar(
					userView,
					"ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
					argsReadMasterDegreeProofVersion);

			Object[] argsChangeMasterDegreeProof =
				{
					userView,
					infoStudentCurricularPlan,
					new GregorianCalendar(2003, Calendar.DECEMBER, 15).getTime(),
					infoMasterDegreeProofVersion.getThesisDeliveryDate(),
					MasterDegreeClassification.APPROVED,
					infoMasterDegreeProofVersion.getAttachedCopiesNumber()};

			serviceManager.executar(userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeProof);
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testExpectedChangeMasterDegreeProofDataSet.xml");
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessChangeExistingMasterDegreeProof " + ex.getMessage());
		}

	}

	public void testUnsuccessfulChangeExistingMasterDegreeProofWithoutScholarshipFinished() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
				(InfoMasterDegreeProofVersion) serviceManager.executar(
					userView,
					"ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
					argsReadMasterDegreeProofVersion);

			Object[] argsChangeMasterDegreeProof =
				{
					userView,
					infoStudentCurricularPlan,
					new GregorianCalendar(2003, Calendar.DECEMBER, 15).getTime(),
					infoMasterDegreeProofVersion.getThesisDeliveryDate(),
					MasterDegreeClassification.APPROVED,
					infoMasterDegreeProofVersion.getAttachedCopiesNumber()};

			serviceManager.executar(userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeProof);

			fail("testUnsuccessfulChangeExistingMasterDegreeProofWithoutScholarshipFinished didn't throw ScholarshipNotFinishedServiceException");

		} catch (ScholarshipNotFinishedServiceException ex) {
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testUnsuccessfulChangeExistingMasterDegreeProofWithoutScholarshipFinished " + ex.getMessage());
		}

	}

	public void testUnsuccessfulChangeMasterDegreeProofWithoutExistingMasterDegreeThesis() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(5461), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsChangeMasterDegreeProof =
				{
					userView,
					infoStudentCurricularPlan,
					new GregorianCalendar(2003, Calendar.DECEMBER, 15).getTime(),
					new GregorianCalendar(2003, Calendar.NOVEMBER, 15).getTime(),
					MasterDegreeClassification.APPROVED,
					new Integer(5)};

			serviceManager.executar(userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeProof);

			fail("testUnsuccessfulChangeMasterDegreeProofWithoutExistingMasterDegreeThesis didn't throw NonExistingServiceException");

		} catch (NonExistingServiceException ex) {
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testUnsuccessfulChangeExistingMasterDegreeProofWithoutScholarshipFinished " + ex.getMessage());
		}

	}

}
