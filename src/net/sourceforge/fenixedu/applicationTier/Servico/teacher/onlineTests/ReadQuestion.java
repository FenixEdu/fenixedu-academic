package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestionWithInfoMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

public class ReadQuestion extends Service {

    private String path = new String();

    public InfoQuestion run(Integer executionCourseId, Integer metadataId, Integer questionId, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        this.path = path.replace('\\', '/');
        
        Metadata metadata = rootDomainObject.readMetadataByOID(metadataId);
                
        Question question = null;
        if (questionId == null || questionId.equals(new Integer(-1))) {
            if (metadataId == null)
                throw new InvalidArgumentsServiceException();
            
            if (metadata == null || metadata.getVisibleQuestions() == null && metadata.getVisibleQuestions().size() == 0) {
                throw new InvalidArgumentsServiceException();
            }

            question = metadata.getVisibleQuestions().get(0);
        } else {
            
            for (Question visibleQuestion : metadata.getVisibleQuestions()) {
                if(visibleQuestion.getIdInternal().equals(questionId)){
                    question = visibleQuestion;
                    break;
                }
            }            
        }
        
        if (question == null) {
            throw new InvalidArgumentsServiceException();
        }
        InfoQuestion infoQuestion = InfoQuestionWithInfoMetadata.newInfoFromDomain(question);
        ParseQuestion parse = new ParseQuestion();
        try {
            infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
            if (infoQuestion.getQuestionType().getType().equals(new Integer(QuestionType.LID)))
                infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion.getResponseProcessingInstructions(), infoQuestion
                        .getOptions()));
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return infoQuestion;
    }
}
