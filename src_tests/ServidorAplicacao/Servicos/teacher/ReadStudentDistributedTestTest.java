/*
 * Created on 8/Set/2003
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
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTestTest extends ServiceNeedsAuthenticationTestCase {

    public ReadStudentDistributedTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadStudentDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentDistributedTest";
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
        Integer studentId = new Integer(3701);
        String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
        Object[] args = { executionCourseId, distributedTestId, studentId, path };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {
        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();

            List serviceStudentTestQuestionList = (List) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), args);

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            Criteria criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            criteria.addEqualTo("keyStudent", args[2]);
            QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            queryCriteria.addOrderBy("testQuestionOrder", true);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            assertEquals(studentTestQuestionList.size(), serviceStudentTestQuestionList.size());
            int i = 0;
            Iterator it = serviceStudentTestQuestionList.iterator();
            while (it.hasNext()) {
                InfoStudentTestQuestion infoServiceStudentTestQuestion = (InfoStudentTestQuestion) it
                        .next();
                InfoStudentTestQuestion infoStudentTestQuestion = copyIStudentTestQuestion2InfoStudentTestQuestion((IStudentTestQuestion) studentTestQuestionList
                        .get(i));
                assertEquals(infoServiceStudentTestQuestion, infoStudentTestQuestion);
                i++;
            }

        } catch (FenixServiceException ex) {
            fail("ReadStudentDistributedTestTest " + ex);
        } catch (Exception ex) {
            fail("ReadStudentDistributedTestTest " + ex);
        }
    }

    public static InfoStudentTestQuestion copyIStudentTestQuestion2InfoStudentTestQuestion(
            IStudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
        infoStudentTestQuestion.setIdInternal(studentTestQuestion.getIdInternal());
        infoStudentTestQuestion.setOptionShuffle(studentTestQuestion.getOptionShuffle());
        infoStudentTestQuestion.setOldResponse(studentTestQuestion.getOldResponse());
        infoStudentTestQuestion.setTestQuestionOrder(studentTestQuestion.getTestQuestionOrder());
        infoStudentTestQuestion.setTestQuestionValue(studentTestQuestion.getTestQuestionValue());
        infoStudentTestQuestion.setTestQuestionMark(studentTestQuestion.getTestQuestionMark());
        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        try {
            CopyUtils.copyProperties(infoDistributedTest, studentTestQuestion.getDistributedTest());
        } catch (Exception e) {
            fail("ReadStudentDistributedTestTest " + "cloner");
        }

        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(studentTestQuestion.getStudent());
        InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(studentTestQuestion.getQuestion());
        infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
        infoStudentTestQuestion.setStudent(infoStudent);
        infoStudentTestQuestion.setQuestion(infoQuestion);
        return infoStudentTestQuestion;
    }
}