package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MasterDegreeClassification;
import Util.State;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest extends ServiceTestCase {

	private GestorServicos serviceManager = GestorServicos.manager();
	private IUserView userView = null;
	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest(String testName) {
		super(testName);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadActiveMasterDegreeProofVersionByStudentCurricularPlanDataSet.xml";
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
		return "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan";
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

	public void testSuccessReadActiveMasterDegreeProofVersion() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
				(InfoMasterDegreeProofVersion) serviceManager.executar(userView, getNameOfServiceToBeTested(), argsReadMasterDegreeProofVersion);

			assertEquals(infoMasterDegreeProofVersion.getIdInternal(), new Integer(10));
			assertEquals(infoMasterDegreeProofVersion.getInfoMasterDegreeThesis().getIdInternal(), new Integer(10));
			assertEquals(infoMasterDegreeProofVersion.getInfoResponsibleEmployee().getIdInternal(), new Integer(1194));
			assertEquals(infoMasterDegreeProofVersion.getCurrentState(), new State(State.ACTIVE));
			assertEquals(infoMasterDegreeProofVersion.getAttachedCopiesNumber(), new Integer(5));
			assertEquals(infoMasterDegreeProofVersion.getFinalResult(), MasterDegreeClassification.APPROVED);
			Date proofDate = new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime();
			assertEquals(infoMasterDegreeProofVersion.getProofDate(), proofDate);
			Date thesisDeliveryDate = new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime();
			assertEquals(infoMasterDegreeProofVersion.getThesisDeliveryDate(), thesisDeliveryDate);

			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessReadActiveMasterDegreeProofVersion " + ex.getMessage());
		}

	}

	public void testReadWithoutScholarShipFinished() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
				(InfoMasterDegreeProofVersion) serviceManager.executar(userView, getNameOfServiceToBeTested(), argsReadMasterDegreeProofVersion);
			
			fail("testReadWithScholarShipNotFinished did not throw ScholarShipNotFinishedServiceException");

		} catch (ScholarshipNotFinishedServiceException ex) {
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testReadWithScholarShipNotFinished " + ex.getMessage());
		}

	}

	public void testReadNonExistentMasterDegreeProofVersion() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(5461), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
				(InfoMasterDegreeProofVersion) serviceManager.executar(
					userView,
					getNameOfServiceToBeTested(),
					argsReadMasterDegreeProofVersion);
			
			fail("testReadNonExistentMasterDegreeProofVersion did not throw NonExistingServiceException");
					
		} catch (NonExistingServiceException ex) {
			//ok

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("testReadNonExistentMasterDegreeProofVersion " + e.getMessage());
		}

	}

}
