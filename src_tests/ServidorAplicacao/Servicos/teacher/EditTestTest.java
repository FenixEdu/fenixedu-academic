/*
 * Created on 11/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
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
public class EditTestTest extends ServiceNeedsAuthenticationTestCase {
    public EditTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditTestTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "EditTest";
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
        //Integer executionCourseId, Integer testId, String title, String
        // information
        Integer executionCourseId = new Integer(34033);
        Integer testId = new Integer(110);
        String title = new String("new test title");
        String info = new String("new test info");
        Object[] args = { executionCourseId, testId, title, info, };
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
            Query queryCriteria = new QueryByCriteria(Test.class, criteria);
            ITest test = (ITest) broker.getObjectByQuery(queryCriteria);
            broker.close();
            //ver se os dados do teste estão correctos
            assertEquals(test.getTitle(), args[2]);
            assertEquals(test.getInformation(), args[3]);
        } catch (FenixServiceException ex) {
            fail("Edit Test " + ex);
        } catch (Exception ex) {
            fail("Edit Test " + ex);
        }
    }
}
/*
 * public class EditTestTest extends TestCaseDeleteAndEditServices {
 * 
 * public EditTestTest(String testName) { super(testName); }
 * 
 * protected void setUp() { super.setUp(); }
 * 
 * protected void tearDown() { super.tearDown(); }
 * 
 * protected String getNameOfServiceToBeTested() { return "EditTest"; }
 * 
 * protected boolean needsAuthorization() { return true; } protected Object[]
 * getArgumentsOfServiceToBeTestedSuccessfuly() { Object[] args = {new
 * Integer(3), new String("new title"), new String("new information")}; return
 * args; }
 * 
 * protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() { return
 * null; } }
 */