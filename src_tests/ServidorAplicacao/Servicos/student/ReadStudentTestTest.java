/*
 * Created on 8/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestTest extends TestCaseReadServices {

    public ReadStudentTestTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentTestTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        String userName = new String("L48283");
        Integer distributedTestId = new Integer(2);
        String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");

        Object[] args = { userName, distributedTestId, new Boolean(true), path };
        return args;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 6;
    }

    protected Object getObjectToCompare() {
        Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
        List infoStudentTestQuestionList = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IStudent student = sp.getIPersistentStudent().readByUsername((String) args[0]);
            assertNotNull("student is null", student);

            IDistributedTest distributedTest = (IDistributedTest) sp.getIPersistentDistributedTest()
                    .readByOID(DistributedTest.class, (Integer) args[1]);

            assertNotNull("DistributedTest is null", distributedTest);
            List studentTestQuestionList = sp.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student, distributedTest);
            sp.confirmarTransaccao();

            Iterator it = studentTestQuestionList.iterator();

            while (it.hasNext()) {
                infoStudentTestQuestionList
                        .add(copyIStudentTestQuestion2InfoStudentTestQuestion((IStudentTestQuestion) it
                                .next()));
            }

        } catch (Exception ex) {
            fail("ReadStudentTestTest " + ex);
        }
        return infoStudentTestQuestionList;
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected String[] getArgsForAuthorizedUser() {
        String argsAutenticacao3[] = { "l48283", "pass", getApplication() };
        return argsAutenticacao3;
    }

    protected String[] getArgsForNotAuthorizedUser() {
        String argsAutenticacao4[] = { "d2543", "pass", getApplication() };
        return argsAutenticacao4;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
    }

    public String getApplication() {
        return Autenticacao.EXTRANET;
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
            fail("ReadStudentTestTest " + "cloner");
        }

        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(studentTestQuestion.getStudent());
        InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(studentTestQuestion.getQuestion());

        infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
        infoStudentTestQuestion.setStudent(infoStudent);
        infoStudentTestQuestion.setQuestion(infoQuestion);
        return infoStudentTestQuestion;
    }

}

