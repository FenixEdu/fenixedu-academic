/*
 * Created on 11/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class InsertTestTest extends ServiceNeedsAuthenticationTestCase {

    public InsertTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertTest";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "D2543", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34882);
        String title = new String("Title");
        String information = new String("Information");
        Object[] args = { executionCourseId, title, information };
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

            //			criteria.addEqualTo("keyClass", args[0]);
            //			criteria.addEqualTo("className",
            // ExecutionCourse.class.getName());
            //			Query queryCriteria = new QueryByCriteria(TestScope.class,
            // criteria);
            //			ITestScope scope = (ITestScope)
            // broker.getObjectByQuery(queryCriteria);

            criteria = new Criteria();
            QueryByCriteria queryCriteria = new QueryByCriteria(Test.class, criteria);
            queryCriteria.addOrderBy("idInternal", false);
            ITest test = (ITest) broker.getObjectByQuery(queryCriteria);
            broker.close();

            assertEquals(test.getTitle(), args[1]);
            assertEquals(test.getInformation(), args[2]);
            //assertEquals(test.getTestScope().getIdInternal(),
            // scope.getIdInternal());

        } catch (FenixServiceException ex) {
            fail("InsertTestTest " + ex);
        } catch (Exception ex) {
            fail("InsertTestTest " + ex);
        }
    }
}