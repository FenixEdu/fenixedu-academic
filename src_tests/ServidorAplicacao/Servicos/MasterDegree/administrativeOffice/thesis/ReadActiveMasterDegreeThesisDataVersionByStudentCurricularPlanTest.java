package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.List;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanTest extends ServiceTestCase {

	private GestorServicos serviceManager = GestorServicos.manager();
	private IUserView userView = null;
	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanTest(String testName) {
		super(testName);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanDataSet.xml";
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
		return "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan";
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

	public void testSuccessReadActiveMasterDegreeThesisDataVersion() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) serviceManager.executar(
					userView,
					getNameOfServiceToBeTested(),
					argsReadMasterDegreeThesisDataVersion);

			assertEquals(infoMasterDegreeThesisDataVersion.getIdInternal(), new Integer(10));
			assertEquals(infoMasterDegreeThesisDataVersion.getInfoMasterDegreeThesis().getIdInternal(), new Integer(10));
			assertEquals(infoMasterDegreeThesisDataVersion.getInfoResponsibleEmployee().getIdInternal(), new Integer(1194));
			assertEquals(infoMasterDegreeThesisDataVersion.getDissertationTitle(), "some title");
			assertEquals(infoMasterDegreeThesisDataVersion.getCurrentState(), new State(State.ACTIVE));
			List infoGuiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
			List infoAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
			List infoExternalAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();
			assertEquals(((InfoTeacher) infoGuiders.get(0)).getIdInternal(), new Integer(954));
			assertEquals(((InfoTeacher) infoAssistentGuiders.get(0)).getIdInternal(), new Integer(955));
			assertEquals(((InfoExternalPerson) infoExternalAssistentGuiders.get(0)).getIdInternal(), new Integer(1));
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessReadActiveMasterDegreeThesis " + ex.getMessage());
		}

	}
	public void testReadWhenMasterDegreeThesisDoesNotExist() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) serviceManager.executar(
					userView,
					getNameOfServiceToBeTested(),
					argsReadMasterDegreeThesisDataVersion);
			
			fail("testReadWhenMasterDegreeThesisDoesNotExist did not throw NonExistingServiceException");
			
		} catch (NonExistingServiceException ex) {
			//ok
			
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("testReadNonExistingActiveMasterDegreeThesisDataVersion " + e.getMessage());
		}

	}

}
