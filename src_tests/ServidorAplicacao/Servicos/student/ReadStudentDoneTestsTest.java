/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.util.CopyUtils;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDoneTestsTest extends TestCaseReadServices {

    public ReadStudentDoneTestsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentDoneTestsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentDoneTests";
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
            Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List distributedTestList = new ArrayList();

            sp.iniciarTransaccao();

            IStudent student = sp.getIPersistentStudent().readByUsername((String) args[0]);
            assertNotNull("student is null", student);

            distributedTestList = sp.getIPersistentDistributedTest().readByStudent(student);

            sp.confirmarTransaccao();

            Iterator it = distributedTestList.iterator();
            List infoDistributedTestList = new ArrayList();
            while (it.hasNext()) {
                IDistributedTest distributedTest = (IDistributedTest) it.next();
                if ((!distributedTest.getIdInternal().equals(new Integer(2)))
                        && (!distributedTest.getIdInternal().equals(new Integer(125))))
                    infoDistributedTestList
                            .add(copyIDistributedTest2InfoDistributedTest(distributedTest));
            }
            infoSiteDistributedTests.setInfoDistributedTests(infoDistributedTestList);
        } catch (Exception ex) {
            fail("ReadStudentDoneTestsTest " + ex);
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
            fail("ReadStudentDoneTestsTest " + "cloner");
        }
        return infoDistributedTest;
    }

}

// extends ServiceNeedsStudentAuthenticationTestCase
//{
//
//	public ReadStudentDoneTestsTest(String testName)
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
//		return "ReadStudentDoneTests";
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
//
//			criteria = new Criteria();
//			criteria.addNotEqualTo("idInternal", new Integer(2));
//			criteria.addNotEqualTo("idInternal", new Integer(125));
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
//			fail("ReadStudentDoneTestsTest " + ex);
//		}
//		catch (Exception ex)
//		{
//			fail("ReadStudentDoneTestsTest " + ex);
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
//			fail("ReadStudentDoneTestsTest " + "cloner");
//		}
//		return infoDistributedTest;
//	}
//}
