/*
 * Created on 26/Ago/2003
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.tests.CorrectionAvailability;
import Util.tests.TestType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTestTest extends ServiceNeedsAuthenticationTestCase {

    public InsertDistributedTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditTestQuestionTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertDistributedTest";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34033);
        Integer testId = new Integer(273);
        String info = new String("informação da ficha");

        Calendar beginDate = Calendar.getInstance();
        beginDate.set(Calendar.DAY_OF_MONTH, 1);
        beginDate.set(Calendar.MONTH, 1);
        beginDate.set(Calendar.YEAR, 2004);
        Calendar beginHour = Calendar.getInstance();
        beginHour.set(Calendar.HOUR_OF_DAY, 10);
        beginHour.set(Calendar.MINUTE, 0);
        beginHour.set(Calendar.SECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.MONTH, 6);
        endDate.set(Calendar.YEAR, 2004);
        Calendar endHour = Calendar.getInstance();
        endHour.set(Calendar.HOUR_OF_DAY, 10);
        endHour.set(Calendar.MINUTE, 0);
        endHour.set(Calendar.SECOND, 0);

        TestType testType = new TestType(1);
        CorrectionAvailability correctionAvailability = new CorrectionAvailability(1);
        Boolean imsFeedback = new Boolean(true);
        String[] selected = { new String("2516") };
        Boolean shifts = new Boolean(false);
        String path = new String("e:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\");
        Object[] args = { executionCourseId, testId, info, beginDate, beginHour, endDate, endHour,
                testType, correctionAvailability, imsFeedback, selected, shifts, path };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            Criteria criteria = new Criteria();

            Query queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
            ((QueryByCriteria) queryCriteria).addOrderBy("idInternal", false);
            IDistributedTest distributedTest = (IDistributedTest) broker.getObjectByQuery(queryCriteria);
            broker.close();

            //ver se os dados do distributed_test estão correctos
            Calendar expectedDistributedTestBeginDate = (Calendar) args[3];
            Calendar expectedDistributedTestBeginHour = (Calendar) args[4];
            Calendar expectedDistributedTestEndDate = (Calendar) args[5];
            Calendar expectedDistributedTestEndHour = (Calendar) args[6];

            //assertEquals(distributedTest.getTestScope().getDomainObject().getIdInternal(),
            // args[0]);

            assertEquals(distributedTest.getBeginDate().get(Calendar.DAY_OF_MONTH),
                    expectedDistributedTestBeginDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(distributedTest.getBeginDate().get(Calendar.MONTH),
                    expectedDistributedTestBeginDate.get(Calendar.MONTH));
            assertEquals(distributedTest.getBeginDate().get(Calendar.YEAR),
                    expectedDistributedTestBeginDate.get(Calendar.YEAR));

            assertEquals(distributedTest.getBeginHour().get(Calendar.HOUR_OF_DAY),
                    expectedDistributedTestBeginHour.get(Calendar.HOUR_OF_DAY));
            assertEquals(distributedTest.getBeginHour().get(Calendar.MINUTE),
                    expectedDistributedTestBeginHour.get(Calendar.MINUTE));
            assertEquals(distributedTest.getBeginHour().get(Calendar.SECOND),
                    expectedDistributedTestBeginHour.get(Calendar.SECOND));

            assertEquals(distributedTest.getEndDate().get(Calendar.DAY_OF_MONTH),
                    expectedDistributedTestEndDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(distributedTest.getEndDate().get(Calendar.MONTH),
                    expectedDistributedTestEndDate.get(Calendar.MONTH));
            assertEquals(distributedTest.getEndDate().get(Calendar.YEAR), expectedDistributedTestEndDate
                    .get(Calendar.YEAR));

            assertEquals(distributedTest.getEndHour().get(Calendar.HOUR_OF_DAY),
                    expectedDistributedTestEndHour.get(Calendar.HOUR_OF_DAY));
            assertEquals(distributedTest.getEndHour().get(Calendar.MINUTE),
                    expectedDistributedTestEndHour.get(Calendar.MINUTE));
            assertEquals(distributedTest.getEndHour().get(Calendar.SECOND),
                    expectedDistributedTestEndHour.get(Calendar.SECOND));

            assertEquals(distributedTest.getTestType(), args[7]);
            assertEquals(distributedTest.getCorrectionAvailability(), args[8]);
            //  assertEquals(distributedTest.getStudentFeedback(), args[9]);

            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            assertEquals(studentTestQuestionList.size(), 2);
            //Iterator it = studentTestQuestionList.iterator();

            //			if (distributedTest.getTestType().getType().intValue() ==
            // (TestType.EVALUATION))
            //			{
            //				broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            //				criteria = new Criteria();
            //				criteria.addEqualTo("keyDistributedTest",
            // distributedTest.getIdInternal());
            //				queryCriteria = new QueryByCriteria(OnlineTest.class, criteria);
            //				IOnlineTest onlineTest = (IOnlineTest)
            // broker.getObjectByQuery(queryCriteria);
            //				
            //				assertNotNull("onlineTest null", onlineTest);
            //
            //				criteria = new Criteria();
            //				criteria.addEqualTo("keyEvaluation", onlineTest.getIdInternal());
            //				queryCriteria = new QueryByCriteria(Mark.class, criteria);
            //				List marksList = (List)
            // broker.getCollectionByQuery(queryCriteria);
            //				broker.close();
            //				
            //				assertEquals(marksList.size(), 1);
            //
            //			}

        } catch (FenixServiceException ex) {
            fail("Insert Distributed Test " + ex);
        } catch (Exception ex) {
            fail("Insert Distributed Test " + ex);
        }
    }

}