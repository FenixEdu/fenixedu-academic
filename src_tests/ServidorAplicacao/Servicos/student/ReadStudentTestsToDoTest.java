/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.util.CopyUtils;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestsToDoTest extends TestCaseReadServices {

    public ReadStudentTestsToDoTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentTestsToDoTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentTestsToDo";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        String userName = new String("L48283");
        Integer executionCourseId = new Integer(34882);
        Object[] args = { userName, executionCourseId };
        return args;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        InfoSiteDistributedTests infoSiteDistributedTests = new InfoSiteDistributedTests();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IDistributedTest distributedTest = (IDistributedTest) sp.getIPersistentDistributedTest()
                    .readByOID(DistributedTest.class, new Integer(2));
            sp.confirmarTransaccao();
            if (distributedTest == null)
                fail("ReadStudentTestsToDoTest " + "no distributedTest");

            List distributedTestList = new ArrayList();
            distributedTestList.add(copyIDistributedTest2InfoDistributedTest(distributedTest));
            infoSiteDistributedTests.setInfoDistributedTests(distributedTestList);
        } catch (Exception ex) {
            fail("ReadStudentTestsToDoTest " + ex);
        }
        return infoSiteDistributedTests;
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected String[] getArgsForAuthorizedUser() {
        String argsAutenticacao3[] = { "l48283", "pass", getApplication() };
        return argsAutenticacao3;
    }

    protected String[] getArgsForNotAuthorizedUser() {
        String argsAutenticacao4[] = { "d2543", "pass", getApplication() };
        return argsAutenticacao4;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
    }

    public String getApplication() {
        return Autenticacao.EXTRANET;
    }

    private static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(
            IDistributedTest distributedTest) {
        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        try {
            CopyUtils.copyProperties(infoDistributedTest, distributedTest);
        } catch (Exception e) {
            fail("ReadStudentTestsToDoTest " + "cloner");
        }
        return infoDistributedTest;
    }

}

//extends ServiceNeedsStudentAuthenticationTestCase
//{
//
//	public ReadStudentTestsToDoTest(String testName)
//	{
//		super(testName);
//	}
//
//	protected String getDataSetFilePath()
//	{
//		return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
//	}
//
//	protected String getNameOfServiceToBeTested()
//	{
//		return "ReadStudentTestsToDo";
//	}
//
//	protected String[] getAuthenticatedAndAuthorizedUser()
//	{
//
//		String[] args = { "L48283", "pass", getApplication()};
//		return args;
//	}
//
//	protected String[] getAuthenticatedAndUnauthorizedUser()
//	{
//
//		String[] args = { "D2543", "pass", getApplication()};
//		return args;
//	}
//
//	protected String[] getNotAuthenticatedUser()
//	{
//
//		String[] args = { "D2543", "pass", getApplication()};
//		return args;
//	}
//
//	protected Object[] getAuthorizeArguments()
//	{
//		String userName = new String("L48283");
//		Integer executionCourseId = new Integer(34882);
//		Object[] args = { userName, executionCourseId };
//		return args;
//	}
//
//	protected String getApplication()
//	{
//		return Autenticacao.EXTRANET;
//	}
//
//	public void testSuccessfull()
//	{
//
//		try
//		{
//			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
//			Object[] args = getAuthorizeArguments();
//
//			InfoSiteDistributedTests infoSiteDistributedTests =
//				(InfoSiteDistributedTests) ServiceManagerServiceFactory.executeService(
//					userView,
//					getNameOfServiceToBeTested(),
//					args);
//
//			PersistenceBroker broker =
// PersistenceBrokerFactory.defaultPersistenceBroker();
//
//			Criteria criteria = new Criteria();
//			criteria = new Criteria();
//			criteria.addEqualTo("idInternal", new Integer(2));
//			Query queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
//			List distributedTestList = (List) broker.getCollectionByQuery(queryCriteria);
//			broker.close();
//
//			List infoDistributedTestList =
// infoSiteDistributedTests.getInfoDistributedTests();
//			assertEquals(distributedTestList.size(), infoDistributedTestList.size());
//
//			//InfoExecutionCourse infoExecutionCourse =
// infoSiteDistributedTests.getExecutionCourse();
//			//assertEquals(infoExecutionCourse.getIdInternal(), args[1]);
//			int i = 0;
//			Iterator it = infoDistributedTestList.iterator();
//			while (it.hasNext())
//			{
//				InfoDistributedTest infoServiceDistributedTest = (InfoDistributedTest)
// it.next();
//				InfoDistributedTest infoDistributedTest =
//					copyIDistributedTest2InfoDistributedTest(
//						(IDistributedTest) distributedTestList.get(i));
//				assertEquals(infoServiceDistributedTest, infoDistributedTest);
//				i++;
//			}
//
//		}
//		catch (FenixServiceException ex)
//		{
//			fail("ReadStudentTestsToDoTest " + ex);
//		}
//		catch (Exception ex)
//		{
//			fail("ReadStudentTestsToDoTest " + ex);
//		}
//	}
//
//	private static InfoDistributedTest
// copyIDistributedTest2InfoDistributedTest(IDistributedTest distributedTest)
//	{
//		InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
//		try
//		{
//			CopyUtils.copyProperties(infoDistributedTest, distributedTest);
//		}
//		catch (Exception e)
//		{
//			fail("ReadStudentTestsToDoTest " + "cloner");
//		}
//		return infoDistributedTest;
//	}
//
//	protected boolean needsAuthorization()
//	{
//		return true;
//	}
//}
