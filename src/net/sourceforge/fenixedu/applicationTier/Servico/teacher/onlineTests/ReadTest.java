/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTest implements IService {

    private String path = new String();

    public SiteView run(Integer executionCourseId, Integer testId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }
            //List<ITestQuestion> questions = persistentSuport.getIPersistentTestQuestion().readByTest(testId);
            List<InfoTestQuestion> result = new ArrayList<InfoTestQuestion>();
            ParseQuestion parse = new ParseQuestion();
            for (ITestQuestion testQuestion : test.getTestQuestions()) {
                InfoTestQuestion infoTestQuestion = InfoTestQuestionWithInfoQuestion.newInfoFromDomain(testQuestion);
                try {
                    infoTestQuestion.setQuestion(parse.parseQuestion(infoTestQuestion.getQuestion().getXmlFile(), infoTestQuestion.getQuestion(),
                            this.path));
                    if (infoTestQuestion.getQuestion().getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                        infoTestQuestion.getQuestion().setResponseProcessingInstructions(
                                parse.newResponseList(infoTestQuestion.getQuestion().getResponseProcessingInstructions(), infoTestQuestion
                                        .getQuestion().getOptions()));

                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                result.add(infoTestQuestion);
            }
            InfoSiteTest infoSiteTest = new InfoSiteTest();
            infoSiteTest.setInfoTestQuestions(result);
            infoSiteTest.setInfoTest(InfoTest.newInfoFromDomain(test));
            infoSiteTest.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(infoSiteTest, infoSiteTest);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}