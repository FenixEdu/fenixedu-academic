/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;
import net.sourceforge.fenixedu.util.tests.XMLQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class CreateExercise implements IService {

	public boolean run(Integer executionCourseId, Integer metadataId, String author, String description,
			QuestionDifficultyType questionDifficultyType, String mainSubject, String secondarySubject,
			Calendar learningTime, String level, InfoQuestion infoQuestion, String questionText,
			String secondQuestionText, String[] options, String[] correctOptions, String[] shuffle,
			String correctFeedbackText, String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
			Boolean breakLineAfterResponseBox, String path) throws FenixServiceException,
			ExcepcaoPersistencia {

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ExecutionCourse executionCourse = (ExecutionCourse) persistentSuport
				.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
		if (executionCourse == null) {
			throw new InvalidArgumentsServiceException();
		}
		Metadata metadata = null;
		if (metadataId == null) {
			metadata = DomainFactory.makeMetadata(executionCourse, null, null);
			metadata.setAuthor(author);
			metadata.setDescription(description);
			metadata.setDifficulty(questionDifficultyType.getTypeString());
			metadata.setExecutionCourse(executionCourse);
			metadata.setLearningTime(learningTime);
			metadata.setMainSubject(mainSubject);
			metadata.setSecondarySubject(secondarySubject);
			metadata.setLevel(level);
			metadata.setVisibility(new Boolean(true));
		} else {
			metadata = (Metadata) persistentSuport.getIPersistentMetadata().readByOID(Metadata.class,
					metadataId);
			if (metadata == null) {
				throw new InvalidArgumentsServiceException();
			}
		}
		IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
		Question question = DomainFactory.makeQuestion();
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
			question.setXmlFileName(persistentQuestion.correctFileName("Pergunta"
					+ visibleQuestions.size() + ".xml", metadataId));

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