/*
 * Created on 26/Ago/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.SiteView;
import DataBeans.util.CopyUtils;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTestsTest extends ServiceNeedsAuthenticationTestCase {

    public ReadDistributedTestsTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadDistributedTestsDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDistributedTests";
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
        //Object[] args = { new Integer(36349)};
        Object[] args = { new Integer(34882) };

        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {
        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();

            SiteView siteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            Criteria criteria = new Criteria();

            criteria = new Criteria();
            //criteria.addEqualTo("keyTestScope", new Integer(2));
            criteria.addEqualTo("keyTestScope", new Integer(1));
            Query queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
            List distributedTestList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            //assertEquals(distributedTestList.size(), 1);
            assertEquals(distributedTestList.size(), 19);

            InfoSiteDistributedTests infoSiteDistributedTests = (InfoSiteDistributedTests) siteView
                    .getComponent();

            List serviceInfoDistributedTestList = infoSiteDistributedTests.getInfoDistributedTests();
            InfoExecutionCourse infoExecutionCourse = infoSiteDistributedTests.getExecutionCourse();
            assertEquals(infoExecutionCourse.getIdInternal(), args[0]);
            int i = 0;
            Iterator it = serviceInfoDistributedTestList.iterator();
            while (it.hasNext()) {
                InfoDistributedTest infoServiceDistributedTest = (InfoDistributedTest) it.next();
                InfoDistributedTest infoDistributedTest = copyIDistributedTest2InfoDistributedTest((IDistributedTest) distributedTestList
                        .get(i));
                assertEquals(infoServiceDistributedTest, infoDistributedTest);
                i++;
            }

        } catch (FenixServiceException ex) {
            fail("ReadDistributedTestsTest " + ex);
        } catch (Exception ex) {
            fail("ReadDistributedTestsTest " + ex);
        }
    }

    private static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(
            IDistributedTest distributedTest) {
        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        try {
            CopyUtils.copyProperties(infoDistributedTest, distributedTest);
        } catch (Exception e) {
            fail("ReadDistributedTestsTest " + "cloner");
        }
        return infoDistributedTest;
    }
}