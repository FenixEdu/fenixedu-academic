/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTest implements IService {

    private String path = new String();

    public ReadTest() {
    }

    public SiteView run(Integer executionCourseId, Integer testId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        ISuportePersistente persistentSuport;
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            List questions = persistentTestQuestion.readByTest(test);
            List result = new ArrayList();
            Iterator iter = questions.iterator();
            ParseQuestion parse = new ParseQuestion();
            while (iter.hasNext()) {
                ITestQuestion testQuestion = (ITestQuestion) iter.next();
                InfoTestQuestion infoTestQuestion = InfoTestQuestionWithInfoQuestion
                        .newInfoFromDomain(testQuestion);
                try {
                    infoTestQuestion.setQuestion(parse.parseQuestion(infoTestQuestion.getQuestion()
                            .getXmlFile(), infoTestQuestion.getQuestion(), this.path));
                    if (infoTestQuestion.getQuestion().getQuestionType().getType().equals(
                            new Integer(QuestionType.LID)))
                        infoTestQuestion.getQuestion().setResponseProcessingInstructions(
                                parse.newResponseList(infoTestQuestion.getQuestion()
                                        .getResponseProcessingInstructions(), infoTestQuestion
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