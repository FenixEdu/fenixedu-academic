/*
 * Created on 31/Jul/2003
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteTest;
import DataBeans.InfoTest;
import DataBeans.InfoTestQuestion;
import DataBeans.InfoTestQuestionWithInfoQuestion;
import DataBeans.SiteView;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionType;
import UtilTests.ParseQuestion;

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
            persistentSuport = SuportePersistenteOJB.getInstance();
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