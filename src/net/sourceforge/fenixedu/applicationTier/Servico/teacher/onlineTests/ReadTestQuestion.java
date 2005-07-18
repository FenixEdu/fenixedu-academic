/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTestQuestion implements IService {

    private String path = new String();

    public InfoTestQuestion run(Integer executionCourseId, Integer testId, Integer questionId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITestQuestion testQuestion = persistentSuport.getIPersistentTestQuestion().readByTestAndQuestion(testId, questionId);
            InfoTestQuestion infoTestQuestion = InfoTestQuestionWithInfoQuestion.newInfoFromDomain(testQuestion);
            ParseQuestion parse = new ParseQuestion();
            try {
                infoTestQuestion.setQuestion(parse.parseQuestion(infoTestQuestion.getQuestion().getXmlFile(), infoTestQuestion.getQuestion(),
                        this.path));
                if (infoTestQuestion.getQuestion().getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                    infoTestQuestion.getQuestion().setResponseProcessingInstructions(
                            parse.newResponseList(infoTestQuestion.getQuestion().getResponseProcessingInstructions(), infoTestQuestion.getQuestion()
                                    .getOptions()));
                infoTestQuestion.setQuestion(correctQuestionValues(infoTestQuestion.getQuestion(), new Double(infoTestQuestion.getTestQuestionValue()
                        .doubleValue())));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }

            return infoTestQuestion;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = new Double(0);
        for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);

            for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue().doubleValue() * difValue));
            }
        }

        return infoQuestion;
    }
}