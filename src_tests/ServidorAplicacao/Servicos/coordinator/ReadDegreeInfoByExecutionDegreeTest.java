package ServidorAplicacao.Servicos.coordinator;

import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Tânia Pousão 
 * Create on 7/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegreeTest extends ServiceTestCase {
	public ReadDegreeInfoByExecutionDegreeTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDegreeInfoByExecutionDegree";
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

	public void testSuccessfullWithOnlyOneDegreeInfo() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(id, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			//Returned anything?
			if (infoDegreeInfo == null) {
				fail("Reading a Degree Info.");
			}

			//Verify if read the correct data was read
			assertEquals(infoDegreeInfo.getObjectives(), new String("Obj"));
			assertEquals(infoDegreeInfo.getHistory(), new String("His"));
			assertEquals(infoDegreeInfo.getProfessionalExits(), new String("Prof"));
			assertEquals(infoDegreeInfo.getAdditionalInfo(), new String("addInfo"));
			assertEquals(infoDegreeInfo.getLinks(), new String("Links"));
			assertEquals(infoDegreeInfo.getTestIngression(), new String("Test"));
			assertEquals(infoDegreeInfo.getDriftsInitial(), new Integer(50));
			assertEquals(infoDegreeInfo.getDriftsFirst(), new Integer(0));
			assertEquals(infoDegreeInfo.getDriftsSecond(), new Integer(0));
			assertEquals(infoDegreeInfo.getClassifications(), new String("Class"));
			assertEquals(infoDegreeInfo.getMarkMin(), new Double(0.0));
			assertEquals(infoDegreeInfo.getMarkMax(), new Double(0.0));
			assertEquals(infoDegreeInfo.getMarkAverage(), new Double(12.0));
			assertEquals(infoDegreeInfo.getInfoDegree().getIdInternal(), new Integer(10));

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testSuccessfullWithMoreThanOneDegreeInfo() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(100);

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

			//Returned anything?
			if (infoDegreeInfo == null) {
				fail("Reading a Degree Info.");
			}

			//Verify if read the correct data was read, the most recently
			assertEquals(infoDegreeInfo.getObjectives(), new String("Obj2"));
			assertEquals(infoDegreeInfo.getHistory(), new String("His2"));
			assertEquals(infoDegreeInfo.getProfessionalExits(), new String("Prof2"));
			assertEquals(infoDegreeInfo.getAdditionalInfo(), new String("addInfo2"));
			assertEquals(infoDegreeInfo.getLinks(), new String("Links2"));
			assertEquals(infoDegreeInfo.getTestIngression(), new String("Test2"));
			assertEquals(infoDegreeInfo.getDriftsInitial(), new Integer(60));
			assertEquals(infoDegreeInfo.getDriftsFirst(), new Integer(0));
			assertEquals(infoDegreeInfo.getDriftsSecond(), new Integer(0));
			assertEquals(infoDegreeInfo.getClassifications(), new String("Class2"));
			assertEquals(infoDegreeInfo.getMarkMin(), new Double(0.0));
			assertEquals(infoDegreeInfo.getMarkMax(), new Double(0.0));
			assertEquals(infoDegreeInfo.getMarkAverage(), new Double(10.0));
			assertEquals(infoDegreeInfo.getInfoDegree().getIdInternal(), new Integer(10));

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testUserUnsuccessfull() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getAuthenticatedAndUnauthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			fail("Reading a degree site with invalid user");
			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}
	
	public void testSecondUserUnsuccessfull() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getNotAuthenticatedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			fail("Reading a degree site with invalid user");
			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNULLArg() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = null;

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
	
	public void testNoDegreeCurricularPlan() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(30);

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
	
	public void testNoDegree() {
		try {
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(20);

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
