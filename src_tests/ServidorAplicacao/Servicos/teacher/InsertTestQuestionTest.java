/*
 * Created on 11/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestionTest extends ServiceNeedsAuthenticationTestCase {

    public InsertTestQuestionTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditTestQuestionTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertTestQuestion";
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
        /*
         * public boolean run(Integer executionCourseId, Integer testId,
         * String[] metadataId, Integer questionOrder, Double questionValue,
         * CorrectionFormula formula, String path)
         */
        Integer executionCourseId = new Integer(34033);
        Integer testId = new Integer(273);
        String[] metadataId = { new String("707") };
        Integer questionOrder = new Integer(1);
        Double questionValue = new Double(2.5);
        CorrectionFormula formula = new CorrectionFormula(CorrectionFormula.FENIX);
        String path = new String("e:\\eclipse-m7\\workspace\\fenix\\build\\standalone\\");
        Object[] args = { executionCourseId, testId, metadataId, questionOrder, questionValue, formula,
                path };
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
            criteria.addEqualTo("keyTest", args[1]);
            Query queryCriteria = new QueryByCriteria(TestQuestion.class, criteria);
            List testQuestionList = (List) broker.getCollectionByQuery(queryCriteria);

            criteria = new Criteria();
            criteria.addEqualTo("idInternal", args[1]);
            queryCriteria = new QueryByCriteria(Test.class, criteria);
            //ITest test = (ITest) broker.getObjectByQuery(queryCriteria);

            broker.close();

            List metadatasIds = new ArrayList();
            CollectionUtils.addAll(metadatasIds, (String[]) args[2]);

            assertEquals(testQuestionList.size(), metadatasIds.size() + 2);
            Iterator it = testQuestionList.iterator();

            while (it.hasNext()) {
                ITestQuestion testQuestion = (ITestQuestion) it.next();
                if (metadatasIds.contains(testQuestion.getQuestion().getKeyMetadata().toString())) {
                    if (args[3] != null && !args[3].equals(new Integer(-1))) {
                        int order = ((Integer) args[3]).intValue() + 1;
                        assertEquals(testQuestion.getTestQuestionOrder(), new Integer(order));
                    }
                    if (args[4] != null)
                        assertEquals(testQuestion.getTestQuestionValue(), args[4]);
                    assertEquals(testQuestion.getCorrectionFormula(), args[5]);
                }
            }
        } catch (FenixServiceException ex) {
            fail("Insert Test Question " + ex);
        } catch (Exception ex) {
            fail("Insert Test Question " + ex);
        }
    }
}