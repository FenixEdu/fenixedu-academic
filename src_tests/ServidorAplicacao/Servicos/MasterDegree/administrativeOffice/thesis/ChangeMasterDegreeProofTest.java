package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.GregorianCalendar;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.MasterDegreeClassification;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProofTest extends AdministrativeOfficeBaseTest {

	/**
	 * @param testName
	 */
	public ChangeMasterDegreeProofTest(String testName) {
		super(testName);
		if (testName.equals("testSuccessfulChangeMasterDegreeProofWhenProofDoesNotExist")) {
			this.dataSetFilePath =
				"etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testChangeMasterDegreeProofWhenProofDoesNotExistDataSet.xml";
		} else {
			this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testChangeMasterDegreeProofDataSet.xml";
		}
	}

	protected String getNameOfServiceToBeTested() {
		return "ChangeMasterDegreeProof";
	}

	protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		infoStudentCurricularPlan.setIdInternal(new Integer(8582));
		Object[] argsChangeMasterDegreeProof =
			{
				null,
				infoStudentCurricularPlan,
				new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime(),
				new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime(),
				MasterDegreeClassification.UNDEFINED,
				new Integer(5)};

		return argsChangeMasterDegreeProof;
	}

	protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException{
		Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
		InfoStudentCurricularPlan infoStudentCurricularPlan =
			(InfoStudentCurricularPlan) serviceManager.executar(
				userViewNotAuthorized,
				"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
				argsReadStudentCurricularPlan);
		Object[] argsChangeMasterDegreeProof =
			{
				userViewNotAuthorized,
				infoStudentCurricularPlan,
				new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime(),
				new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime(),
				MasterDegreeClassification.UNDEFINED,
				new Integer(5)};

		return argsChangeMasterDegreeProof;
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

	public void testSuccessfulChangeMasterDegreeProofWhenProofDoesNotExist() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsChangeMasterDegreeProof =
				{
					userView,
					infoStudentCurricularPlan,
					new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime(),
					new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime(),
					MasterDegreeClassification.UNDEFINED,
					new Integer(5)};

			serviceManager.executar(userView, getNameOfServiceToBeTested(), argsChangeMasterDegreeProof);
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testExpectedChangeMasterDegreeProofWhenProofDoesNotExistDataSet.xml");
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessChangeExistingMasterDegreeProof " + ex.getMessage());
		}

	}

}
