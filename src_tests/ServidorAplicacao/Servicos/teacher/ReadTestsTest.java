/*
 * Created on 12/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTests;
import net.sourceforge.fenixedu.dataTransferObject.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
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
public class ReadTestsTest extends ServiceNeedsAuthenticationTestCase {

    public ReadTestsTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadTestsDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadTests";
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
        //		Integer executionCourseId = new Integer(34882);
        Integer executionCourseId = new Integer(36349);
        Object[] args = { executionCourseId };
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
            //			criteria.addEqualTo("keyTestScope", new Integer(1));
            criteria.addEqualTo("keyTestScope", new Integer(2));
            Query queryCriteria = new QueryByCriteria(Test.class, criteria);
            List testList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            InfoSiteTests infoSiteTests = (InfoSiteTests) siteView.getComponent();
            List infoServiceTestsList = infoSiteTests.getInfoTests();
            //InfoExecutionCourse executionCourse =
            // infoSiteTests.getExecutionCourse();

            assertEquals(infoServiceTestsList.size(), testList.size());
            int i = 0;
            Iterator it = infoServiceTestsList.iterator();
            while (it.hasNext()) {
                InfoTest infoServiceTest = (InfoTest) it.next();

                InfoTest infoTest = copyITest2InfoTest((ITest) testList.get(i));
                assertEquals(infoServiceTest, infoTest);
                i++;
            }

        } catch (FenixServiceException ex) {
            fail("ReadTestsTest " + ex);
        } catch (Exception ex) {
            fail("ReadTestsTest " + ex);
        }
    }

    public static InfoTest copyITest2InfoTest(ITest test) {
        InfoTest infoTest = new InfoTest();
        try {
            CopyUtils.copyProperties(infoTest, test);
        } catch (Exception e) {
            fail("ReadDistributedTestsTest " + "cloner");
        }
        return infoTest;
    }
}