package ServidorAplicacao.Servicos.coordinator;

import java.util.List;

import DataBeans.InfoDegreeInfo;
import Dominio.CursoExecucao;
import Dominio.DegreeInfo;
import Dominio.ICursoExecucao;
import Dominio.IDegreeInfo;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ICursoExecucaoPersistente;
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
	
	public InfoDegreeInfo getNewDegreeInfoForm(Integer infoDegreeInfoId) {
		InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();

		infoDegreeInfo.setIdInternal(infoDegreeInfoId);

		infoDegreeInfo.setObjectives("objectivesNew");
		infoDegreeInfo.setHistory("historyNew");
		infoDegreeInfo.setProfessionalExits("professionalExitsNew");
		infoDegreeInfo.setAdditionalInfo("additionalInfoNew");
		infoDegreeInfo.setLinks("linksNew");
		infoDegreeInfo.setTestIngression("testIngressionNew");
		infoDegreeInfo.setDriftsInitial(new Integer(100));
		infoDegreeInfo.setDriftsFirst(new Integer(75));
		infoDegreeInfo.setDriftsSecond(new Integer(25));
		infoDegreeInfo.setClassifications("classificationsNew");
		infoDegreeInfo.setMarkMin(new Double(15.0));
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

			Object[] args = { infoExecutionDegreeCode, infoDegreeInfoCode, infoDegreeInfo };

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

			//Read the change in degree info
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
			assertNotNull(degreeInfoAck.getDegree());
			assertEquals(degreeInfoAck.getDegree().getIdInternal(), new Integer(10));

			System.out.println(
				"EditDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: testSuccessfull");

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNewDegreeInfo() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(1000);
			Integer infoDegreeInfoCode = new Integer(0);
			InfoDegreeInfo infoDegreeInfo = getNewDegreeInfoForm(infoDegreeInfoCode);

			Object[] args = { infoExecutionDegreeCode, infoDegreeInfoCode, infoDegreeInfo };

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

			//Read the change in degree info
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente  persistenExecutionDegree = sp.getICursoExecucaoPersistente();
			
			//read executionDegree for find degree
			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(infoExecutionDegreeCode);
			
			sp.iniciarTransaccao();
			executionDegree = (ICursoExecucao) persistenExecutionDegree.readByOId(executionDegree, false);
			sp.confirmarTransaccao();
			
			assertNotNull(executionDegree);
			assertNotNull(executionDegree.getCurricularPlan());
			assertNotNull(executionDegree.getCurricularPlan().getDegree());
			assertEquals(executionDegree.getCurricularPlan().getDegree().getIdInternal(), new Integer(1000));
			
			//read degree info by degree
			IPersistentDegreeInfo persistentDegreeInfo = sp.getIPersistentDegreeInfo();

			sp.iniciarTransaccao();
			List degreeInfoList = (List) persistentDegreeInfo.readDegreeInfoByDegree(executionDegree.getCurricularPlan().getDegree());
			sp.confirmarTransaccao();

			assertEquals(new Integer(degreeInfoList.size()), new Integer(1));
			
			//verify change maded	
			IDegreeInfo degreeInfoAck = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size()-1);
			
			assertNotNull(degreeInfoAck);
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
			assertNotNull(degreeInfoAck.getDegree());
			assertEquals(degreeInfoAck.getDegree().getIdInternal(), new Integer(1000));

			System.out.println(
				"EditDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: testNewDegreeInfo");

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}		
	}
}
