/*
 * Created on 6/Ago/2003
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionType;
import Util.tests.ResponseProcessing;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestQuestion implements IService {

    private String path = new String();

    public ReadTestQuestion() {
    }

    public SiteView run(Integer executionCourseId, Integer testId,
            Integer questionId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTest persistentTest = persistentSuport
                    .getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentQuestion persistentQuestion = persistentSuport
                    .getIPersistentQuestion();
            IQuestion question = new Question(questionId);
            question = (IQuestion) persistentQuestion.readByOID(Question.class,
                    questionId);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }

            InfoQuestion infoQuestion = Cloner
                    .copyIQuestion2InfoQuestion(question);
            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(),
                        infoQuestion, this.path);
                if (infoQuestion.getQuestionType().getType().equals(
                        new Integer(QuestionType.LID)))
                    infoQuestion.setResponseProcessingInstructions(parse
                            .newResponseList(infoQuestion
                                    .getResponseProcessingInstructions(),
                                    infoQuestion.getOptions()));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            ITestQuestion testQuestion = persistentTestQuestion
                    .readByTestAndQuestion(test, question);
            InfoTestQuestion infoTestQuestion = Cloner
                    .copyITestQuestion2InfoTestQuestion(testQuestion);
            infoTestQuestion.setQuestion(correctQuestionValues(infoQuestion,
                    new Double(infoTestQuestion.getTestQuestionValue()
                            .doubleValue())));
            InfoSiteTestQuestion bodyComponent = new InfoSiteTestQuestion();
            bodyComponent.setInfoTestQuestion(infoTestQuestion);
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner
                    .get(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent,
                    bodyComponent);
            return siteView;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion,
            Double questionValue) {
        Double maxValue = new Double(0);

        Iterator it = infoQuestion.getResponseProcessingInstructions()
                .iterator();
        while (it.hasNext()) {
            ResponseProcessing responseProcessing = (ResponseProcessing) it
                    .next();
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            it = infoQuestion.getResponseProcessingInstructions().iterator();
            double difValue = questionValue.doubleValue()
                    * Math.pow(maxValue.doubleValue(), -1);

            while (it.hasNext()) {
                ResponseProcessing responseProcessing = (ResponseProcessing) it
                        .next();
                responseProcessing.setResponseValue(new Double(
                        responseProcessing.getResponseValue().doubleValue()
                                * difValue));
            }
        }

        return infoQuestion;
    }
}