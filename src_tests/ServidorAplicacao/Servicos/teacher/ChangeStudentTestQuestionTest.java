/*
 * Created on Oct 29, 2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestionTest extends ServiceNeedsAuthenticationTestCase {

    public ChangeStudentTestQuestionTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testChangeStudentTestQuestionTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ChangeStudentTestQuestion";
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
        Integer distributedTestId = new Integer(254);
        Integer oldQuestionId = new Integer(10190);
        Integer newMetadataId = new Integer(661);
        Integer studentId = new Integer(2516);
        TestQuestionChangesType testQuestionChangesType = new TestQuestionChangesType(
                TestQuestionChangesType.CHANGE_VARIATION);
        Boolean delete = new Boolean(false);
        TestQuestionStudentsChangesType testQuestionStudentsChangesType = new TestQuestionStudentsChangesType(
                TestQuestionStudentsChangesType.THIS_STUDENT);
        String path = new String("e:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, distributedTestId, oldQuestionId, newMetadataId, studentId,
                testQuestionChangesType, delete, testQuestionStudentsChangesType, path };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = null;
            Query queryCriteria = null;
            IMetadata metadata = null;
            if (args[3] == null) {
                criteria = new Criteria();
                criteria.addEqualTo("idInternal", args[2]);
                queryCriteria = new QueryByCriteria(Question.class, criteria);
                metadata = ((IQuestion) broker.getObjectByQuery(queryCriteria)).getMetadata();
            } else {
                criteria = new Criteria();
                criteria.addEqualTo("idInternal", args[3]);
                queryCriteria = new QueryByCriteria(Metadata.class, criteria);
                metadata = (IMetadata) broker.getObjectByQuery(queryCriteria);
            }
            System.out.println("ola");
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println("ola1");
            criteria = new Criteria();
            criteria.addEqualTo("keyDistributedTest", args[1]);
            criteria.addEqualTo("keyStudent", args[4]);
            queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
            List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();
            System.out.println("ola2");
            assertEquals(studentTestQuestionList.size(), 2);
            Iterator it = studentTestQuestionList.iterator();
            boolean exist = false;
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getQuestion().getMetadata().getIdInternal().equals(
                        metadata.getIdInternal()))
                    exist = true;
            }
            if (exist == false)
                fail("ChangeStudentTestQuestionTest " + "Não tem pergunta nova");
            if (((Boolean) args[6]).booleanValue() == true) {
                broker = PersistenceBrokerFactory.defaultPersistenceBroker();
                criteria = new Criteria();
                criteria.addEqualTo("idInternal", args[2]);
                queryCriteria = new QueryByCriteria(Question.class, criteria);
                IQuestion question = (IQuestion) broker.getObjectByQuery(queryCriteria);
                broker.close();
                if (question != null)
                    if (question.getVisibility().booleanValue() != false)
                        fail("ChangeStudentTestQuestionTest " + "Não apagou a pergunta antiga");
            }

        } catch (FenixServiceException ex) {
            fail("ChangeStudentTestQuestionTest " + ex);
        } catch (Exception ex) {
            fail("ChangeStudentTestQuestionTest " + ex);
        }
    }
}