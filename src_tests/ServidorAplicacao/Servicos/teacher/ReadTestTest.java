/*
 * Created on 11/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadTestTest(String testName) {
        super(testName);

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(26), new Integer(4) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoSiteTest bodyComponent = new InfoSiteTest();
        InfoExecutionCourse infoExecutionCourse = null;
        InfoTest infoTest = null;
        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, new Integer(26));
            assertNotNull("executionCourse null", executionCourse);
            IPersistentTest persistentTest = sp.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, new Integer(4));
            assertNotNull("test null", test);
            IPersistentTestQuestion persistentTestQuestion = sp.getIPersistentTestQuestion();
            ITestQuestion testQuestion = (ITestQuestion) persistentTestQuestion.readByOID(
                    TestQuestion.class, new Integer(8));
            assertNotNull("testQuestion null", testQuestion);
            sp.confirmarTransaccao();
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            //  infoTest = Cloner.copyITest2InfoTest(test);

            ParseQuestion parse = new ParseQuestion();
            InfoTestQuestion infoTestQuestion = InfoTestQuestion.newInfoFromDomain(testQuestion);
            try {
                infoTestQuestion.setQuestion(parse.parseQuestion(infoTestQuestion.getQuestion()
                        .getXmlFile(), infoTestQuestion.getQuestion(), ""));
            } catch (Exception e) {
                fail("exception: ExcepcaoPersistencia ");
            }
            result.add(infoTestQuestion);
        } catch (ExcepcaoPersistencia e) {
            fail("exception: ExcepcaoPersistencia ");
        }

        bodyComponent.setExecutionCourse(infoExecutionCourse);
        bodyComponent.setInfoTest(infoTest);
        bodyComponent.setInfoTestQuestions(result);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;
    }

    protected boolean needsAuthorization() {
        return true;
    }
}