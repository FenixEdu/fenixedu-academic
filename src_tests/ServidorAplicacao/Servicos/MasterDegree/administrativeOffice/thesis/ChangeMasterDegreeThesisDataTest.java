package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.List;

import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeThesisDataTest extends ServiceTestCase {

	private GestorServicos serviceManager = GestorServicos.manager();
	private IUserView userView = null;
	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public ChangeMasterDegreeThesisDataTest(String testName) {
		super(testName);
		this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testChangeMasterDegreeThesisDataSet.xml";
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
		return "ChangeMasterDegreeThesisData";
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

	public void testSuccessfulChangeMasterDegreeThesisData() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) serviceManager.executar(
					userView,
					"ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
					argsReadMasterDegreeThesis);

			InfoTeacher infoTeacherGuider = new InfoTeacher();
			infoTeacherGuider.setIdInternal(new Integer(956));
			InfoTeacher infoTeacherAssistent = new InfoTeacher();
			infoTeacherAssistent.setIdInternal(new Integer(957));

			List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
			List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
			List externalAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();

			guiders.add(infoTeacherGuider);
			assistentGuiders.add(infoTeacherAssistent);

			Object[] argsChangeMasterDegreeThesis =
				{ userView, infoStudentCurricularPlan, "some title", guiders, assistentGuiders, externalAssistentGuiders };

			serviceManager.executar(this.userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeThesis);
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testExpectedChangeMasterDegreeThesisDataSet.xml");
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessfulChangeMasterDegreeThesis " + ex.getMessage());
		}
	}

	public void testChangeMasterDegreeThesisDataUsingExistingDissertationTitle() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) serviceManager.executar(
					userView,
					"ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
					argsReadMasterDegreeThesis);

			InfoTeacher infoTeacherGuider = new InfoTeacher();
			infoTeacherGuider.setIdInternal(new Integer(956));
			InfoTeacher infoTeacherAssistent = new InfoTeacher();
			infoTeacherAssistent.setIdInternal(new Integer(957));

			List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
			List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
			List externalAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();

			guiders.add(infoTeacherGuider);
			assistentGuiders.add(infoTeacherAssistent);

			Object[] argsChangeMasterDegreeThesis =
				{ userView, infoStudentCurricularPlan, "Existing Title", guiders, assistentGuiders, externalAssistentGuiders };
			serviceManager.executar(this.userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeThesis);

			fail("testChangeMasterDegreeThesisDataUsingExistingDissertationTitle did not throw ExistingServiceException");

		} catch (ExistingServiceException ex) {
			//test passed
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testChangeMasterDegreeThesisUsingExistingDissertationTitle" + ex.getMessage());
		}
	}

	public void testChangeWhenMasterDegreeThesisDoesNotExist() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(180), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) serviceManager.executar(
					userView,
					"ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
					argsReadMasterDegreeThesisDataVersion);

			fail("testChangeWhenMasterDegreeThesisDoesNotExist did not throw NonExistingServiceException");

		} catch (NonExistingServiceException ex) {
			//ok

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("testChangeWhenMasterDegreeThesisDoesNotExist " + e.getMessage());
		}

	}

}
