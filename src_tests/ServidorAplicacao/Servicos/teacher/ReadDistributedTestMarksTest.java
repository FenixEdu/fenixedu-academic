/*
 * Created on 18/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestionMark;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
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
public class ReadDistributedTestMarksTest extends ServiceNeedsAuthenticationTestCase {
    public ReadDistributedTestMarksTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDistributedTestMarks";
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
            InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = (InfoSiteStudentsTestMarks) siteView
                    .getComponent();
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addEqualTo("idInternal", args[0]);
            QueryByCriteria queryCriteria = new QueryByCriteria(ExecutionCourse.class, criteria);
            IExecutionCourse executionCourse = (IExecutionCourse) broker.getObjectByQuery(queryCriteria);

            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            queryCriteria.addOrderBy("keyStudent", true);
            queryCriteria.addOrderBy("testQuestionOrder", true);

            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            assertEquals(studentTestQuestionList.size(), infoSiteStudentsTestMarks
                    .getInfoStudentTestQuestionList().size());
            assertEquals(Cloner.get(executionCourse), infoSiteStudentsTestMarks.getExecutionCourse());
            assertEquals(
                    copyIDistributedTest2InfoDistributedTest(((IStudentTestQuestion) studentTestQuestionList
                            .get(0)).getDistributedTest()), infoSiteStudentsTestMarks
                            .getInfoDistributedTest());
            Iterator it = infoSiteStudentsTestMarks.getInfoStudentTestQuestionList().iterator();
            int i = 0;
            while (it.hasNext()) {
                InfoStudentTestQuestionMark infoStudentTestQuestionMark = (InfoStudentTestQuestionMark) it
                        .next();
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentTestQuestionList
                        .get(i);
                assertEquals(infoStudentTestQuestionMark, InfoStudentTestQuestionMark
                        .newInfoFromDomain(studentTestQuestion));
                i++;
            }

        } catch (FenixServiceException ex) {
            fail("ReadDistributedTestMarksTest " + ex);
        } catch (Exception ex) {
            fail("ReadDistributedTestMarksTest " + ex);
        }
    }

    private static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(
            IDistributedTest distributedTest) {
        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        try {
            CopyUtils.copyProperties(infoDistributedTest, distributedTest);
        } catch (Exception e) {
            fail("ReadStudentTestsToDoTest " + "cloner");
        }
        return infoDistributedTest;
    }
}