/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;
import net.sourceforge.fenixedu.util.tests.XMLQuestion;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class CreateExercise {

    protected Boolean run(ExecutionCourse executionCourse, String metadataId, String author, String description,
            QuestionDifficultyType questionDifficultyType, String mainSubject, String secondarySubject, Calendar learningTime,
            String level, SubQuestion subQuestion, String questionText, String secondQuestionText, String[] options,
            String[] correctOptions, String[] shuffle, String correctFeedbackText, String wrongFeedbackText,
            Boolean breakLineBeforeResponseBox, Boolean breakLineAfterResponseBox) throws FenixServiceException {

        Metadata metadata = null;
        if (metadataId == null) {
            metadata =
                    new Metadata(executionCourse, author, description, questionDifficultyType.getTypeString(), learningTime,
                            mainSubject, secondarySubject, level);
        } else {
            metadata = FenixFramework.getDomainObject(metadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
        }
        Question question = new Question();

        question.setVisibility(new Boolean(true));
        try {
            question.setXmlFile(getQuestion(subQuestion, questionText, secondQuestionText, options, shuffle, correctFeedbackText,
                    wrongFeedbackText, breakLineBeforeResponseBox, breakLineAfterResponseBox));
        } catch (UnsupportedEncodingException e) {
            throw new FenixServiceException(e);
        }
        final List<Question> visibleQuestions = metadata.getVisibleQuestions();
        if (metadataId == null) {
            question.setXmlFileName("Pergunta" + visibleQuestions.size() + ".xml");
        } else {
            question.setXmlFileName(metadata.correctFileName("Pergunta" + visibleQuestions.size() + ".xml"));
        }

        question.setMetadata(metadata);
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            question = parse.parseSubQuestion(question);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return true;
    }

    private String getQuestion(SubQuestion subQuestion, String questionText, String secondQuestionText, String[] options,
            String[] shuffle, String correctFeedbackText, String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox) throws UnsupportedEncodingException {
        String xmlFile = new String();
        XMLQuestion xmlQuestion = new XMLQuestion();
        xmlFile =
                new String(xmlQuestion.getXmlQuestion(questionText, secondQuestionText, subQuestion.getQuestionType(), options,
                        shuffle, subQuestion.getResponseProcessingInstructions(), correctFeedbackText, wrongFeedbackText,
                        breakLineBeforeResponseBox, breakLineAfterResponseBox).getBytes(), Charset.defaultCharset().name());
        return xmlFile;
    }

    // Service Invokers migrated from Berserk

    private static final CreateExercise serviceInstance = new CreateExercise();

    @Atomic
    public static Boolean runCreateExercise(ExecutionCourse executionCourse, String metadataId, String author,
            String description, QuestionDifficultyType questionDifficultyType, String mainSubject, String secondarySubject,
            Calendar learningTime, String level, SubQuestion subQuestion, String questionText, String secondQuestionText,
            String[] options, String[] correctOptions, String[] shuffle, String correctFeedbackText, String wrongFeedbackText,
            Boolean breakLineBeforeResponseBox, Boolean breakLineAfterResponseBox) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        return serviceInstance.run(executionCourse, metadataId, author, description, questionDifficultyType, mainSubject,
                secondarySubject, learningTime, level, subQuestion, questionText, secondQuestionText, options, correctOptions,
                shuffle, correctFeedbackText, wrongFeedbackText, breakLineBeforeResponseBox, breakLineAfterResponseBox);
    }

}