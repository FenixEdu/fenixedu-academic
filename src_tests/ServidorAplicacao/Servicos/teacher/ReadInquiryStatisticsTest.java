/*
 * Created on Oct 15, 2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoInquiryStatistics;
import DataBeans.InfoSiteInquiryStatistics;
import DataBeans.SiteView;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadInquiryStatisticsTest extends ServiceNeedsAuthenticationTestCase {

    public ReadInquiryStatisticsTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadInquiryStatistics";
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
        String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
        Object[] args = { executionCourseId, distributedTestId, path };

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
            Query queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);

            InfoSiteInquiryStatistics infoSiteInquiryStatistics = (InfoSiteInquiryStatistics) siteView
                    .getComponent();
            List infoInquiryStatisticsList = infoSiteInquiryStatistics.getInfoInquiryStatistics();
            Iterator it = infoInquiryStatisticsList.iterator();
            while (it.hasNext()) {
                InfoInquiryStatistics infoInquiryStatistics = (InfoInquiryStatistics) it.next();
                assertEquals(infoInquiryStatistics.getOptionStatistics().size() - 1,
                        infoInquiryStatistics.getInfoStudentTestQuestion().getQuestion()
                                .getOptionNumber().intValue());
                Iterator statisticsIt = infoInquiryStatistics.getOptionStatistics().iterator();
                int index = 1;
                while (statisticsIt.hasNext()) {
                    LabelValueBean serviceLabelValueBean = (LabelValueBean) statisticsIt.next();
                    criteria = new Criteria();
                    criteria.addEqualTo("keyDistributedTest", args[1]);
                    criteria.addEqualTo("testQuestionOrder", infoInquiryStatistics
                            .getInfoStudentTestQuestion().getTestQuestionOrder());
                    criteria.addNotEqualTo("response", new Integer(0));
                    queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
                    int numOfStudentResponses = broker.getCount(queryCriteria);
                    LabelValueBean l = null;
                    if (index <= infoInquiryStatistics.getInfoStudentTestQuestion().getQuestion()
                            .getOptionNumber().intValue()) {
                        criteria = new Criteria();
                        criteria.addEqualTo("keyDistributedTest", args[1]);
                        criteria.addEqualTo("testQuestionOrder", infoInquiryStatistics
                                .getInfoStudentTestQuestion().getTestQuestionOrder());
                        criteria.addEqualTo("response", new Integer(index));
                        queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
                        int statistic = broker.getCount(queryCriteria);
                        DecimalFormat df = new DecimalFormat("#%");
                        String percentage = new String("0%");
                        if (statistic != 0)
                            percentage = df.format(statistic
                                    * java.lang.Math.pow(numOfStudentResponses, -1));
                        l = new LabelValueBean(new Integer(index).toString(), percentage);
                        index++;
                    } else {
                        l = new LabelValueBean(new String("Número de alunos que responderam"),
                                new Integer(numOfStudentResponses).toString());
                    }
                    assertEquals(l.getLabel(), serviceLabelValueBean.getLabel());
                    assertEquals(l.getValue(), serviceLabelValueBean.getValue());
                }
            }

            broker.close();

        } catch (FenixServiceException ex) {
            fail("ReadInquiryStatisticsTest " + ex);
        } catch (Exception ex) {
            fail("ReadInquiryStatisticsTest " + ex);
        }
    }
}