/*
 * Created on 18/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsTestMarksStatistics;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarksStatisticsTest extends ServiceNeedsAuthenticationTestCase {
    public ReadDistributedTestMarksStatisticsTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDistributedTestMarksStatistics";
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
        Integer distributedTestId = new Integer(1);
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

            SiteView siteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            InfoSiteStudentsTestMarksStatistics infoSiteStudentsTestMarksStatistics = (InfoSiteStudentsTestMarksStatistics) siteView
                    .getComponent();

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addEqualTo("idInternal", args[0]);
            Query queryCriteria = new QueryByCriteria(ExecutionCourse.class, criteria);
            //IExecutionCourse executionCourse = (IExecutionCourse)
            // broker.getObjectByQuery(queryCriteria);

            criteria = new Criteria();
            criteria.addEqualTo("idInternal", args[1]);
            queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
            IDistributedTest distributedTest = (IDistributedTest) broker.getObjectByQuery(queryCriteria);

            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            int numOfStudent = broker.getCount(queryCriteria)
                    / distributedTest.getNumberOfQuestions().intValue();

            int numberOfCorrectAswers = 0;
            int numberOfWrongAswers = 0;
            int numberNotAswered = 0;
            List correctAnswersPercentageList = new ArrayList(), wrongAnswersPercentageList = new ArrayList(), notAnsweredPercentageList = new ArrayList();
            DecimalFormat df = new DecimalFormat("#%");
            for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++) {
                criteria = new Criteria();
                criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
                criteria.addEqualTo("testQuestionOrder", new Integer(i));
                criteria.addGreaterThan("testQuestionMark", new Integer(0));
                queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
                numberOfCorrectAswers = broker.getCount(queryCriteria);
                correctAnswersPercentageList.add(df.format(numberOfCorrectAswers
                        * java.lang.Math.pow(numOfStudent, -1)));

                criteria = new Criteria();
                criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
                criteria.addEqualTo("testQuestionOrder", new Integer(i));
                criteria.addLessThan("testQuestionMark", new Integer(0));
                queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
                numberOfWrongAswers = broker.getCount(queryCriteria);
                wrongAnswersPercentageList.add(df.format(numberOfWrongAswers
                        * java.lang.Math.pow(numOfStudent, -1)));

                criteria = new Criteria();
                criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
                criteria.addEqualTo("testQuestionOrder", new Integer(i));
                criteria.addEqualTo("testQuestionMark", new Integer(0));
                queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
                numberNotAswered = broker.getCount(queryCriteria);
                notAnsweredPercentageList.add(df.format(numberNotAswered
                        * java.lang.Math.pow(numOfStudent, -1)));

            }
            broker.close();

            assertEquals(correctAnswersPercentageList, infoSiteStudentsTestMarksStatistics
                    .getCorrectAnswersPercentage());
            assertEquals(wrongAnswersPercentageList, infoSiteStudentsTestMarksStatistics
                    .getWrongAnswersPercentage());
            assertEquals(notAnsweredPercentageList, infoSiteStudentsTestMarksStatistics
                    .getNotAnsweredPercentage());
        } catch (FenixServiceException ex) {
            fail("ReadDistributedTestMarksStatisticsTest " + ex);
        } catch (Exception ex) {
            fail("ReadDistributedTestMarksStatisticsTest " + ex);
        }
    }
}