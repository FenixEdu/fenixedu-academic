package ServidorAplicacao.Servicos.publico;

import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ReadDegreeInfoTest extends ServiceTestCase {
	public ReadDegreeInfoTest(String name) {
		super(name);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets_templates/servicos/coordinator/testDataSetDegreeSite.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDegreeInfo";
	}

	public void testSuccessfull() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(10);
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			//Returned anything?
			if (infoDegreeInfo == null) {
				fail("Reading a Degree Info.");
			}

			//Verify if read the correct data was read
			assertEquals(new String("Obj"), infoDegreeInfo.getObjectives());
			assertEquals(new String("His"), infoDegreeInfo.getHistory());
			assertEquals(new String("Prof"), infoDegreeInfo.getProfessionalExits());
			assertEquals(new String("addInfo"), infoDegreeInfo.getAdditionalInfo());
			assertEquals(new String("Links"), infoDegreeInfo.getLinks());
			assertEquals(new String("Test"), infoDegreeInfo.getTestIngression());
			assertEquals(new Integer(50), infoDegreeInfo.getDriftsInitial());
			assertEquals(new Integer(0), infoDegreeInfo.getDriftsFirst());
			assertEquals(new Integer(0), infoDegreeInfo.getDriftsSecond());
			assertEquals(new String("Class"), infoDegreeInfo.getClassifications());
			assertEquals(new Double(0.0), infoDegreeInfo.getMarkMin());
			assertEquals(new Double(0.0), infoDegreeInfo.getMarkMax());
			assertEquals(new Double(12.0), infoDegreeInfo.getMarkAverage());
			assertEquals(new Integer(10), infoDegreeInfo.getInfoDegree().getIdInternal());

			System.out.println(
				"ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfull");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testSuccessfullWithMoreThanOneDegreeInfo() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(100);
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			//Returned anything?
			if (infoDegreeInfo == null) {
				fail("Reading a Degree Info.");
			}

			//Verify if read the correct data was read, the most recently
			assertEquals(new String("Obj2"), infoDegreeInfo.getObjectives());
			assertEquals(new String("His2"), infoDegreeInfo.getHistory());
			assertEquals(new String("Prof2"), infoDegreeInfo.getProfessionalExits());
			assertEquals(new String("addInfo2"), infoDegreeInfo.getAdditionalInfo());
			assertEquals(new String("Links2"), infoDegreeInfo.getLinks());
			assertEquals(new String("Test2"), infoDegreeInfo.getTestIngression());
			assertEquals(new Integer(60), infoDegreeInfo.getDriftsInitial());
			assertEquals(new Integer(0), infoDegreeInfo.getDriftsFirst());
			assertEquals(new Integer(0), infoDegreeInfo.getDriftsSecond());
			assertEquals(new String("Class2"), infoDegreeInfo.getClassifications());
			assertEquals(new Double(0.0), infoDegreeInfo.getMarkMin());
			assertEquals(new Double(0.0), infoDegreeInfo.getMarkMax());
			assertEquals(new Double(10.0), infoDegreeInfo.getMarkAverage());
			assertEquals(new Integer(100), infoDegreeInfo.getInfoDegree().getIdInternal());

			System.out.println(
				"ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfullWithMoreThanOneDegreeInfo");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testSuccessfullWithDegreeInfoLastYear() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(2002);
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode  };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			assertNull(infoDegreeInfo);

			System.out.println(
				"ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfullWithDegreeInfoLastYear");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNULLArg1() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(10);
			Integer infoExecutionPeriodCode = null;

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1, e.getMessage().lastIndexOf(".") + 21);
				if (!msg.equals(new String("impossibleDegreeSite"))) {
					e.printStackTrace();
					fail("Reading a degree information");
				}
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNULLArg1");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}
	
	public void testNULLArg2() {
		try {
			//Service Argument
			Integer infoDegreeCode = null;
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1, e.getMessage().lastIndexOf(".") + 21);
				if (!msg.equals(new String("impossibleDegreeSite"))) {
					e.printStackTrace();
					fail("Reading a degree information");
				}
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNULLArg2");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNoExecutionPeriod() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(10);
			Integer infoExecutionPeriodCode = new Integer(1000);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1, e.getMessage().lastIndexOf(".") + 21);
				if (!msg.equals(new String("impossibleDegreeSite"))) {
					e.printStackTrace();
					fail("Reading a degree information");
				}
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoExecutionPeriod");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNoExecutionYear() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(10);
			Integer infoExecutionPeriodCode = new Integer(5);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1, e.getMessage().lastIndexOf(".") + 21);
				if (!msg.equals(new String("impossibleDegreeSite"))) {
					e.printStackTrace();
					fail("Reading a degree information");
				}
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoExecutionYear");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}
	
	public void testNoDegree() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(99);
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1, e.getMessage().lastIndexOf(".") + 21);
				if (!msg.equals(new String("impossibleDegreeSite"))) {
					e.printStackTrace();
					fail("Reading a degree information");
				}
			}

			System.out.println("ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoDegree");
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}

	public void testNoDegreeInfo() {
		try {
			//Service Argument
			Integer infoDegreeCode = new Integer(1000);
			Integer infoExecutionPeriodCode = new Integer(3);

			Object[] args = { infoExecutionPeriodCode, infoDegreeCode  };

			//Service
			InfoDegreeInfo infoDegreeInfo = null;
			try {
				infoDegreeInfo = (InfoDegreeInfo) gestor.executar(null, getNameOfServiceToBeTested(), args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Reading a degree information" + e);
			}

			assertNull(infoDegreeInfo);

			System.out.println(
				"ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoDegreeInfo");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree information" + e);
		}
	}
}
