/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;
import net.sourceforge.fenixedu.util.tests.XMLQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class CreateExercise extends Service {

    public boolean run(Integer executionCourseId, Integer metadataId, String author, String description,
            QuestionDifficultyType questionDifficultyType, String mainSubject, String secondarySubject,
            Calendar learningTime, String level, InfoQuestion infoQuestion, String questionText,
            String secondQuestionText, String[] options, String[] correctOptions, String[] shuffle,
            String correctFeedbackText, String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox, String path) throws FenixServiceException,
            ExcepcaoPersistencia {

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }
        Metadata metadata = null;
        if (metadataId == null) {
            metadata = new Metadata(executionCourse, author, description,
                    questionDifficultyType.getTypeString(), learningTime, mainSubject, secondarySubject,
                    level);
        } else {
            metadata = (Metadata) rootDomainObject.readMetadataByOID(metadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
        }
        Question question = new Question();
        question.setMetadata(metadata);
        question.setVisibility(new Boolean(true));
        try {
            question.setXmlFile(getQuestion(infoQuestion, questionText, secondQuestionText, options,
                    shuffle, correctFeedbackText, wrongFeedbackText, breakLineBeforeResponseBox,
                    breakLineAfterResponseBox));
        } catch (UnsupportedEncodingException e) {
            throw new FenixServiceException(e);
        }
        final List<Question> visibleQuestions = metadata.getVisibleQuestions();
        if (metadataId == null)
            question.setXmlFileName("Pergunta" + visibleQuestions.size() + ".xml");
        else
            question.setXmlFileName(metadata.correctFileName("Pergunta"
                    + visibleQuestions.size() + ".xml"));

        infoQuestion.setXmlFile(question.getXmlFile());
        ParseQuestion parse = new ParseQuestion();
        try {
            infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, path);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return true;
    }

    private String getQuestion(InfoQuestion infoQuestion, String questionText,
            String secondQuestionText, String[] options, String[] shuffle, String correctFeedbackText,
            String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox) throws UnsupportedEncodingException {
        String xmlFile = new String();
        XMLQuestion xmlQuestion = new XMLQuestion();
        xmlFile = new String(xmlQuestion.getXmlQuestion(questionText, secondQuestionText,
                infoQuestion.getQuestionType(), options, shuffle,
                infoQuestion.getResponseProcessingInstructions(), correctFeedbackText,
                wrongFeedbackText, breakLineBeforeResponseBox, breakLineAfterResponseBox).getBytes(),
                "ISO-8859-1");
        return xmlFile;
    }
}