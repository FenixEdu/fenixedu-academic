package ServidorAplicacao.Servicos.coordinator;

import DataBeans.InfoDegreeInfo;
import Dominio.DegreeInfo;
import Dominio.IDegreeInfo;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 7/Nov/2003
 */
public class EditDegreeInfoByExecutionDegreeTest extends ServiceTestCase {
	public EditDegreeInfoByExecutionDegreeTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested() {
		return "EditDegreeInfoByExecutionDegree";
	}

	protected String getDataSetFilePath() {
		return "etc/testDataSetDegreeSite.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "userC", "pass", getApplication()};
		return args;
	}

	protected String[] getSecondAuthenticatedAndAuthorizedUser() {
		String[] args = { "userC2", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndAlreadyAuthorizedUser() {
		String[] args = { "userT", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "userE", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionDegreeCode = new Integer(10);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}

	protected Object[] getExecutionDegreeUnsuccessfullArguments() {
		Integer infoExecutionDegreeCode = new Integer(1000);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}

	protected Object[] getExecutionDegreeNullArguments() {
		Integer infoExecutionDegreeCode = null;

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}

	public InfoDegreeInfo getDegreeInfoForm(Integer infoDegreeInfoId) {
		InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();

		infoDegreeInfo.setIdInternal(infoDegreeInfoId);
		infoDegreeInfo.setObjectives("objectives");
		infoDegreeInfo.setHistory("history");
		infoDegreeInfo.setProfessionalExits("professionalExits");
		infoDegreeInfo.setAdditionalInfo("additionalInfo");
		infoDegreeInfo.setLinks("links");
		infoDegreeInfo.setTestIngression("testIngression");
		infoDegreeInfo.setDriftsInitial(new Integer(100));
		infoDegreeInfo.setDriftsFirst(new Integer(90));
		infoDegreeInfo.setDriftsSecond(new Integer(10));
		infoDegreeInfo.setClassifications("classifications");
		infoDegreeInfo.setMarkMin(new Double(0.0));
		infoDegreeInfo.setMarkMax(new Double(20.0));
		infoDegreeInfo.setMarkAverage(new Double(10.0));

		return infoDegreeInfo;
	}

	public void testSuccessfull() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(10);
			Integer infoDegreeInfoCode = new Integer(1);
			InfoDegreeInfo infoDegreeInfo = getDegreeInfoForm(infoDegreeInfoCode);

			Object[] args = { infoExecutionDegreeCode, infoDegreeInfoCode, infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			//Service
			try {
				gestor.executar(id, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			IPersistentDegreeInfo persistentDegreeInfo = sp.getIPersistentDegreeInfo();

			sp.iniciarTransaccao();
			IDegreeInfo degreeInfoAck = new DegreeInfo();
			degreeInfoAck.setIdInternal(infoDegreeInfoCode);

			degreeInfoAck = (IDegreeInfo) persistentDegreeInfo.readByOId(degreeInfoAck, false);
			sp.confirmarTransaccao();

			assertEquals(infoDegreeInfo.getObjectives(), degreeInfoAck.getObjectives());
			assertEquals(infoDegreeInfo.getHistory(), degreeInfoAck.getHistory());
			assertEquals(infoDegreeInfo.getProfessionalExits(), degreeInfoAck.getProfessionalExits());
			assertEquals(infoDegreeInfo.getAdditionalInfo(), degreeInfoAck.getAdditionalInfo());
			assertEquals(infoDegreeInfo.getLinks(), degreeInfoAck.getLinks());
			assertEquals(infoDegreeInfo.getTestIngression(), degreeInfoAck.getTestIngression());
			assertEquals(infoDegreeInfo.getDriftsInitial(), degreeInfoAck.getDriftsInitial());
			assertEquals(infoDegreeInfo.getDriftsFirst(), degreeInfoAck.getDriftsFirst());
			assertEquals(infoDegreeInfo.getDriftsSecond(), degreeInfoAck.getDriftsSecond());
			assertEquals(infoDegreeInfo.getClassifications(), degreeInfoAck.getClassifications());
			assertEquals(infoDegreeInfo.getMarkMin(), degreeInfoAck.getMarkMin());
			assertEquals(infoDegreeInfo.getMarkMax(), degreeInfoAck.getMarkMax());
			assertEquals(infoDegreeInfo.getMarkAverage(), degreeInfoAck.getMarkAverage());
			assertEquals(infoDegreeInfo.getInfoDegree().getIdInternal(), degreeInfoAck.getDegree().getIdInternal());

			System.out.println("EditDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNoDegreeInfo() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(1000);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(id, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}
}
