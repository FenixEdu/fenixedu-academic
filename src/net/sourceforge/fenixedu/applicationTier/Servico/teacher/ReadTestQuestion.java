/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestQuestion implements IService {

    private String path = new String();

    public ReadTestQuestion() {
    }

    public SiteView run(Integer executionCourseId, Integer testId, Integer questionId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion question = new Question(questionId);
            question = (IQuestion) persistentQuestion.readByOID(Question.class, questionId);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }

            InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);

            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
                if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                    infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion
                            .getResponseProcessingInstructions(), infoQuestion.getOptions()));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            ITestQuestion testQuestion = persistentTestQuestion.readByTestAndQuestion(test, question);
            InfoTestQuestion infoTestQuestion = InfoTestQuestion.newInfoFromDomain(testQuestion);
            infoTestQuestion.setQuestion(correctQuestionValues(infoQuestion, new Double(infoTestQuestion
                    .getTestQuestionValue().doubleValue())));
            InfoSiteTestQuestion bodyComponent = new InfoSiteTestQuestion();
            bodyComponent.setInfoTestQuestion(infoTestQuestion);
            bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = new Double(0);

        Iterator it = infoQuestion.getResponseProcessingInstructions().iterator();
        while (it.hasNext()) {
            ResponseProcessing responseProcessing = (ResponseProcessing) it.next();
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            it = infoQuestion.getResponseProcessingInstructions().iterator();
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);

            while (it.hasNext()) {
                ResponseProcessing responseProcessing = (ResponseProcessing) it.next();
                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue()
                        .doubleValue()
                        * difValue));
            }
        }

        return infoQuestion;
    }
}