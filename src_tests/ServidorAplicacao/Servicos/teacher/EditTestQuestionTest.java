/*
 * Created on 11/Ago/2003
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

import Dominio.ITestQuestion;
import Dominio.TestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.tests.CorrectionFormula;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestionTest extends ServiceNeedsAuthenticationTestCase {

    public EditTestQuestionTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditTestQuestionTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "EditTestQuestion";
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
        Integer testQuestionId = new Integer(313);
        Integer questionOrder = new Integer(1);
        Double questionValue = new Double(2.3);
        CorrectionFormula formula = new CorrectionFormula(CorrectionFormula.FENIX);
        Object[] args = { executionCourseId, testId, testQuestionId, questionOrder, questionValue,
                formula };
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

            //ver se esta pergunta esta no teste
            //posicao/cotação certa
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("keyTest", args[1]);
            Query queryCriteria = new QueryByCriteria(TestQuestion.class, criteria);
            List testQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            Iterator it = testQuestionList.iterator();
            while (it.hasNext()) {
                ITestQuestion testQuestion = (ITestQuestion) it.next();
                if (testQuestion.getIdInternal().equals(args[2])) {
                    int order = ((Integer) args[3]).intValue();
                    if (order != -2 && order != -1)
                        assertEquals(testQuestion.getTestQuestionOrder(), new Integer(order + 1));
                    assertEquals(testQuestion.getTestQuestionValue(), args[4]);
                    assertEquals(testQuestion.getCorrectionFormula(), args[5]);
                }
            }
        } catch (FenixServiceException ex) {
            fail("Edit Test Question" + ex);
        } catch (Exception ex) {
            fail("Edit Test Question" + ex);
        }
    }
}