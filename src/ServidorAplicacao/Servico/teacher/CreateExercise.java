/*
 * Created on 23/Set/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoQuestion;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.QuestionDifficultyType;
import Util.tests.XMLQuestion;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class CreateExercise implements IService {

    private String path = new String();

    public CreateExercise() {
    }

    public boolean run(Integer executionCourseId, Integer metadataId,
            String author, String description,
            QuestionDifficultyType questionDifficultyType, String mainSubject,
            String secondarySubject, Calendar learningTime, String level,
            InfoQuestion infoQuestion, String questionText,
            String secondQuestionText, String[] options,
            String[] correctOptions, String[] shuffle,
            String correctFeedbackText, String wrongFeedbackText,
            Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox, String path)
            throws FenixServiceException, NotExecuteException {
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) { throw new InvalidArgumentsServiceException(); }
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
                metadata = (IMetadata) persistentSuport
                        .getIPersistentMetadata().readByOID(Metadata.class,
                                metadataId);
                if (metadata == null) { throw new InvalidArgumentsServiceException(); }
                metadata.setNumberOfMembers(new Integer(metadata
                        .getNumberOfMembers().intValue() + 1));
            }
            persistentSuport.getIPersistentMetadata().simpleLockWrite(
                    metadata);
            IPersistentQuestion persistentQuestion = persistentSuport
                    .getIPersistentQuestion();
            IQuestion question = new Question();
            question.setMetadata(metadata);
            question.setVisibility(new Boolean(true));
            try {
                question.setXmlFile(getQuestion(infoQuestion, questionText,
                        secondQuestionText, options, shuffle, correctFeedbackText,
                        wrongFeedbackText, breakLineBeforeResponseBox,
                        breakLineAfterResponseBox));
            } catch (UnsupportedEncodingException e) {
                throw new FenixServiceException(e);
            }
            if (metadataId == null)
                question.setXmlFileName("Pergunta"
                        + metadata.getNumberOfMembers() + ".xml");
            else
                question.setXmlFileName(persistentQuestion.correctFileName(
                        "Pergunta" + metadata.getNumberOfMembers() + ".xml",
                        metadataId));
            persistentQuestion.simpleLockWrite(question);

            infoQuestion.setXmlFile(question.getXmlFile());
            ParseQuestion parse = new ParseQuestion();
            try {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(),
                        infoQuestion, this.path);
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private String getQuestion(InfoQuestion infoQuestion, String questionText,
            String secondQuestionText, String[] options, String[] shuffle,
            String correctFeedbackText, String wrongFeedbackText,
            Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox) throws UnsupportedEncodingException {
        String xmlFile = new String();
        XMLQuestion xmlQuestion = new XMLQuestion();
        xmlFile = new String(xmlQuestion.getXmlQuestion(questionText, secondQuestionText,
                infoQuestion.getQuestionType(), options, shuffle, infoQuestion
                        .getResponseProcessingInstructions(),
                correctFeedbackText, wrongFeedbackText,
                breakLineBeforeResponseBox, breakLineAfterResponseBox).getBytes(), "ISO-8859-1");
        return xmlFile;
    }
}