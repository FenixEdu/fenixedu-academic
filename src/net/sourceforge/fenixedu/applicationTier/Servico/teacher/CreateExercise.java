/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;
import net.sourceforge.fenixedu.util.tests.XMLQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class CreateExercise implements IService {

    public CreateExercise() {
    }

    public boolean run(Integer executionCourseId, Integer metadataId, String author, String description,
            QuestionDifficultyType questionDifficultyType, String mainSubject, String secondarySubject,
            Calendar learningTime, String level, InfoQuestion infoQuestion, String questionText,
            String secondQuestionText, String[] options, String[] correctOptions, String[] shuffle,
            String correctFeedbackText, String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox, String path) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IMetadata metadata = null;
            if (metadataId == null) {
                metadata = new Metadata();
                metadata.setAuthor(author);
                metadata.setDescription(description);
                metadata.setDifficulty(questionDifficultyType.getTypeString());
                metadata.setExecutionCourse(executionCourse);
                metadata.setLearningTime(learningTime);
                metadata.setMainSubject(mainSubject);
                metadata.setSecondarySubject(secondarySubject);
                metadata.setLevel(level);
                metadata.setVisibility(new Boolean(true));
                metadata.setNumberOfMembers(new Integer(1));
            } else {
                metadata = (IMetadata) persistentSuport.getIPersistentMetadata().readByOID(
                        Metadata.class, metadataId);
                if (metadata == null) {
                    throw new InvalidArgumentsServiceException();
                }
                metadata.setNumberOfMembers(new Integer(metadata.getNumberOfMembers().intValue() + 1));
            }
            persistentSuport.getIPersistentMetadata().simpleLockWrite(metadata);
            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion question = new Question();
            question.setMetadata(metadata);
            question.setVisibility(new Boolean(true));
            try {
                question.setXmlFile(getQuestion(infoQuestion, questionText, secondQuestionText, options,
                        shuffle, correctFeedbackText, wrongFeedbackText, breakLineBeforeResponseBox,
                        breakLineAfterResponseBox));
            } catch (UnsupportedEncodingException e) {
                throw new FenixServiceException(e);
            }
            if (metadataId == null)
                question.setXmlFileName("Pergunta" + metadata.getNumberOfMembers() + ".xml");
            else
                question.setXmlFileName(persistentQuestion.correctFileName("Pergunta"
                        + metadata.getNumberOfMembers() + ".xml", metadataId));
            persistentQuestion.simpleLockWrite(question);

            infoQuestion.setXmlFile(question.getXmlFile());
            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, path);
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
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