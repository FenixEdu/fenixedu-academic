/*
 * Created on 25/Set/2003
 *
 */

package ServidorAplicacao.Servicos.teacher;

import java.util.List;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.OnlineTest;
import Dominio.StudentTestLog;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class DeleteDistributedTestTest extends ServiceNeedsAuthenticationTestCase {
    public DeleteDistributedTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testDeleteDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteDistributedTest";
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
        Integer distributedTestId = new Integer(190);
        Object[] args = { executionCourseId, distributedTestId };
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
            criteria.addEqualTo("idInternal", args[1]);
            Query queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
            IDistributedTest distributedTest = (IDistributedTest) broker.getObjectByQuery(queryCriteria);
            broker.close();
            if (distributedTest != null)
                fail("DeleteDistributedTestTest " + "Não apagou o teste distribuído!");
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            if (studentTestQuestionList != null && studentTestQuestionList.size() != 0)
                fail("DeleteDistributedTestTest " + "Não apagou as perguntas dos alunos!");
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            queryCriteria = new QueryByCriteria(StudentTestLog.class, criteria);
            List studentTestLogList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            if (studentTestLogList != null && studentTestLogList.size() != 0)
                fail("DeleteDistributedTestTest " + "Não apagou os logs!");
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            queryCriteria = new QueryByCriteria(OnlineTest.class, criteria);
            List onlineTests = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            if (onlineTests != null && onlineTests.size() != 0)
                fail("DeleteDistributedTestTest " + "Não apagou os OnlineTests!");
        } catch (FenixServiceException ex) {
            fail("DeleteDistributedTestTest " + ex);
        } catch (Exception ex) {
            fail("DeleteDistributedTestTest " + ex);
        }
    }
}